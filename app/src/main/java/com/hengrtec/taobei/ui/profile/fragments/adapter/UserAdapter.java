package com.hengrtec.taobei.ui.profile.fragments.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.CardQueryModel;
import com.jtech.adapter.RecyclerAdapter;
import com.jtech.view.RecyclerHolder;

/**
 * 用户列表适配器
 * Created by wuxubaiyang on 2016/2/6.
 */
public class UserAdapter extends RecyclerAdapter<CardQueryModel.AccountsEntity> {

  public UserAdapter(Activity activity) {
    super(activity);
  }

  @Override public View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
    return inflater.inflate(R.layout.view_user_item, parent, false);
  }

  @Override public void convert(final RecyclerHolder holder, CardQueryModel.AccountsEntity item, int position) {
    holder.setText(R.id.textview_user_item_name, item.getBankName());
    holder.setText(R.id.textview_user_item_sex, item.getAccountName());
    holder.setText(R.id.textview_user_item_age, item.getAccountUserName());
    //设置一个视图可以触摸拖动
    String code = item.getBankNameCode();
    if (init(code) != 0) {
      holder.setImageResource(R.id.imageview, init(code));
    }
    addItemTouchToMove(holder, R.id.imageview);

  }

  /*
  * 中国建设银行——CCB
  * 中国农业银行——ABC
  * 中国工商银行——ICBC
  * 中国银行——BOC
  * 中国民生银行——CMBC
  * 招商银行 ——CMB
  * 兴业银行 ——CIB
  * 交通银行——BCM
  * 华夏银行--HXB
  * 邮政储蓄银行---PSBC
  */
  private int init(String code) {
    if (null != code) {
      if (code.equals("CCB")) {
        return R.mipmap.jianshe;
      } else if (code.equals("ABC")) {
        return R.mipmap.zhongguonongye;
      } else if (code.equals("ICBC")) {
        return R.mipmap.gongshang;
      } else if (code.equals("BOC")) {
        return R.mipmap.zhongguoyinhang;
      } else if (code.equals("CMBC")) {
        return R.mipmap.minsheng;
      } else if (code.equals("CMB")) {
        return R.mipmap.zhaoshang;
      } else if (code.equals("ChinaCITICBank")) {
        return R.mipmap.zhongxinshiye;
      } else if (code.equals("BCM")) {
        return R.mipmap.jiaotong;
      } else if (code.equals("PSBC")) {
        return R.mipmap.youzheng;
      } else if (code.equals("HXB")) {
        return R.mipmap.huaxia;
      } else {
        return 0;
      }
    }
    return 0;
  }
}
