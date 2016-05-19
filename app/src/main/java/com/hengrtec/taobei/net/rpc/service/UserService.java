/*
 * 文件名: UserService
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.net.rpc.service;

import com.hengrtec.taobei.net.rpc.model.CommentModel;
import com.hengrtec.taobei.net.rpc.model.FriendsCircleModel;
import com.hengrtec.taobei.net.rpc.model.MyBenefitModel;
import com.hengrtec.taobei.net.rpc.model.ProfitRecordModel;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.model.SignInModel;
import com.hengrtec.taobei.net.rpc.service.params.CommentParams;
import com.hengrtec.taobei.net.rpc.service.params.DeleteCommentParams;
import com.hengrtec.taobei.net.rpc.service.params.FriendsCircleParams;
import com.hengrtec.taobei.net.rpc.service.params.MyBenefitParams;
import com.hengrtec.taobei.net.rpc.service.params.ProfitRecordsParams;
import com.hengrtec.taobei.net.rpc.service.params.SignInParams;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用户信息<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public interface UserService {

  @POST("selectcheckin.do")
  Observable<Response<ResponseModel<SignInModel>>> selectCheckIn(@Body SignInParams params);

  @POST("docheckin.do")
  Observable<Response<ResponseModel<Boolean>>> docheckin(@Body SignInParams params);

  @POST("mybenefit.do")
  Observable<Response<ResponseModel<MyBenefitModel>>> myBenefit(@Body MyBenefitParams params);

  @POST("mytransactionlist.do")
  Observable<Response<ResponseModel<List<ProfitRecordModel>>>> myTransactionList(@Body
                                                                                 ProfitRecordsParams
                                                                                     params);

  @POST("friendscircle.do")
  Observable<Response<ResponseModel<List<FriendsCircleModel>>>> friendsCircle(@Body
                                                                              FriendsCircleParams
                                                                                  params);

  @POST("mycommentslist.do")
  Observable<Response<ResponseModel<List<CommentModel>>>> myCommentList(@Body CommentParams params);

  @POST("delcomment.do")
  Observable<Response<ResponseModel<String>>> deleteComment(@Body DeleteCommentParams params);

}
