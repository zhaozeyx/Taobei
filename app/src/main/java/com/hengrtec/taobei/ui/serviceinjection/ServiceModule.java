/*
 * 文件名: AppServiceModule
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.serviceinjection;

import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RetrofitFactory;
import com.hengrtec.taobei.net.RpcCallManager;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.AppService;
import com.hengrtec.taobei.net.rpc.service.AuthService;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.utils.preferences.CustomAppPreferences;
import dagger.Module;
import dagger.Provides;

/**
 * 服务器接口 Module<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
@Module(includes = GlobalModule.class)
public class ServiceModule {

  @Provides
  public AppService providesAppService() {
    return RetrofitFactory.createAppService();
  }

  @Provides
  public AdvertisementService providesAdvertisementService() {
    return RetrofitFactory.createAdvertisementService();
  }

  @Provides
  public UserService providesUserService(CustomAppPreferences appPreferences) {
    return RetrofitFactory.createUserService(appPreferences);
  }

  @Provides
  public AuthService providesAuthService(CustomAppPreferences appPreferences) {
    return RetrofitFactory.createAuthService(appPreferences);
  }

  @Provides
  public RpcCallManager providerRpcCallManager() {
    return new RpcCallManager.RpcCallManagerImpl();
  }
}

