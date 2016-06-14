/*
 * 文件名: ActivityComponent
 * 版    权：  Copyright Paitao Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/15
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.injection;

import com.hengrtec.taobei.manager.UpgradeHelper;
import com.hengrtec.taobei.net.RpcCallManager;
import com.squareup.otto.Bus;
import dagger.Component;
import javax.inject.Singleton;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/15]
 */
@ActivityScope
@Component(dependencies = GlobalComponent.class, modules = ActivityModule.class)
public interface ActivityComponent extends GlobalComponent {

  @ActivityBus
  Bus getActivityBus();

  RpcCallManager rpcCallManager();

  UpgradeHelper upgradeHelper();
}
