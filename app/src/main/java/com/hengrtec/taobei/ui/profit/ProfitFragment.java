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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;

/**
 * 收益界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class ProfitFragment extends BasicTitleBarFragment {
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

  @Override
  protected void onCreateViewCompleted(View view) {

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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
      savedInstanceState) {
    // TODO: inflate a fragment view
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
