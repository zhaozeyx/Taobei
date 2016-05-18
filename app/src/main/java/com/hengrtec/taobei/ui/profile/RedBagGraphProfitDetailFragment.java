/*
 * 文件名: RedbagTextProfitDetailFragment
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;
import java.util.ArrayList;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public class RedBagGraphProfitDetailFragment extends BaseGraphProfitDetailFragment {

  public static RedBagGraphProfitDetailFragment newInstance(MyBenefitModel model) {

    Bundle args = new Bundle();
    args.putParcelable(BUNDLE_KEY_MODEL, model);
    RedBagGraphProfitDetailFragment fragment = new RedBagGraphProfitDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    mModel = getArguments().getParcelable(BUNDLE_KEY_MODEL);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    bindData();
  }

  @Override
  protected int getDisplayTitleColor() {
    return R.color.font_color_primary;
  }

  @Override
  protected int getDisplayTitle() {
    return R.string.activity_profit_detail_graph_display_red_bag_title;
  }

  private void bindData() {

    mGraphTitleView.setText(R.string.activity_profit_detail_graph_title_red_bag);
    mUnitView.setText(R.string.unit_currency_graph);

    mLineChartView.getAxisLeft().setAxisMinValue(0);
    mLineChartView.getAxisLeft().setAxisMaxValue(40);
    mLineChartView.getAxisRight().setEnabled(false);
    mLineChartView.getAxisLeft().setDrawGridLines(false);
    mLineChartView.getXAxis().setValues(mModel.getXData());
    mLineChartView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    mLineChartView.getXAxis().setDrawAxisLine(false);
    mLineChartView.getXAxis().setValueFormatter(new XAxisValueFormatter() {
      @Override
      public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return mModel.getXData().get(index) + getResources().getString(R.string.unit_day);
      }
    });

    LineData lineData = new LineData();
    for (int value : mModel.getYData()) {
      lineData.addXValue(String.valueOf(value));
    }

    LineDataSet set1;
    ArrayList<Entry> yVals = new ArrayList<>();

    for (int i = 0; i < mModel.getYData().size(); i++) {
      yVals.add(new Entry(mModel.getYData().get(i), i));
    }
    set1 = new LineDataSet(yVals, null);

    set1.setColor(getResources().getColor(R.color.font_color_red_dark));
    set1.setCircleColor(getResources().getColor(R.color.font_color_yellow));
    set1.setLineWidth(1f);
    set1.setCircleRadius(3f);
    set1.setDrawCircleHole(false);
    set1.setDrawFilled(false);
    set1.setValueFormatter(new ValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler
          viewPortHandler) {
        return String.valueOf((int) value);
      }
    });
    set1.setValueTextColor(getResources().getColor(android.R.color.transparent));
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1);
    lineData.addDataSet(set1);

    mLineChartView.setData(lineData);
    mLineChartView.getLegend().setEnabled(false);
  }

}
