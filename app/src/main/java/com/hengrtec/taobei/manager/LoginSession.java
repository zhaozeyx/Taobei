/*
 * 文件名: LogInSession
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/27
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.manager;

import android.text.TextUtils;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.ui.login.event.LoginEvent;
import com.hengrtec.taobei.ui.login.event.LogoutEvent;
import com.hengrtec.taobei.utils.JsonConverter;
import com.hengrtec.taobei.utils.preferences.CustomAppPreferences;

/**
 * Session<BR>
 * 处理登录登出相关
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/27]
 */
public class LoginSession {

  private CustomApp mContext;
  private UserInfo mUserInfo;

  public LoginSession(CustomApp app) {
    mContext = app;
    switchUserInfo();
  }

  public int getUserId() {
    return mUserInfo.getUserId();
  }

  private void switchUserInfo() {
    String userJson = mContext.getGlobalComponent().appPreferences().getString
        (CustomAppPreferences.KEY_USER_INFO, "");
    if (!TextUtils.isEmpty(userJson)) {
      mUserInfo = JsonConverter.jsonToObject(UserInfo.class, userJson);
      return;
    }
    mUserInfo = new UserInfo();
  }

  public UserInfo getUserInfo() {
    return mUserInfo;
  }

  public void login(UserInfo userInfo) {
    mContext.getGlobalComponent().appPreferences().put(CustomAppPreferences.KEY_USER_INFO,
        JsonConverter
            .objectToJson(userInfo));
    mContext.getGlobalComponent().appPreferences().put(CustomAppPreferences.KEY_USER_ID, userInfo
        .getUserId());
    mContext.getGlobalComponent().appPreferences().put(CustomAppPreferences.KEY_USER_TYPE,
        userInfo.getType());
    mContext.getGlobalComponent().getGlobalBus().post(new LoginEvent());
    onLoginStatusChanged();
  }

  public void logout() {
    mContext.getGlobalComponent().appPreferences().put(CustomAppPreferences.KEY_USER_INFO,
        "");
    mContext.getGlobalComponent().getGlobalBus().post(new LogoutEvent());
  }

  public void onLoginStatusChanged() {
    switchUserInfo();
  }
}
