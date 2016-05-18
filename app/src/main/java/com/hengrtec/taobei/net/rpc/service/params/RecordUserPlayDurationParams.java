/*
 * 文件名: RecordUserPlayDurationParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/17
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

/**
 * 记录用户播放时间<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public class RecordUserPlayDurationParams {
  int userId;
  int advId;
  int timeLength;

  public RecordUserPlayDurationParams(int userId, int advId, int timeLength) {
    this.userId = userId;
    this.advId = advId;
    this.timeLength = timeLength;
  }
}
