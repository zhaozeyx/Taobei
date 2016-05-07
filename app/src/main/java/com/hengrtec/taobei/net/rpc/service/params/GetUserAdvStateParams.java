/*
 * 文件名: GetUserAdvStateParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/5
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 获取用户广告状态接口<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/5]
 */
public class GetUserAdvStateParams {
  public String userId;
  public String advId;

  public GetUserAdvStateParams(String userId, String advId) {
    this.userId = userId;
    this.advId = advId;
  }
}
