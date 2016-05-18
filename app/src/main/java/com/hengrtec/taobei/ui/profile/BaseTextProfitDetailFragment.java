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
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
public abstract class BaseTextProfitDetailFragment extends BasicFragment {
  protected static final String BUNDLE_KYE_MODEL = "key_model";
  protected MyBenefitModel mModel;

  @Bind(R.id.data_display_title)
  TextView mDataDisplayTitle;
  @Bind(R.id.red_bag_number)
  TextView mRedBagNumberView;
  @Bind(R.id.login_reward)
  TextView mLoginRewardView;
  @Bind(R.id.task_reward)
  TextView mTaskRewardView;
  @Bind(R.id.btn_withdraw)
  FrameLayout mBtnWithdraw;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profit_detail_text, container, false);
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

  @OnClick(R.id.btn_withdraw)
  public void onClick() {
  }

  private void initView() {
    mDataDisplayTitle.setText(getDisplayTitle());
    mDataDisplayTitle.setTextColor(getResources().getColor(getDisplayTitleColor()));
    mBtnWithdraw.setVisibility(showWithdrawBtn() ? View.VISIBLE : View.GONE);
  }

  protected abstract int getDisplayTitleColor();

  protected abstract int getDisplayTitle();

  protected abstract boolean showWithdrawBtn();
}
