/*
 * 文件名: MessageDetailActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/23
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.database.model.MessageModel;
import com.hengrtec.taobei.net.rpc.model.GrabMyMessageModel;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.profile.event.MessageDeleteEvent;
import com.hengrtec.taobei.utils.DateUtils;
import com.hengrtec.taobei.utils.JsonConverter;
import java.util.Date;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/23]
 */
public class MessageDetailActivity extends BasicTitleBarActivity {
  public static final String BUNDLE_KEY_MODEL = "bundle_key_model";
  @Bind(R.id.message_title)
  TextView mMessageTitleView;
  @Bind(R.id.message_type)
  TextView mMessageTypeView;
  @Bind(R.id.date)
  TextView mDateView;
  @Bind(R.id.content)
  TextView mContentView;

  private MessageModel mMessageModel;

  @Override
  public int getLayoutId() {
    return R.layout.activity_message_detail;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    mMessageModel = getIntent().getParcelableExtra(BUNDLE_KEY_MODEL);
    initView();
  }

  private void initView() {
    GrabMyMessageModel.MessageBean bean = JsonConverter.jsonToObject(GrabMyMessageModel
        .MessageBean.class, mMessageModel.getMsgContent());
    mMessageTitleView.setText(bean.getMsgTitle());
    mMessageTypeView.setText(bean.getMsgType());
    mDateView.setText(DateUtils.getFormatDateTime(new Date(bean.getMsgTime()), DateUtils
        .FORMAT_YEAR_MONTH_DAY));
    mContentView.setText(bean.getMsgContent());
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_message_detail);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    setRightImgButton(R.mipmap.icon_title_bar_delete, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
        getComponent().getGlobalBus().post(new MessageDeleteEvent(mMessageModel.getMsgId()));
      }
    });
    return true;
  }

  public static Intent makeIntent(Context context, MessageModel model) {
    Intent intent = new Intent(context, MessageDetailActivity.class);
    intent.putExtra(BUNDLE_KEY_MODEL, model);
    return intent;
  }
}
