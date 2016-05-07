/*
 * 文件名: AdvertisementListView
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

import com.hengrtec.taobei.net.rpc.model.Advertisement;
import com.hengrtec.taobei.ui.IView;
import java.util.List;

/**
 * 广告列表View<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/21]
 */
public interface AdvertisementListView extends IView {
  void onDataEmpty();

  void onDataChanged(List<Advertisement> advertisementList, boolean moreData);
}
