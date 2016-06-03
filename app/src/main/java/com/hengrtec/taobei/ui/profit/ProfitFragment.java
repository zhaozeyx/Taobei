/*
 * 文件名: PofitFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.BibiModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.BibiParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.basic.widget.BarChartView;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.ShareUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import javax.inject.Inject;

/**
 * 收益界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class ProfitFragment extends BasicTitleBarFragment {
    private static final String[] LEFT_TITLE = new String[]{"0", "10", "20", "30", "40"};
    private static final int MAX_VALUE = 40;
    @Bind(R.id.today_income)
    TextView mTodayIncome;
    @Bind(R.id.profit_today_container)
    LinearLayout mProfitTodayContainer;
    @Bind(R.id.adv_view_count)
    TextView mAdvViewCount;
    @Bind(R.id.view_total_time)
    TextView mViewTotalTime;
    @Bind(R.id.adv_be_friend_rank)
    TextView mAdvBeFriendRank;
    @Bind(R.id.view_be_visited_count)
    TextView mViewBeVisitedCount;
    @Bind(R.id.profit_beyond)
    TextView mProfitBeyond;
    @Bind(R.id.profit_beyond_description)
    TextView profitBeyondDescription;
    @Bind(R.id.cheered)
    TextView mCheered;
    @Bind(R.id.beyond_divider)
    ImageView beyondDivider;
    @Bind(R.id.btn_share_moments)
    FrameLayout mBtnShareMoments;
    @Bind(R.id.bar_chart)
    BarChartView mBarChart;

    @Inject
    AdvertisementService mAdvService;
    @Bind(R.id.profit_info_container)
    LinearLayout profitInfoContainer;
    @Bind(R.id.info)
    TextView info;
    @Bind(R.id.profit_info)
    RelativeLayout profitInfo;
    @Bind(R.id.test)
    ImageView test;
    @Bind(R.id.iv_zxing)
    ImageView ivZxing;
    @Bind(R.id.lv_zxing)
    LinearLayout lvZxing;
    @Bind(R.id.rl_bibi)
    RelativeLayout rlBibi;

    @Override
    protected void onCreateViewCompleted(View view) {
        ButterKnife.bind(this, view);
        inject();
        initData();
    }

    private void inject() {
        DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
                GlobalModule((CustomApp) getActivity().getApplication()))
                .build().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profit;
    }

    @Override
    public boolean initializeTitleBar() {
        setMiddleTitle(R.string.fragment_profit_title);
        return true;
    }

    private void initData() {
        showProgressDialog("");
        manageRpcCall(mAdvService.bibi(new BibiParams(String.valueOf(getComponent().loginSession()
                .getUserId()))), new UiRpcSubscriber<BibiModel>(getActivity()) {


            @Override
            protected void onSuccess(BibiModel model) {
                bindData(model);
            }

            @Override
            protected void onEnd() {
                closeProgressDialog();
            }
        });
    }

    private void bindData(BibiModel model) {
        mAdvViewCount.setText(getString(R.string.fragment_profit_view_count, model.getAdvQuantity()));
        mAdvBeFriendRank.setText(getString(R.string.fragment_profit_friend_rank, Integer.parseInt
                (model.getRanking())));
        mProfitBeyond.setText(getString(R.string.fragment_profit_beyond, model.getRankingPercentage()));
        mTodayIncome.setText(model.getBenefit() + getString(R.string.unit_cash));
        int hour = model.getPlayDuration() / 3600;
        int minute = (model.getPlayDuration() % 3600) / 60;
        mViewTotalTime.setText(getString(R.string.format_duration, hour, minute));
        mViewBeVisitedCount.setText(String.valueOf(model.getViewTimes()) + getString(R.string
                .unit_times));

        updateBar(model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (null != mAdvService) {
            initData();
        }
    }

    private void updateBar(BibiModel model) {
        BibiModel.BenefitsBean bean = model.getBenefits();
        String[] topTitle = bean.getKey().toArray(new String[]{});
        int valueSize = bean.getValue().size();
        int[] data = new int[valueSize];
        for (int i = 0; i < valueSize; i++) {
            data[i] = Integer.parseInt(bean.getValue().get(i));
        }
        mBarChart.setData(LEFT_TITLE, topTitle, data, MAX_VALUE, getString(R.string.unit_cash));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_share_moments)
    public void onClick() {
//        screenshot();
        // 获取内置SD卡路径
        createQRImage("http://img003.21cnimg.com/photos/album/20160516/m600/70DEAFE38FD36B70646E9B369827F600.jpeg");
        Bitmap bmp = createViewBitmap(rlBibi);
        Bitmap bmp1 = createViewBitmap(lvZxing);
        Bitmap bitmap = add2Bitmap(bmp, bmp1);
        if (bitmap != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "bb.png";

                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
        ShareUtils.showShareWechatMoments(getActivity(),Environment.getExternalStorageDirectory().getPath()+File.separator+"bb.png",null);
    }

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setTitle("测试的标题");
        sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
        sp.setText("测试的文本");
        //                Log.e("==", ""+childfile.getPath());
        //                        sp.setImagePath(childfile.getPath());
        //                        Bitmap  bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //                        sp.setImageData(bm);
        //sp.setImageUrl("http://www.baidu.com/img/bdlogo.gif");
        sp.setImagePath(Environment.getExternalStorageDirectory().getPath()+File.separator+"bb.png");
        sp.setSite("呵呵");
        sp.setSiteUrl("http://www.baidu.com");
        Platform qzone = ShareSDK.getPlatform (getActivity(), WechatMoments.NAME);

        qzone. setPlatformActionListener (new  PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Log.e("share", "onError");
            }
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Log.e("share", "onComplete");
            }
            @Override
            public void onCancel(Platform arg0, int arg1) {
                Log.e("share", "onCancel");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qzone.share(sp);

//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(getString(R.string.share));
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
////        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath(Environment.getExternalStorageDirectory().getPath()+File.separator+"bb.png");//确保SDcard下面存在此张图片
//
//
//// 启动分享GUI
//        oks.show(getActivity());
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private Bitmap add2Bitmap(Bitmap first, Bitmap second) {
//        int width = first.getWidth() + second.getWidth();
//        int height = Math.max(first.getHeight(), second.getHeight());
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }


    //Edited by mythou
//http://www.cnblogs.com/mythou/
    private int QR_WIDTH = 200, QR_HEIGHT = 200;
    Bitmap bitmap_zx;

    public void createQRImage(String url) {
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap_zx = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap_zx.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            ivZxing.setImageBitmap(bitmap_zx);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
