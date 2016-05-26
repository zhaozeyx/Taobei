package com.hengrtec.taobei.ui.profile.fragments.bean;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

/**
 * Created by jiao on 2016/5/23.
 */
public class ChooseCardActivity extends BasicTitleBarActivity {
  @Bind(R.id.zhongguo) LinearLayout zhongguo;
  @Bind(R.id.nongye) LinearLayout nongye;
  @Bind(R.id.jianshe) LinearLayout jianshe;
  @Bind(R.id.gongshang) LinearLayout gongshang;
  @Bind(R.id.youzheng) LinearLayout youzheng;
  @Bind(R.id.zhaoshang) LinearLayout zhaoshang;
  @Bind(R.id.zhongxinshiye) LinearLayout zhongxinshiye;
  @Bind(R.id.huaxia) LinearLayout huaxia;
  @Bind(R.id.jiaotong) LinearLayout jiaotong;
  @Bind(R.id.minsheng) LinearLayout minsheng;
  @Bind(R.id.zhongguo1) TextView zhongguo1;
  @Bind(R.id.nongye1) TextView nongye1;
  @Bind(R.id.jianshe1) TextView jianshe1;
  @Bind(R.id.gongshang1) TextView gongshang1;
  @Bind(R.id.youzheng1) TextView youzheng1;
  @Bind(R.id.zhaoshang1) TextView zhaoshang1;
  @Bind(R.id.zhongxinshiye1) TextView zhongxinshiye1;
  @Bind(R.id.huaxia1) TextView huaxia1;
  @Bind(R.id.jiaotong1) TextView jiaotong1;
  @Bind(R.id.minsheng1) TextView minsheng1;
  public static final String KEY_USER_ID="bankname";
  private Intent intent=new Intent();
  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
  }

  @Override public int getLayoutId() {
    return R.layout.activity_choose_card;
  }

  private void initView() {

  }
  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.my_account_choose_bank_card);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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

  @OnClick({
      R.id.zhongguo, R.id.nongye, R.id.jianshe, R.id.gongshang, R.id.youzheng, R.id.zhaoshang,
      R.id.zhongxinshiye, R.id.huaxia, R.id.jiaotong, R.id.minsheng
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.zhongguo:
        intent.putExtra(KEY_USER_ID, zhongguo1.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.nongye:
        intent.putExtra(KEY_USER_ID, nongye1.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.jianshe:
        intent.putExtra(KEY_USER_ID, jianshe1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.gongshang:
        intent.putExtra(KEY_USER_ID, gongshang1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.youzheng:
        intent.putExtra(KEY_USER_ID, youzheng1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.zhaoshang:
        intent.putExtra(KEY_USER_ID, zhaoshang1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.zhongxinshiye:
        intent.putExtra(KEY_USER_ID, zhongxinshiye1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.huaxia:
        intent.putExtra(KEY_USER_ID, huaxia1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.jiaotong:
        intent.putExtra(KEY_USER_ID, jiaotong1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
      case R.id.minsheng:
        intent.putExtra(KEY_USER_ID, minsheng1.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        break;
    }
  }
}
