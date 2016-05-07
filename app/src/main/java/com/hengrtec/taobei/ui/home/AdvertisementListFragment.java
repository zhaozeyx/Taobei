/*
 * 文件名: AdvertisementListFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/20
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.Advertisement;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.ui.basic.BasicFragment;
import com.hengrtec.taobei.utils.AdvertisementValueBindUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 广告列表界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/20]
 */
public class AdvertisementListFragment extends BasicFragment implements AdvertisementListView {

  private static final int PAGE_COUNT = 20;
  private static final int LOAD_MORE_LAST_INDEX = 2;
  private static final String BUNDLE_ARGS_CATEGORY = "args_category";
  @Bind(R.id.notification_info)
  TextView mNotificationInfo;
  @Bind(R.id.notification_info_setting)
  TextView mNotificationInfoSetting;
  @Bind(R.id.adv_notification)
  LinearLayout mAdvNotification;
  private String mCategory;
  private List<Advertisement> mAdvertisementList = new ArrayList<>();
  private int mCurrentPage = 1;

  @Inject
  AdvertisementListPresenter mListPresenter;

  @Bind(R.id.list_view)
  SuperRecyclerView mListView;
  private AdvertisementListAdapter mListAdapter;


  public static AdvertisementListFragment newInstance(String category) {
    AdvertisementListFragment fragment = new AdvertisementListFragment();
    Bundle ars = new Bundle();
    ars.putString(BUNDLE_ARGS_CATEGORY, category);
    fragment.setArguments(ars);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_advertisement_list, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    DaggerPresenterComponent.builder()
        .presenterModule(new PresenterModule(getActivity()))
        .activityComponent(getComponent()).build().inject(this);
    mListPresenter.attachView(this);
    mCategory = getArguments().getString(BUNDLE_ARGS_CATEGORY);
    initListView();
    getData();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (TextUtils.equals(mCategory, AdvertisementConstant.ADV_STATE_UN_LAUNCH)) {
        // TODO 还有条件是用户第一次进入到这个界面
        mAdvNotification.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn).duration(500).withListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {

          }

          @Override
          public void onAnimationEnd(Animator animation) {
            YoYo.with(Techniques.Swing).playOn(mAdvNotification);
          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
        }).playOn(mAdvNotification);
      } else {
        if (null != mAdvNotification) {
          mAdvNotification.setVisibility(View.GONE);
        }
      }
    }
  }

  private OnMoreListener onMoreListener = new OnMoreListener() {
    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int
        maxLastVisiblePosition) {
      getData();
    }
  };

  private void initListView() {
    mListAdapter = new AdvertisementListAdapter();
    mListView.setAdapter(mListAdapter);
    mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

    mListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mCurrentPage = 1;
        getData();
      }
    });
  }

  private void getData() {
    // TODO 获取真实的userId
    mListPresenter.getList(mCategory, getComponent().appPreferences().getUserId(), mCurrentPage,
        PAGE_COUNT);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onDataEmpty() {
    mListView.getEmptyView().setVisibility(View.VISIBLE);
    mListView.setRefreshing(false);
    mListView.setLoadingMore(false);
  }

  @Override
  public void onDataChanged(List<Advertisement> advertisementList, boolean moreData) {
    if (mCurrentPage == 1) {
      mAdvertisementList.clear();
    }
    mAdvertisementList.addAll(advertisementList);
    if (moreData) {
      mCurrentPage++;
      mListView.setLoadingMore(true);
      mListView.setupMoreListener(onMoreListener, LOAD_MORE_LAST_INDEX);
    } else {
      mListView.setLoadingMore(false);
      mListView.removeMoreListener();
    }
    mListView.getEmptyView().setVisibility(View.GONE);
    mListAdapter.notifyDataSetChanged();
  }

  @Override
  public Context getActivityContext() {
    return getActivity();
  }

  @Override
  public void onServiceInvokeEnd() {
    mListView.setRefreshing(false);
    mListView.setLoadingMore(false);
  }

  @OnClick(R.id.notification_info_setting)
  public void onSettingClick() {
  }

  @OnClick(R.id.adv_notification)
  public void onNotificationClick() {
    YoYo.with(Techniques.FadeOutUp).duration(500).withListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mAdvNotification.setVisibility(View.GONE);
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    }).playOn(mAdvNotification);
  }

  private class AdvertisementListAdapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout
          .advertisement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Advertisement advertisement = mAdvertisementList.get(position);
      ImageLoader.loadOptimizedHttpImage(getActivity(), advertisement.getThumbnail())
          .into(holder.advSnapshot);
      String type = advertisement.getAdvType();

      switch (type) {
        case AdvertisementConstant.ADV_TYPE_VIDEO:
          holder.advType.setText(R.string.adv_item_type_video);
          break;
        case AdvertisementConstant.ADV_TYPE_PIC:
          holder.advType.setText(R.string.adv_item_type_pic);
          break;
        case AdvertisementConstant.ADV_TYPE_H5:
          holder.advType.setText(R.string.adv_item_type_h5);
          break;
      }

      holder.advTitle.setText(advertisement.getName());
      holder.advSubTitle.setText(advertisement.getSubTitle());

      String benefitType = advertisement.getBenefitType();
      AdvertisementValueBindUtils.setProfit(holder.advProfit, benefitType, advertisement.getPrice(),
          R.string.adv_item_profit_real, R.string.adv_item_profit_virtual);

      holder.rootView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          startActivity(AdvertisementDetailActivity.makeIntent(getActivity(), advertisement
              .getAdvId()));
        }
      });
    }

    @Override
    public int getItemCount() {
      return mAdvertisementList.size();
    }

  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.adv_snapshot)
    ImageView advSnapshot;
    @Bind(R.id.adv_type)
    TextView advType;
    @Bind(R.id.adv_title)
    TextView advTitle;
    @Bind(R.id.adv_sub_title)
    TextView advSubTitle;
    @Bind(R.id.adv_profit)
    TextView advProfit;
    @Bind(R.id.root_view)
    View rootView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
