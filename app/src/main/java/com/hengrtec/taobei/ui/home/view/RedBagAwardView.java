/*
 * 文件名: RedBagAwardView
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/28
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.ui.home.DetailSysQuestionActivity;
import com.hengrtec.taobei.ui.home.event.AwardReceiveClickedEvent;
import com.hengrtec.taobei.ui.profile.MyAccountActivity;

/**
 * 红包奖励视图<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/28]
 */
public class RedBagAwardView extends FrameLayout implements IAwardDisplay {
  @Bind(R.id.award_get)
  TextView mAwardGetView;
  @Bind(R.id.award_not_get)
  TextView mAwardNotGetView;
  @Bind(R.id.congratulations_info)
  TextView mCongratulationsInfoView;
  @Bind(R.id.divider)
  View divider;
  @Bind(R.id.adv_detail_profit_info_label_wallet)
  TextView advDetailProfitInfoLabelWallet;
  @Bind(R.id.label_container)
  RelativeLayout labelContainer;
  @Bind(R.id.btn_left)
  TextView mBtnLeftView;
  @Bind(R.id.btn_right)
  TextView mBtnRightView;
  @Bind(R.id.award_get_info)
  FrameLayout awardGetInfo;

  public RedBagAwardView(Context context) {
    super(context);
    initView();
  }

  public RedBagAwardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.award_red_bag_view, this, false);
    addView(view);
    ButterKnife.bind(this, view);
  }

  public void displayIfHasGotten(AdvertisementDetail detail, int awardNumber) {
    awardGetInfo.setVisibility(View.VISIBLE);
    mAwardNotGetView.setVisibility(View.GONE);
    mCongratulationsInfoView.setText(getResources().getString(R.string
        .adv_detail_profit_info_congratulations_real, awardNumber));
    // 如果登录，显示我的钱包，跳转到我的钱包
    if (((CustomApp) getContext().getApplicationContext()).getGlobalComponent().isLogin()) {
      mBtnLeftView.setVisibility(View.VISIBLE);
      mBtnLeftView.setText(R.string.adv_detail_profit_info_btn_wallet);
      mBtnLeftView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          getContext().startActivity(new Intent(MyAccountActivity.INTENT_ACTION_WALLET));
        }
      });
    } else {
      mBtnLeftView.setVisibility(View.VISIBLE);
      mBtnLeftView.setText(R.string.adv_detail_profit_info_btn_withdraw);
      mBtnLeftView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          getContext().startActivity(new Intent(MyAccountActivity.INTENT_ACTION_WITHDRAW));
        }
      });
    }

    if (TextUtils.equals(detail.getNeedSysQuestion(), AdvertisementConstant.ADV_HAS_QUESTION)) {
      mBtnRightView.setVisibility(VISIBLE);
      mBtnRightView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          getContext().startActivity(DetailSysQuestionActivity.makeDetailSysQuestionIntent
              (getContext(), true));
        }
      });
    } else {
      mBtnRightView.setVisibility(View.GONE);
    }
  }

  @Override
  public void displayNotGot(AdvertisementDetail detail, int awardNumber, final String watchId) {
    awardGetInfo.setVisibility(View.GONE);
    mAwardNotGetView.setVisibility(View.VISIBLE);
    mAwardNotGetView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ((CustomApp) getContext().getApplicationContext()).getGlobalComponent().getGlobalBus()
            .post(new AwardReceiveClickedEvent(AdvertisementConstant
                .ADV_BENEFIT_TYPE_REALITY_CURRENCY, watchId));
      }
    });
  }

  @OnClick({R.id.award_not_get, R.id.btn_left, R.id.btn_right})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.award_not_get:
        break;
      case R.id.btn_left:
        break;
      case R.id.btn_right:
        break;
    }
  }
}
