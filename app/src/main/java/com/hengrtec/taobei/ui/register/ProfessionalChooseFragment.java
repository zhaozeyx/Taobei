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
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelStraightPicker;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * [一句话功能简述]<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/25]
 */
public class ProfessionalChooseFragment extends BasicFragment {
  @Bind(R.id.main_category_picker)
  WheelStraightPicker mMainCategoryPicker;
  @Bind(R.id.detail_category_picker)
  WheelStraightPicker mDetailCategoryPicker;

  private String mCurrentProfessional;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_professinal_choose, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initView() {

    final List<Professional> professionals = initData();
    List<String> mainList = new ArrayList<>();
    for (Professional professional : professionals) {
      mainList.add(professional.name);
    }
    mMainCategoryPicker.setData(mainList);
    mDetailCategoryPicker.setData(professionals.get(0).mData);

    mMainCategoryPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
      @Override
      public void onWheelScrolling(float deltaX, float deltaY) {

      }

      @Override
      public void onWheelSelected(int index, String data) {
        mDetailCategoryPicker.setData(professionals.get(index).mData);
      }

      @Override
      public void onWheelScrollStateChanged(int state) {

      }
    });

    mDetailCategoryPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
      @Override
      public void onWheelScrolling(float deltaX, float deltaY) {

      }

      @Override
      public void onWheelSelected(int index, String data) {
        mCurrentProfessional = data;
      }

      @Override
      public void onWheelScrollStateChanged(int state) {

      }
    });

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.btn_confirm)
  public void onClick() {
    getComponent().getGlobalBus().post(new ProfessionalChooseEvent(mCurrentProfessional));
  }

  private List<Professional> initData() {
    List<Professional> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Professional professional = new Professional();
      professional.name = "Professional_" + i;
      for (int j = 0; j < new Random().nextInt(7) + 3; j++) {
        professional.mData.add("detail_" + j);
      }
      list.add(professional);
    }
    return list;
  }

  private class Professional {
    String name;
    List<String> mData = new ArrayList();
  }

  class ProfessionalChooseEvent {
    public String professional;

    public ProfessionalChooseEvent(String professional) {
      this.professional = professional;
    }
  }
}
