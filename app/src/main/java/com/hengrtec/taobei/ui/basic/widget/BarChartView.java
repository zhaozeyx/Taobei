/*
 * 文件名: BarChartView
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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;

/**
 * 柱状图<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/10]
 */
public class BarChartView extends FrameLayout {
  @Bind(R.id.left_title)
  LinearLayout mLeftTitleView;
  @Bind(R.id.bar_view_container)
  LinearLayout mBarViewContainer;
  @Bind(R.id.left_title_unit)
  TextView mLeftTitleUnitView;
  @Bind(R.id.base_line_view)
  View baseLineView;

  private String[] mLeftTitle;
  private String[] mTopTitle;
  private int[] mItemData;
  private int mMaxValue;

  private boolean mAttachData = false;

  public BarChartView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.bar_chart_view, this, false);
    ButterKnife.bind(this, view);
    addView(view);
  }

  public void setData(String[] leftTitle, String[] topTitle, int[] itemData, int maxValue, String
      unit) {
    mLeftTitle = leftTitle;
    mTopTitle = topTitle;
    mLeftTitleUnitView.setText("(" + unit + ")");
    mItemData = itemData;
    mMaxValue = maxValue;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    addBarViews();
  }

  private void addBarViews() {
    if (null == mTopTitle || null == mLeftTitle || mAttachData) {
      return;
    }
    int leftTitleSize = mLeftTitle.length;
    int topTitleSize = mTopTitle.length;
    int leftTitleHeight = mLeftTitleView.getHeight() - mLeftTitleUnitView.getHeight();


    TextView tempView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout
            .bar_text_view,
        mLeftTitleView, false);

    int bottomMargin = (leftTitleHeight - (tempView.getHeight() * mLeftTitle.length)) /
        (mLeftTitle.length - 2);
    for (int i = mLeftTitle.length - 1; i >= 0; i--) {
      TextView leftTitleView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout
              .bar_text_view,
          mLeftTitleView, false);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
          .WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT);
      if (i != 0) {
        params.weight = 1;
      }
      leftTitleView.setText(mLeftTitle[i]);
      mLeftTitleView.addView(leftTitleView, params);
    }

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
        .WRAP_CONTENT,
        LinearLayout.LayoutParams.MATCH_PARENT);
    for (int i = 0; i < topTitleSize; i++) {
      BarView barView = new BarView(getContext());
      if (i < mItemData.length) {
        barView.setBarValue(mItemData[i], R.string.fragment_profit_bar_format);
        barView.setMaxValue(mMaxValue);
        barView.setTopTitle(mTopTitle[i]);
        if (i == 3) {
          barView.setSelected(true);
        }
        if (i == 4) {
          barView.setBarViewColor(R.color.bar_color_red);
        }
      }
      params.weight = 1;
      //params.gravity = Gravity.BOTTOM;
      mBarViewContainer.addView(barView, params);
      mBarViewContainer.setGravity(Gravity.BOTTOM);
    }
    mAttachData = true;
  }
}
