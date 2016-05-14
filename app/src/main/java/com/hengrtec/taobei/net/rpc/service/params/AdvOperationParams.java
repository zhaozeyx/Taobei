/*
 * 文件名: AdvOperationParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/12
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 广告操作接口<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/12]
 */
public class AdvOperationParams {
  public static final String FLAG_TRUE = "1";
  public static final String FLAG_FALSE = "0";
  public String flag;
  public String advId;
  public String userId;

  public AdvOperationParams(String flag, String advId, int userId) {
    this.flag = flag;
    this.advId = advId;
    this.userId = String.valueOf(userId);
  }
}
