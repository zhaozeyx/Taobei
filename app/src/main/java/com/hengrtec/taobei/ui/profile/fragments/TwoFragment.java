package com.hengrtec.taobei.ui.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CardQueryModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.profile.fragments.bean.AddMyAccountavtivity;
import com.hengrtec.taobei.ui.profile.fragments.bean.ChooseMyAccountavtivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.DateUtils;
import java.util.Date;
import javax.inject.Inject;

public class TwoFragment extends BasicTitleBarFragment {

  public static final String TYPE = "type";
  @Bind(R.id.tv_out_time) TextView tvOutTime;
  @Bind(R.id.tv_money) TextView tvMoney;
  @Bind(R.id.out_my_account) Button outMyAccount;
  public static final int REQUSET = 1;
  private String nick = "1";
  private String name = null;
  private String cardnum = null;
  private String bank = null;
  private String bankcode = null;
  @Inject UserService mAdvService;
  @Override public int getLayoutId() {
    return R.layout.fragment_two;
  }

  @Override protected void onCreateViewCompleted(View view) {
    ButterKnife.bind(this, view);
    inject();
    initData();
  }

  public static TwoFragment newInstance(String text) {
    TwoFragment fragment = new TwoFragment();
    Bundle bundle = new Bundle();
    bundle.putString(TYPE, text);
    fragment.setArguments(bundle);
    return fragment;
  }



  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);

  }
  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getActivity().getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }
  private void initData() {
    manageRpcCall(mAdvService.getAdvCardQueryList(
        new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
        new UiRpcSubscriber<CardQueryModel>(getActivity()) {
          @Override protected void onSuccess(CardQueryModel cardQueryModels) {
            if (null != cardQueryModels) {

              tvOutTime.setText( DateUtils.getFormatDateTime(new Date(System.currentTimeMillis()),DateUtils.FORMAT_FULL_TIME));

              tvMoney.setText(String.valueOf(cardQueryModels.getMoney()) + "元");
            } else {

            }
          }
          @Override protected void onEnd() {

          }
        });
  }
  @OnClick(R.id.out_my_account) public void onClick() {
    Intent intent = new Intent(getActivity(), ChooseMyAccountavtivity.class);
    //发送意图标示为REQUSET=1
    startActivityForResult(intent, REQUSET);
  }
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUSET && resultCode == getActivity().RESULT_OK) {
      name = data.getStringExtra(AddMyAccountavtivity.KEY_USER_ID);
      cardnum = data.getStringExtra(AddMyAccountavtivity.KEY_USER);
      bank = data.getStringExtra(AddMyAccountavtivity.KEY);
      if (bank != null) {
        if (bank.equals("建设银行")) {
          bankcode = "CCB";
        } else if (bank.equals("中国农业银行")) {
          bankcode = "ABC";
        } else if (bank.equals("中国工商银行")) {
          bankcode = "ICBC";
        } else if (bank.equals("中国银行")) {
          bankcode = "BOC";
        } else if (bank.equals("中国民生银行")) {
          bankcode = "CMBC";
        } else if (bank.equals("招商银行")) {
          bankcode = "CMB";
        } else if (bank.equals("中信银行")) {
          bankcode = "ChinaCITICBank";
        } else if (bank.equals("交通银行")) {
          bankcode = "BCM";
        } else if (bank.equals("华夏银行")) {
          bankcode = "HXB";
        } else if (bank.equals("中国邮政储蓄银行")) {
          bankcode = "PSBC";
        }
      }

      //manageRpcCall(mAdvService.getAdvAddCardList(
      //    new GetAddCardParams(String.valueOf(getComponent().loginSession().getUserId()), nick,
      //        bankcode, name, cardnum, bank)), new UiRpcSubscriber<CardQueryModel>(getActivity()) {
      //  @Override protected void onSuccess(CardQueryModel cardQueryModel) {
      //    showShortToast("添加成功");
      //    initData();
      //  }
      //
      //  @Override protected void onEnd() {
      //
      //  }
      //
      //  @Override public void onApiError(RpcApiError apiError) {
      //    super.onApiError(apiError);
      //    showShortToast("添加失败");
      //  }
      //});
    }
  }
}
