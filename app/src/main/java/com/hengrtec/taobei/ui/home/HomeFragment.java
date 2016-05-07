/*
 * 文件名: HomeFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalBus;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.commonevent.UserAvatarClickedEvent;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.squareup.otto.Bus;
import javax.inject.Inject;

/**
 * 首页界面，主要展示广告列表信息<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class HomeFragment extends BasicTitleBarFragment implements HomeView {

  @Bind(R.id.home_pager)
  ViewPager mHomePager;

  @GlobalBus
  @Inject
  Bus mGlobalBus;

  @Override
  protected void onCreateViewCompleted(View view) {
    ButterKnife.bind(this, view);
    inject();
    initPager();
  }

  private void initPager() {
    mHomePager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));

    mHomePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        setTitleBarTabIndex(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  private void inject() {
    DaggerPresenterComponent.builder().presenterModule(new PresenterModule(getActivity()))
        .activityComponent(getComponent()).build().inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_home;
  }

  @Override
  public boolean initializeTitleBar() {
    setTitleBarTabs(new SegmentControl.OnSegmentControlClickListener() {
      @Override
      public void onSegmentControlClick(int index) {
        mHomePager.setCurrentItem(index);
      }
    }, getResources().getStringArray(R.array.home_title_bar_tab_titles));
    setUserAvatarVisible(true);
    ImageLoader.loadOptimizedHttpImage(getActivity(), "").placeholder(R.drawable.ic_launcher)
        .into(getUserAvatarView());

    getUserAvatarView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mGlobalBus.post(new UserAvatarClickedEvent());
      }
    });
    return true;
  }

  @Override
  public Context getActivityContext() {
    return getActivity();
  }

  @Override
  public void onServiceInvokeEnd() {
    closeProgressDialog();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private static class HomePagerAdapter extends FragmentPagerAdapter {
    private static final String[] ADV_TYPE = new String[]{AdvertisementConstant
        .ADV_STATE_LAUNCHING, AdvertisementConstant.ADV_STATE_UN_LAUNCH};


    public HomePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return AdvertisementListFragment.newInstance(ADV_TYPE[position]);
    }

    @Override
    public int getCount() {
      return ADV_TYPE.length;
    }
  }
}
