/*
 * 文件名: RegisterParams
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

import com.hengrtec.taobei.utils.MD5Encoder;

/**
 * 注册<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/29]
 */
public class PayRetPswParams {
  public String mobileNo;
  public String oldPayPwd;
  public String payPwd;

  public PayRetPswParams(String mobileNo, String oldPayPwd, String payPwd) {

    this.mobileNo = mobileNo;
    this.oldPayPwd = MD5Encoder.encode(oldPayPwd);
    this.payPwd = MD5Encoder.encode(payPwd);

  }
}
