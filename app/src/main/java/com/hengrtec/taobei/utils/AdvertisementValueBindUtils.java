/*
 * 文件名: AdvertisementValueBindUtils
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/26
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/26]
 */
public class AdvertisementValueBindUtils {
  public static void setProfit(@NonNull TextView view, @NonNull String benefitType, String price,
                               int profitRealTextId,
                               int profitVirtualTextId) {
    Context context = view.getContext();
    if (TextUtils.equals(AdvertisementConstant.ADV_BENEFIT_TYPE_REALITY_CURRENCY, benefitType)) {
      view.setText(context.getResources().getString(profitRealTextId,
          price));
    } else if (TextUtils.equals(AdvertisementConstant.ADV_BENEFIT_TYPE_VIRTUAL_CURRENCY,
        benefitType)) {
      view.setText(context.getResources().getString(profitVirtualTextId,
          price));
    } else if (TextUtils.equals(AdvertisementConstant.ADV_BENEFIT_TYPE_COUPON, benefitType)) {
      view.setText(R.string.adv_item_profit_coupon);
    }
  }
}
