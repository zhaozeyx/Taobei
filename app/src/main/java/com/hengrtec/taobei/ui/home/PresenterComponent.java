/*
 * 文件名: HomeActivityComponent
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import com.hengrtec.taobei.injection.ActivityComponent;
import com.hengrtec.taobei.ui.PerActivity;
import dagger.Component;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
@PerActivity
@Component(dependencies = ActivityComponent.class, modules = PresenterModule.class)
public interface PresenterComponent extends ActivityComponent {

  AdvertisementListPresenter advertisementListPresenter();

  void inject(HomeFragment fragment);

  void inject(AdvertisementListFragment fragment);
}
