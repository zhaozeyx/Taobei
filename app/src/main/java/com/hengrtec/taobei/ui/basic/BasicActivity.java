package com.hengrtec.taobei.ui.basic;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.injection.ActivityComponent;
import com.hengrtec.taobei.injection.ActivityModule;
import com.hengrtec.taobei.injection.DaggerActivityComponent;
import com.hengrtec.taobei.net.RpcCallManager;
import com.hengrtec.taobei.ui.login.event.LoginEvent;
import com.hengrtec.taobei.ui.login.event.LogoutEvent;
import com.squareup.otto.Subscribe;
import dmax.dialog.SpotsDialog;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhaozeyang on 16/4/11.
 */
public class BasicActivity extends BaseFragmentActivity implements RpcCallManager {
  private RpcCallManager.RpcCallManagerImpl rpcCallManager =
      new RpcCallManager.RpcCallManagerImpl();

  private ActivityComponent mActivityComponent;

  private boolean mIsPaused = false;

  private SessionEventsHandler mSessionHandler = new SessionEventsHandler();

  /**
   * 进度显示框
   */
  private SpotsDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    createActivityComponent();
    getComponent().getGlobalBus().register(this);
    getComponent().getGlobalBus().register(mSessionHandler);
  }

  public <T> void manageRpcCall(Observable<T> observable, Subscriber<T> subscribe) {
    rpcCallManager.manageRpcCall(observable, subscribe);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    getComponent().getGlobalBus().unregister(this);
    getComponent().getGlobalBus().unregister(mSessionHandler);
    rpcCallManager.unsubscribeAll();
    closeProgressDialog();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mIsPaused = true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    mIsPaused = false;
  }

  protected void showShortToast(int msgId) {
    Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
  }

  protected void showShortToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  /**
   * 显示进度框<BR>
   *
   * @param message 对话框显示信息
   * @param cancelable 对话框可取消标志
   */
  protected void showProgressDialog(String message, boolean cancelable) {
    if (mProgressDialog == null) {
      //mProDialog = new ProgressDialog(getActivity(), cancelable);
      mProgressDialog = new SpotsDialog(this);
      mProgressDialog.setCancelable(cancelable);
      mProgressDialog.setCanceledOnTouchOutside(cancelable);
    }
    mProgressDialog.setMessage(message);
    showProgressDialog(mProgressDialog);
  }

  /**
   * 弹出进度框<BR>
   *
   * @param proDialog 对话框显示信息
   */
  protected void showProgressDialog(AlertDialog proDialog) {
    if (!mIsPaused) {
      proDialog.show();
    }
  }

  /**
   * 关闭进度框<BR>
   */
  protected void closeProgressDialog() {
    // 关闭ProgressDialog
    if (null != mProgressDialog) {
      mProgressDialog.dismiss();
      mProgressDialog = null;
    }
  }

  private void createActivityComponent() {
    mActivityComponent = DaggerActivityComponent.builder().activityModule(new ActivityModule(this))
        .globalComponent(((CustomApp) getApplication()).getGlobalComponent())
        .build();
  }

  protected ActivityComponent getComponent() {
    return mActivityComponent;
  }

  /**
   * 当焦点停留在view上时，隐藏输入法栏
   *
   * @param view view
   */
  protected void hideInputWindow(View view) {
    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    if (null != imm && null != view) {
      imm.hideSoftInputFromWindow(view.getWindowToken(),
          InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  /**
   * 当焦点停留在view上时，显示输入法栏
   *
   * @param view view
   */
  protected void showInputWindow(View view) {
    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    if (null != imm && null != view) {
      imm.showSoftInput(view, 0);
    }
  }

  protected void onLogin() {

  }

  protected void onLogout() {

  }

  private class SessionEventsHandler {
    @Subscribe
    public void dispatchLogin(LoginEvent event) {
      onLogin();
    }

    @Subscribe
    public void dispatchLogout(LogoutEvent event) {
      onLogout();
    }
  }

}
