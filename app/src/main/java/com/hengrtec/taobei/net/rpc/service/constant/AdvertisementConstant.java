/*
 * 文件名: AdvertisementConstant
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/21
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service.constant;

/**
 * 广告常量<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/21]
 */
public class AdvertisementConstant {
  /**
   * 广告状态: 未投放
   */
  public static final String ADV_STATE_UN_LAUNCH = "0";

  /**
   * 广告状态: 投放中(为您精选)
   */
  public static final String ADV_STATE_LAUNCHING = "1";

  /**
   * 广告状态: 结束
   */
  public static final String ADV_STATE_END = "2";

  /**
   * 广告状态: 取消
   */
  public static final String ADV_STATE_CANCEL = "3";

  /**
   * 广告状态: 异常
   */
  public static final String ADV_STATE_ERROR = "4";

  /**
   * 广告类型: 视频
   */
  public static final String ADV_TYPE_VIDEO = "1";

  /**
   * 广告类型: 图片
   */
  public static final String ADV_TYPE_PIC = "2";

  /**
   * 广告类型: 微场景广告
   */
  public static final String ADV_TYPE_H5 = "3";

  /**
   * 收益类型: 真实货币(就是钱啊，亲~~~)
   */
  public static final String ADV_BENEFIT_TYPE_REALITY_CURRENCY = "1";

  /**
   * 收益类型: 虚拟货币(贝贝金)
   */
  public static final String ADV_BENEFIT_TYPE_VIRTUAL_CURRENCY = "0";

  /**
   * 用户观看广告状态: 未观看
   */
  public static final String ADV_USER_ADV_STATE_UN_VIEW = "0";

  /**
   * 用户观看广告状态: 已观看领取过红包
   */
  public static final String ADV_USER_ADV_STATE_VIEWED_RCEIVED_RED_BAG = "2";

  /**
   * 用户观看广告状态: 已观看获得过收益
   */
  public static final String ADV_USER_ADV_STATE_VIEWED_GET_PROFIT = "3";

  /**
   * 用户观看广告状态: 已观看未回答问题
   */
  public static final String ADV_USER_ADV_STATE_HAS_VIEWED_NOT_COMMIT_QUESTION = "4";

  /**
   * 问题类型: 选择题 有正确答案
   */
  public static final String ADV_QUESTION_TYPE_CHOICE_WITH_ANSWER = "1";

  /**
   * 问题类型: 选择题 无正确答案
   */
  public static final String ADV_QUESTION_TYPE_CHOICE_NO_ANSWER = "2";

  /**
   * 问题类型: 问答题
   */
  public static final String ADV_QUESTION_TYPE_EASY_QUESTION = "3";

  /**
   * 是否有问题: 有
   */
  public static final String ADV_HAS_QUESTION = "1";

  /**
   * 是否有问题: 无
   */
  public static final String ADV_NO_QUESTION = "0";

}
