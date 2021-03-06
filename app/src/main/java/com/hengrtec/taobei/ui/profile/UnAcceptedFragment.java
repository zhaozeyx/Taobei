/*
 * 文件名: UnAcceptedFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/26
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import com.hengrtec.taobei.net.rpc.service.params.WatchedListParams;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/26]
 */
public class UnAcceptedFragment extends BaseWatchedListFragment {
  @Override
  protected int getListType() {
    return WatchedListParams.FLAG_NOT_ACCEPT;
  }

  @Override
  protected void updateList(int advId) {
    removeItem(advId);
  }

  @Override
  protected void onDataLoadFinished(boolean hasData) {
    super.onDataLoadFinished(hasData);
    getComponent().getGlobalBus().post(new UnAcceptDataLoadEvent(hasData));
  }

  public class UnAcceptDataLoadEvent {
    public boolean hasData;

    public UnAcceptDataLoadEvent(boolean hasData) {
      this.hasData = hasData;
    }
  }
}
