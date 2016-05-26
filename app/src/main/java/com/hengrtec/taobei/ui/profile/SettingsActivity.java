package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CardQueryModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;

/**
 * Created by jiao on 2016/5/16.
 */
public class SettingsActivity extends BasicTitleBarActivity {
  @Bind(R.id.rl_about) RelativeLayout mAbout;
  @Bind(R.id.rl_primary) RelativeLayout mPrimary;
  @Bind(R.id.setting_qingchuhuancun) RelativeLayout settingQingchuhuancun;
  @Bind(R.id.mTogBtn) ToggleButton mTogBtn;
  @Bind(R.id.mTogBtn2) ToggleButton mTogBtn2;
  @Bind(R.id.mTogBtn3) ToggleButton mTogBtn3;
  @Bind(R.id.mTogBtn4) ToggleButton mTogBtn4;
  @Bind(R.id.mTogBtn5) ToggleButton mTogBtn5;
  @Bind(R.id.mTogBtn6) ToggleButton mTogBtn6;
  @Bind(R.id.exit) Button exit;
  @Inject UserService mAdvService;

  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initListView();
    //        loadData(true);
  }

  private void initListView() {
    mAbout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
      }
    });
    mPrimary.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(SettingsActivity.this, PrimaryActivity.class));
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

  @OnClick(R.id.exit) public void onClick() {
    manageRpcCall(mAdvService.getAdvExitList(
        new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
        new UiRpcSubscriber<CardQueryModel>(this) {
          @Override protected void onSuccess(CardQueryModel cardQueryModel) {

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
  }
}
