/*
 * 文件名: LikeCommentParams
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
 * 评论<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/12]
 */
public class LikeCommentParams {
  String commentId;
  String userId;

  public LikeCommentParams(String commentId, int userId) {
    this.commentId = commentId;
    this.userId = String.valueOf(userId);
  }
}
