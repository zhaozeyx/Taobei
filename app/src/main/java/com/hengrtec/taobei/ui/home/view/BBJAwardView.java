package com.hengrtec.taobei.ui.home.view;

import android.content.Context;
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
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.ui.home.DetailSysQuestionActivity;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/28]
 */
public class BBJAwardView extends FrameLayout implements IAwardDisplay {
  @Bind(R.id.virtual_profit_not_get)
  TextView mAwardNotGetView;
  @Bind(R.id.virtual_profit_get)
  TextView mAwardGetFView;
  @Bind(R.id.profit_congratulations_info)
  TextView mCongratulationsInfo;
  @Bind(R.id.divider)
  View divider;
  @Bind(R.id.adv_detail_profit_info_label_wallet)
  TextView advDetailProfitInfoLabelWallet;
  @Bind(R.id.adv_detail_profit_info_label_wallet_container)
  RelativeLayout advDetailProfitInfoLabelWalletContainer;
  @Bind(R.id.btn_left)
  TextView mBtnLeft;
  @Bind(R.id.btn_right)
  TextView mBtnRight;
  @Bind(R.id.adv_detail_profit_get_info)
  FrameLayout advDetailProfitGetInfo;

  public BBJAwardView(Context context) {
    super(context);
    initView();
  }

  public BBJAwardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.award_bbj_view, this, false);
    addView(view);
    ButterKnife.bind(this, view);
  }

  @Override
  public void displayIfHasGotten(AdvertisementDetail detail, int awardNumber) {
    // TODO 设置贝贝金收益信息
    mCongratulationsInfo.setText(getResources().getString(R.string
        .adv_detail_profit_info_congratulations_virtual, awardNumber));
    mBtnLeft.setVisibility(View.VISIBLE);
    mBtnLeft.setText(R.string.adv_detail_profit_info_btn_my_profit);
    mBtnLeft.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO 跳转到我的收益界面
        // TODO 判断是否登录
      }
    });

    if (TextUtils.equals(detail.getNeedSysQuestion(), AdvertisementConstant.ADV_HAS_QUESTION)) {
      mBtnRight.setVisibility(VISIBLE);
      mBtnRight.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          // TODO 跳转到我的答题领取贝贝金界面
          getContext().startActivity(DetailSysQuestionActivity.makeDetailSysQuestionIntent
              (getContext(), true));
        }
      });
    } else {
      mBtnRight.setVisibility(View.GONE);
    }
  }

  @Override
  public void displayNotGot(AdvertisementDetail detail, int awardNumber) {
    // 如果没领取，显示领取红包按钮
    // 理论上不存在贝贝金未领取的可能，按照产品设计，播放完毕自动领取红包
    advDetailProfitGetInfo.setVisibility(View.GONE);
    mAwardNotGetView.setVisibility(TextUtils.equals(detail.getBenefitType(),
        AdvertisementConstant
            .ADV_BENEFIT_TYPE_REALITY_CURRENCY) ? VISIBLE : GONE);
    mAwardNotGetView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO 跳转到领取红包界面
        // TODO POST EVENT ???
      }
    });
  }

  @OnClick(R.id.virtual_profit_get)
  public void onClick() {
  }
}