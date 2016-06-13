package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;

/**
 * Created by jiao on 2016/6/13.
 */
public class QuestionLook extends BasicTitleBarActivity {
  @Bind(R.id.webView1) WebView webView1;

  @Override public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_settings_questionnaire);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override public int getLayoutId() {
    return R.layout.question_look_activity;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    initView();
  }
  private void initView() {
    //webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    //webView1.getSettings().setBuiltInZoomControls(true);
    //webView1.getSettings().setJavaScriptEnabled(true);
    //webView1.addJavascriptInterface(new JsToJava(), "stub");
    webView1.loadUrl("http://192.168.15.201:8080/taobei/questionnaire.html?userId="+String.valueOf(getComponent()
        .loginSession().getUserId()));
    //webView1.loadUrl("javascript:setValuesJson('hello world')");

  }
  private class JsToJava
  {
    public void jsMethod(String paramFromJS)
    {
      //Log.i("CDH", paramFromJS);
      System.out.println("js返回结果" + paramFromJS);//处理返回的结果
    }
  }
}
