package com.hengrtec.taobei;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.database.adapter.RealmFactory;
import com.hengrtec.taobei.injection.DaggerGlobalComponent;
import com.hengrtec.taobei.injection.GlobalComponent;
import com.hengrtec.taobei.injection.GlobalModule;

/**
 * Created by zhaozeyang on 16/4/13.
 */
public class CustomApp extends Application {

  private GlobalComponent mGlobalComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    Logger.init();
    Fresco.initialize(this);
    RealmFactory.initRealm(this);

    mGlobalComponent = DaggerGlobalComponent.builder().globalModule(new GlobalModule(this)).build();
    JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
    JPushInterface.init(this);
  }

  public GlobalComponent getGlobalComponent() {
    return mGlobalComponent;
  }


}
