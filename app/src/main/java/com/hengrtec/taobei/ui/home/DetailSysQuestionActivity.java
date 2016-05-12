/*
 * 文件名: DetailSysQuestionActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/11
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.Question;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.SysQuestionParams;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import java.util.List;
import javax.inject.Inject;

/**
 * 详情问题列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/11]
 */
public class DetailSysQuestionActivity extends BaseSysQuestionActivity {

  @Inject
  AdvertisementService mAdvService;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    super.afterCreate(savedInstance);
    inject();
  }

  @Override
  protected void loadFirstQuestion() {
    showProgressDialog("", true);
    manageRpcCall(mAdvService.getSysQuestion(new SysQuestionParams(String.valueOf(getComponent()
        .loginSession().getUserId()))), new UiRpcSubscriber<List<Question>>(this) {


      @Override
      protected void onSuccess(List<Question> questions) {
        updateListView(questions);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  public static Intent makeDetailSysQuestionIntent(Context context, boolean
      firstQuestion) {
    Intent intent = new Intent(context, ReportSysQuestionActivity.class);
    intent.putExtra(BUNDLE_KEY_FIRST_QUESTION, firstQuestion);
    return intent;
  }

  @Override
  protected Intent getNextIntent(Context context) {
    return makeDetailSysQuestionIntent(context, false);
  }
}
