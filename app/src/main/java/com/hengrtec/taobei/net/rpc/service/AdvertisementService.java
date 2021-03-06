/*
 * 文件名: AdvertisementService
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

import com.hengrtec.taobei.net.rpc.model.AdvPlayInfo;
import com.hengrtec.taobei.net.rpc.model.AdvQuestionState;
import com.hengrtec.taobei.net.rpc.model.Advertisement;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.model.BibiModel;
import com.hengrtec.taobei.net.rpc.model.BibiReport;
import com.hengrtec.taobei.net.rpc.model.CollectAdvModel;
import com.hengrtec.taobei.net.rpc.model.GetMoneyModel;
import com.hengrtec.taobei.net.rpc.model.MyTaskModel;
import com.hengrtec.taobei.net.rpc.model.Question;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.params.AdvOperationParams;
import com.hengrtec.taobei.net.rpc.service.params.AdvPlayParams;
import com.hengrtec.taobei.net.rpc.service.params.AdvWatchedParams;
import com.hengrtec.taobei.net.rpc.service.params.BibiParams;
import com.hengrtec.taobei.net.rpc.service.params.BibiReportParams;
import com.hengrtec.taobei.net.rpc.service.params.DoMyCommentParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvQuestionListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementDetailParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetBenefitParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCollectionsParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCommentListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetSettingsParams;
import com.hengrtec.taobei.net.rpc.service.params.GetUserAdvStateParams;
import com.hengrtec.taobei.net.rpc.service.params.LikeCommentParams;
import com.hengrtec.taobei.net.rpc.service.params.RecordUserPlayDurationParams;
import com.hengrtec.taobei.net.rpc.service.params.SubAdvQuestionAnswerParams;
import com.hengrtec.taobei.net.rpc.service.params.SysQuestionByIdParams;
import com.hengrtec.taobei.net.rpc.service.params.SysQuestionParams;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/*
 * 文件名: AdvertisementService
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
public interface AdvertisementService {
  @POST("advlist.do") Observable<Response<ResponseModel<List<Advertisement>>>> getAdvertisements(
      @Body GetAdvertisementListParams params);

  @POST("advdetail.do") Observable<Response<ResponseModel<AdvertisementDetail>>> getAdvertisement(
      @Body GetAdvertisementDetailParams params);

  @POST("useradvstate.do") Observable<Response<ResponseModel<String>>> getUserAdvState(
      @Body GetUserAdvStateParams params);

  @POST("getqlist.do") Observable<Response<ResponseModel<List<Question>>>> getAdvQuestionList(
      @Body GetAdvQuestionListParams params);

  @POST("advplay.do") Observable<Response<ResponseModel<AdvPlayInfo>>> advPlay(
      @Body AdvPlayParams params);

  @POST("advwatched.do") Observable<Response<ResponseModel<AdvQuestionState>>> advWatched(
      @Body AdvWatchedParams params);

  @POST("subanswer.do") Observable<Response<ResponseModel<String>>> subAnswer(
      @Body SubAdvQuestionAnswerParams params);

  @POST("bibi.do") Observable<Response<ResponseModel<BibiModel>>> bibi(@Body BibiParams params);

  @POST("bbreport.do") Observable<Response<ResponseModel<BibiReport>>> bibiReport(
      @Body BibiReportParams params);

  @POST("getsysquestion.do") Observable<Response<ResponseModel<List<Question>>>> getSysQuestion(
      @Body SysQuestionParams params);

  @POST("getquestion.do") Observable<Response<ResponseModel<List<Question>>>> getQuestion(
      @Body SysQuestionByIdParams params);

  @POST("likeadv.do") Observable<Response<ResponseModel<String>>> likeAdv(
      @Body AdvOperationParams params);

  @POST("collectadv.do") Observable<Response<ResponseModel<String>>> collectadv(
      @Body AdvOperationParams params);

  @POST("likecomments.do") Observable<Response<ResponseModel<String>>> likeComments(
      @Body LikeCommentParams params);

  @POST("domycomments.do")
  Observable<Response<ResponseModel<List<AdvertisementDetail.Comment>>>> doMycomments(
      @Body DoMyCommentParams params);

  @POST("commentslist.do")
  Observable<Response<ResponseModel<List<AdvertisementDetail.Comment>>>> getCommentList(
      @Body GetCommentListParams params);

  @POST("advcollectlist.do")
  Observable<Response<ResponseModel<List<CollectAdvModel>>>> getAdvCollectionList(
      @Body GetCollectionsParams params);

  @POST("subfeedback.do.do")
  Observable<Response<ResponseModel<List<CollectAdvModel>>>> getAdvSettingsList(
      @Body GetSettingsParams params);

  @POST("recordUserPlayDuration.do")
  Observable<Response<ResponseModel<String>>> recordUserPlayDuration(
      @Body RecordUserPlayDurationParams params);

  @POST("getmytask.do") Observable<Response<ResponseModel<MyTaskModel>>> getAdvTaskList(
      @Body GetCardQueryParams params);

  @POST("getbenefit.do") Observable<Response<ResponseModel<String>>> getBenefit(
      @Body GetBenefitParams params);
  @POST("getmoney.do") Observable<Response<ResponseModel<GetMoneyModel>>> getMoney(
      @Body GetBenefitParams params);
}
