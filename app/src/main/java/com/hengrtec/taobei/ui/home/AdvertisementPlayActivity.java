/*
 * 文件名: AdvertisementPlayActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/22
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.AdvPlayInfo;
import com.hengrtec.taobei.net.rpc.model.AdvQuestionState;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.AdvPlayParams;
import com.hengrtec.taobei.net.rpc.service.params.AdvWatchedParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.home.event.PlayCompletedEvent;
import com.hengrtec.taobei.ui.home.event.PlayStartEvent;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;

/**
 * 广告播放界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/22]
 */
public class AdvertisementPlayActivity extends BasicTitleBarActivity {
  private static final String FRAGMENT_TAG = "play_activity";

  public static final String BUNDLE_KEY_ADV_ID = "adv_id";
  public static final String BUNDLE_KEY_ADV_TYPE = "adv_type";
  public static final String BUNDLE_KEY_ADV_URL = "adv_url";
  public static final String BUNDLE_KEY_ADV_BENEFIT_TYPE = "adv_benefit_type";
  public static final String RESULT_DATA_KEY_BENEFIT_FINAL = "benefit_final";
  public static final String RESULT_DATA_KEY_BENEFIT_TYPE = "benefit_type";
  public static final String RESULT_DATA_KEY_WATCH_ID = "watch_id";
  private int mAdvId;
  private String mAdvType;
  private String mAdvUrl;
  private String mBenefitType;
  private AdvPlayInfo mAdvPlayInfo;

  @Inject
  AdvertisementService mAdvService;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    inject();
    mAdvId = getIntent().getIntExtra(BUNDLE_KEY_ADV_ID, -1);
    mAdvType = getIntent().getStringExtra(BUNDLE_KEY_ADV_TYPE);
    mAdvUrl = getIntent().getStringExtra(BUNDLE_KEY_ADV_URL);
    mBenefitType = getIntent().getStringExtra(BUNDLE_KEY_ADV_BENEFIT_TYPE);
    Fragment fragment;
    switch (mAdvType) {
      case AdvertisementConstant.ADV_TYPE_VIDEO:
        fragment = VideoPlayFragment.newInstance(mAdvUrl);
        break;
      case AdvertisementConstant.ADV_TYPE_PIC:
        fragment = ImagePlayFragment.newInstance(mAdvUrl);
        break;
      case AdvertisementConstant.ADV_TYPE_H5:
        fragment = WebPlayFragment.newInstance(mAdvUrl);
        break;
      default:
        fragment = VideoPlayFragment.newInstance(mAdvUrl);
        break;
    }

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fragment_container, fragment,
        FRAGMENT_TAG);
    transaction.commitAllowingStateLoss();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_advertisement_play;
  }

  public static Intent makeIntent(Context context, int advId, String advType, String advUrl,
                                  String benefitType) {
    Intent intent = new Intent(context, AdvertisementPlayActivity.class);
    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_KEY_ADV_ID, advId);
    bundle.putString(BUNDLE_KEY_ADV_TYPE, advType);
    bundle.putString(BUNDLE_KEY_ADV_URL, advUrl);
    bundle.putString(BUNDLE_KEY_ADV_BENEFIT_TYPE, benefitType);
    intent.putExtras(bundle);
    return intent;
  }

  @Subscribe
  public void onPlayCompleted(PlayCompletedEvent event) {
    if (null == mAdvPlayInfo) {
      return;
    }
    manageRpcCall(mAdvService.advWatched(new AdvWatchedParams(mAdvPlayInfo.getWatchId(),
        String.valueOf(getComponent().loginSession().getUserId()), String.valueOf(mAdvId),
        mBenefitType, String.valueOf(event.timeLength))), new UiRpcSubscriber<AdvQuestionState>
        (this) {


      @Override
      protected void onSuccess(AdvQuestionState state) {
        if (TextUtils.equals(state.getNeedQuestion(), AdvertisementConstant.ADV_HAS_QUESTION)) {
          // TODO 跳转到问题列表界面 关闭本页面
          startActivity(AdvQuestionListActivity.makeIntent(AdvertisementPlayActivity.this, mAdvId));
          finish();
        } else {
          // TODO 关闭本界面，返回详情界面
          // TODO 领取红包？？？
          finishWithDada();
        }
      }

      @Override
      protected void onEnd() {

      }
    });
  }

  @Override
  public void onBackPressed() {
    // TODO 测试代码
    Intent data = new Intent();
    data.putExtra(RESULT_DATA_KEY_BENEFIT_FINAL, "10");
    data.putExtra(RESULT_DATA_KEY_BENEFIT_TYPE, AdvertisementConstant.ADV_BENEFIT_TYPE_VIRTUAL_CURRENCY);
    data.putExtra(RESULT_DATA_KEY_WATCH_ID, "");
    setResult(RESULT_OK, data);
    finish();
  }

  private void finishWithDada() {
    Intent data = new Intent();
    data.putExtra(RESULT_DATA_KEY_BENEFIT_FINAL, mAdvPlayInfo.getBenefitFinal());
    data.putExtra(RESULT_DATA_KEY_BENEFIT_TYPE, mAdvPlayInfo.getBenefitType());
    data.putExtra(RESULT_DATA_KEY_WATCH_ID, mAdvPlayInfo.getWatchId());
    setResult(RESULT_OK, data);
    finish();
  }

  @Subscribe
  public void onPlayStart(PlayStartEvent event) {
    manageRpcCall(mAdvService.advPlay(new AdvPlayParams(String.valueOf(getComponent()
        .loginSession().getUserId()), String.valueOf(mAdvId))), new UiRpcSubscriber<AdvPlayInfo>
        (this) {


      @Override
      protected void onSuccess(AdvPlayInfo advPlayInfo) {
        mAdvPlayInfo = advPlayInfo;
      }

      @Override
      protected void onEnd() {

      }
    });
  }
}
