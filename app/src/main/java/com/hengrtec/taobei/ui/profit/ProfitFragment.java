/*
 * 文件名: PofitFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profit;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.BibiModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.BibiParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.basic.widget.BarChartView;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;

/**
 * 收益界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class ProfitFragment extends BasicTitleBarFragment {
  private static final String[] LEFT_TITLE = new String[]{"0", "10", "20", "30", "40"};
  private static final int MAX_VALUE = 40;
  @Bind(R.id.today_income)
  TextView mTodayIncome;
  @Bind(R.id.profit_today_container)
  LinearLayout mProfitTodayContainer;
  @Bind(R.id.adv_view_count)
  TextView mAdvViewCount;
  @Bind(R.id.view_total_time)
  TextView mViewTotalTime;
  @Bind(R.id.adv_be_friend_rank)
  TextView mAdvBeFriendRank;
  @Bind(R.id.view_be_visited_count)
  TextView mViewBeVisitedCount;
  @Bind(R.id.profit_beyond)
  TextView mProfitBeyond;
  @Bind(R.id.profit_beyond_description)
  TextView profitBeyondDescription;
  @Bind(R.id.cheered)
  TextView mCheered;
  @Bind(R.id.beyond_divider)
  ImageView beyondDivider;
  @Bind(R.id.btn_share_moments)
  FrameLayout mBtnShareMoments;
  @Bind(R.id.bar_chart)
  BarChartView mBarChart;

  @Inject
  AdvertisementService mAdvService;

  @Override
  protected void onCreateViewCompleted(View view) {
    ButterKnife.bind(this, view);
    inject();
    initData();
  }

  private void inject() {
    DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
        GlobalModule((CustomApp) getActivity().getApplication()))
        .build().inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_profit;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.fragment_profit_title);
    return true;
  }

  private void initData() {
    showProgressDialog("");
    manageRpcCall(mAdvService.bibi(new BibiParams(String.valueOf(getComponent().loginSession()
        .getUserId()))), new UiRpcSubscriber<BibiModel>(getActivity()) {


      @Override
      protected void onSuccess(BibiModel model) {
        bindData(model);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  private void bindData(BibiModel model) {
    mAdvViewCount.setText(getString(R.string.fragment_profit_view_count, model.getAdvQuantity()));
    mAdvBeFriendRank.setText(getString(R.string.fragment_profit_friend_rank, Integer.parseInt
        (model.getRanking())));
    mProfitBeyond.setText(getString(R.string.fragment_profit_beyond, model.getRankingPercentage()));
    mTodayIncome.setText(model.getBenefit() + getString(R.string.unit_cash));
    // TODO
    //mViewTotalTime.setText();
    //mViewBeVisitedCount.setText();

    updateBar(model);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void updateBar(BibiModel model) {
    BibiModel.BenefitsBean bean = model.getBenefits();
    String[] topTitle = bean.getKey().get(0).split(",");
    String[] value = bean.getValue().get(0).split(",");
    int valueSize = value.length;
    int[] data = new int[valueSize];
    for (int i = 0; i < valueSize; i++) {
      data[i] = Integer.parseInt(value[i]);
    }
    mBarChart.setData(LEFT_TITLE, topTitle, data, MAX_VALUE, getString(R.string.unit_cash));
  }
}
