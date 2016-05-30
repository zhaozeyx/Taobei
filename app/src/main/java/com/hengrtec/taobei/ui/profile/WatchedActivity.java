/*
 * 文件名: WathcedActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/26
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/26]
 */
public class WatchedActivity extends BasicTitleBarActivity {
  @Bind(R.id.tab_layout)
  TabLayout mTabLayout;
  @Bind(R.id.pager)
  ViewPager mPager;

  private View mStateView;
  private View mUnAcceptTtileView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initView();
  }

  @Override
  public boolean initializeTitleBar() {
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    setMiddleTitle(R.string.activity_watched_title);
    return true;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_watched;
  }

  private void initView() {
    String[] titles = getResources().getStringArray(R.array.watched_titles);
    WatchedPagerAdapter adapter = new WatchedPagerAdapter(getSupportFragmentManager());
    mPager.setAdapter(adapter);
    View view = LayoutInflater.from(this).inflate(R.layout.watched_pager_title_view, mTabLayout, false);
    view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    mTabLayout.setupWithViewPager(mPager);
    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
      if (i == WatchedType.UN_ACCEPT.ordinal()) {
        mTabLayout.getTabAt(i).setCustomView(view);
        ((TextView) view.findViewById(R.id.title)).setText(titles[i]);
        mStateView = view.findViewById(R.id.state);
      } else {
        mTabLayout.getTabAt(i).setText(titles[i]);
      }
    }

    mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == WatchedType.UN_ACCEPT.ordinal()) {
        }
        mPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

  }

  class WatchedPagerAdapter extends FragmentPagerAdapter {

    public WatchedPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      BaseWatchedListFragment fragment;
      switch (position) {
        case 0:
          fragment = new AllFragement();
          break;
        case 1:
          fragment = new UnAcceptedFragment();
          break;
        default:
          fragment = new AllFragement();
          break;
      }
      return fragment;
    }

    @Override
    public int getCount() {
      return WatchedType.values().length;
    }
  }

  private enum WatchedType {
    ALL, UN_ACCEPT
  }
}
