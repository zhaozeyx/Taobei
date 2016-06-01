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
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.CouponModel;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.utils.DateUtils;
import java.util.Date;

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
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    mCouponModel = getIntent().getParcelableExtra(BUNDLE_KEY_MODEL);
    initView();
  }

k  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_share_moments:
        break;
      case R.id.btn_share_friends:
        break;
    }
  }

  public static Intent makeIntent(Context context, CouponModel model) {
    Intent intent = new Intent(context, CouponDetailActivity.class);
    intent.putExtra(BUNDLE_KEY_MODEL, model);
    return intent;
  }
}
