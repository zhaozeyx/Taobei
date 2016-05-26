package com.hengrtec.taobei.ui.profile.fragments.bean;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.hengrtec.taobei.net.rpc.service.params.GetOutCardParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import javax.inject.Inject;

/**
 * Created by jiao on 2016/5/23.
 */
public class OutMyAccountavtivity extends BasicTitleBarActivity {
  @Bind(R.id.username) TextView username;
  @Bind(R.id.cardnum) TextView cardnum;
  @Bind(R.id.choose_card) TextView chooseCard;
  @Bind(R.id.add) TextView add;
  public static final int REQUSET = 1;
  public static final String KEY_USER_ID = "name";
  public static final String KEY_USER = "cardnum";
  public static final String KEY = "chooseCard";
  @Bind(R.id.et_money) EditText etMoney;
  @Inject UserService mAdvService;
  private String name ;
  private String account;
  private String bank ;
  private String bankcode;
  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);

    bank=getIntent().getExtras().getString("bank");
    bankcode=getIntent().getExtras().getString("bankcode");
    account=getIntent().getExtras().getString("cardnum");
    name=getIntent().getExtras().getString("name");
    username.setText(name);
    cardnum.setText(account);
    chooseCard.setText(bank);
    inject();
  }

  @Override public int getLayoutId() {
    return R.layout.activity_out_card;
  }

  @Override public boolean initializeTitleBar() {
    setMiddleTitle(R.string.my_account_out_money);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }


  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }
  @OnClick({ R.id.choose_card, R.id.add }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.choose_card:
        //Intent intent = new Intent(this, ChooseCardActivity.class);
        //
        ////发送意图标示为REQUSET=1
        //startActivityForResult(intent, REQUSET);
        break;
      case R.id.add:


        if (!"".equals(etMoney.getText().toString())) {
          manageRpcCall(mAdvService.getAdvOutCardList(

              new GetOutCardParams(String.valueOf(getComponent().loginSession().getUserId()),bankcode,name,account,etMoney.getText().toString())),
              new UiRpcSubscriber<CardQueryModel>(this) {
                @Override protected void onSuccess(CardQueryModel cardQueryModel) {
                  showShortToast("提现成功");


                }

                @Override protected void onEnd() {

                }

                @Override public void onApiError(RpcApiError apiError) {
                  super.onApiError(apiError);
                  showShortToast("提现失败");
                }
              });
          //intent1.putExtra(KEY_USER_ID, username.getText().toString());
          //intent1.putExtra(KEY_USER, cardnum.getText().toString());
          //intent1.putExtra(KEY, chooseCard.getText().toString());
          //00
          //setResult(RESULT_OK, intent1);
          finish();
        } else {
          showShortToast("请填写提现金额");
        }
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUSET && resultCode == RESULT_OK) {
      chooseCard.setText(data.getStringExtra(ChooseCardActivity.KEY_USER_ID));
    }
  }
}
