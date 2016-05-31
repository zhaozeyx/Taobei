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

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.manager.UserInfoChangedEvent;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.squareup.otto.Subscribe;
import java.util.Calendar;
import org.apmem.tools.layouts.FlowLayout;
import rx.subscriptions.CompositeSubscription;

/**
 * 个人详情页面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/14]
 */
public class ProfileDetailActivity extends BasicTitleBarActivity {

  private static final int REQUEST_CODE_NICK_NAME = 3;
  private static final int REQUEST_CODE_NICK_INTRODUCTION = 4;

  @Bind(R.id.user_avatar_display)
  SimpleDraweeView mUserAvatarView;
  @Bind(R.id.tags_container)
  FlowLayout tagsContainer;
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

  private AvatarChoosePresenter mAvatarChoosePresenter;

  private UserInfo mUserInfo;

  private CompositeSubscription mSubscriptions = new CompositeSubscription();

  private AlertDialog mGenderChooseDialog;
  private DatePickerDialog mBirthdayChooseDialog;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    bindData();
    mAvatarChoosePresenter = new AvatarChoosePresenter(this);
  }

  private UserInfo getUserInfo() {
    return getComponent().loginSession().getUserInfo();
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
        mAvatarChoosePresenter.showChooseAvatarDialog();
        break;
      case R.id.certify_setting:
        startActivity(new Intent(this, CertifyActivity.class));
        break;
      case R.id.phone_setting:
        break;
      case R.id.nick_name_setting:
        startActivityForResult(new Intent(this, NickNameActivity.class), REQUEST_CODE_NICK_NAME);
        break;
      case R.id.introduction_setting:
        startActivityForResult(new Intent(this, IntroductionActivity.class), REQUEST_CODE_NICK_INTRODUCTION);
        break;
      case R.id.gender_setting:
        showGenderChooseDialog();
        break;
      case R.id.age_setting:
        showBirthChooseDialog();
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

  private void showGenderChooseDialog() {
    if (null == mGenderChooseDialog) {
      final String[] genderItems = getResources().getStringArray(R.array.gender_items);
      mGenderChooseDialog = new AlertDialog.Builder(this).setItems(genderItems, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setGender(genderItems[which]).update());
        }
      }).create();
    }
    if (mGenderChooseDialog.isShowing()) {
      return;
    }
    mGenderChooseDialog.show();
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_profile_detail_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_CODE_NICK_NAME:
        mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder()
            .setUserName(data.getStringExtra(NickNameActivity.RESULT_KEY_NICK_NAME)).update());
        return;
      case REQUEST_CODE_NICK_INTRODUCTION:
        mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder()
            .setIntroduce(data.getStringExtra(IntroductionActivity.RESULT_KEY_INTRODUCTION)).update());
        return;
    }
    mAvatarChoosePresenter.onActivityResult(requestCode, resultCode, data);
  }

  @Subscribe
  public void onUserInfoChangedEvent(UserInfoChangedEvent event) {
    bindData();
  }

  private void bindData() {
    //ImageLoader.loadOptimizedHttpImage(this, mUserInfo.getAvart()).placeholder(R.mipmap
    //    .src_avatar_default_drawer).into(mUserAvatarView);
    mUserAvatarView.setImageURI(Uri.parse(getUserInfo().getAvart()));
    mNickNameValueView.setText(getUserInfo().getUserName());
    mIntroductionValueView.setText(TextUtils.isEmpty(getUserInfo().getIntroduce()) ? getString(R.string.activity_introduction_hint) : getUserInfo().getIntroduce());
    mGenderValueView.setText(getUserInfo().getGender());
    mAgeValueView.setText(computeAge(getUserInfo().getBirthday()));
    showUserLabel();
    setCertifyStatus();
  }

  private String computeAge(String birthday) {
    if (TextUtils.isEmpty(birthday)) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());

    Calendar calendar1 = Calendar.getInstance();
    String[] date = birthday.split("-");
    calendar1.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

    return String.valueOf(calendar.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR));
  }

  private void showBirthChooseDialog() {
    if (null == mBirthdayChooseDialog) {
      String birthday = getUserInfo().getBirthday();
      String[] birthDate = !TextUtils.isEmpty(birthday) ? birthday.split("-") : null;
      Calendar today = Calendar.getInstance();
      int year = today.get(Calendar.YEAR);
      int month = today.get(Calendar.MONTH);
      int day = today.get(Calendar.DAY_OF_MONTH);
      if (null != birthDate) {
        year = birthDate.length > 0 && !TextUtils.isEmpty(birthDate[0]) ? Integer.parseInt(birthDate[0]) : year;
        month = birthDate.length > 1 && !TextUtils.isEmpty(birthDate[1]) ? Integer.parseInt(birthDate[1]) : month;
        day = birthDate.length > 2 && !TextUtils.isEmpty(birthDate[2]) ? Integer.parseInt(birthDate[2]) : day;
      }

      mBirthdayChooseDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setBirthday(year + "-" + monthOfYear + "-" + dayOfMonth).update());
          mBirthdayChooseDialog = null;
        }
      }, year, month, day);
      mBirthdayChooseDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          mBirthdayChooseDialog = null;
        }
      });
      mBirthdayChooseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          mBirthdayChooseDialog = null;
        }
      });
      mBirthdayChooseDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
      Calendar maxDay = Calendar.getInstance();
      maxDay.set(Calendar.YEAR, today.get(Calendar.YEAR) - 100);
      mBirthdayChooseDialog.getDatePicker().setMinDate(maxDay.getTimeInMillis());
    }
    if (mBirthdayChooseDialog.isShowing()) {
      return;
    }
    mBirthdayChooseDialog.show();
  }

  private void setCertifyStatus() {
    String certified = getComponent().loginSession().getUserInfo().getCertified();
    mCertifyValueView.setVisibility(View.VISIBLE);
    switch (certified) {
      case UserInfo.CERTIFY_STATUS_CERTIFY_FAILED:
        mCertifyValueView.setText(R.string.activity_certify_status_failed);
        break;
      case UserInfo.CERTIFY_STATUS_CERTIFY_PASS:
        mCertifyValueView.setText(R.string.activity_certify_status_pass);
        break;
      case UserInfo.CERTIFY_STATUS_CERTIFYING:
        mCertifyValueView.setText(R.string.activity_certify_status_certifying);
        break;
      default:
        mCertifyValueView.setVisibility(View.GONE);
        break;
    }
  }

  private void showUserLabel() {
    tagsContainer.removeAllViews();
    addTag(getString(R.string.activity_profile_detail_add_tag), R.drawable.bg_btn_gray_round_corner);
    tagsContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(ProfileDetailActivity.this, AddTagsActivity.class));
      }
    });
    String userLabel = getComponent().loginSession().getUserInfo().getUserLabel();
    if (TextUtils.isEmpty(userLabel)) {
      return;
    }
    String[] labels = userLabel.split(",");
    for (String label : labels) {
      addTag(label, R.drawable.bg_btn_yellow_round_corner);
    }
  }

  private void addTag(String label, int backGroundResource) {
    TextView labelView = (TextView) LayoutInflater.from(this).inflate(R.layout.tag_item_big, tagsContainer, false);
    labelView.setBackgroundResource(backGroundResource);
    labelView.setText(label);
    labelView.setTextColor(getResources().getColor(R.color.font_color_primary));
    tagsContainer.addView(labelView);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mAvatarChoosePresenter.onDestroy();
    mSubscriptions.unsubscribe();
  }
}
