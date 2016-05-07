/*
 * 文件名: GetAdvertisementDetailParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/25
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 获得列表详情数据<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/25]
 */
public class GetAdvertisementDetailParams {
  public String userId;
  public int advId;

  public GetAdvertisementDetailParams(String userId, int advId) {
    this.userId = userId;
    this.advId = advId;
  }

  public String getUserId() {
    return userId;
  }

  public int getAdvId() {
    return advId;
  }
}
