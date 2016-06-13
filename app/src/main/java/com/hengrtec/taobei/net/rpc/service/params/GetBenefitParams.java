/*
 * 文件名: GetBenefitParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/6/2
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
 * @version [Taobei Client V20160411, 16/6/2]
 */
public class GetBenefitParams {
  public String watchId;
  public int userId;
  public int advId;
  public int benefitType;

  public GetBenefitParams(String watchId, int userId, int advId, int benefitType) {
    this.watchId = watchId;
    this.userId = userId;
    this.advId = advId;
    this.benefitType = benefitType;
  }

  public GetBenefitParams(int userId, int advId) {
    this.userId = userId;
    this.advId = advId;
  }
}
