package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.view.View;

import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

import butterknife.ButterKnife;

/**
 * Created by jiao on 2016/5/16.
 */
public class AboutActivity extends BasicTitleBarActivity {

    @Override
    protected void afterCreate(Bundle savedInstance) {
        ButterKnife.bind(this);
//        inject();
        initListView();
//        loadData(true);
    }

    private void initListView() {

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
