/*
 * 文件名: AllFragement
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
public class AllFragment extends BaseWatchedListFragment {
  @Override
  protected int getListType() {
    return WatchedListParams.FLAG_ALL;
  }
}
