package com.hengrtec.taobei.ui.profile.fragments.bean;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

/**
 * Created by jiao on 2016/5/23.
 */
public class AddMyAccountavtivity extends BasicTitleBarActivity {
  @Bind(R.id.username) EditText username;
  @Bind(R.id.cardnum) EditText cardnum;
  @Bind(R.id.choose_card) TextView chooseCard;
  @Bind(R.id.add) TextView add;
  public static final int REQUSET = 1;
  public static final String KEY_USER_ID = "name";
  public static final String KEY_USER = "cardnum";
  public static final String KEY = "chooseCard";

  @Override protected void afterCreate(Bundle savedInstance) {

  }

  @Override public int getLayoutId() {
    return R.layout.activity_add_card;
  }

  @Override public boolean initializeTitleBar() {
    setMiddleTitle(R.string.my_account_add_card);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
  }

  @OnClick({ R.id.choose_card, R.id.add }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.choose_card:
        Intent intent = new Intent(this, ChooseCardActivity.class);

        //发送意图标示为REQUSET=1
        startActivityForResult(intent, REQUSET);
        break;
      case R.id.add:
        Intent intent1 = new Intent();

        if (!"".equals(username.getText().toString())
            && !"".equals(cardnum.getText().toString())
            && !"".equals(chooseCard.getText().toString())) {
          intent1.putExtra(KEY_USER_ID, username.getText().toString());
          intent1.putExtra(KEY_USER, cardnum.getText().toString());
          intent1.putExtra(KEY, chooseCard.getText().toString());

          setResult(RESULT_OK, intent1);
          finish();
        } else {
          showShortToast("请填写姓名卡号或者选择银行卡");
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
