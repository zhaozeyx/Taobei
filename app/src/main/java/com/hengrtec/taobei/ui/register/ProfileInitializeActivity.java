/*
 * 文件名: ProfileInitializeActivity
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicActivity;
import com.hengrtec.taobei.ui.tab.MainTabActivity;
import com.squareup.otto.Subscribe;
import rx.subscriptions.CompositeSubscription;

/**
 * 初始化个人信息<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/25]
 */
public class ProfileInitializeActivity extends BasicActivity {
  @Bind(R.id.btn_overlook)
  TextView mBtnOverlook;
  @Bind(R.id.content_container)
  FrameLayout contentContainer;

  private GenderChooseFragment mGenderChooseFragment;
  private BirthdayChooseFragment mBirthdayChooseFragment;
  private ProfessionalChooseFragment mProfessionalChooseFragment;

  private Step mCurrentStep;
  private Step mGenderStep;
  private Step mBirthdayStep;
  private Step mProfessionalStep;

  private CompositeSubscription mSubscriptions = new CompositeSubscription();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_initialize);
    ButterKnife.bind(this);
    initialize();
  }

  private void initialize() {
    mGenderChooseFragment = new GenderChooseFragment();
    mBirthdayChooseFragment = new BirthdayChooseFragment();
    mProfessionalChooseFragment = new ProfessionalChooseFragment();

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.content_container, mGenderChooseFragment);
    transaction.add(R.id.content_container, mBirthdayChooseFragment);
    transaction.add(R.id.content_container, mProfessionalChooseFragment);
    transaction.commitAllowingStateLoss();

    mGenderStep = new GenderStep();
    mBirthdayStep = new BirthdayStep();
    mProfessionalStep = new ProfessionalStep();
    mGenderStep.entry();
  }

  @OnClick(R.id.btn_overlook)
  public void onClick() {
    mCurrentStep.next();
  }

  @Override
  public void onBackPressed() {
    if (mCurrentStep != mGenderStep) {
      mCurrentStep.back();
      return;
    }
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mSubscriptions.unsubscribe();
  }

  @Subscribe
  public void onGenderChooseEvent(GenderChooseFragment.GenderChooseEvent event) {
    mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setGender(event
        .gender).update());
    mCurrentStep.next();
  }

  @Subscribe
  public void onBirtdayChooseEvent(BirthdayChooseFragment.BirthdayChooseEvent event) {
    mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setBirthday(event
        .birthday).update());
    mCurrentStep.next();
  }

  @Subscribe
  public void onProfeesionalChooseEvent(ProfessionalChooseFragment.ProfessionalChooseEvent event) {
    mSubscriptions.add(getComponent().loginSession().userInfoChangeBuilder().setOccupation(event
        .professional).update());
    mCurrentStep.next();
  }

  interface Step {
    void entry();

    void back();

    void next();
  }

  class GenderStep implements Step {

    @Override
    public void entry() {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      showFragment(mGenderChooseFragment, transaction);
      hideFragment(mBirthdayChooseFragment, transaction);
      hideFragment(mProfessionalChooseFragment, transaction);
      transaction.commitAllowingStateLoss();
      mCurrentStep = this;
    }

    @Override
    public void back() {
      onBackPressed();
    }

    @Override
    public void next() {
      mBirthdayStep.entry();
    }
  }

  class BirthdayStep implements Step {

    @Override
    public void entry() {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      hideFragment(mGenderChooseFragment, transaction);
      showFragment(mBirthdayChooseFragment, transaction);
      hideFragment(mProfessionalChooseFragment, transaction);
      transaction.commitAllowingStateLoss();
      mCurrentStep = this;
    }

    @Override
    public void back() {
      mGenderStep.entry();
    }

    @Override
    public void next() {
      mProfessionalStep.entry();
    }
  }

  class ProfessionalStep implements Step {

    @Override
    public void entry() {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      hideFragment(mGenderChooseFragment, transaction);
      hideFragment(mBirthdayChooseFragment, transaction);
      showFragment(mProfessionalChooseFragment, transaction);
      transaction.commitAllowingStateLoss();
      mCurrentStep = this;
    }

    @Override
    public void back() {
      mBirthdayStep.entry();
    }

    @Override
    public void next() {
      startActivity(new Intent(ProfileInitializeActivity.this, MainTabActivity.class));
      finish();
    }
  }

  private void showFragment(Fragment fragment, FragmentTransaction transaction) {
    transaction.show(fragment);
  }

  private void hideFragment(Fragment fragment, FragmentTransaction transaction) {
    transaction.hide(fragment);
  }

}
