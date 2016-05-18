/*
 * 文件名: WebPlayFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;

/**
 * Web广告播放界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/3]
 */
public class WebPlayFragment extends BaseAdvPlayFragment {
  private static final String ARGS_KEY_URI = "key_uri";
  @Bind(R.id.webview)
  WebView mWebView;
  private String mUrl;

  public static WebPlayFragment newInstance(String uri) {
    WebPlayFragment fragment = new WebPlayFragment();
    Bundle args = new Bundle();
    args.putString(ARGS_KEY_URI, uri);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_web_play, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mUrl = getArguments().getString(ARGS_KEY_URI);
    initViews();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void initViews() {
    WebSettings webSettings = mWebView.getSettings();
    //设置支持JS
    webSettings.setJavaScriptEnabled(true);
    //设置加载进来的页面自适应手机屏幕
    webSettings.setUseWideViewPort(true);
    //设置WebView加载的页面的模式，适应屏幕宽度
    webSettings.setLoadWithOverviewMode(true);
    //设置支持缩放
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    webSettings.setAllowFileAccess(true);
    webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    webSettings.setDomStorageEnabled(true);
    webSettings.setDatabaseEnabled(true);

    //添加JS调用java接口
    //mWebView.addJavascriptInterface(new DealJs(), JS_INTERFACE_NAME);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      //不显示缩放的按钮
      webSettings.setDisplayZoomControls(false);
    }

    mWebView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        //载入网页  根据URL里的关键字符跳转到应用内的页面
        mWebView.loadUrl(mUrl);
        return true;
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description,
                                  String failingUrl) {
        //错误提示
        showShortToast(description);
      }
    });
    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int progress) {
        //载入进度改变而触发
        if (progress == 100) {
          //如果全部载入,隐藏进度对话框
          closeProgressDialog();
        }
        super.onProgressChanged(view, progress);
      }

      @Override
      public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
      }
    });
  }
}
