/*
 * 文件名: AdvertisementListPresenter
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/21
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.app.Activity;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcCallManager;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.Advertisement;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementListParams;
import com.hengrtec.taobei.ui.IPresenter;
import com.hengrtec.taobei.ui.IView;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import java.util.List;
import javax.inject.Inject;

/**
 * 广告列表Presenter<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/21]
 */
public class AdvertisementListPresenter implements IPresenter {
  private AdvertisementListView mView;

  @Inject
  AdvertisementService mAdvertisementService;
  @Inject
  RpcCallManager rpcCallManager;

  public AdvertisementListPresenter() {

  }

  @Override
  public void onResume() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public void attachView(IView view) {
    mView = (AdvertisementListView) view;
    DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
        GlobalModule((CustomApp) ((Activity) mView.getActivityContext()).getApplication())).build
        ().inject(this);
  }

  public void getList(String state, String userId, int currentPage, final int pageCount) {
    rpcCallManager.manageRpcCall(mAdvertisementService.getAdvertisements(new
        GetAdvertisementListParams(state, userId, currentPage, pageCount)), new
        UiRpcSubscriber<List<Advertisement>>(mView.getActivityContext()) {


          @Override
          protected void onSuccess(List<Advertisement> advertisements) {
            if (null == advertisements || advertisements.size() == 0) {
              mView.onDataEmpty();
              return;
            }
            mView.onDataChanged(advertisements, advertisements.size() >= pageCount);
          }

          @Override
          protected void onEnd() {
            mView.onServiceInvokeEnd();
          }
        });
  }
}
