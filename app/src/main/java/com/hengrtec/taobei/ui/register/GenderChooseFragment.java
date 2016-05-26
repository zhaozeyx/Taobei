/*
 * 文件名: GenderChooseFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/25
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.service.constant.UserConstant;
import com.hengrtec.taobei.ui.basic.BasicFragment;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/25]
 */
public class GenderChooseFragment extends BasicFragment {
  @Bind(R.id.btn_female)
  RadioButton btnFemale;
  @Bind(R.id.btn_male)
  RadioButton btnMale;
  @Bind(R.id.gender_choose_group)
  RadioGroup mGenderChooseGroup;
  @Bind(R.id.btn_next)
  TextView mBtnNext;

  private String mChooseGender;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gender_choose, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    iniView();
  }

  private void iniView() {
    mGenderChooseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.btn_male:
            mChooseGender = UserConstant.GENDER_MALE;
            break;
          case R.id.btn_female:
            mChooseGender = UserConstant.GENDER_FEMALE;
            break;
        }
      }
    });
  }

  @OnClick(R.id.btn_next)
  public void onNextBtnClicked() {
    getComponent().getGlobalBus().post(new GenderChooseEvent(mChooseGender));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  class GenderChooseEvent {
    public String gender;

    public GenderChooseEvent(String gender) {
      this.gender = gender;
    }
  }
}
