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
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.net.rpc.service.AuthService;
import com.hengrtec.taobei.net.rpc.service.params.ThirdLoginParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.ui.tab.MainTabActivity;
import java.util.HashMap;
import javax.inject.Inject;

/**
 * 登录方式选择界面<BR>
 * TODO 根据登录是否成功，关闭该页面
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/27]
 */
public class LoginWayActivity extends BasicTitleBarActivity {
  @Bind(R.id.btn_login_web_chat) TextView mBtnLoginWebChat;
  @Bind(R.id.btn_login_phone) TextView mBtnLoginPhone;
  @Bind(R.id.btn_login_qq) TextView mBtnLoginQq;
  @Bind(R.id.other_way_register) TextView mOtherWayRegister;
  @Bind(R.id.other_way_sina) TextView mOtherWaySina;
  @Bind(R.id.other_way_container) RelativeLayout otherWayContainer;
  @Inject AuthService mAuthService;

  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    ShareSDK.initSDK(this);
  }

  @Override public int getLayoutId() {
    return R.layout.activity_login_way;
  }

  @OnClick({
      R.id.btn_login_web_chat, R.id.btn_login_phone, R.id.btn_login_qq, R.id.other_way_register,
      R.id.other_way_sina
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_web_chat:
        //ShareSDK.initSDK(this);
        // TODO 微信登录
        Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);

        wechat.setPlatformActionListener(new PlatformActionListener() {
          @Override
          public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (platform.isAuthValid()) {
              PlatformDb platDB = platform.getDb();//获取数平台数据DB
              //通过DB获取各种数据
              //platDB.getToken();
              //platDB.getUserGender();
              //platDB.getUserIcon();
             init(platDB.getUserId(),platDB.getUserName());
            }
          }

          @Override public void onError(Platform platform, int i, Throwable throwable) {

          }

          @Override public void onCancel(Platform platform, int i) {

          }
        });
        wechat.authorize();

        break;
      case R.id.btn_login_phone:
        startActivity(LoginActivity.makeIntent(this, LoginActivity.LOGIN_WAY_PASSWORD));
        break;
      case R.id.btn_login_qq:
        Platform qq= ShareSDK.getPlatform(this, QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
          @Override
          public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (platform.isAuthValid()) {
              PlatformDb platDB = platform.getDb();//获取数平台数据DB
              //通过DB获取各种数据
              //platDB.getToken();
              //platDB.getUserGender();
              //platDB.getUserIcon();
              init(platDB.getUserId(),platDB.getUserName());
            }
          }

          @Override public void onError(Platform platform, int i, Throwable throwable) {

          }

          @Override public void onCancel(Platform platform, int i) {

          }
        });
        qq.authorize();
        // TODO QQ登录
        break;
      case R.id.other_way_register:
        // TODO 注册
        startActivity(new Intent(this, RegisterActivity.class));
        break;
      case R.id.other_way_sina:
        Platform sina= ShareSDK.getPlatform(this, SinaWeibo.NAME);
        sina.setPlatformActionListener(new PlatformActionListener() {
          @Override
          public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (platform.isAuthValid()) {
              PlatformDb platDB = platform.getDb();//获取数平台数据DB
              //通过DB获取各种数据
              //platDB.getToken();
              //platDB.getUserGender();
              //platDB.getUserIcon();
              init(platDB.getUserId(),platDB.getUserName());
            }
          }

          @Override public void onError(Platform platform, int i, Throwable throwable) {

          }

          @Override public void onCancel(Platform platform, int i) {

          }
        });
        sina.authorize();
        // TODO 微博
        break;
    }
  }

  private void inject() {
    DaggerServiceComponent.builder()
        .serviceModule(new ServiceModule())
        .globalModule(new GlobalModule((CustomApp) getApplication()))
        .build()
        .inject(this);
  }

  private void authorize(Platform plat) {
    if (plat == null) {
      return;
    }

    plat.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
        authorize(arg0);
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
        String userId = arg2.get("id").toString();//ID
        String nickName = arg2.get("name").toString();//用户名
        init(userId, nickName);
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    });
    // true不使用SSO授权，false使用SSO授权
    plat.SSOSetting(true);
    //获取用户资料
    plat.showUser(null);
  }

  private void init(String userid, String userName) {
    manageRpcCall(mAuthService.thirdLogin(new ThirdLoginParams(userid, userName)),
        new UiRpcSubscriber<UserInfo>(this) {

          @Override protected void onSuccess(UserInfo info) {
            getComponent().loginSession().login(info);
            startActivity(new Intent(LoginWayActivity.this, MainTabActivity.class));
          }

          @Override protected void onEnd() {
            closeProgressDialog();
          }
        });
  }

  @Override protected void onLogin() {
    super.onLogin();
    finish();
  }
}
