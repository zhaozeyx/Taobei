/*
 * 文件名: SignInDialogItemView
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/16
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;

/**
 * 签到Item<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/16]
 */
public class SignInDialogItemView extends FrameLayout {
  public static final int STATE_DISABLED = 0;
  public static final int STATE_ENABLED = 1;
  public static final int STATE_CHECKED = 2;

  @Bind(R.id.day)
  TextView mDayView;
  @Bind(R.id.award_info)
  TextView mAwardInfoView;
  @Bind(R.id.container)
  FrameLayout mAwardInfoContainer;
  @Bind(R.id.icon_received)
  ImageView mIconReceivedView;
  @Bind(R.id.cover)
  ImageView mCoverView;
  @Bind(R.id.award_icon)
  ImageView mAwardIconView;

  public SignInDialogItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sign_in_item, this,
        false);
    addView(view);
    ButterKnife.bind(this, view);
  }

  public void bindData(int day, int state, int iconId, int textId) {
    mDayView.setText(getResources().getString(R.string.dialog_award_info_day, day));
    mAwardInfoView.setText(textId);
    mAwardIconView.setImageResource(iconId);
    switch (state) {
      case STATE_DISABLED:
        mDayView.setTextColor(getResources().getColor(R.color.font_color_primary));
        mIconReceivedView.setVisibility(View.GONE);
        mCoverView.setVisibility(View.GONE);
        mCoverView.setEnabled(false);
        mDayView.setSelected(false);
        mAwardInfoContainer.setEnabled(false);
        //mAwardInfoContainer.setBackgroundResource(R.drawable.bg_dialog_sign_in_item_disabled);
        break;
      case STATE_ENABLED:
        mDayView.setTextColor(getResources().getColor(R.color.font_color_primary));
        mIconReceivedView.setVisibility(View.GONE);
        mCoverView.setVisibility(View.GONE);
        mCoverView.setEnabled(true);
        mDayView.setSelected(false);
        mAwardInfoContainer.setEnabled(true);
        //mAwardInfoContainer.setBackgroundResource(R.drawable.bg_dialog_sign_in_item_enabled);
        break;
      case STATE_CHECKED:
        mDayView.setTextColor(getResources().getColor(R.color.font_color_secondary));
        mIconReceivedView.setVisibility(View.VISIBLE);
        mCoverView.setVisibility(View.VISIBLE);
        mDayView.setSelected(true);
        mAwardInfoContainer.setEnabled(false);
        //mAwardInfoContainer.setBackgroundResource(R.drawable.bg_dialog_sign_in_item_disabled);
        break;
    }
  }
}
