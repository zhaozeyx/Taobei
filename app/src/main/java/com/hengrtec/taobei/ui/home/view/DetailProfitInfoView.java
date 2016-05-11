/*
 * 文件名: DetailProfitInfoView
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/4
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;

/**
 * 广告详情页面收益提示界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/4]
 */
public class DetailProfitInfoView extends FrameLayout {

  @Bind(R.id.real_profit_not_get)
  TextView mRealProfitNotGet;
  @Bind(R.id.virtual_profit_not_get)
  TextView mVirtualProfitNotGet;
  @Bind(R.id.real_profit_get)
  TextView mRealProfitGet;
  @Bind(R.id.virtual_profit_get)
  TextView mVirtualProfitGet;
  @Bind(R.id.profit_congratulations_info)
  TextView mProfitCongratulationsInfo;
  @Bind(R.id.adv_detail_profit_info_label_wallet_container)
  RelativeLayout mAdvDetailProfitInfoLabelWalletContainer;
  @Bind(R.id.btn_left)
  TextView mBtnLeft;
  @Bind(R.id.btn_right)
  TextView mBtnRight;
  @Bind(R.id.adv_detail_profit_get_info)
  FrameLayout mAdvDetailProfitGetInfo;

  private CustomApp mCustomApp;
  private boolean hasLogin;

  private OnReceiveBtnClickListener mReceiveBtnClickListener;

  public DetailProfitInfoView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mCustomApp = (CustomApp) context.getApplicationContext();
    hasLogin = mCustomApp.getGlobalComponent().isLogin();
    initView();
  }

  public void initView() {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    View childView = inflater.inflate(R.layout.view_adv_detail_profit_info, this, false);
    addView(childView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    ButterKnife.bind(this, childView);
  }

  @OnClick(R.id.real_profit_get)
  public void onClick() {
    // TODO 点击领取金币
  }

  public void setOnReceiveBtnClickListener(OnReceiveBtnClickListener listener) {
    mReceiveBtnClickListener = listener;
  }

  /**
   * 根据观看状态,收益类型,是否登录,收益金额展示界面
   */
  public void update(AdvertisementDetail detail, int profitCount, boolean
      viewCompleted, boolean hasGotten, boolean hasQuestion) {
    Logger.d("DetailProfitInfoView", "benefitType %s   has Gotten %s ", detail.getBenefitType(),
        String.valueOf
            (hasGotten));
    // 如果观看未完成,全部隐藏
    if (!viewCompleted) {
      mAdvDetailProfitGetInfo.setVisibility(GONE);
      mRealProfitGet.setVisibility(GONE);
      mRealProfitNotGet.setVisibility(GONE);
      mVirtualProfitGet.setVisibility(GONE);
      mVirtualProfitNotGet.setVisibility(GONE);
      return;
    }
    // 如果已经领取
    if (hasGotten) {
      mAdvDetailProfitGetInfo.setVisibility(View.VISIBLE);

      if (TextUtils.equals(detail.getBenefitType(), AdvertisementConstant
          .ADV_BENEFIT_TYPE_REALITY_CURRENCY)) {
        // TODO 设置红包收益信息
        mProfitCongratulationsInfo.setText(getResources().getString(R.string
            .adv_detail_profit_info_congratulations_real, profitCount));
        // 如果登录，显示我的钱包，跳转到我的钱包
        if (hasLogin) {
          mBtnLeft.setVisibility(View.VISIBLE);
          mBtnLeft.setText(R.string.adv_detail_profit_info_btn_wallet);
          mBtnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              // TODO 跳转到我的钱包
            }
          });
        } else {
          mBtnLeft.setVisibility(View.VISIBLE);
          mBtnLeft.setText(R.string.adv_detail_profit_info_btn_withdraw);
          mBtnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              // TODO 跳转到提现界面
            }
          });
        }
      } else if (TextUtils.equals(detail.getBenefitType(), AdvertisementConstant
          .ADV_BENEFIT_TYPE_VIRTUAL_CURRENCY)) {
        // TODO 设置贝贝金收益信息
        mProfitCongratulationsInfo.setText(getResources().getString(R.string
            .adv_detail_profit_info_congratulations_virtual, profitCount));
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setText(R.string.adv_detail_profit_info_btn_my_profit);
        mBtnLeft.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            // TODO 跳转到我的收益界面
            // TODO 判断是否登录
          }
        });
      }
      if (hasQuestion) {
        mBtnRight.setVisibility(VISIBLE);
        mBtnRight.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            // TODO 跳转到我的答题领取贝贝金界面
          }
        });
      } else {
        mBtnRight.setVisibility(View.GONE);
      }
    } else {
      // 如果没领取，显示领取红包按钮
      // 理论上不存在贝贝金未领取的可能，按照产品设计，播放完毕自动领取红包
      mAdvDetailProfitGetInfo.setVisibility(View.GONE);
      mRealProfitNotGet.setVisibility(TextUtils.equals(detail.getBenefitType(),
          AdvertisementConstant
              .ADV_BENEFIT_TYPE_REALITY_CURRENCY) ? VISIBLE : GONE);
      mVirtualProfitNotGet.setVisibility(TextUtils.equals(detail.getBenefitType(),
          AdvertisementConstant
              .ADV_BENEFIT_TYPE_REALITY_CURRENCY) ? GONE : VISIBLE);
      mRealProfitNotGet.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          // TODO 跳转到领取红包界面
          if (null != mReceiveBtnClickListener) {
            mReceiveBtnClickListener.onBtnClicked();
          }
        }
      });
      mVirtualProfitNotGet.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (null != mReceiveBtnClickListener) {
            mReceiveBtnClickListener.onBtnClicked();
          }
        }
      });
    }

  }

  public interface OnReceiveBtnClickListener {
    void onBtnClicked();
  }
}
