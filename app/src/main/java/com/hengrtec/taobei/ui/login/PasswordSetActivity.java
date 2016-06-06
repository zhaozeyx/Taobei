/*
 * 文件名: PasswordSetActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/28
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.net.rpc.service.AuthService;
import com.hengrtec.taobei.net.rpc.service.params.RegisterParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.register.ProfileInitializeActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;

/**
 * 设置密码<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/28]
 */
public class PasswordSetActivity extends BasicTitleBarActivity {
  protected static final String BUNDLE_KEY_PHONE = "register_phone";
  protected static final String BUNDLE_KEY_VERIFY_CODE = "verify_code";
  @Bind(R.id.btn_password_visible_set)
  ImageView mBtnPasswordVisibleSet;
  @Bind(R.id.password_input)
  EditText mPasswordInput;
  @Bind(R.id.btn_password_confirm_visible_set)
  ImageView mBtnPasswordConfirmVisibleSet;
  @Bind(R.id.password_confirm_input)
  EditText mPasswordConfirmInput;
  @Bind(R.id.btn_register_confirm)
  Button mBtnRegisterConfirm;

  @Inject
  AuthService mAuthService;

  private String mPhoneNumber;
  private String mVerifyCode;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    mPhoneNumber = getIntent().getStringExtra(BUNDLE_KEY_PHONE);
    mVerifyCode = getIntent().getStringExtra(BUNDLE_KEY_VERIFY_CODE);
    initView();
    inject();
  }

  @OnTextChanged({R.id.password_input, R.id.password_confirm_input})
  public void onInputChanged() {
    if (!checkInput(mPasswordInput.getText(), getResources().getInteger(R.integer
        .max_password_length), getResources().getInteger(R.integer.min_password_length))) {
      mBtnRegisterConfirm.setEnabled(false);
      return;
    }

    if (!checkInput(mPasswordConfirmInput.getText(), getResources().getInteger(R.integer
        .max_password_length), getResources().getInteger(R.integer.min_password_length))) {
      mBtnRegisterConfirm.setEnabled(false);
      return;
    }

    if (!TextUtils.equals(mPasswordConfirmInput.getText(), mPasswordInput.getText())) {
      mBtnRegisterConfirm.setEnabled(false);
      return;
    }
    mBtnRegisterConfirm.setEnabled(true);
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void initView() {

  }

  public boolean checkInput(Editable charSequence, int maxLimit, int minLimit) {
    return !TextUtils.isEmpty(charSequence) && charSequence.length() <= maxLimit
        && charSequence.length() >= minLimit;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_password_set;
  }

  @OnClick(R.id.btn_password_visible_set)
  public void onClick() {
  }

  @OnClick(R.id.btn_register_confirm)
  public void onRegisterClick() {
    showProgressDialog("", false);
    manageRpcCall(mAuthService.register
        (new RegisterParams(getComponent().deviceId(), mPasswordInput.getEditableText().toString(), mPhoneNumber,
            mVerifyCode, getComponent().loginSession().getUserId())), new UiRpcSubscriber<UserInfo>(this) {
      @Override
      protected void onSuccess(UserInfo userInfo) {
        // 通知所有登录相关界面
        getComponent().loginSession().login(userInfo);
        startActivity(new Intent(PasswordSetActivity.this, ProfileInitializeActivity.class));
        finish();
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }

      @Override
      public void onApiError(RpcApiError apiError) {
        super.onApiError(apiError);
        if (!TextUtils.isEmpty(apiError.getMessage())) {
          showShortToast(apiError.getMessage());
        }
      }
    });
  }

  public static Intent makeIntent(Context context, String phoneNumber, String verifyCode) {
    Intent intent = new Intent(context, PasswordSetActivity.class);
    intent.putExtra(BUNDLE_KEY_PHONE, phoneNumber);
    intent.putExtra(BUNDLE_KEY_VERIFY_CODE, verifyCode);
    return intent;
  }
}
