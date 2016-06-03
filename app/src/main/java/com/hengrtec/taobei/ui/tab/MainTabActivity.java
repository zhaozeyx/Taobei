/*
 * 文件名: MainTabActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.manager.LoginSession;
import com.hengrtec.taobei.manager.UserInfoChangedEvent;
import com.hengrtec.taobei.net.rpc.model.UserInfo;
import com.hengrtec.taobei.ui.basic.tab.BaseTabActivity;
import com.hengrtec.taobei.ui.commonevent.UserAvatarClickedEvent;
import com.hengrtec.taobei.ui.home.HomeFragment;
import com.hengrtec.taobei.ui.login.LoginWayActivity;
import com.hengrtec.taobei.ui.nearby.NearbyFragment;
import com.hengrtec.taobei.ui.profile.CollectionActivity;
import com.hengrtec.taobei.ui.profile.PrimaryActivity;
import com.hengrtec.taobei.ui.profile.ProfileFragment;
import com.hengrtec.taobei.ui.profile.SettingsActivity;
import com.hengrtec.taobei.ui.profit.ProfitFragment;
import com.hengrtec.taobei.ui.register.ProfileInitializeActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.otto.Subscribe;

/**
 * 主界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class MainTabActivity extends BaseTabActivity {
  private DrawerLayout mDrawerLayout;
  private RelativeLayout mDrawerContainer;
  private DrawerLayoutController mDrawerController;

  @Override protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    initDrawerContainer();
  }

  @Override public int getLayoutId() {
    return R.layout.main_tab;
  }

  @Override protected Class[] getContentClazzes() {
    return new Class[] {
        HomeFragment.class, ProfitFragment.class, NearbyFragment.class, ProfileFragment.class
    };
  }

  @Override protected String[] getTabTitles() {
    return getResources().getStringArray(R.array.main_tab_titles);
  }

  @Override protected int[] getTabIcons() {
    return new int[] {
        R.drawable.icon_tab_home, R.drawable.icon_tab_profit, R.drawable.icon_tab_nearby,
        R.drawable.icon_tab_profile
    };
  }

  @Subscribe public void onTitleBarAvatarClicked(UserAvatarClickedEvent event) {
    mDrawerLayout.openDrawer(Gravity.LEFT);
  }

  public void initDrawerContainer() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerContainer = (RelativeLayout) findViewById(R.id.drawer_container);
    mDrawerController = new DrawerLayoutController(this, mDrawerContainer);
    mDrawerController.bindData();
  }

  @Override protected void onLogin() {
    super.onLogin();
    mDrawerController.bindData();
  }

  @Override protected void onLogout() {
    super.onLogout();
    mDrawerController.bindData();
    finish();
  }

  class DrawerLayoutController implements View.OnClickListener {
    private View mDrawerContainer;
    private ImageView mUserAvatarView;
    private TextView mUserNamView;
    /**
     * 今日收益
     */
    private TextView mProfitView;
    private TextView mTotalProfitView;
    private TextView mSignInView;
    private TextView mMenuTask;
    private TextView mMenuShare;
    private TextView mMenuAttention;
    private TextView mMenuCollection;
    private TextView mMenuSearch;
    private TextView mBtnSetting;
    private TextView mBtnFeedBack;

    private Context mContext;

    private SignInDialogFragment mSignInDialog;

    public DrawerLayoutController(Activity activity, View drawerContainer) {
      mContext = activity;
      mDrawerContainer = drawerContainer;
      ButterKnife.bind(activity, drawerContainer);
      setUp();
      bindData();
    }

    private void setUp() {
      mUserAvatarView = (ImageView) mDrawerContainer.findViewById(R.id.user_avatar);
      mUserNamView = (TextView) mDrawerContainer.findViewById(R.id.user_name);
      mProfitView = (TextView) mDrawerContainer.findViewById(R.id.profit);
      mTotalProfitView = (TextView) mDrawerContainer.findViewById(R.id.drawer_total_profit);
      mSignInView = (TextView) mDrawerContainer.findViewById(R.id.sign_in);
      mMenuTask = (TextView) mDrawerContainer.findViewById(R.id.btn_task);
      mMenuShare = (TextView) mDrawerContainer.findViewById(R.id.btn_share);
      mMenuAttention = (TextView) mDrawerContainer.findViewById(R.id.btn_attention);
      mMenuCollection = (TextView) mDrawerContainer.findViewById(R.id.btn_collection);
      mMenuSearch = (TextView) mDrawerContainer.findViewById(R.id.btn_search);
      mBtnSetting = (TextView) mDrawerContainer.findViewById(R.id.btn_settings);
      mBtnFeedBack = (TextView) mDrawerContainer.findViewById(R.id.btn_feed_back);

      mSignInView.setOnClickListener(this);
      mMenuTask.setOnClickListener(this);
      mMenuShare.setOnClickListener(this);
      mMenuAttention.setOnClickListener(this);
      mMenuCollection.setOnClickListener(this);
      mMenuSearch.setOnClickListener(this);
      mBtnSetting.setOnClickListener(this);
      mBtnFeedBack.setOnClickListener(this);
    }

    private void bindData() {
      LoginSession session =
          ((CustomApp) mContext.getApplicationContext()).getGlobalComponent().loginSession();
      UserInfo info = session.getUserInfo();
      if (TextUtils.isEmpty(info.getType())) {
        mUserNamView.setText(R.string.drawer_name_un_login);
        mSignInView.setEnabled(false);
        mUserNamView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            mContext.startActivity(new Intent(mContext, LoginWayActivity.class));
          }
        });
      } else {
        switch (info.getType()) {
          case UserInfo.USER_TYPE_PHONE:
          case UserInfo.USER_TYPE_THIRD:
            mUserNamView.setText(info.getUserName());
            mUserNamView.setOnClickListener(null);
            mSignInView.setEnabled(true);
            break;
          default:
            mUserNamView.setText(R.string.drawer_name_un_login);
            mUserNamView.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, LoginWayActivity.class));
              }
            });
            mSignInView.setEnabled(false);
        }
      }
      mProfitView.setText(mContext.getString(R.string.drawer_profit, info.getTodayBenefit()));
      if (info.getMoney() <= 0) {
        mTotalProfitView.setText(R.string.drawer_total_profit_un_login);
      } else {
        Link link =
            new Link(mContext.getString(R.string.drawer_total_profit_withdraw)).setTextColor(
                mContext.getResources().getColor(R.color.font_color_yellow))
                .setOnClickListener(new Link.OnClickListener() {

                  @Override public void onClick(String clickedText) {
                    // 跳转到提现界面
                  }
                });
        LinkBuilder.on(mTotalProfitView)
            .addLink(link)
            .setText(
                mContext.getString(R.string.drawer_total_profit_login) + " " + mContext.getString(
                    R.string.drawer_total_profit_withdraw))
            .build();
      }
      //ImageLoader.loadOptimizedHttpImage(mContext, info.getAvart()).placeholder(R.mipmap
      //    .src_avatar_default_drawer).into(mUserAvatarView);
      mUserAvatarView.setImageURI(Uri.parse(info.getAvart()));
    }

    @Override public void onClick(View view) {
      switch (view.getId()) {
        case R.id.sign_in:
          showSignInDialog();
          break;
        case R.id.btn_task:
          // TODO 测试代码
          startActivity(new Intent(MainTabActivity.this, ProfileInitializeActivity.class));
          break;
        case R.id.btn_share:
          showShare();
          break;
        case R.id.btn_attention:
          break;
        case R.id.btn_collection:
          mContext.startActivity(new Intent(mContext, CollectionActivity.class));
          break;
        case R.id.btn_search:
          break;
        case R.id.btn_settings:
          mContext.startActivity(new Intent(mContext, SettingsActivity.class));
          break;
        case R.id.btn_feed_back:
          mContext.startActivity(new Intent(mContext, PrimaryActivity.class));
          break;
      }
      mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void showShare() {
      ShareSDK.initSDK(MainTabActivity.this);
      OnekeyShare oks = new OnekeyShare();
      //关闭sso授权
      oks.disableSSOWhenAuthorize();

      // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
      //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
      // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
      oks.setTitle(getString(R.string.share));
      // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
      oks.setTitleUrl("http://sharesdk.cn");
      // text是分享文本，所有平台都需要这个字段
      oks.setText("我是分享文本");
      // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
      //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
      // url仅在微信（包括好友和朋友圈）中使用
      oks.setUrl("http://sharesdk.cn");
      // comment是我对这条分享的评论，仅在人人网和QQ空间使用
      oks.setComment("我是测试评论文本");
      // site是分享此内容的网站名称，仅在QQ空间使用
      oks.setSite(getString(R.string.app_name));
      // siteUrl是分享此内容的网站地址，仅在QQ空间使用
      oks.setSiteUrl("http://sharesdk.cn");

      // 启动分享GUI
      oks.show(MainTabActivity.this);
    }

    private void showSignInDialog() {
      new SignInDialogFragment().show(getSupportFragmentManager(), "");
    }
  }

  @Subscribe public void onUserInfoChanged(UserInfoChangedEvent event) {
    mDrawerController.bindData();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    getComponent().loginSession().onDestroy();
  }
}
