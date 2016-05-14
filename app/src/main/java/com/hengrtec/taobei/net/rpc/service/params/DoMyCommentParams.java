/*
 * 文件名: DoMyCommentParams
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
 * 广告评论<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/13]
 */
public class DoMyCommentParams {
  public int advId;
  public int userId;
  public String contents;

  public DoMyCommentParams(int advId, int userId, String contents) {
    this.advId = advId;
    this.userId = userId;
    this.contents = contents;
  }
}
