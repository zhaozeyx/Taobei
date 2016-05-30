/*
 * 文件名: AwardReceiveClickedEvent
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/28
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home.event;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/28]
 */
public class AwardReceiveClickedEvent {
  public String benefitType;

  public AwardReceiveClickedEvent(String benefitType) {
    this.benefitType = benefitType;
  }
}
