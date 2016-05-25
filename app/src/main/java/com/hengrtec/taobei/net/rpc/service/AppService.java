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
import com.hengrtec.taobei.net.rpc.model.UploadModel;
import java.io.File;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
  Observable<Response<ResponseModel<UploadModel>>> upload(@Part("image\"; filename=\"文件名.jpg")
                                                          RequestBody file);

  @POST("upload.do")
  @Multipart
  Observable<Response<ResponseModel<UploadModel>>> upload(@Part(value = "avatar", encoding =
      "image/*") File file);

  @Multipart
  @POST("upload.do")
  Observable<Response<ResponseModel<String>>> upload(@Part MultipartBody.Part file);
}
