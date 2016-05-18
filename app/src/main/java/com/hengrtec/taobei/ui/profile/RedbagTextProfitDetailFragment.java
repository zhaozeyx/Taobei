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
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public class RedBagTextProfitDetailFragment extends BaseTextProfitDetailFragment {

  public static RedBagTextProfitDetailFragment newInstance(MyBenefitModel model) {

    Bundle args = new Bundle();
    args.putParcelable(BUNDLE_KYE_MODEL, model);
    RedBagTextProfitDetailFragment fragment = new RedBagTextProfitDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mModel = getArguments().getParcelable(BUNDLE_KYE_MODEL);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected int getDisplayTitleColor() {
    return R.color.font_color_primary;
  }

  @Override
  protected int getDisplayTitle() {
    return R.string.activity_profit_detail_text_display_red_bag_title;
  }

  @Override
  protected boolean showWithdrawBtn() {
    return true;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    bindData();
  }

  private void bindData() {
    mTaskRewardView.setText(mModel.getTodayBenefit() + getString(R.string.unit_bbj));
    mRedBagNumberView.setText(mModel.getTotalAwards() + getString(R.string.unit_cash));
  }
}
