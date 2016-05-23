/*
 * 文件名: ChartsActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.FriendsCircleModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.FriendsCircleParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 排行榜<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/18]
 */
public class ChartsActivity extends BasicTitleBarActivity {
  private static final int MAX_COUNT = 10;
  @Bind(R.id.charts_list)
  SuperRecyclerView mChartsListView;

  @Inject
  UserService mUserService;

  private int mCurrentPage = 1;
  private RankListAdapter mListAdapter;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initView();
    showProgressDialog("", true);
    loadData();
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_charts;
  }

  @Override
  public boolean initializeTitleBar() {
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    setMiddleTitle(R.string.activity_charts_title);
    return true;
  }

  private void initView() {
    mChartsListView.setLayoutManager(new LinearLayoutManager(this));
    mChartsListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        loadData();
      }
    });
    mListAdapter = new RankListAdapter();
    mChartsListView.setAdapter(mListAdapter);
  }

  private void loadData() {
    manageRpcCall(mUserService.friendsCircle(new FriendsCircleParams(getComponent().loginSession
        ().getUserId(), mCurrentPage, MAX_COUNT)), new UiRpcSubscriber<List<FriendsCircleModel>>
        (this) {
      @Override
      protected void onSuccess(List<FriendsCircleModel> friendsCircleModels) {
        mListAdapter.clear();
        mListAdapter.addAll(friendsCircleModels);
        mListAdapter.notifyDataSetChanged();
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

  public class RankListAdapter extends RecyclerView.Adapter<RankViewHolder> {
    private static final int HIGH_RANK = 3;
    private List<FriendsCircleModel> mData = new ArrayList<>();

    public RankListAdapter() {
    }

    public void addAll(List<FriendsCircleModel> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(ChartsActivity.this).inflate(R.layout
          .activity_charts_list_item, parent, false);
      return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
      FriendsCircleModel model = mData.get(position);
      if (position <= HIGH_RANK) {
        holder.mContentContainer.setBackgroundResource(R.drawable.bg_rank_content_dark);
        holder.mRankNumberView.setBackgroundResource(R.mipmap.bg_rank_number_dark);
        holder.mRankNumberView.setTextColor(getResources().getColor(R.color
            .report_rank_color_primary));
        holder.mUserNameView.setTextColor(getResources().getColor(R.color.font_color_primary));
        holder.mBenefitView.setTextColor(getResources().getColor(R.color
            .report_rank_color_primary));
      } else {
        holder.mContentContainer.setBackgroundResource(R.drawable.bg_rank_content_light);
        holder.mRankNumberView.setBackgroundResource(R.mipmap.bg_rank_number_light);
        holder.mRankNumberView.setTextColor(getResources().getColor(R.color
            .report_rank_color_secondary));
        holder.mUserNameView.setTextColor(getResources().getColor(R.color.font_color_dark_gray));
        holder.mBenefitView.setTextColor(getResources().getColor(R.color
            .report_rank_color_secondary));
      }
      ImageLoader.loadOptimizedHttpImage(ChartsActivity.this, model.getAvart()).placeholder(R
          .drawable.src_avatar_default).into(holder
          .mAvatarView);
      holder.mRankNumberView.setText(String.valueOf(position + 1));
      holder.mUserNameView.setText(model.getUserName());
      holder.mBenefitView.setText("￥" + model.getMoney());
    }

    @Override
    public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }
  }

  public class RankViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.rank_number)
    TextView mRankNumberView;
    @Bind(R.id.avatar)
    CircleImageView mAvatarView;
    @Bind(R.id.user_name)
    TextView mUserNameView;
    @Bind(R.id.benefit)
    TextView mBenefitView;
    @Bind(R.id.rank_content_container)
    View mContentContainer;

    public RankViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
