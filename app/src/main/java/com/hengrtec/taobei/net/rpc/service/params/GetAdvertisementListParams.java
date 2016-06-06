/*
 * 文件名: AdvertisemetParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/20
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/20]
 */
public class GetAdvertisementListParams {

  public String state;
  public String userId;
  public int pageNo;
  public int pageSize;

  public GetAdvertisementListParams(String state, String userId, int page, int count) {
    this.state = state;
    this.userId = userId;
    this.pageNo = page;
    this.pageSize = count;
  }
}
