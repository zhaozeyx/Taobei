/*
 * 文件名: ProfileRecordsActivity
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
import com.hengrtec.taobei.net.rpc.model.ProfitRecordModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.ProfitRecordsParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.DateUtils;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * 收益记录<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/18]
 */
public class ProfitRecordsActivity extends BasicTitleBarActivity {
  private static final int LOAD_MORE_LIMIT = 3;
  private static final int PAGE_COUNT = 20;

  @Inject
  UserService mUserService;
  @Bind(R.id.profit_record_list)
  SuperRecyclerView mRecordListView;

  private int mCurrentPage = 1;
  private RecordListAdapter mListAdapter;


  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initView();
    showProgressDialog("", true);
    loadData();
  }

  private void loadData() {
    manageRpcCall(mUserService.myTransactionList(new ProfitRecordsParams(getComponent()
        .loginSession().getUserId(), mCurrentPage, PAGE_COUNT)), new
        UiRpcSubscriber<List<ProfitRecordModel>>(this) {


          @Override
          protected void onSuccess(List<ProfitRecordModel> profitRecordModels) {
            if (null == profitRecordModels || profitRecordModels.size() < PAGE_COUNT) {
              mRecordListView.removeMoreListener();
            }
            if (profitRecordModels.size() == PAGE_COUNT) {
              mCurrentPage++;
            }
            if (mCurrentPage == 1) {
              mListAdapter.clear();
            }
            mListAdapter.addAll(profitRecordModels);
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

  private void initView() {
    mRecordListView.setLayoutManager(new LinearLayoutManager(this));
    mRecordListView.setupMoreListener(new OnMoreListener() {
      @Override
      public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int
          maxLastVisiblePosition) {
        if (mRecordListView.isLoadingMore()) {
          return;
        }
        loadData();
      }
    }, LOAD_MORE_LIMIT);
    mRecordListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mCurrentPage = 1;
        loadData();
      }
    });

    mListAdapter = new RecordListAdapter();
    mRecordListView.setAdapter(mListAdapter);
  }


  @Override
  public int getLayoutId() {
    return R.layout.activity_profit_records;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_profit_records_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  class RecordListAdapter extends RecyclerView.Adapter<ChildViewHolder> {
    private List<ProfitRecordModel> mData = new ArrayList<>();

    public RecordListAdapter() {
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(ProfitRecordsActivity.this).inflate(R.layout
          .activity_profit_records_list_item_child, parent, false);
      return new ChildViewHolder(view);
    }

    public void addAll(List<ProfitRecordModel> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
      ProfitRecordModel model = mData.get(position);
      holder.mDateView.setText(DateUtils.getFormatDateTime(new Date(model.getRecordTime()),
          DateUtils.FORMAT_YEAR_MONTH));
      holder.mTimeView.setText(DateUtils.getOneHourTimeSlot(model.getRecordTime()));
      holder.mProfitSingleView.setText("+" + model.getBenefit());
      holder.mDescriptionView.setText(model.getRecordDesc());
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }
  }

  static class GroupViewHolder {
    @Bind(R.id.date)
    TextView mDateView;
    @Bind(R.id.total_profit)
    TextView mTotalProfitView;

    GroupViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class ChildViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.date)
    TextView mDateView;
    @Bind(R.id.time)
    TextView mTimeView;
    @Bind(R.id.profit_single)
    TextView mProfitSingleView;
    @Bind(R.id.description)
    TextView mDescriptionView;

    public ChildViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

}
