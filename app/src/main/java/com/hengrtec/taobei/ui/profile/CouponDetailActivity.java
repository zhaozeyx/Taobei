/*
 * 文件名: CouponDetailActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/6/1
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.CouponModel;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.utils.DateUtils;
import java.util.Date;
import java.util.Hashtable;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/6/1]
 */
public class CouponDetailActivity extends BasicTitleBarActivity {
  private static final String BUNDLE_KEY_MODEL = "bundle_key_model";

  @Bind(R.id.congratulations_info)
  TextView mCongratulationsInfoView;
  @Bind(R.id.coupon_info)
  TextView mCouponInfoView;
  @Bind(R.id.coupon_shop_info)
  TextView mCouponShopInfoView;
  @Bind(R.id.qr_code)
  ImageView mQrCodeView;
  @Bind(R.id.code)
  TextView mCodeView;

  private CouponModel mCouponModel;

  @Override
  public int getLayoutId() {
    return R.layout.activity_coupon_detail;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_coupon_detail_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  private void initView() {
    mCongratulationsInfoView.setText(getString(R.string.activity_coupon_label_congratulations,
        getComponent().loginSession().getUserInfo().getUserName()));
    switch (mCouponModel.getType()) {
      case CouponModel.TYPE_COUPON:
        mCouponInfoView.setText(getString(R.string.activity_coupon_info_coupon, mCouponModel
            .getCouponDesc()));
        break;
      case CouponModel.TYPE_DISCOUNT:
        mCouponInfoView.setText(getString(R.string.activity_coupon_info_discount, mCouponModel
            .getCouponDesc()));
        break;
    }

    mCouponShopInfoView.setText(getString(R.string.activity_coupon_info_shop, DateUtils
        .getFormatDateTime(new Date(mCouponModel.getStartTime()), DateUtils
            .FORMAT_FULL_TIME_CHINESE), DateUtils.getFormatDateTime(new Date(mCouponModel
        .getEndTime()), DateUtils.FORMAT_FULL_TIME_CHINESE), mCouponModel.getSellerName()));
    mCodeView.setText(mCouponModel.getCouponCode());
    createQRImage(mCouponModel.getCouponCode());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    mCouponModel = getIntent().getParcelableExtra(BUNDLE_KEY_MODEL);
    initView();
  }

  @OnClick({R.id.btn_share_moments, R.id.btn_share_friends})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_share_moments:
        break;
      case R.id.btn_share_friends:
        break;
    }
  }

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
      BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH,
          QR_HEIGHT, hints);
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
      mQrCodeView.setImageBitmap(bitmap_zx);
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != bitmap_zx && !bitmap_zx.isRecycled()) {
      bitmap_zx.recycle();
    }
  }

  public static Intent makeIntent(Context context, CouponModel model) {
    Intent intent = new Intent(context, CouponDetailActivity.class);
    intent.putExtra(BUNDLE_KEY_MODEL, model);
    return intent;
  }
}
