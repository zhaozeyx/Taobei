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
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.AdvQuestionState;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.AdvWatchedParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.home.event.PlayCompletedEvent;
import com.hengrtec.taobei.ui.home.event.PlayNotCompletedEvent;
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
  public static final String RESULT_KEY_WATCH_ID = "result_key_watch_id";
  private static final String FRAGMENT_TAG = "play_activity";

  public static final String BUNDLE_KEY_ADV_ID = "adv_id";
  public static final String BUNDLE_KEY_ADV_TYPE = "adv_type";
  public static final String BUNDLE_KEY_ADV_URL = "adv_url";
  public static final String BUNDLE_KEY_ADV_BENEFIT_TYPE = "adv_benefit_type";
  public static final String BUNDLE_KEY_BENEFIT_FINAL = "benefit_final";
  public static final String BUNDLE_KEY_WATCH_ID = "watch_id";
  private int mAdvId;
  private String mAdvType;
  private String mAdvUrl;
  private String mBenefitType;
  private String mWatchId;
  private String mBenefitFinal;
  @Inject
  AdvertisementService mAdvService;

  private boolean mPlayCompleted = false;
  private int mPlayingTime = 0;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    inject();
    mAdvId = getIntent().getIntExtra(BUNDLE_KEY_ADV_ID, -1);
    mAdvType = getIntent().getStringExtra(BUNDLE_KEY_ADV_TYPE);
    mAdvUrl = getIntent().getStringExtra(BUNDLE_KEY_ADV_URL);
    mBenefitType = getIntent().getStringExtra(BUNDLE_KEY_ADV_BENEFIT_TYPE);
    mWatchId = getIntent().getStringExtra(BUNDLE_KEY_WATCH_ID);
    mBenefitFinal = getIntent().getStringExtra(BUNDLE_KEY_BENEFIT_FINAL);
    BaseAdvPlayFragment fragment;
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

    fragment.setOnPlayingListener(new BaseAdvPlayFragment.OnPlayingListener() {
      @Override
      public void onPlaying(int playTime) {
        mPlayingTime = playTime;
      }
    });

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
                                  String benefitType, String benefitFinal, String watchId) {
    Intent intent = new Intent(context, AdvertisementPlayActivity.class);
    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_KEY_ADV_ID, advId);
    bundle.putString(BUNDLE_KEY_ADV_TYPE, advType);
    bundle.putString(BUNDLE_KEY_ADV_URL, advUrl);
    bundle.putString(BUNDLE_KEY_ADV_BENEFIT_TYPE, benefitType);
    bundle.putString(BUNDLE_KEY_BENEFIT_FINAL, benefitFinal);
    bundle.putString(BUNDLE_KEY_WATCH_ID, watchId);
    intent.putExtras(bundle);
    return intent;
  }

  @Subscribe
  public void onPlayCompleted(PlayCompletedEvent event) {
    mPlayCompleted = true;
    manageRpcCall(mAdvService.advWatched(new AdvWatchedParams(mWatchId,
        String.valueOf(getComponent().loginSession().getUserId()), String.valueOf(mAdvId),
        mBenefitType, String.valueOf(event.timeLength))), new UiRpcSubscriber<AdvQuestionState>
        (this) {

      @Override
      protected void onSuccess(AdvQuestionState state) {
        if (TextUtils.equals(state.getNeedQuestion(), AdvertisementConstant.ADV_HAS_QUESTION)) {
          // 跳转到问题列表界面 关闭本页面
          startActivity(AdvQuestionListActivity.makeIntent(AdvertisementPlayActivity.this,
              mAdvId, mWatchId));
          finish();
        } else {
          Intent intent = new Intent();
          intent.putExtra(RESULT_KEY_WATCH_ID, mWatchId);
          setResult(RESULT_OK, intent);
          finish();
        }
      }

      @Override
      protected void onEnd() {

      }
    });
  }

  @Override
  protected void onDestroy() {
    if (!mPlayCompleted) {
      getComponent().getGlobalBus().post(new PlayNotCompletedEvent(mPlayingTime));
    }
    super.onDestroy();
  }
}
