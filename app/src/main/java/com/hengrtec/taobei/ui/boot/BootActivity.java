/*
 * 文件名: HomeActivity
 * 版    权：  Copyright Paitao Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/15
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.boot;

import android.content.Intent;
import android.os.Bundle;
import com.hengrtec.taobei.ui.basic.BasicActivity;
import com.hengrtec.taobei.ui.login.LoginWayActivity;
import com.hengrtec.taobei.ui.tab.MainTabActivity;

/**
 * 主页<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/15]
 */
public class BootActivity extends BasicActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dispatchForwardUi();
  }


  private void dispatchForwardUi() {
    Intent intent;
    // TODO 判断是否登录？？？判断是否第一次启动
    if (getComponent().appPreferences().showGuideView()) {
      intent = new Intent(this, LeadActivity.class);
    } else if (getComponent().isLogin()) {
      intent = new Intent(this, MainTabActivity.class);
    } else {
      // TODO 跳转到登录界面
      intent = new Intent(this, LoginWayActivity.class);
    }
    startActivity(intent);
    finish();
  }

}
