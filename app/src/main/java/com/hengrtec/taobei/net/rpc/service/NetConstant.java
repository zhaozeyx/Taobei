/*
 * 文件名: NetConstant
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service;

/**
 * 服务器常量<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class NetConstant {
  public static final String BASE_URL = "http://139.129.133.223/api/";
  //public static final String BASE_URL = "http://192.168.15.201:8080/taobei/api/";

  private NetConstant() {

  }

  public class HttpCodeConstant {
    public static final int UNKNOWN_ERROR = -1;
    public static final int SUCCESS = 200;
    public static final int HTTP_ERROR_NOT_FOUND = 404;
  }
}
