/*
 * 文件名: WatchedListParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/26
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
 * @version [Taobei Client V20160411, 16/5/26]
 */
public class WatchedListParams {
  public static final int FLAG_ALL = 1;
  public static final int FLAG_NOT_ACCEPT = 2;
  public int userId;
  public int flag;
  public String seeDate;

  public WatchedListParams(int userId, int flag, String seeDate) {
    this.userId = userId;
    this.flag = flag;
    this.seeDate = seeDate;
  }
}
