package com.hengrtec.taobei.ui.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.login.PayPasswordActivity;
import com.hengrtec.taobei.ui.login.PayPasswordSetActivity;
import com.hengrtec.taobei.ui.login.ResetPasswordActivity;

/**
 * Created by jiao on 2016/5/27.
 */
public class MyAccountSafeActivity extends BasicTitleBarActivity {
  @Bind(R.id.textView) TextView textView;
  @Bind(R.id.iv_level) ImageView ivLevel;
  @Bind(R.id.textView2) TextView textView2;
  @Bind(R.id.setting_qingchuhuancun) RelativeLayout settingQingchuhuancun;
  @Bind(R.id.tv_phone) TextView tvPhone;
  @Bind(R.id.tv_login_psd) TextView tvLoginPsd;
  @Bind(R.id.tv_pay_psw) TextView tvPayPsw;

  @Override public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_settings_account);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override public int getLayoutId() {
    return R.layout.activity_my_account_safe;
  }

  @OnClick({
      R.id.iv_level, R.id.setting_qingchuhuancun, R.id.tv_phone, R.id.tv_login_psd, R.id.tv_pay_psw
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.iv_level:
        break;
      case R.id.setting_qingchuhuancun:
        break;
      case R.id.tv_phone:
        startActivity(new Intent(MyAccountSafeActivity.this, ResetPhoneActivity.class));
        break;
      case R.id.tv_login_psd:
        startActivity(new Intent(MyAccountSafeActivity.this, ResetPasswordActivity.class));
        break;
      case R.id.tv_pay_psw:
        if ("".equals(getComponent().loginSession().getUserInfo().getPayPwd())) {
          startActivity(new Intent(MyAccountSafeActivity.this, PayPasswordSetActivity.class));
        } else {
          startActivity(new Intent(MyAccountSafeActivity.this, PayPasswordActivity.class));
        }

        break;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    initData();
  }

  private void initData() {

    if ("".equals(getComponent().loginSession().getUserInfo().getMobileNo())) {
      tvPhone.setText(R.string.activity_settings_account_settings);
    } else {
      if (!TextUtils.isEmpty(getComponent().loginSession().getUserInfo().getMobileNo())
          && getComponent().loginSession().getUserInfo().getMobileNo().length() > 6) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getComponent().loginSession().getUserInfo().getMobileNo().length();
            i++) {
          char c = getComponent().loginSession().getUserInfo().getMobileNo().charAt(i);
          if (i >= 3 && i <= 6) {
            sb.append('*');
          } else {
            sb.append(c);
          }
        }

        tvPhone.setText(sb.toString());
      }
    }
    if ("".equals(getComponent().loginSession().getUserInfo().getPassword())) {
      tvLoginPsd.setText(R.string.activity_settings_account_settings);
    } else {
      tvLoginPsd.setText(R.string.activity_settings_account_set_psw);
    }
    if ("".equals(getComponent().loginSession().getUserInfo().getPayPwd())) {
      tvPayPsw.setText(R.string.activity_settings_account_settings);
    } else {
      tvPayPsw.setText(R.string.activity_settings_account_set_pay_psw);
    }
    if (!"".equals(getComponent().loginSession().getUserInfo().getSecurityLevel())) {
      if ("l".equals(getComponent().loginSession().getUserInfo().getSecurityLevel())) {
        ivLevel.setImageResource(R.mipmap.level_1);
      } else if ("m".equals(getComponent().loginSession().getUserInfo().getSecurityLevel())) {
        ivLevel.setImageResource(R.mipmap.level_2);
      } else if ("h".equals(getComponent().loginSession().getUserInfo().getSecurityLevel())) {
        ivLevel.setImageResource(R.mipmap.level_3);
      }
    }
  }
}
