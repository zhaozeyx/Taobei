/*
 * 文件名: BarView
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.basic.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.component.log.Logger;

/**
 * 柱状图单个矩形柱<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/10]
 */
public class BarView extends FrameLayout {

  @Bind(R.id.top_title)
  TextView mTopTitleView;
  @Bind(R.id.bar)
  View mBarView;
  @Bind(R.id.bar_value)
  TextView mBarValueView;
  @Bind(R.id.container)
  LinearLayout mContainer;
  @Bind(R.id.cover)
  View mCover;

  private int mMaxValue = -1;
  private int mBarValue = -1;
  private boolean mAttachData = false;

  public BarView(Context context) {
    super(context);
    initView();
  }

  private void initView() {
    View childView = LayoutInflater.from(getContext()).inflate(R.layout.bar_view, this, false);
    ButterKnife.bind(this, childView);
    addView(childView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams
        .MATCH_PARENT));
  }

  public void setTopTitle(String title) {
    mTopTitleView.setText(title);
  }

  public void setTopTitleColor(int color) {
    mTopTitleView.setTextColor(getResources().getColor(color));
  }

  public void setBarValue(int barValue, int valueFormatId) {
    mBarValueView.setText(getResources().getString(valueFormatId, barValue));
    mBarValue = barValue;
    if (0 == barValue) {
      mBarValueView.setVisibility(View.INVISIBLE);
    }
  }

  public void setBarViewColor(int color) {
    mBarView.setBackgroundColor(getResources().getColor(color));
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
    mTopTitleView.setSelected(selected);
    mTopTitleView.setTextColor(getResources().getColor(android.R.color.black));
    mTopTitleView.setBackgroundColor(getResources().getColor(R.color.bar_color_yellow));
    setBarViewColor(R.color.bar_color_yellow);
    mCover.setVisibility(selected ? VISIBLE : GONE);
  }

  public void setMaxValue(int max) {
    mMaxValue = max;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    updateBar();
  }

  private void updateBar() {
    if (-1 == mBarValue || -1 == mMaxValue || 0 == mMaxValue || mAttachData) {
      return;
    }
    int showHeight = getMeasuredHeight();
    Logger.d("YZZ", "measuredHeight : %d   height : ", getMeasuredHeight(), getHeight());
    float percent = (float) mBarValue / (float) mMaxValue;
    Logger.d("YZZ", "percent : %f : ", percent);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mBarView
        .getMeasuredWidth(),
        mBarView.getMinimumHeight());
    if (mBarValue != 0) {
      params = new RelativeLayout.LayoutParams(mBarView.getMeasuredWidth(),
          ((int) (percent * showHeight)) / 2);
    }
    Logger.d("YZZ", "final -height :%d  density : %d ", ((int) (percent * showHeight)), (int)
        getResources().getDisplayMetrics().scaledDensity);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    mBarView.setLayoutParams(params);
    mAttachData = true;
  }

  @OnClick(R.id.container)
  public void onClick() {
  }
}
