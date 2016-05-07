/*
 * 文件名: GetAdvListParams
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
 * 获取广告问题列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/5]
 */
public class GetAdvQuestionListParams {
  public String userId;
  public String advId;

  public GetAdvQuestionListParams(String userId, String advId) {
    this.userId = userId;
    this.advId = advId;
  }
}
