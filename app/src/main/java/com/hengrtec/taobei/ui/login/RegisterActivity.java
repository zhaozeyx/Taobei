/*
 * 文件名: RegisterActivity
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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.iwgang.countdownview.CountdownView;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.service.AuthService;
import com.hengrtec.taobei.net.rpc.service.params.CheckVerifyCodeParams;
import com.hengrtec.taobei.net.rpc.service.params.GetVerifyCodeParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;

/**
 * 注册界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/28]
 */
public class RegisterActivity extends BasicTitleBarActivity {
  private static final long COUNT_DOWN_INTERVAL = 60 * 1000L;
  @Bind(R.id.btn_clear)
  ImageView mBtnClear;
  @Bind(R.id.phone_input)
  EditText mPhoneInput;
  @Bind(R.id.count_down_default)
  TextView mCountDownDefault;
  @Bind(R.id.count_down_view)
  CountdownView mCountDownView;
  @Bind(R.id.verify_code_input)
  EditText mVerifyCodeInput;
  @Bind(R.id.btn_register)
  Button mBtnRegister;
  @Bind(R.id.licence)
  TextView mLicence;

  @Inject
  AuthService mAuthService;

  @OnTextChanged({R.id.phone_input, R.id.verify_code_input})
  void onTextChanged() {
    if (!checkInput(mPhoneInput.getText(), getResources().getInteger(R.integer.max_phone_length))) {
      mBtnRegister.setEnabled(false);
      return;
    }
    if (!checkInput(mVerifyCodeInput.getText(), getResources().getInteger(R.integer
        .max_verify_code_length))) {
      mBtnRegister.setEnabled(false);
      return;
    }
    mBtnRegister.setEnabled(true);
  }

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    initView();
    inject();
  }

  private void initView() {
    stopCountDown();
    mCountDownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
      @Override
      public void onEnd(CountdownView cv) {
        stopCountDown();
      }
    });
  }

  private void inject() {
    DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
        GlobalModule((CustomApp) getApplication())).build().inject(this);
  }

  private boolean checkInput(Editable editable, int lengthLimit) {
    return !TextUtils.isEmpty(editable) && editable.length() == lengthLimit;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_register;
  }

  @OnClick(R.id.count_down_default)
  public void onClick() {
    if (!checkInput(mPhoneInput.getText(), getResources().getInteger(R.integer.max_phone_length))) {
      showShortToast(R.string.register_phone_invalid);
      return;
    }
    // TODO 调用获取验证码接口
    manageRpcCall(mAuthService.getVerifyCode(new GetVerifyCodeParams(mPhoneInput.getText()
        .toString())), new UiRpcSubscriber<String>(this) {


      @Override
      protected void onSuccess(String s) {
      }

      @Override
      protected void onEnd() {

      }
    });
    startCountDown();
  }

  @OnClick(R.id.btn_register)
  public void onRegisterClick() {
    // TODO 调用校验验证码接口，这个按钮是否应该叫下一步？？？？！！
    manageRpcCall(mAuthService.checkVerifyCode(new CheckVerifyCodeParams(mPhoneInput.getText()
        .toString(), mVerifyCodeInput.getText
        ().toString())), new UiRpcSubscriber<Void>(this) {
      @Override
      protected void onSuccess(Void s) {
        // 跳转到下一步
        startActivity(PasswordSetActivity.makeIntent(RegisterActivity.this, mPhoneInput.getText()
            .toString(), mVerifyCodeInput.getText().toString()));
      }

      @Override
      protected void onEnd() {

      }

      @Override
      public void onApiError(RpcApiError apiError) {
        super.onApiError(apiError);
      }
    });
  }

  @OnClick(R.id.btn_clear)
  public void onPhoneClearClick() {
    mPhoneInput.setText("");
  }

  private void startCountDown() {
    mCountDownDefault.setVisibility(View.INVISIBLE);
    mCountDownView.setVisibility(View.VISIBLE);
    mCountDownView.start(COUNT_DOWN_INTERVAL);
  }

  private void stopCountDown() {
    mCountDownView.setVisibility(View.GONE);
    mCountDownDefault.setVisibility(View.VISIBLE);
  }

  @Override
  protected void onLogin() {
    super.onLogin();
    finish();
  }
}
