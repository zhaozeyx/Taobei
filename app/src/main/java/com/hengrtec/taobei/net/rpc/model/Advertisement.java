/*
 * 文件名: Advertisement
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/20
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.model;

import java.util.List;

/**
 * 广告<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/20]
 */
public class Advertisement {

  /**
   * activities : 无
   * advId : 6
   * advType : 2
   * area : 烟台
   * bbjPrice : 10
   * benefitType : 1
   * categoryId : 1
   * collected : false
   * collectionNum : 0
   * collectors :
   * commentsHotList : []
   * commentsTimeList : []
   * commentsTotal : 0
   * companyName :
   * cost : 10
   * createDate : 2016-04-11
   * creator : admin
   * endTime : 2016-04-12 10:21:00
   * fee : 1000
   * filePath : http://139.129.133.223:8080/taobei/uploads/adv/adv6.png
   * hearts : 0
   * heartsClicked : false
   * heartsUser :
   * leftTime :
   * moneyPrice :
   * name : Inspection灵越5000系列金属板
   * planCounts : 1000
   * playCounts : 8
   * playType : 1
   * price : 10~20
   * questionMode : 1
   * result :
   * startTime : 2016-04-21 11:11:35
   * startTimeE :
   * startTimeS :
   * state : 0
   * subTitle : titile6
   * summary : Inspection灵越5000系列金属板
   * tags : 无
   * tagsArr : ["无"]
   * thumbnail : http://139.129.133.223:8080/taobei/uploads/adv/adv6.png
   * totalTime :
   * userAdvState :
   */
  private String activities;
  private int advId;
  private String advType;
  private String area;
  private String bbjPrice;
  private String benefitType;
  private int categoryId;
  private boolean collected;
  private int collectionNum;
  private String collectors;
  private int commentsTotal;
  private String companyName;
  private int cost;
  private String createDate;
  private String creator;
  private String endTime;
  private int fee;
  private String filePath;
  private int hearts;
  private boolean heartsClicked;
  private String heartsUser;
  private String leftTime;
  private String moneyPrice;
  private String name;
  private int planCounts;
  private int playCounts;
  private String playType;
  private String price;
  private String questionMode;
  private String result;
  private String startTime;
  private String startTimeE;
  private String startTimeS;
  private String state;
  private String subTitle;
  private String summary;
  private String tags;
  private String thumbnail;
  private String totalTime;
  private String userAdvState;
  private List<?> commentsHotList;
  private List<?> commentsTimeList;
  private List<String> tagsArr;

  public String getActivities() {
    return activities;
  }

  public void setActivities(String activities) {
    this.activities = activities;
  }

  public int getAdvId() {
    return advId;
  }

  public void setAdvId(int advId) {
    this.advId = advId;
  }

  public String getAdvType() {
    return advType;
  }

  public void setAdvType(String advType) {
    this.advType = advType;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getBbjPrice() {
    return bbjPrice;
  }

  public void setBbjPrice(String bbjPrice) {
    this.bbjPrice = bbjPrice;
  }

  public String getBenefitType() {
    return benefitType;
  }

  public void setBenefitType(String benefitType) {
    this.benefitType = benefitType;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public boolean isCollected() {
    return collected;
  }

  public void setCollected(boolean collected) {
    this.collected = collected;
  }

  public int getCollectionNum() {
    return collectionNum;
  }

  public void setCollectionNum(int collectionNum) {
    this.collectionNum = collectionNum;
  }

  public String getCollectors() {
    return collectors;
  }

  public void setCollectors(String collectors) {
    this.collectors = collectors;
  }

  public int getCommentsTotal() {
    return commentsTotal;
  }

  public void setCommentsTotal(int commentsTotal) {
    this.commentsTotal = commentsTotal;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public int getFee() {
    return fee;
  }

  public void setFee(int fee) {
    this.fee = fee;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public int getHearts() {
    return hearts;
  }

  public void setHearts(int hearts) {
    this.hearts = hearts;
  }

  public boolean isHeartsClicked() {
    return heartsClicked;
  }

  public void setHeartsClicked(boolean heartsClicked) {
    this.heartsClicked = heartsClicked;
  }

  public String getHeartsUser() {
    return heartsUser;
  }

  public void setHeartsUser(String heartsUser) {
    this.heartsUser = heartsUser;
  }

  public String getLeftTime() {
    return leftTime;
  }

  public void setLeftTime(String leftTime) {
    this.leftTime = leftTime;
  }

  public String getMoneyPrice() {
    return moneyPrice;
  }

  public void setMoneyPrice(String moneyPrice) {
    this.moneyPrice = moneyPrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPlanCounts() {
    return planCounts;
  }

  public void setPlanCounts(int planCounts) {
    this.planCounts = planCounts;
  }

  public int getPlayCounts() {
    return playCounts;
  }

  public void setPlayCounts(int playCounts) {
    this.playCounts = playCounts;
  }

  public String getPlayType() {
    return playType;
  }

  public void setPlayType(String playType) {
    this.playType = playType;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getQuestionMode() {
    return questionMode;
  }

  public void setQuestionMode(String questionMode) {
    this.questionMode = questionMode;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getStartTimeE() {
    return startTimeE;
  }

  public void setStartTimeE(String startTimeE) {
    this.startTimeE = startTimeE;
  }

  public String getStartTimeS() {
    return startTimeS;
  }

  public void setStartTimeS(String startTimeS) {
    this.startTimeS = startTimeS;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(String totalTime) {
    this.totalTime = totalTime;
  }

  public String getUserAdvState() {
    return userAdvState;
  }

  public void setUserAdvState(String userAdvState) {
    this.userAdvState = userAdvState;
  }

  public List<?> getCommentsHotList() {
    return commentsHotList;
  }

  public void setCommentsHotList(List<?> commentsHotList) {
    this.commentsHotList = commentsHotList;
  }

  public List<?> getCommentsTimeList() {
    return commentsTimeList;
  }

  public void setCommentsTimeList(List<?> commentsTimeList) {
    this.commentsTimeList = commentsTimeList;
  }

  public List<String> getTagsArr() {
    return tagsArr;
  }

  public void setTagsArr(List<String> tagsArr) {
    this.tagsArr = tagsArr;
  }
}
