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
public class GetAddCardParams {
  public String userId;
  public String accountType;
  public String bankNameCode;
  public String accountUserName;
  public String accountName;
  public String bankName;


  public GetAddCardParams(String userId, String accountType, String bankNameCode,
      String accountUserName, String accountName,String bankName) {
    this.userId = userId;
    this.accountType = accountType;
    this.bankNameCode = bankNameCode;
    this.accountUserName = accountUserName;
    this.accountName = accountName;
    this.bankName = bankName;
  }
}
