/*
 * 文件名: LogInWayActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/27
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

/**
 * 登录方式选择界面<BR>
 * TODO 根据登录是否成功，关闭该页面
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/27]
 */
public class LoginWayActivity extends BasicTitleBarActivity {
  @Bind(R.id.btn_login_web_chat)
  TextView mBtnLoginWebChat;
  @Bind(R.id.btn_login_phone)
  TextView mBtnLoginPhone;
  @Bind(R.id.btn_login_qq)
  TextView mBtnLoginQq;
  @Bind(R.id.other_way_register)
  TextView mOtherWayRegister;
  @Bind(R.id.other_way_sina)
  TextView mOtherWaySina;
  @Bind(R.id.other_way_container)
  RelativeLayout otherWayContainer;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_login_way;
  }

  @OnClick({R.id.btn_login_web_chat, R.id.btn_login_phone, R.id.btn_login_qq, R.id
      .other_way_register, R.id.other_way_sina})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_web_chat:
        // TODO 微信登录
        break;
      case R.id.btn_login_phone:
        startActivity(LoginActivity.makeIntent(this, LoginActivity.LOGIN_WAY_PASSWORD));
        break;
      case R.id.btn_login_qq:
        // TODO QQ登录
        break;
      case R.id.other_way_register:
        // TODO 注册
        startActivity(new Intent(this, RegisterActivity.class));
        break;
      case R.id.other_way_sina:
        // TODO 微博
        break;
    }
  }

  @Override
  protected void onLogin() {
    super.onLogin();
    finish();
  }
}
