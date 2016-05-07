/*
 * 文件名: GetVerifyCodeParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/29
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 获取验证码接口<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/29]
 */
public class GetVerifyCodeParams {
  private String mobileNo;

  public GetVerifyCodeParams(String mobileNo) {
    this.mobileNo = mobileNo;
  }
}
