package com.hengrtec.taobei.database.adapter;

import android.content.Context;
import com.hengrtec.taobei.database.model.MessageModel;

/**
 * Created by zhaozeyang on 15/7/3.
 */
public class MessageDbAdapter extends DbAdapter<MessageModel> {
  public MessageDbAdapter(Context context) {
    super(context);
  }

  @Override protected Class<MessageModel> getObjectClass() {
    return MessageModel.class;
  }
}
