/*
 * 文件名: CustomAppPreferences
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/25
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.utils.preferences;

import android.content.Context;
import javax.inject.Inject;
import net.grandcentrix.tray.AppPreferences;

/**
 * 全局Preference<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/25]
 */
public class CustomAppPreferences extends AppPreferences {

  public static final String KEY_USER_ID = "uid";
  public static final String KEY_USER_TYPE = "user_type";
  public static final String KEY_HAS_VIEW_GUIDE = "has_view_guide";
  public static final String KEY_COOKIE_SESSION_ID = "cookie_session_id";
  public static final String KEY_USER_INFO = "key_user_info";

  @Inject
  public CustomAppPreferences(Context context) {
    super(context);
  }

  public void saveUserId(String userId) {
    put(KEY_USER_ID, userId);
  }

  public String getUserId() {
    return getString(KEY_USER_ID, "");
  }

  public boolean showGuideView() {
    return getBoolean(KEY_HAS_VIEW_GUIDE, true);
  }

  public void setNoGuideView() {
    put(KEY_HAS_VIEW_GUIDE, false);
  }

}
