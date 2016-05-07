/*
 * 文件名: HomeActivityModule
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

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
@Module
public class PresenterModule {
  private Activity mActivity;

  public PresenterModule(Activity activity) {
    mActivity = activity;
  }

  @Provides
  public AdvertisementListPresenter providesAdvertisementListPresenter() {
    return new AdvertisementListPresenter();
  }
}
