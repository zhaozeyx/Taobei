package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiao on 2016/5/16.
 */
public class SettingsActivity extends BasicTitleBarActivity {
    @Bind(R.id.rl_about)
    RelativeLayout mAbout;
    @Bind(R.id.rl_primary)
    RelativeLayout mPrimary;

    @Override
    protected void afterCreate(Bundle savedInstance) {
        ButterKnife.bind(this);
//        inject();
        initListView();
//        loadData(true);
    }

    private void initListView() {
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });
        mPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PrimaryActivity.class));
            }
        });
    }

    @Override
    public boolean initializeTitleBar() {
        setMiddleTitle(R.string.activity_settings_title);
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
        return R.layout.activity_settings;
    }
}
