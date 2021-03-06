/*
 * 文件名: ProfileFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.database.model.MessageModel;
import com.hengrtec.taobei.manager.UserInfoChangedEvent;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.login.LoginWayActivity;
import com.hengrtec.taobei.ui.login.RegisterActivity;
import com.hengrtec.taobei.ui.profile.event.MessageDeleteEvent;
import com.hengrtec.taobei.ui.profile.event.MessageStatusChangeEvent;
import com.squareup.otto.Subscribe;
import io.realm.Realm;

/**
 * 个人页面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class ProfileFragment extends BasicTitleBarFragment {
  @Bind(R.id.btn_register)
  Button mBtnRegister;
  @Bind(R.id.btn_login)
  Button mBtnLogin;
  @Bind(R.id.un_login_container)
  LinearLayout mUnLoginContainer;
  @Bind(R.id.avatar)
  SimpleDraweeView mAvatarView;
  @Bind(R.id.profit)
  TextView mProfitView;
  @Bind(R.id.account)
  TextView mAccountView;
  @Bind(R.id.virtual_money)
  TextView mVirtualMoney;
  @Bind(R.id.icon_new_message)
  TextView mIconNewMessage;
  @Bind(R.id.message_center)
  RelativeLayout mMessageCenterView;
  @Bind(R.id.icon_new_honour)
  ImageView mIconNewHonour;
  @Bind(R.id.member_honour)
  RelativeLayout mMemberHonourView;
  @Bind(R.id.member_achievement_label)
  TextView memberAchievementLabel;
  @Bind(R.id.icon_new_achievement)
  TextView mIconNewAchievement;
  @Bind(R.id.member_achievement)
  RelativeLayout mMemberAchievementView;
  @Bind(R.id.watched)
  RelativeLayout mWatchedView;
  @Bind(R.id.comments)
  RelativeLayout mCommentsView;
  @Bind(R.id.invite_friends)
  RelativeLayout mInviteFriendsView;
  @Bind(R.id.btn_red_bag)
  LinearLayout btnRedBag;
  @Bind(R.id.ll_my_acount)
  LinearLayout llMyAcount;
  @Bind(R.id.btn_bbj)
  LinearLayout btnBbj;
  @Bind(R.id.message_center_label)
  TextView messageCenterLabel;
  @Bind(R.id.member_honour_label)
  TextView memberHonourLabel;
  @Bind(R.id.member_watched_label)
  TextView memberWatchedLabel;
  @Bind(R.id.member_comments_label)
  TextView memberCommentsLabel;
  @Bind(R.id.member_invite_friends_label)
  TextView memberInviteFriendsLabel;

  @Override
  protected void onCreateViewCompleted(View view) {
    ButterKnife.bind(this, view);
    initView();
  }

  private void initView() {
    UserInfo info = getComponent().loginSession().getUserInfo();
    if (getComponent().isLogin()) {
      mUnLoginContainer.setVisibility(View.GONE);
      mAvatarView.setVisibility(View.VISIBLE);
      mAvatarView.setImageURI(Uri.parse(info.getAvart()));
    } else {
      mUnLoginContainer.setVisibility(View.VISIBLE);
      mAvatarView.setVisibility(View.GONE);
    }

    if (info.getTodayBenefit() == 0) {
      mProfitView.setText(R.string.fragment_profile_no_profit);
    } else {
      mProfitView.setText(getString(R.string.fragment_profile_has_profit, String.valueOf(info
          .getTodayBenefit())));
    }
    mAccountView.setText(getString(R.string.fragment_profile_account_value, String.valueOf(info
        .getMoney())));
    mVirtualMoney.setText(getString(R.string.fragment_profile_virtual_money_value, info
        .getVirtualMoney()));
    setNewMsgCount();
  }

  private void setNewMsgCount() {
    Realm realm = Realm.getDefaultInstance();
    long newMsgCount = realm.where(MessageModel.class).equalTo(MessageModel
        .COLUMNS_MESSAGE_STATUS, MessageModel.MSG_UN_READ).count();
    mIconNewMessage.setVisibility(newMsgCount > 0 ? View.VISIBLE : View.GONE);
    mIconNewMessage.setText(String.valueOf(newMsgCount));
  }

  @Subscribe
  public void handleMessageStatusChangeEvent(MessageStatusChangeEvent event) {
    setNewMsgCount();
  }

  @Subscribe
  public void handleMessageDeleteEvent(MessageDeleteEvent event) {
    setNewMsgCount();
  }

  @Subscribe
  public void onUserInfoChanged(UserInfoChangedEvent event) {
    initView();
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.fragment_profile_title);
    return true;
  }

  @Override
  protected void onLogout() {
    super.onLogout();
    initView();
  }

  @Override
  protected void onLogin() {
    super.onLogin();
    initView();
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_profile;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({R.id.btn_register, R.id.ll_my_acount, R.id.btn_login, R.id.avatar, R.id
      .icon_new_message, R.id.message_center, R.id.member_honour, R.id.member_achievement, R.id
      .watched, R.id.comments, R.id.invite_friends, R.id.btn_red_bag, R.id.btn_bbj})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_register:
        startActivity(new Intent(getActivity(), RegisterActivity.class));
        break;
      case R.id.btn_login:
        startActivity(new Intent(getActivity(), LoginWayActivity.class));
        break;
      case R.id.avatar:
        startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
        break;
      case R.id.icon_new_message:
        break;
      case R.id.message_center:
        startActivity(new Intent(getActivity(), MessageListActivity.class));
        break;
      case R.id.member_honour:
        break;
      case R.id.member_achievement:
        break;
      case R.id.watched:
        startActivity(new Intent(getActivity(), WatchedActivity.class));
        break;
      case R.id.comments:
        startActivity(new Intent(getActivity(), CommentListActivity.class));
        break;
      case R.id.ll_my_acount:
        startActivity(new Intent(getActivity(), MyAccountActivity.class));
        break;
      case R.id.invite_friends:
        startActivity(new Intent(getActivity(), InviteFriendsActivity.class));
        break;
      case R.id.btn_red_bag:
        startActivity(ProfitActivity.makeRedBagIntent(getActivity()));
        break;
      case R.id.btn_bbj:
        startActivity(ProfitActivity.makeBBJIntent(getActivity()));
        break;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
      savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @OnClick({R.id.btn_coupon, R.id.btn_dream})
  public void onBtnClick(View view) {
    switch (view.getId()) {
      case R.id.btn_coupon:
        startActivity(new Intent(getActivity(), CouponListActivity.class));
        break;
      case R.id.btn_dream:
        break;
    }
  }
}
