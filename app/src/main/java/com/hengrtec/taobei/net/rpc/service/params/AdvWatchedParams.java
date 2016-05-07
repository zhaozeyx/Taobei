/*
 * 文件名: AdvWatchedParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/6
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
 * @version [Taobei Client V20160411, 16/5/6]
 */
public class AdvWatchedParams {
  public String watchId;
  public String userId;
  public String advId;
  public String benefitType;
  public String timeLength;

  public AdvWatchedParams(String watchId, String userId, String advId, String benefitType
      , String timeLength) {
    this.watchId = watchId;
    this.userId = userId;
    this.advId = advId;
    this.benefitType = benefitType;
    this.timeLength = timeLength;
  }
}
