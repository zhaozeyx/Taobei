/*
 * 文件名: SignInDialogFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/16
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalComponent;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.RpcHttpError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.SignInModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.SignInParams;
import com.hengrtec.taobei.ui.basic.BasicDialogFragment;
import com.hengrtec.taobei.ui.home.view.SignInDialogItemView;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import java.util.List;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;

/**
 * 签到对话框<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/16]
 */
public class SignInDialogFragment extends BasicDialogFragment {
  private static final int TYPE_BBJ = 0;
  private static final int TYPE_RED_BAG = 1;
  private static final int TYPE_RED_COUPON = 2;

  private static final String IS_CHEKIN = "1";

  private static final int[] ICON = new int[]{R.mipmap.icon_dialog_sign_in_bbj, R.mipmap
      .icon_dialog_sign_in_red_bag,
      R.mipmap.icon_dialog_sign_in_adv};
  private static final int[] LABEL = new int[]{R.string.dialog_sign_in_award_bbj, R.string
      .dialog_sign_in_award_red_bag, R.string.dialog_sign_in_award_adv};
  @Bind(R.id.welcome_title)
  TextView welcomeTitle;
  @Bind(R.id.first_day)
  SignInDialogItemView mFirstDayView;
  @Bind(R.id.second_day)
  SignInDialogItemView mSecondDayView;
  @Bind(R.id.third_day)
  SignInDialogItemView mThirdDayView;
  @Bind(R.id.forth_day)
  SignInDialogItemView mForthDayView;
  @Bind(R.id.fifth_day)
  SignInDialogItemView mFifthDayView;
  @Bind(R.id.sixth_day)
  SignInDialogItemView mSixthDayView;
  @Bind(R.id.seventh_day)
  SignInDialogItemView mSeventhDayView;
  @Bind(R.id.btn_sign_in)
  TextView mBtnSignIn;
  @Inject
  UserService mUserService;

  private GlobalComponent mGlobalComponent;

  private int mTodayAward;
  private String mTodayAwardType;
  private SignInModel mSignInModel;

  private CompositeSubscription mSubscriptions = new CompositeSubscription();

  @Bind({R.id.first_day, R.id.second_day, R.id.third_day, R.id.forth_day, R.id.fifth_day, R.id
      .sixth_day, R.id.seventh_day})
  List<SignInDialogItemView> mSignInViews;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_sign_in, container, false);
    ButterKnife.bind(this, view);
    inject();
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadData();
  }

  private void inject() {
    mGlobalComponent = ((CustomApp) getActivity().getApplication()).getGlobalComponent();
    DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
        GlobalModule((CustomApp) getActivity().getApplication()))
        .build().inject(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    mSubscriptions.unsubscribe();
  }

  @OnClick({R.id.first_day, R.id.second_day, R.id.third_day, R.id.forth_day, R.id.fifth_day, R.id
      .sixth_day, R.id.seventh_day, R.id.btn_sign_in})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.first_day:
        break;
      case R.id.second_day:
        break;
      case R.id.third_day:
        break;
      case R.id.forth_day:
        break;
      case R.id.fifth_day:
        break;
      case R.id.sixth_day:
        break;
      case R.id.seventh_day:
        break;
      case R.id.btn_sign_in:
        break;
    }
  }

  private void loadData() {
    manageRpcCall(mUserService.selectCheckIn(new SignInParams(mGlobalComponent.loginSession()
        .getUserId())), new UiRpcSubscriber<SignInModel>(getActivity()) {


      @Override
      protected void onSuccess(SignInModel signInModel) {
        mSignInModel = signInModel;
        updateUI(signInModel);
        mSubscriptions.add(getComponent().loginSession().loadUserInfo());
      }

      @Override
      protected void onEnd() {

      }

      @Override
      public void onApiError(RpcApiError apiError) {
        super.onApiError(apiError);
        mBtnSignIn.setEnabled(false);
      }

      @Override
      public void onHttpError(RpcHttpError httpError) {
        super.onHttpError(httpError);
        mBtnSignIn.setEnabled(false);
      }
    });
  }

  private void updateUI(SignInModel signInModel) {
    int checkInCount = signInModel.getCheckinCounts();
    boolean isCheckIn = Integer.parseInt(signInModel.getIsCheckin()) > 0;
    mBtnSignIn.setEnabled(true);
    for (int i = 0; i < mSignInViews.size(); i++) {
      SignInModel.BenefitRulesBean bean = signInModel.getBenefitRules().get(i);
      SignInDialogItemView view = mSignInViews.get(i);
      int type = Integer.parseInt(bean.getBenefitType());
      int day = i + 1;
      // 如果返回的签到天数 显示已签到
      if (day < checkInCount) {
        view.bindData(day, SignInDialogItemView.STATE_CHECKED, getIcon(type), getTextId
            (type));
      } else if (day == checkInCount && isCheckIn) {
        view.bindData(day, SignInDialogItemView.STATE_CHECKED, getIcon(type), getTextId
            (type));
        mTodayAward = bean.getBenefit();
        mTodayAwardType = bean.getBenefitType();
        mBtnSignIn.setEnabled(false);
        mBtnSignIn.setText(R.string.dialog_sign_in_btn_commit_disabled);
      } else if (day == checkInCount && !isCheckIn) {
        view.bindData(day, SignInDialogItemView.STATE_ENABLED, getIcon(type), getTextId
            (type));
        mTodayAward = bean.getBenefit();
        mTodayAwardType = bean.getBenefitType();
      } else {
        view.bindData(day, SignInDialogItemView.STATE_DISABLED, getIcon(type), getTextId
            (type));
      }
    }
  }

  @OnClick(R.id.btn_close)
  void close() {
    dismissAllowingStateLoss();
  }

  @OnClick(R.id.btn_sign_in)
  void signIn() {
    manageRpcCall(mUserService.docheckin(new SignInParams(getComponent().loginSession().getUserId
        ())), new UiRpcSubscriber<Boolean>(getActivity()) {


      @Override
      protected void onSuccess(Boolean aBoolean) {
        if (aBoolean) {
          showShortToast(R.string.dialog_sign_in_success);
          mSignInModel.setIsCheckin(IS_CHEKIN);
          updateUI(mSignInModel);
        }
      }

      @Override
      protected void onEnd() {

      }

      @Override
      public void onApiError(RpcApiError apiError) {
        super.onApiError(apiError);
        showShortToast(R.string.dialog_sign_in_failed);
      }
    });
  }

  private int getIcon(int type) {
    return ICON[type];
  }

  private int getTextId(int type) {
    return LABEL[type];
  }
}
