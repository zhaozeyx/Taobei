/*
 * 文件名: ResidenceChooseActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/6/1
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelStraightPicker;
import com.hengrtec.taobei.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/6/1]
 */
public class ProfessionalChooseView extends FrameLayout {

  @Bind(R.id.main_picker)
  WheelStraightPicker mMainPicker;
  @Bind(R.id.sub_picker)
  WheelStraightPicker mSubPicker;

  private String mSelectedMain;
  private String mSelectedSub;

  protected ProfessionalChooseView(Context context) {
    super(context);
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_professional_choose, null);
    ButterKnife.bind(this, view);
    final HashMap<String, List<String>> professionData = new HashMap<>();
    for (int i = 0; i < 30; i++) {
      List<String> subList = new ArrayList<>();
      for (int j = 0; j < new Random().nextInt(20); j++) {
        subList.add("sub" + j);
      }
      professionData.put("professional" + i, subList);
    }
    List<String> list = new ArrayList<>();
    Iterator<Map.Entry<String, List<String>>> iterator = professionData.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, List<String>> entry = iterator.next();
      list.add(entry.getKey());
    }
    mMainPicker.setData(list);
    mMainPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
      @Override
      public void onWheelScrolling(float deltaX, float deltaY) {

      }

      @Override
      public void onWheelSelected(int index, String data) {
        mSelectedMain = data;
        mSubPicker.setData(professionData.get(data));
      }

      @Override
      public void onWheelScrollStateChanged(int state) {

      }
    });
    mSubPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
      @Override
      public void onWheelScrolling(float deltaX, float deltaY) {

      }

      @Override
      public void onWheelSelected(int index, String data) {
        mSelectedSub = data;
      }

      @Override
      public void onWheelScrollStateChanged(int state) {

      }
    });
    addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public String getSelectData() {
    return mSelectedMain + mSelectedSub;
  }

}
