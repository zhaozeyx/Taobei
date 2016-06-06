/*
 * 文件名: AppServiceComponent
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.serviceinjection;

import com.hengrtec.taobei.manager.LoginSession;
import com.hengrtec.taobei.net.RpcCallManager;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.AppService;
import com.hengrtec.taobei.net.rpc.service.AuthService;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.ui.home.AdvQuestionListActivity;
import com.hengrtec.taobei.ui.home.AdvertisementDetailActivity;
import com.hengrtec.taobei.ui.home.AdvertisementListPresenter;
import com.hengrtec.taobei.ui.home.AdvertisementPlayActivity;
import com.hengrtec.taobei.ui.home.BaseSysQuestionActivity;
import com.hengrtec.taobei.ui.home.DetailSysQuestionActivity;
import com.hengrtec.taobei.ui.home.ReportActivity;
import com.hengrtec.taobei.ui.home.ReportSysQuestionActivity;
import com.hengrtec.taobei.ui.login.LoginWayActivity;
import com.hengrtec.taobei.ui.login.LoginWithPasswordFragment;
import com.hengrtec.taobei.ui.login.LoginWithVerifyCodeFragment;
import com.hengrtec.taobei.ui.login.PasswordSetActivity;
import com.hengrtec.taobei.ui.login.PayPasswordActivity;
import com.hengrtec.taobei.ui.login.PayPasswordSetActivity;
import com.hengrtec.taobei.ui.login.RegisterActivity;
import com.hengrtec.taobei.ui.login.ResetPasswordActivity;
import com.hengrtec.taobei.ui.profile.AddTagsActivity;
import com.hengrtec.taobei.ui.profile.AvatarChoosePresenter;
import com.hengrtec.taobei.ui.profile.BaseMessageListFragment;
import com.hengrtec.taobei.ui.profile.BaseWatchedListFragment;
import com.hengrtec.taobei.ui.profile.ChartsActivity;
import com.hengrtec.taobei.ui.profile.CollectionActivity;
import com.hengrtec.taobei.ui.profile.CommentListActivity;
import com.hengrtec.taobei.ui.profile.CouponListActivity;
import com.hengrtec.taobei.ui.profile.DealRecordsActivity;
import com.hengrtec.taobei.ui.profile.IdCardChoosePresenter;
import com.hengrtec.taobei.ui.profile.MessageListActivity;
import com.hengrtec.taobei.ui.profile.PrimaryActivity;
import com.hengrtec.taobei.ui.profile.ProfitActivity;
import com.hengrtec.taobei.ui.profile.ProfitRecordsActivity;
import com.hengrtec.taobei.ui.profile.SettingsActivity;
import com.hengrtec.taobei.ui.profile.fragments.OneFragment;
import com.hengrtec.taobei.ui.profile.fragments.ResetPhoneActivity;
import com.hengrtec.taobei.ui.profile.fragments.TwoFragment;
import com.hengrtec.taobei.ui.profile.fragments.bean.ChooseMyAccountavtivity;
import com.hengrtec.taobei.ui.profile.fragments.bean.OutMyAccountavtivity;
import com.hengrtec.taobei.ui.profit.ProfitFragment;
import com.hengrtec.taobei.ui.tab.MainTabActivity;
import com.hengrtec.taobei.ui.tab.SignInDialogFragment;
import dagger.Component;
import javax.inject.Singleton;

/**
 * 服务器接口 Component<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
@Singleton
@Component(modules = ServiceModule.class)
public interface ServiceComponent {
  RpcCallManager rpcCallManager();

  AppService appService();

  AdvertisementService advertisementService();

  AuthService authService();

  UserService userService();

  void inject(AdvertisementListPresenter presenter);

  void inject(AdvertisementDetailActivity activity);

  void inject(LoginWithPasswordFragment fragment);

  void inject(LoginWithVerifyCodeFragment fragment);

  void inject(RegisterActivity activity);

  void inject(PasswordSetActivity activity);

  void inject(AdvQuestionListActivity activity);

  void inject(AdvertisementPlayActivity activity);

  void inject(ProfitFragment fragment);

  void inject(ReportActivity activity);

  void inject(BaseSysQuestionActivity activity);

  void inject(ReportSysQuestionActivity activity);

  void inject(DetailSysQuestionActivity activity);

  void inject(CollectionActivity activity);

  void inject(PrimaryActivity activity);

  void inject(LoginSession loginSession);

  void inject(SignInDialogFragment fragment);

  void inject(ProfitActivity activity);

  void inject(ProfitRecordsActivity activity);

  void inject(ChartsActivity activity);

  void inject(CommentListActivity activity);

  void inject(OneFragment fragment);

  void inject(TwoFragment fragment);

  void inject(ChooseMyAccountavtivity activity);

  void inject(OutMyAccountavtivity activity);

  void inject(BaseMessageListFragment fragment);

  void inject(MessageListActivity activity);

  void inject(SettingsActivity activity);

  void inject(AvatarChoosePresenter presenter);

  void inject(ResetPhoneActivity activity);

  void inject(ResetPasswordActivity activity);

  void inject(PayPasswordActivity activity);

  void inject(PayPasswordSetActivity activity);

  void inject(IdCardChoosePresenter presenter);

  void inject(BaseWatchedListFragment fragment);

  void inject(AddTagsActivity activity);

  void inject(CouponListActivity couponListActivity);

  void inject(LoginWayActivity activity);

  void inject(DealRecordsActivity dealRecordsActivity);

  void inject(MainTabActivity mainTabActivity);
}
