/*
 * 文件名: ProfileDetailActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/14
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import org.apmem.tools.layouts.FlowLayout;

/**
 * 个人详情页面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/14]
 */
public class ProfileDetailActivity extends BasicTitleBarActivity {
  @Bind(R.id.user_avatar)
  CircleImageView mUserAvatarView;
  @Bind(R.id.tags_container)
  FlowLayout tagsContainer;
  @Bind(R.id.tags_arrow)
  ImageView tagsArrow;
  @Bind(R.id.certify_value)
  TextView mCertifyValueView;
  @Bind(R.id.phone_value)
  TextView mPhoneValueView;
  @Bind(R.id.nick_name_value)
  TextView mNickNameValueView;
  @Bind(R.id.introduction_value)
  TextView mIntroductionValueView;
  @Bind(R.id.gender_value)
  TextView mGenderValueView;
  @Bind(R.id.age_value)
  TextView mAgeValueView;
  @Bind(R.id.city_value)
  TextView mCityValueView;
  @Bind(R.id.birthplace_value)
  TextView mBirthplaceValueView;
  @Bind(R.id.profession_value)
  TextView mProfessionValueView;
  @Bind(R.id.education_value)
  TextView mEducationValueView;
  @Bind(R.id.account_bind_value)
  TextView mAccountBindValueView;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_profile_detail;
  }

  @OnClick({R.id.avatar_setting, R.id.certify_setting, R.id.phone_setting, R.id
      .nick_name_setting, R.id.introduction_setting, R.id.gender_setting, R.id.age_setting, R.id
      .city_setting, R.id.birthplace_setting, R.id.profession_setting, R.id.education_setting, R
      .id.account_bind_setting})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.avatar_setting:
        break;
      case R.id.certify_setting:
        break;
      case R.id.phone_setting:
        break;
      case R.id.nick_name_setting:
        break;
      case R.id.introduction_setting:
        break;
      case R.id.gender_setting:
        break;
      case R.id.age_setting:
        break;
      case R.id.city_setting:
        break;
      case R.id.birthplace_setting:
        break;
      case R.id.profession_setting:
        break;
      case R.id.education_setting:
        break;
      case R.id.account_bind_setting:
        break;
    }
  }
}
