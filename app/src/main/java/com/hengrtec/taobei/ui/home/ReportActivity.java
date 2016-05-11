/*
 * 文件名: ReportActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.service.constant.UserConstant;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import org.apmem.tools.layouts.FlowLayout;

/**
 * 贝贝报告界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/10]
 */
public class ReportActivity extends BasicTitleBarActivity {

  /**
   * TODO 前两张图片是不是应该替换成别的，游民，长工
   */
  private static final int[] ICON_LEVEL_MALE = new int[]{R.mipmap.icon_pinnong, R.mipmap
      .icon_pinnong
      , R.mipmap.icon_pinnong, R.mipmap.icon_zhongnong, R.mipmap.icon_funong, R.mipmap.icon_tucaizhu
      , R.mipmap.icon_tuhaozha, R.mipmap.icon_dafuweng};
  private static final int[] ICON_LEVEL_FEMALE = new int[]{R.mipmap.icon_pinnong, R.mipmap
      .icon_pinnong
      , R.mipmap.icon_pinnong, R.mipmap.icon_zhongnong, R.mipmap.icon_funong, R.mipmap.icon_tucaizhu
      , R.mipmap.icon_baifumei, R.mipmap.icon_dafuweng};

  private static final int[][] LEVEL = new int[][]{
      {0, 200}, {201, 1000}, {1001, 2000}, {2001, 10000}, {10001, 40000}, {40001, 1000000},
      {1000001, Integer.MAX_VALUE}
  };

  @Bind(R.id.report_wheel)
  ImageView mReportWheel;
  @Bind(R.id.level_icon)
  TextView mLevelIconText;
  @Bind(R.id.adv_view_count)
  TextView mAdvViewCount;
  @Bind(R.id.total_money_get)
  TextView mTotalMoneyGet;
  @Bind(R.id.total_view_time)
  TextView mTotalViewTime;
  @Bind(R.id.predict_value)
  TextView mPredictValue;
  @Bind(R.id.medal_container)
  FlowLayout mMedalContainer;
  @Bind(R.id.question_list)
  RecyclerView mQuestionList;

  private String[] mLevelLabels;
  private int[] mLevelIcon = ICON_LEVEL_MALE;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    initLevelRes();
    initRotationAnim(180);
  }

  private void initLevelRes() {
    if (TextUtils.equals(getComponent().loginSession().getUserInfo().getGender(), UserConstant
        .GENDER_MALE)) {
      mLevelLabels = getResources().getStringArray(R.array.label_report_level_male);
      mLevelIcon = ICON_LEVEL_MALE;
    } else {
      mLevelLabels = getResources().getStringArray(R.array.label_report_level_female);
      mLevelIcon = ICON_LEVEL_FEMALE;
    }
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_report;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_report_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  private void initRotationAnim(float toDegrees) {
    RotateAnimation animation = new RotateAnimation(0f, 360 - toDegrees, Animation.RELATIVE_TO_SELF,
        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    animation.setDuration(3000);
    animation.setFillAfter(true);
    animation.setInterpolator(new DecelerateInterpolator());
    mReportWheel.setAnimation(animation);
    animation.startNow();
  }

  private void setLevel(int money) {
    int level = 0;
    for (int i = 0; i < LEVEL.length; i++) {
      if (money >= LEVEL[i][0] && money <= LEVEL[i][1]) {
        level = i;
      }
    }
    mLevelIconText.setCompoundDrawables(null, getResources().getDrawable(mLevelIcon[level]),
        null, null);
    mLevelIconText.setText(mLevelLabels[level]);
  }

}
