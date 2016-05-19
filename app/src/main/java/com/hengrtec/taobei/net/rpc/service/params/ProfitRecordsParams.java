/*
 * 文件名: ChartRecordsParams
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/18
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
 * @version [Taobei Client V20160411, 16/5/18]
 */
public class ProfitRecordsParams {
  public int userId;
  public int pageNum;
  public int pageSize;

  public ProfitRecordsParams(int userId, int pageNum, int pageSize) {
    this.userId = userId;
    this.pageNum = pageNum;
    this.pageSize = pageSize;
  }
}
