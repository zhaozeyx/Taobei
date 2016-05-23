/*
 * 文件名: BaseMessageListActivity
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
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.database.model.MessageModel;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.ui.basic.BasicFragment;
import com.hengrtec.taobei.ui.profile.event.MessageDeleteEvent;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.squareup.otto.Subscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 消息列表基类<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/20]
 */
public abstract class BaseMessageListFragment extends BasicFragment {
  protected static final String BUNDLE_KEY_MSG_TYPE = "bundle_key_message_type";
  protected List<String> mMessageIds = new ArrayList<>();
  protected RealmResults<MessageModel> mData;

  @Bind(R.id.message_list)
  SuperRecyclerView mMessageListView;

  @Inject
  UserService mUserService;

  protected boolean mIsInEditMode = false;
  @Bind(R.id.btn_choose_all)
  ImageView mBtnChooseAll;
  @Bind(R.id.select_all_container)
  RelativeLayout mSelectAllContainer;

  private CompositeSubscription mSubscriptions = new CompositeSubscription();
  private MessageEventHandler mMessageEventHandler = new MessageEventHandler();

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_message_list, container, false);
    ButterKnife.bind(this, view);
    getComponent().getGlobalBus().register(mMessageEventHandler);
    inject();
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    loadData();
  }

  private void loadData() {
    Subscription subscription = Realm.getDefaultInstance().asObservable().subscribeOn
        (AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread()).doOnNext(new Action1<Realm>() {
          @Override
          public void call(Realm realm) {
            mData = realm.where(MessageModel.class).equalTo
                (MessageModel.COLUMNS_MESSAGE_TYPE, getArguments().getInt
                    (BUNDLE_KEY_MSG_TYPE)).findAll();
            getListAdapter().notifyDataSetChanged();
          }
        }).subscribe();
    mSubscriptions.add(subscription);
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getActivity()
        .getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void initView() {
    mMessageListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mMessageListView.setAdapter(getAdapter());
  }

  protected abstract RecyclerView.Adapter getAdapter();

  private RecyclerView.Adapter getListAdapter() {
    if (null == mMessageListView.getAdapter()) {
      return getAdapter();
    }
    return mMessageListView.getAdapter();
  }

  public void startEditMode() {
    mIsInEditMode = true;
    if (null != getAdapter()) {
      getListAdapter().notifyDataSetChanged();
    }
    updateSelectAllStatus();
  }

  public void quitEditMode() {
    mIsInEditMode = false;
    if (null != getAdapter()) {
      getListAdapter().notifyDataSetChanged();
    }
    updateSelectAllStatus();
    deSelectAll();
  }

  private void updateSelectAllStatus() {
    mSelectAllContainer.setVisibility(mIsInEditMode ? View.VISIBLE : View.GONE);
  }

  private void deSelectAll() {
    mMessageIds.clear();
  }

  @OnClick(R.id.btn_choose_all)
  void onChooseAllClicked() {
    Subscription subscription = Observable.just(mData.size() == mMessageIds.size()).subscribeOn
        (AndroidSchedulers.mainThread())
        .doOnNext(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            deSelectAll();
            if (!aBoolean) {
              for (MessageModel model : mData) {
                mMessageIds.add(model.getMsgId());
              }
            }
          }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            getListAdapter().notifyDataSetChanged();
            mBtnChooseAll.setImageDrawable(!aBoolean ? getResources().getDrawable(R.mipmap
                .icon_message_delete_chosen) : null);
          }
        }).subscribe();
    mSubscriptions.add(subscription);
  }

  private void updateBtnSelectAll() {
    mBtnChooseAll.setImageDrawable(mData.size() == mMessageIds.size() ? getResources()
        .getDrawable(R.mipmap
            .icon_message_delete_chosen) : null);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mSubscriptions.unsubscribe();
    getComponent().getGlobalBus().unregister(mMessageEventHandler);
  }

  @OnClick(R.id.btn_delete)
  void onDeleteClicked() {
    if (mMessageIds.size() == 0) {
      return;
    }
    MessageModel[] beans = mData.toArray(new MessageModel[mData.size()]);
    for (MessageModel model : beans) {
      if (mMessageIds.contains(model.getMsgId())) {
        final RealmQuery<MessageModel> modelResult = mData.where().equalTo(MessageModel
            .COLUMNS_MESSAGE_ID, model.getMsgId());
        Realm.getDefaultInstance().executeTransaction(new Realm
            .Transaction() {
          @Override
          public void execute(Realm bgRealm) {
            modelResult.findFirst().deleteFromRealm();
          }
        });
      }
    }
    getListAdapter().notifyDataSetChanged();
    mMessageIds.clear();
  }

  public boolean isEditable() {
    return mIsInEditMode;
  }

  @Override
  public boolean onBackPressed() {
    if (mIsInEditMode) {
      quitEditMode();
      return true;
    }
    return super.onBackPressed();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  abstract class BaseMessageAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView
      .Adapter<T> {

    @Override
    public void onBindViewHolder(T holder, final int position) {
      getBtnChoose(holder).setVisibility(mIsInEditMode ? View.VISIBLE : View.GONE);
      getBtnChoose(holder).setImageDrawable(mMessageIds.contains(mData.get(position).getMsgId())
          ? getResources
          ().getDrawable(R.mipmap.icon_message_delete_chosen) : null);
      onChooseBtnClicked(getBtnChoose(holder), mData.get(position));
      onBindData(holder, position);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          updateStatus(mData.get(position).getMsgId());
          // 跳转界面
          startActivity(MessageDetailActivity.makeIntent(getActivity(), mData.get(position)));
        }
      });

    }

    @Override
    public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }

    public abstract void onBindData(T holder, int position);

    protected abstract ImageView getBtnChoose(T holder);

    private void onChooseBtnClicked(final ImageView btnChoose, final MessageModel bean) {
      btnChoose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mMessageIds.contains(bean.getMsgId())) {
            mMessageIds.remove(bean.getMsgId());
          } else {
            mMessageIds.add(bean.getMsgId());
          }
          btnChoose.setImageDrawable(mMessageIds.contains(bean.getMsgId()) ? getResources
              ().getDrawable(R.mipmap.icon_message_delete_chosen) : null);
          updateBtnSelectAll();
        }
      });
    }
  }

  private void updateStatus(final String msgId) {

    RealmQuery<MessageModel> query = mData.where().equalTo(MessageModel
        .COLUMNS_MESSAGE_ID, msgId);
    Realm.getDefaultInstance().beginTransaction();
    query.findFirst().setStatus(MessageModel.MSG_READ);
    Realm.getDefaultInstance().commitTransaction();
    // TODO 调用服务器接口
  }

  public class MessageEventHandler {
    @Subscribe
    public void onMessageDelete(MessageDeleteEvent event) {
      MessageModel[] beans = mData.toArray(new MessageModel[mData.size()]);
      for (MessageModel model : beans) {
        if (TextUtils.equals(event.eventId, model.getMsgId())) {
          RealmQuery<MessageModel> modelResult = mData.where().equalTo(MessageModel
              .COLUMNS_MESSAGE_ID, model.getMsgId());
          Realm.getDefaultInstance().beginTransaction();
          modelResult.findFirst().deleteFromRealm();
          Realm.getDefaultInstance().commitTransaction();
          break;
        }
      }
      getListAdapter().notifyDataSetChanged();
    }
  }

}
