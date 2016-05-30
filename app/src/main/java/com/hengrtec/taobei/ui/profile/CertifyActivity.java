/*
 * 文件名: CertifyActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/30
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.manager.UserInfoChangedEvent;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.net.rpc.service.NetConstant;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.squareup.otto.Subscribe;
import rx.subscriptions.CompositeSubscription;

/**
 * 会员认证<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/30]
 */
public class CertifyActivity extends BasicTitleBarActivity {

  @Bind(R.id.avatar)
  SimpleDraweeView mAvatarView;
  @Bind(R.id.certify_status)
  TextView mCertifyStatusView;
  @Bind(R.id.btn_add)
  TextView mBtnAddView;
  @Bind(R.id.img_id_card)
  ImageView mImgIdCardView;
  @Bind(R.id.btn_submit)
  TextView mBtnSubmitView;
  @Bind(R.id.user_name)
  TextView mUserNameView;

  private IdCardChoosePresenter mChoosePresenter;
  private String mIdCardPath;
  private CompositeSubscription mSubscriptions = new CompositeSubscription();

  @Override
  public int getLayoutId() {
    return R.layout.activity_certify;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_certify_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    mChoosePresenter = new IdCardChoosePresenter(this);
    initView();
  }

  private void initView() {
    mAvatarView.setImageURI(Uri.parse(getComponent().loginSession().getUserInfo().getAvart()));
    mUserNameView.setText(getComponent().loginSession().getUserInfo().getUserName());
    ImageLoader.loadOptimizedHttpImage(this, NetConstant.BASE_URL_LOCATION + getComponent().loginSession()
        .getUserInfo().getIdCardImg()).into(mImgIdCardView);
    setCertifyStatus();
  }

  private void setCertifyStatus() {
    String certified = getComponent().loginSession().getUserInfo().getCertified();
    switch (certified) {
      case UserInfo.CERTIFY_STATUS_CERTIFY_FAILED:
        setStatusView(R.color.font_color_red_dark, R.mipmap.icon_certify_failed, R.string.activity_certify_status_failed);
        break;
      case UserInfo.CERTIFY_STATUS_CERTIFY_PASS:
        setStatusView(R.color.font_color_brand, R.mipmap.icon_certify_pass, R.string.activity_certify_status_pass);
        break;
      case UserInfo.CERTIFY_STATUS_CERTIFYING:
        setStatusView(R.color.font_color_secondary, R.mipmap.icon_certifying, R.string.activity_certify_status_certifying);
        break;
      default:
        mCertifyStatusView.setVisibility(View.GONE);
        break;
    }
  }

  private void setStatusView(int textColor, int drawableLeft, int text) {
    mCertifyStatusView.setVisibility(View.VISIBLE);
    mCertifyStatusView.setText(text);
    mCertifyStatusView.setTextColor(getResources().getColor(textColor));
    mCertifyStatusView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
  }

  @OnClick({R.id.btn_add, R.id.img_id_card})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_add:
      case R.id.img_id_card:
        mChoosePresenter.showChooseDialog();
        break;
    }
  }

  @OnClick(R.id.btn_submit)
  void onSubmitClicked() {
    mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setIdCardImg(mIdCardPath)
        .setCertified(UserInfo.CERTIFY_STATUS_CERTIFYING).update());
  }

  @Subscribe
  public void handlerUserInfoChangeEvent(UserInfoChangedEvent event) {
    if (!event.changeSuccess) {
      showShortToast(R.string.activity_certify_submit_failed);
      return;
    }
    showShortToast(R.string.activity_certify_submit_success);
    initView();
  }

  @Subscribe
  public void handleIdCardUpLoadEvent(IdCardChoosePresenter.UploadIdCardEvent event) {
    mIdCardPath = event.path;
    ImageLoader.loadOptimizedHttpImage(this, NetConstant.BASE_URL_LOCATION + getComponent().loginSession()
        .getUserInfo().getIdCardImg()).into(mImgIdCardView);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    mChoosePresenter.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mSubscriptions.unsubscribe();
    mChoosePresenter.onDestroy();
  }
}
