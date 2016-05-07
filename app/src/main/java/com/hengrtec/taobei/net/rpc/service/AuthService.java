/*
 * 文件名: AuthService
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/29
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service;

import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.net.rpc.service.params.CheckVerifyCodeParams;
import com.hengrtec.taobei.net.rpc.service.params.GetVerifyCodeParams;
import com.hengrtec.taobei.net.rpc.service.params.LoginParams;
import com.hengrtec.taobei.net.rpc.service.params.LoginWithVerifyCodeParams;
import com.hengrtec.taobei.net.rpc.service.params.RegisterParams;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 鉴权接口<BR>
 * 包括 登录，验证码获取和验证，注册，获取用户信息
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/29]
 */
public interface AuthService {
  @POST("smsSend.do")
  Observable<Response<ResponseModel<String>>> getVerifyCode(@Body GetVerifyCodeParams params);

  @POST("smsVerify.do")
  Observable<Response<ResponseModel<Void>>> checkVerifyCode(@Body CheckVerifyCodeParams params);

  @POST("reg.do")
  Observable<Response<ResponseModel<UserInfo>>> register(@Body RegisterParams params);

  @POST("applogin.do")
  Observable<Response<ResponseModel<UserInfo>>> loginWithPassword(@Body LoginParams params);

  @POST("smslogin.do")
  Observable<Response<ResponseModel<UserInfo>>> loginWithVerifyCode(@Body LoginWithVerifyCodeParams params);

}
