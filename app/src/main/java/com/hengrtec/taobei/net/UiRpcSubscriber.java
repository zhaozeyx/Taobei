package com.hengrtec.taobei.net;

import android.content.Context;
import android.text.TextUtils;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.NetConstant;
import com.hengrtec.taobei.utils.preferences.CustomAppPreferences;
import retrofit2.Response;
import rx.Subscriber;

/**
 * 简化的，供UI调用网络接口使用的RpcSubscriber，统一处理HttpError提示
 */
public abstract class UiRpcSubscriber<T> extends Subscriber<Response<ResponseModel<T>>> {
  private static final String TAG = "SERVER_ERROR";
  HttpErrorUiNotifier httpErrorUiNotifier;
  private Context mContext;

  public UiRpcSubscriber(Context context) {
    mContext = context;
    httpErrorUiNotifier =
        ((CustomApp) context.getApplicationContext()).getGlobalComponent().httpErrorUiNotifier();
  }

  @Override
  public final void onCompleted() {
    onEnd();
  }

  @Override
  public final void onError(Throwable e) {
    onHttpError(new RpcHttpError(NetConstant.HttpCodeConstant.UNKNOWN_ERROR, ""));
    Logger.e(TAG, e, e.getMessage());
    onCompleted();
  }


  @Override
  public final void onNext(Response<ResponseModel<T>> responseModelResponse) {
    if (null == responseModelResponse || null == responseModelResponse.body()) {
      onHttpError(new RpcHttpError(NetConstant.HttpCodeConstant.UNKNOWN_ERROR, ""));
      return;
    }

    if (responseModelResponse.code() == NetConstant.HttpCodeConstant.SUCCESS
        && responseModelResponse.body().getResult() == 1) {
      // 存储token
      if (!TextUtils.isEmpty(responseModelResponse.body().getToken())) {
        ((CustomApp) mContext.getApplicationContext()).getGlobalComponent().appPreferences().put
            (CustomAppPreferences.KEY_COOKIE_SESSION_ID,
                responseModelResponse.body().getToken());
      }
      onSuccess(responseModelResponse.body().getData());
      return;
    }
    if (responseModelResponse.code() == NetConstant.HttpCodeConstant.HTTP_ERROR_NOT_FOUND) {
      onHttpError(new RpcHttpError(responseModelResponse.code(), responseModelResponse.message()));
      return;
    }
    onApiError(new RpcApiError(responseModelResponse.body().getResult(), responseModelResponse
        .body().getMessage()));
    onCompleted();
  }

  public void onApiError(RpcApiError apiError) {

  }

  public void onHttpError(RpcHttpError httpError) {
    httpErrorUiNotifier.notifyUi(httpError);
  }

  protected abstract void onSuccess(T t);

  protected abstract void onEnd();

}
