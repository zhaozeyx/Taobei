package com.hengrtec.taobei.database.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by zhaozeyang on 15/7/1.
 */
@RealmClass
public class MessageModel extends RealmObject implements Parcelable {
  public static final int MSG_READ = 1;
  public static final int MSG_UN_READ = 0;
  public static final int MSG_TYPE_SYSTEM = 1;
  public static final int MSG_TYPE_PROMOTION = 2;
  public static final int MSG_TYPE_PRAISED = 3;

  public static final String COLUMNS_MESSAGE_ID = "mMsgId";
  public static final String COLUMNS_MESSAGE_TYPE = "mMessageType";

  @PrimaryKey
  private String mMsgId;
  private long msgTime;
  private String msgContent;
  private int mStatus;
  private int mMessageType;

  public MessageModel(long msgTime, String msgContent) {
    this.msgTime = msgTime;
    this.msgContent = msgContent;
  }

  public MessageModel() {

  }

  public void setMsgTime(long msgTime) {
    this.msgTime = msgTime;
  }

  public long getMsgTime() {
    return this.msgTime;
  }

  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }

  public String getMsgContent() {
    return this.msgContent;
  }

  public int getStatus() {
    return mStatus;
  }

  public void setStatus(int mStatus) {
    this.mStatus = mStatus;
  }

  public String getMsgId() {
    return mMsgId;
  }

  public void setMsgId(String mMsgId) {
    this.mMsgId = mMsgId;
  }

  public int getMessageType() {
    return mMessageType;
  }

  public void setMessageType(int mMessageType) {
    this.mMessageType = mMessageType;
  }

  /**
   * 备用字段:整型
   */
  private int reserveInt1;

  /**
   * 备用字段:整型
   */
  private int reserveInt2;

  /**
   * 备用字段:long型
   */
  private long reserveLong1;

  /**
   * 备用字段:long型
   */
  private long reserveLong2;

  /**
   * 备用字段:字符串
   */
  private String reserveStr1;

  /**
   * 备用字段:字符串
   */
  private String reserveStr2;

  /**
   * 备用字段:字符串
   */
  private String reserveStr3;

  /**
   * 备用字段:字符串
   */
  private String reserveStr4;

  /**
   * 从服务器返回的json字符串
   */
  private String serverData;

  /**
   * 本地json字符串
   */
  private String extraData;

  public int getReserveInt1() {
    return reserveInt1;
  }

  public void setReserveInt1(int reserveInt1) {
    this.reserveInt1 = reserveInt1;
  }

  public int getReserveInt2() {
    return reserveInt2;
  }

  public void setReserveInt2(int reserveInt2) {
    this.reserveInt2 = reserveInt2;
  }

  public long getReserveLong1() {
    return reserveLong1;
  }

  public void setReserveLong1(long reserveLong1) {
    this.reserveLong1 = reserveLong1;
  }

  public long getReserveLong2() {
    return reserveLong2;
  }

  public void setReserveLong2(long reserveLong2) {
    this.reserveLong2 = reserveLong2;
  }

  public String getReserveStr1() {
    return reserveStr1;
  }

  public void setReserveStr1(String reserveStr1) {
    this.reserveStr1 = reserveStr1;
  }

  public String getReserveStr2() {
    return reserveStr2;
  }

  public void setReserveStr2(String reserveStr2) {
    this.reserveStr2 = reserveStr2;
  }

  public String getReserveStr3() {
    return reserveStr3;
  }

  public void setReserveStr3(String reserveStr3) {
    this.reserveStr3 = reserveStr3;
  }

  public String getReserveStr4() {
    return reserveStr4;
  }

  public void setReserveStr4(String reserveStr4) {
    this.reserveStr4 = reserveStr4;
  }

  public String getServerData() {
    return serverData;
  }

  public void setServerData(String serverData) {
    this.serverData = serverData;
  }

  public String getExtraData() {
    return extraData;
  }

  public void setExtraData(String extraInfo) {
    this.extraData = extraInfo;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mMsgId);
    dest.writeLong(this.msgTime);
    dest.writeString(this.msgContent);
    dest.writeInt(this.mStatus);
    dest.writeInt(this.mMessageType);
    dest.writeInt(this.reserveInt1);
    dest.writeInt(this.reserveInt2);
    dest.writeLong(this.reserveLong1);
    dest.writeLong(this.reserveLong2);
    dest.writeString(this.reserveStr1);
    dest.writeString(this.reserveStr2);
    dest.writeString(this.reserveStr3);
    dest.writeString(this.reserveStr4);
    dest.writeString(this.serverData);
    dest.writeString(this.extraData);
  }

  protected MessageModel(Parcel in) {
    this.mMsgId = in.readString();
    this.msgTime = in.readLong();
    this.msgContent = in.readString();
    this.mStatus = in.readInt();
    this.mMessageType = in.readInt();
    this.reserveInt1 = in.readInt();
    this.reserveInt2 = in.readInt();
    this.reserveLong1 = in.readLong();
    this.reserveLong2 = in.readLong();
    this.reserveStr1 = in.readString();
    this.reserveStr2 = in.readString();
    this.reserveStr3 = in.readString();
    this.reserveStr4 = in.readString();
    this.serverData = in.readString();
    this.extraData = in.readString();
  }

  public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
    @Override
    public MessageModel createFromParcel(Parcel source) {
      return new MessageModel(source);
    }

    @Override
    public MessageModel[] newArray(int size) {
      return new MessageModel[size];
    }
  };
}
