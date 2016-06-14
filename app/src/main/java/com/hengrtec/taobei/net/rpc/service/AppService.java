/*
 * 文件名: AppService
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

import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.model.UpgradeModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public interface AppService {

  @Multipart
  @POST("upload.do")
  Observable<Response<ResponseModel<String>>> upload(@Part MultipartBody.Part file);

  @POST("getlastversion.do")
  Observable<Response<ResponseModel<UpgradeModel>>> getLatestVersion();

  @GET
  @Streaming
  Observable<Response<ResponseBody>> downloadAPK(@Url String fileUrl);
}
