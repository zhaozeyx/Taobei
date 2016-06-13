package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.profile.fragments.MyAccountSafeActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.DataCleanManager;
import com.hengrtec.taobei.utils.preferences.CustomAppPreferences;
import javax.inject.Inject;

/**
 * Created by jiao on 2016/5/16.
 */
public class SettingsActivity extends BasicTitleBarActivity {

  @Bind(R.id.rl_about) RelativeLayout mAbout;
  @Bind(R.id.rl_primary) RelativeLayout mPrimary;
  @Bind(R.id.account_safe) RelativeLayout mAccountSafe;
  @Bind(R.id.mTogBtn) ToggleButton mTogBtn;
  @Bind(R.id.mTogBtn2) ToggleButton mTogBtn2;
  @Bind(R.id.mTogBtn3) ToggleButton mTogBtn3;
  @Bind(R.id.mTogBtn4) ToggleButton mTogBtn4;
  @Bind(R.id.mTogBtn5) ToggleButton mTogBtn5;
  @Bind(R.id.mTogBtn6) ToggleButton mTogBtn6;
  @Bind(R.id.exit) Button exit;
  @Inject UserService mAdvService;
  @Bind(R.id.clear_cache) RelativeLayout clearCache;
  @Bind(R.id.cache) TextView cache;
  @Bind(R.id.question_setting) RelativeLayout questionSetting;

  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initView();
    initdata();
  }

  private void initView() {
    try {
      cache.setText(DataCleanManager.getTotalCacheSize(this));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initdata() {
    if (("0").equals(getComponent().appPreferences().getString("key_screen", null))) {
      mTogBtn.setChecked(true);
    } else {
      mTogBtn.setChecked(false);
    }
    if (("0").equals(getComponent().appPreferences().getString("key_movement", null))) {
      mTogBtn2.setChecked(true);
    } else {
      mTogBtn2.setChecked(false);
    }
    if (("0").equals(getComponent().appPreferences().getString("key_system_message", null))) {
      mTogBtn3.setChecked(true);
    } else {
      mTogBtn3.setChecked(false);
    }
    if (("0").equals(getComponent().appPreferences().getString("key_traffic_alert", null))) {
      mTogBtn4.setChecked(true);
    } else {
      mTogBtn4.setChecked(false);
    }

    mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          getComponent().appPreferences().put(CustomAppPreferences.KEY_SCREEN, "0");
        } else {
          //未选中
          getComponent().appPreferences().put(CustomAppPreferences.KEY_SCREEN, "1");
        }
      }
    });
    mTogBtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          getComponent().appPreferences().put(CustomAppPreferences.KEY_MOVEMENT, "0");
          JPushInterface.resumePush(getApplicationContext());
        } else {
          //未选中
          getComponent().appPreferences().put(CustomAppPreferences.KEY_MOVEMENT, "1");
          JPushInterface.stopPush(getApplicationContext());
        }
      }
    });
    mTogBtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          getComponent().appPreferences().put(CustomAppPreferences.KEY_SYSTEM_MESSAGE, "0");
        } else {
          //未选中
          getComponent().appPreferences().put(CustomAppPreferences.KEY_SYSTEM_MESSAGE, "1");
        }
      }
    });
    mTogBtn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          getComponent().appPreferences().put(CustomAppPreferences.KEY_TRAFFIC_ALERT, "0");
        } else {
          //未选中
          getComponent().appPreferences().put(CustomAppPreferences.KEY_TRAFFIC_ALERT, "1");
        }
      }
    });
  }

  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }

  @Override public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_settings_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override public int getLayoutId() {
    return R.layout.activity_settings;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
  }

  @OnClick({
      R.id.account_safe, R.id.mTogBtn, R.id.clear_cache, R.id.question_setting, R.id.mTogBtn2,
      R.id.mTogBtn3, R.id.mTogBtn4, R.id.mTogBtn5, R.id.mTogBtn6, R.id.rl_primary, R.id.rl_about,
      R.id.exit
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.account_safe:
        if (getComponent().isLogin()) {
          startActivity(new Intent(SettingsActivity.this, MyAccountSafeActivity.class));
        } else {
          showShortToast("请登录");
        }
        break;
      case R.id.mTogBtn:
        break;
      case R.id.mTogBtn2:
        break;
      case R.id.mTogBtn3:
        break;
      case R.id.mTogBtn4:
        break;
      case R.id.mTogBtn5:
        break;
      case R.id.mTogBtn6:
        break;

      case R.id.clear_cache:
        DataCleanManager.cleanApplicationData(this);
        showShortToast("清除成功");
        cache.setText("0kB");
        //FileUtiles.DeleteTempFiles(Url.getDeleteFilesPath());
        break;
      case R.id.question_setting:
        startActivity(new Intent(SettingsActivity.this, QuestionLook.class));
        break;
      case R.id.rl_primary:
        startActivity(new Intent(SettingsActivity.this, PrimaryActivity.class));
        break;
      case R.id.rl_about:
        startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
        break;
      case R.id.exit:
        manageRpcCall(mAdvService.logout(
            new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
            new UiRpcSubscriber<String>(this) {
              @Override protected void onSuccess(String data) {

                showShortToast("退出成功");
                getComponent().loginSession().logout();
              }

              @Override protected void onEnd() {
                finish();
              }

              @Override public void onApiError(RpcApiError apiError) {
                super.onApiError(apiError);
                showShortToast("退出失败");
              }
            });
        break;
    }
  }
}
