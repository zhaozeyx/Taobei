/*
 * 文件名: GetCommentListParams
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
 * 获得评论列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/13]
 */
public class GetCommentListParams {
  public int advId;
  public int userId;
  public int pageNo;
  public int pageSize;
  public int orderBy;

  public GetCommentListParams(int advId, int userId, int pageNo, int pageSize, int orderBy) {
    this.advId = advId;
    this.userId = userId;
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.orderBy = orderBy;
  }
}
