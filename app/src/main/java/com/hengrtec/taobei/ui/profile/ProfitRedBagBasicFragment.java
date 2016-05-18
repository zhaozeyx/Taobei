/*
 * 文件名: ProfitRedBagBasicFragment
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
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;
import com.hengrtec.taobei.ui.basic.BasicFragment;

/**
 * 收益界面红包基本信息<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public class ProfitRedBagBasicFragment extends BasicFragment {
  private static final String BUNDLE_KEY_MODEL = "model";
  private MyBenefitModel mModel;
  @Bind(R.id.date)
  TextView mDateView;
  @Bind(R.id.red_bag_number)
  TextView mRedBagNumber;
  @Bind(R.id.progress)
  DonutProgress mProgressView;
  @Bind(R.id.profit_type)
  TextView profitType;
  @Bind(R.id.red_bag_with_draw_info)
  TextView mRedBagWithDrawInfoView;


  public static ProfitRedBagBasicFragment newInstance(MyBenefitModel model) {

    Bundle args = new Bundle();
    args.putParcelable(BUNDLE_KEY_MODEL, model);
    ProfitRedBagBasicFragment fragment = new ProfitRedBagBasicFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    mModel = getArguments().getParcelable(BUNDLE_KEY_MODEL);
    View view = inflater.inflate(R.layout.fragment_profit_basic_red_bag, container, false);
    ButterKnife.bind(this, view);
    bindData();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void bindData() {
    mDateView.setText(mModel.getToday());
    mRedBagNumber.setText("￥" + mModel.getTodayBenefit());
    mRedBagWithDrawInfoView.setText(getResources().getString(R.string
        .activity_profit_basic_profit_info_red_bag, mModel.getTodayBenefit()));
    mProgressView.setProgress(mModel.getPercentage() * 360 / 100);
  }
}
