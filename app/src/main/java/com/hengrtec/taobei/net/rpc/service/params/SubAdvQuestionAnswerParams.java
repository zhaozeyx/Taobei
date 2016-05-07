/*
 * 文件名: SubAdvQuestionAnswerParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/7
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.params;

import java.util.HashMap;

/**
 * 提交问题答案接口<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/7]
 */
public class SubAdvQuestionAnswerParams {
  public String watchId;
  public String userId;
  public String advId;
  public HashMap<String, String> qMap;

  public SubAdvQuestionAnswerParams(String watchId, String userId, String advId, HashMap<String,
      String> qMap) {
    this.watchId = watchId;
    this.userId = userId;
    this.advId = advId;
    this.qMap = qMap;
  }
}
