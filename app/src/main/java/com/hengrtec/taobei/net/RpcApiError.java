/*
 * 文件名: RpcApiError
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net;

/**
 * 服务器返回的错误信息<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/18]
 */
public class RpcApiError {
  private int mHttpCode;
  private String mMessage;

  public RpcApiError(int httpCode, String message) {
    mHttpCode = httpCode;
    mMessage = message;
  }

  public int getHttpCode() {
    return mHttpCode;
  }

  public String getMessage() {
    return mMessage;
  }
}
