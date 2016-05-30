/*
 * 文件名: AwardViewFactory
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

import android.view.View;
import android.view.ViewGroup;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/28]
 */
public class AwardViewController {
  public ViewGroup mAwardViewContainer;

  public AwardViewController(ViewGroup awardViewContainer) {
    this.mAwardViewContainer = awardViewContainer;
  }

  public void loadUi(AdvertisementDetail detail, boolean watchComplete, int finalProfit, boolean hasGotten) {
    if (!watchComplete) {
      mAwardViewContainer.setVisibility(View.GONE);
      return;
    }
    mAwardViewContainer.setVisibility(View.VISIBLE);
    View awardView = loadAwardViewByType(detail.getBenefitType());
    if (!(awardView instanceof IAwardDisplay)) {
      mAwardViewContainer.setVisibility(View.GONE);
      return;
    }
    mAwardViewContainer.addView(awardView);
    if (hasGotten) {
      ((IAwardDisplay) awardView).displayIfHasGotten(detail, finalProfit);
    } else {
      ((IAwardDisplay) awardView).displayNotGot(detail, finalProfit);
    }
  }

  private View loadAwardViewByType(String benefitType) {
    switch (benefitType) {
      case AdvertisementConstant.ADV_BENEFIT_TYPE_REALITY_CURRENCY:
        return new RedBagAwardView(mAwardViewContainer.getContext());
      case AdvertisementConstant.ADV_BENEFIT_TYPE_VIRTUAL_CURRENCY:
        return new BBJAwardView(mAwardViewContainer.getContext());
      case AdvertisementConstant.ADV_BENEFIT_TYPE_COUPON:
        return new CouponAwardView(mAwardViewContainer.getContext());
    }
    mAwardViewContainer.setVisibility(View.GONE);
    return null;
  }
}
