package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.profile.adapter.FragmentViewPagerAdapter;
import com.hengrtec.taobei.ui.profile.fragments.OneFragment;
import com.hengrtec.taobei.ui.profile.fragments.TwoFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiao on 2016/5/19.
 */
public class MyAccountActivity extends BasicTitleBarActivity {


    @Bind(R.id.tablayout)
    android.support.design.widget.TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.tab_home_normal,
            R.mipmap.tab_mine_normal,
            R.mipmap.tab_info_normal
    };
    private int[] tabIconsPressed = {
            R.mipmap.tab_home_passed,
            R.mipmap.tab_mine_passed,
            R.mipmap.tab_info_passed
    };
    private List<String> titles;
    private List<Fragment> fragments;
    @Override
    protected void afterCreate(Bundle savedInstance) {
        ButterKnife.bind(this);
        initValue();
        initEvent();
    }
    @Override
    public boolean initializeTitleBar() {
        setMiddleTitle(R.string.myaccount);
        setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_account;

    }
    private void initValue() {
        fragments = new ArrayList<>();
        fragments.add(OneFragment.newInstance("钱包"));
        fragments.add(TwoFragment.newInstance("提现"));
        titles = new ArrayList<>();
        titles.add("钱包");
        titles.add("提现");
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void initEvent() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
//        txt_title.setTextColor(Color.YELLOW);
        if (txt_title.getText().toString().equals("钱包")) {
            img_title.setImageResource(R.mipmap.tab_home_passed);
            viewPager.setCurrentItem(0);
        } else if (txt_title.getText().toString().equals("提现")) {
            img_title.setImageResource(R.mipmap.tab_mine_passed);
            viewPager.setCurrentItem(1);
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
//        txt_title.setTextColor(Color.WHITE);
        if (txt_title.getText().toString().equals("钱包")) {
            img_title.setImageResource(R.mipmap.tab_home_normal);
        } else if (txt_title.getText().toString().equals("提现")) {
            img_title.setImageResource(R.mipmap.tab_mine_normal);
        }
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        tabLayout.getTabAt(1).setCustomView(getTabView(1));

    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(titles.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);

        if (position == 0) {
//            txt_title.setTextColor(Color.YELLOW);
            img_title.setImageResource(tabIconsPressed[position]);
        } else {
//            txt_title.setTextColor(Color.WHITE);
            img_title.setImageResource(tabIcons[position]);
        }
        return view;
    }

}
