/*
 * 文件名: BaseWatchedListFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/26
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.WatchedModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.WatchedListParams;
import com.hengrtec.taobei.ui.basic.BasicFragment;
import com.hengrtec.taobei.ui.basic.widget.PullToRefreshWrapper;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 我看过的<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/26]
 */
public abstract class BaseWatchedListFragment extends BasicFragment {
  private static final int PAGE_SIZE = 20;
  private static final int START_INDEX = 1;
  @Bind(R.id.refresh_wrapper)
  PullToRefreshWrapper mRefreshWrapper;

  @Inject
  UserService mUserService;

  private ExpandableListView mListView;

  private String mLastDate = "";
  private WatchedListAdapter mAdapter;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_watched_list, container, false);
    inject();
    ButterKnife.bind(this, view);
    initView();
    loadData();
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initView() {
    mListView = (ExpandableListView) LayoutInflater.from(getActivity()).inflate(R.layout.common_expandable_list_view, mRefreshWrapper, false);
    mListView.setDividerHeight(0);
    mListView.setDivider(null);
    mAdapter = new WatchedListAdapter();
    mListView.setAdapter(mAdapter);
    mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
      }
    });
    mRefreshWrapper.wrapView(mListView);

    mRefreshWrapper.setScrollToLoadListener(new PullToRefreshWrapper.ScrollToLoadListener() {
      @Override
      public void onPullUpLoadData() {
        loadData();
      }

      @Override
      public void onPullDownLoadData() {
        mLastDate = "";
        loadData();
      }
    }, 3);
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getActivity().getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  protected abstract int getListType();

  private void loadData() {
    manageRpcCall(mUserService.watchedList(new WatchedListParams(getComponent().loginSession().getUserId(), getListType(), mLastDate)),
        new UiRpcSubscriber<List<WatchedModel>>(getActivity()) {
          @Override
          protected void onSuccess(List<WatchedModel> watchedModels) {
            if (null == watchedModels || watchedModels.size() == 0) {
              mRefreshWrapper.setPullUpToRefresh(false);
              return;
            }
            if (TextUtils.isEmpty(mLastDate)) {
              mAdapter.clear();
            }
            mLastDate = watchedModels.get(watchedModels.size() - 1).getSeeDate();
            mRefreshWrapper.setPullUpToRefresh(true);
            mAdapter.addAll(watchedModels);
            mAdapter.notifyDataSetChanged();
            expandAll(mAdapter.mData);
          }

          @Override
          protected void onEnd() {
            mRefreshWrapper.resetPullStatus();
          }
        });
  }

  private void expandAll(List<WatchedModel> models) {
    for (int i = 0; i < models.size(); i++) {
      mListView.expandGroup(i);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  class WatchedListAdapter extends BaseExpandableListAdapter {

    private List<WatchedModel> mData = new ArrayList<>();

    public void addAll(List<WatchedModel> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public int getGroupCount() {
      return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      return null == mData.get(groupPosition).getList() ? 0 : mData.get(groupPosition).getList().size();
    }

    @Override
    public WatchedModel getGroup(int groupPosition) {
      return mData.get(groupPosition);
    }

    @Override
    public WatchedModel.ListBean getChild(int groupPosition, int childPosition) {
      return mData.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public boolean hasStableIds() {
      return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      GroupViewHolder holder;
      if (null == convertView) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_watched_list_item_group, parent, false);
        holder = new GroupViewHolder(convertView);
        convertView.setTag(holder);
      }
      holder = (GroupViewHolder) convertView.getTag();
      holder.mDateView.setText(getGroup(groupPosition).getSeeDate());
      return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      ChildViewHolder holder;
      if (null == convertView) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_watched_list_item_child, parent, false);
        holder = new ChildViewHolder(convertView);
        convertView.setTag(holder);
      }
      holder = (ChildViewHolder) convertView.getTag();
      final WatchedModel.ListBean bean = getChild(groupPosition, childPosition);
      ImageLoader.loadOptimizedHttpImage(getContext(), bean.getThumbnail()).into(holder.mSnapshotView);
      holder.mDescriptionView.setText(bean.getName());
      setViewByState(holder.mAwardInfoView, holder.mBtnView, bean);
      return convertView;
    }

    private void setViewByState(TextView awardView, TextView btnView, WatchedModel.ListBean bean) {
      String state = bean.getState();
      String advState = bean.getAdvState();
      btnView.setEnabled(true);
      switch (state) {
        case WatchedModel.STATE_ANSWER:
          btnView.setText(R.string.activity_watched_btn_answer);
          btnView.setTextColor(getResources().getColor(android.R.color.white));
          btnView.setBackgroundResource(R.drawable.bg_btn_red_round_corner);
          btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // TODO 答题界面
            }
          });
          awardView.setText(R.string.activity_watched_info_accept);
          awardView.setText(R.string.activity_watched_info_accept);
          break;
        case WatchedModel.STATE_ACCEPT:
          if (TextUtils.equals(advState, AdvertisementConstant.ADV_STATE_LAUNCHING)) {
            btnView.setText(R.string.activity_watched_btn_accept);
            btnView.setTextColor(getResources().getColor(android.R.color.white));
            btnView.setBackgroundResource(R.drawable.bg_btn_red_round_corner);
            btnView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                // TODO 领取红包
              }
            });
            awardView.setText(getString(R.string.activity_watched_info, bean.getMoney(), bean.getVirtualMoney()));
          } else if (TextUtils.equals(advState, AdvertisementConstant.ADV_STATE_END)) {
            btnView.setText(R.string.activity_watched_btn_over);
            btnView.setTextColor(getResources().getColor(R.color.font_color_secondary));
            btnView.setBackgroundResource(R.drawable.bg_btn_gray_round_corner);
            btnView.setEnabled(false);
          }
          awardView.setText(getString(R.string.activity_watched_info, bean.getMoney(), bean.getVirtualMoney()));
          break;
        case WatchedModel.STATE_WATCH_AGAIN:
          btnView.setText(R.string.activity_watched_btn_view_again);
          btnView.setTextColor(getResources().getColor(android.R.color.white));
          btnView.setBackgroundResource(R.drawable.bg_btn_brown_round_corner);
          btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // TODO 跳转到播放界面
            }
          });
          awardView.setText(getString(R.string.activity_watched_info, bean.getMoney(), bean.getVirtualMoney()));
          break;
        case WatchedModel.STATE_OVER:
          btnView.setText(R.string.activity_watched_btn_over);
          btnView.setTextColor(getResources().getColor(R.color.font_color_secondary));
          btnView.setBackgroundResource(R.drawable.bg_btn_gray_round_corner);
          btnView.setEnabled(false);
          break;
      }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

  }

  class GroupViewHolder {
    @Bind(R.id.date)
    TextView mDateView;

    public GroupViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  class ChildViewHolder {
    @Bind(R.id.snapshot)
    ImageView mSnapshotView;
    @Bind(R.id.description)
    TextView mDescriptionView;
    @Bind(R.id.btn)
    TextView mBtnView;
    @Bind(R.id.award_info)
    TextView mAwardInfoView;

    public ChildViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
