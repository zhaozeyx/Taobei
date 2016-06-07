/*
 * 文件名: MessageListActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/20
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.database.model.MessageModel;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber1;
import com.hengrtec.taobei.net.rpc.model.GrabMyMessageModel;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GrabMyMessageParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.JsonConverter;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/20]
 */
public class MessageListActivity extends BasicTitleBarActivity {

  @Bind(R.id.tab_layout)
  TabLayout mTabLayout;
  @Bind(R.id.view_pager)
  ViewPager mViewPager;

  @Inject
  UserService mUserService;
  private BaseMessageListFragment mCurrentFragment;
  private MessagePagerAdapter mAdapter;

  private BaseMessageListFragment mSystemFragment;
  private BaseMessageListFragment mPromotionFragment;
  private BaseMessageListFragment mPraisedFragment;

  @Override
  public int getLayoutId() {
    return R.layout.activity_message_list;
  }

  @Override
  public boolean initializeTitleBar() {
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    setRightImgButton(R.mipmap.icon_title_bar_delete, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentFragment.startEditMode();
        updateRightTitleBarButton(true);
      }
    });

    setRightTextButton(R.string.activity_message_list_title_bar_cancel_delete, new View
        .OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentFragment.quitEditMode();
        updateRightTitleBarButton(false);
      }
    });

    setMiddleTitle(R.string.activity_message_list_title);
    updateRightTitleBarButton(false);
    return true;
  }

  private void updateRightTitleBarButton(boolean edit) {
    setRightImgButtonVisible(!edit);
    setRightTextButtonVisible(edit);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    inject();
    loadData();
  }

  @Override
  public void onBackPressed() {
    if (null != mCurrentFragment && mCurrentFragment.isEditable()) {
      mCurrentFragment.onBackPressed();
      updateRightTitleBarButton(mCurrentFragment.mIsInEditMode);
      return;
    }
    super.onBackPressed();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void loadData() {
    showProgressDialog("", true);
    Observable<GrabMyMessageModel> observable = mUserService.grabMyMessage(new
        GrabMyMessageParams(getComponent().loginSession
        ().getUserId())).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation())
        .filter(new Func1<Response<ResponseModel<GrabMyMessageModel>>, Boolean>() {


          @Override
          public Boolean call(Response<ResponseModel<GrabMyMessageModel>> responseModelResponse) {
            return null != responseModelResponse.body() && null != responseModelResponse.body()
                .getData();
          }
        }).map(new Func1<Response<ResponseModel<GrabMyMessageModel>>, GrabMyMessageModel>() {
          @Override
          public GrabMyMessageModel call(Response<ResponseModel<GrabMyMessageModel>>
                                             responseModelResponse) {
            return responseModelResponse.body().getData();
          }
        }).doOnNext(new Action1<GrabMyMessageModel>() {
          @Override
          public void call(GrabMyMessageModel grabMyMessageModel) {
            addMessageToDb(grabMyMessageModel.getActivityBroadcast());
            addMessageToDb(grabMyMessageModel.getCommentsPraise());
            addMessageToDb(grabMyMessageModel.getSystemNotification());
          }
        });
    manageRpcCall(observable, new UiRpcSubscriber1<GrabMyMessageModel>(this) {

      protected void onSuccess(GrabMyMessageModel grabMyMessageModel) {
        mSystemFragment = MessageSystemFragment.newInstance(MessageModel.MSG_TYPE_SYSTEM);
        mPromotionFragment = MessagePromotionFragment.newInstance(MessageModel.MSG_TYPE_PROMOTION);
        mPraisedFragment = MessagePraisedFragment.newInstance(MessageModel.MSG_TYPE_PRAISED);

        initView();
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }

    });
  }

  private void addMessageToDb(List<GrabMyMessageModel.MessageBean> list) {
    if (null == list || list.size() == 0) {
      return;
    }
    List<MessageModel> data = new ArrayList<>();
    for (GrabMyMessageModel.MessageBean bean : list) {
      MessageModel model = new MessageModel();
      model.setMsgId(bean.getMsgId());
      model.setMsgContent(JsonConverter.objectToJson(bean));
      model.setMsgTime(bean.getMsgTime());
      model.setStatus(MessageModel.MSG_UN_READ);
      model.setMessageType(Integer.parseInt(bean.getMsgType()));
      data.add(model);
    }
    Realm.getDefaultInstance().beginTransaction();
    Realm.getDefaultInstance().copyToRealmOrUpdate(data);
    Realm.getDefaultInstance().commitTransaction();
  }

  private void initView() {
    mAdapter = new MessagePagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mAdapter);
    mViewPager.setOffscreenPageLimit(MessageListItemType.values().length);
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        mCurrentFragment = mAdapter.getItem(position);
        updateRightTitleBarButton(mCurrentFragment.mIsInEditMode);

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    mTabLayout.setupWithViewPager(mViewPager);

    String[] titles = getResources().getStringArray(R.array.message_list_type);
    for (int i = 0; i < titles.length; i++) {
      mTabLayout.getTabAt(i).setText(titles[i]);
    }

    mCurrentFragment = mAdapter.getItem(0);
  }

  private class MessagePagerAdapter extends FragmentPagerAdapter {
    public MessagePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public BaseMessageListFragment getItem(int position) {
      BaseMessageListFragment fragment = null;
      switch (position) {
        case 0:
          fragment = mSystemFragment;
          break;
        case 1:
          fragment = mPromotionFragment;
          break;
        case 2:
          fragment = mPraisedFragment;
          break;
      }
      return fragment;
    }

    @Override
    public int getCount() {
      return MessageListItemType.values().length;
    }
  }

  private enum MessageListItemType {
    SYSTEM_MESSAGE, PROMOTION_MESSAGE, PRAISED_MESSAGE
  }

}
