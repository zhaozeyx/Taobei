/*
 * 文件名: GetCollectionsParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/13
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 获取收藏列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/13]
 */
public class GetSettingsParams {
  public int userId;
  public String isLike;
  public String willingShare;
  public String suggestion;
  public String contact;
  public GetSettingsParams(int userId,String isLike,String willingShare, String suggestion,String contact) {
    this.userId = userId;
    this.isLike = isLike;
    this.willingShare = willingShare;
    this.suggestion = suggestion;
    this.contact = contact;
  }
}
