/*
 * 文件名: BaseTextProfitDetailFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/17
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.mikephil.charting.charts.LineChart;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;
import com.hengrtec.taobei.ui.basic.BasicFragment;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public abstract class BaseGraphProfitDetailFragment extends BasicFragment {
  protected static final String BUNDLE_KEY_MODEL = "bundle_key_model";
  protected MyBenefitModel mModel;

  @Bind(R.id.data_display_title)
  TextView mDataDisplayTitle;
  @Bind(R.id.line_chart_1)
  LineChart mLineChartView;
  @Bind(R.id.unit)
  TextView mUnitView;
  @Bind(R.id.graph_title)
  TextView mGraphTitleView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profit_detail_graph, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initView() {
    mDataDisplayTitle.setText(getDisplayTitle());
    mDataDisplayTitle.setTextColor(getResources().getColor(getDisplayTitleColor()));

    mLineChartView.setPinchZoom(false);

    mLineChartView.setNoDataText("");
    mLineChartView.setNoDataTextDescription("");
    mLineChartView.setDescription("");
  }

  protected abstract int getDisplayTitleColor();

  protected abstract int getDisplayTitle();

  @OnClick(R.id.unit)
  public void onClick() {
  }
}
