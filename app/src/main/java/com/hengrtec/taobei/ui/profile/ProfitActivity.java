/*
 * 文件名: ProfitActivity
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.MyBenifitParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;
import me.relex.circleindicator.CircleIndicator;

/**
 * 收益界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public class ProfitActivity extends BasicTitleBarActivity {
  private static final String ACTION_START_FOR_RED_BAG = "com.hengrtec.taobei.ui.profile" +
      ".ProfitActivity.RED_BAG";
  private static final String ACTION_START_FOR_BBJ = "com.hengrtec.taobei.ui.profile" +
      ".ProfitActivity.BBJ";

  @Bind(R.id.view_pager)
  ViewPager mViewPager;
  @Bind(R.id.btn_chart)
  FrameLayout mBtnChart;
  @Bind(R.id.btn_invite)
  FrameLayout mbBtnInvite;
  @Bind(R.id.indicator)
  CircleIndicator mIndicator;
  @Bind(R.id.bottom_container)
  FrameLayout mBottomContainer;

  @Inject
  UserService mUserService;

  private Fragment mDetailBBJFragment;
  private Fragment mDetailRedBagFragment;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initViewPager();
    loadData();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp)
        getApplication())).serviceModule(new ServiceModule()).build().inject(this);
  }

  private void loadData() {
    showProgressDialog("", true);
    manageRpcCall(mUserService.myBenefit(new MyBenifitParams(getComponent().loginSession()
        .getUserId())), new UiRpcSubscriber<MyBenefitModel>(this) {

      @Override
      protected void onSuccess(MyBenefitModel myBenefitModel) {
        mViewPager.setAdapter(new TopPagerAdapter(getSupportFragmentManager(), myBenefitModel));
        initDetailFragment(myBenefitModel);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  private void initViewPager() {
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
          case 0:
            transaction.hide(mDetailBBJFragment);
            transaction.show(mDetailRedBagFragment);
            break;
          case 1:
            transaction.hide(mDetailRedBagFragment);
            transaction.show(mDetailBBJFragment);
            break;
        }
        transaction.commitAllowingStateLoss();
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    String action = TextUtils.isEmpty(getIntent().getAction()) ? ACTION_START_FOR_RED_BAG :
        getIntent().getAction();
    switch (action) {
      case ACTION_START_FOR_RED_BAG:
        mViewPager.setCurrentItem(ProfitType.RED_BAG.ordinal());
        break;
      case ACTION_START_FOR_BBJ:
        mViewPager.setCurrentItem(ProfitType.BBJ.ordinal());
        break;
    }
  }

  private void initDetailFragment(MyBenefitModel model) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (getComponent().isLogin()) {
      mDetailBBJFragment = BBJGraphProfitDetailFragment.newInstance(model);
      mDetailRedBagFragment = RedBagGraphProfitDetailFragment.newInstance(model);
    } else {
      mDetailRedBagFragment = RedBagGraphProfitDetailFragment.newInstance(model);
      mDetailBBJFragment = BBJTextProfitDetailFragment.newInstance(model);
    }
    transaction.add(R.id.bottom_container, mDetailBBJFragment);
    transaction.add(R.id.bottom_container, mDetailRedBagFragment);
    transaction.commitAllowingStateLoss();
    String action = TextUtils.isEmpty(getIntent().getAction()) ? ACTION_START_FOR_RED_BAG :
        getIntent().getAction();
    transaction = getSupportFragmentManager().beginTransaction();
    switch (action) {
      case ACTION_START_FOR_RED_BAG:
        transaction.hide(mDetailBBJFragment);
        transaction.show(mDetailRedBagFragment);
        break;
      case ACTION_START_FOR_BBJ:
        transaction.show(mDetailBBJFragment);
        transaction.hide(mDetailRedBagFragment);
        break;
    }
    transaction.commitAllowingStateLoss();
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_profit;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
  }

  private class TopPagerAdapter extends FragmentPagerAdapter {

    private MyBenefitModel mModel;

    public TopPagerAdapter(FragmentManager fm, MyBenefitModel model) {
      super(fm);
      mModel = model;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch (position) {
        case 0:
          fragment = ProfitRedBagBasicFragment.newInstance(mModel);
          break;
        case 1:
          fragment = ProfitBBJBasicFragment.newInstance(mModel);
          break;
      }
      return fragment;
    }

    @Override
    public int getCount() {
      return ProfitType.values().length;
    }
  }

  private enum ProfitType {
    RED_BAG, BBJ
  }

  public static Intent makeRedBagIntent(Context context) {
    return new Intent(ACTION_START_FOR_RED_BAG);
  }

  public static Intent makeBBJIntent(Context context) {
    return new Intent(ACTION_START_FOR_BBJ);
  }
}
