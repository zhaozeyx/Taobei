/*
 * 文件名: ReportSysQuestionActivity
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
import com.hengrtec.taobei.net.rpc.service.params.SysQuestionByIdParams;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import java.util.List;
import javax.inject.Inject;

/**
 * 广告进入平台问题列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/11]
 */
public class ReportSysQuestionActivity extends BaseSysQuestionActivity {
  private static final String BUNDLE_KEY_QUESTION_ID = "question_id";
  private String mQuestionId;
  @Inject
  AdvertisementService mAdvService;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    inject();
    mQuestionId = getIntent().getStringExtra(BUNDLE_KEY_QUESTION_ID);
    super.afterCreate(savedInstance);
  }

  @Override
  protected void loadFirstQuestion() {
    // TODO 替换成根据问题ID获取接口的数据
    showProgressDialog("", true);
    manageRpcCall(mAdvService.getQuestion(new SysQuestionByIdParams(mQuestionId)), new
        UiRpcSubscriber<List<Question>>(this) {


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

  public static Intent makeReportSysQuestionIntent(Context context, String questionId, boolean
      firstQuestion) {
    Intent intent = new Intent(context, ReportSysQuestionActivity.class);
    intent.putExtra(BUNDLE_KEY_QUESTION_ID, questionId);
    intent.putExtra(BUNDLE_KEY_FIRST_QUESTION, firstQuestion);
    return intent;
  }

  @Override
  protected Intent getNextIntent(Context context) {
    Intent intent = new Intent(context, ReportSysQuestionActivity.class);
    intent.putExtra(BUNDLE_KEY_FIRST_QUESTION, false);
    return intent;
  }
}
