/*
 * 文件名: AdvertisementInvoke
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/6
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service;

import com.hengrtec.taobei.net.RetrofitFactory;
import com.hengrtec.taobei.net.rpc.model.Question;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvQuestionListParams;
import java.util.List;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/6]
 */
public class AdvertisementInvoke {

  public static final Observable<List<Question>> getAdvPlayInfo(GetAdvQuestionListParams params) {
    return ResponseCaster.castResponseObservable(RetrofitFactory.createAdvertisementService()
        .getAdvQuestionList(params));
  }

  static class ResponseCaster {
    public static <T> Observable<T> castResponseObservable(Observable<Response<ResponseModel<T>>>
                                                               observable) {
      return observable.map(new Func1<Response<ResponseModel<T>>, T>() {
        @Override
        public T call(Response<ResponseModel<T>> responseModelResponse) {
          return responseModelResponse.body().getData();
        }
      });
    }
  }
}
