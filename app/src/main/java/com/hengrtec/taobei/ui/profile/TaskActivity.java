package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.MyTaskModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.home.DetailSysQuestionActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.ui.tab.MainTabActivity;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by jiao on 2016/6/6.
 */
public class TaskActivity extends BasicTitleBarActivity {
  @Bind(R.id.task) TextView task;
  @Bind(R.id.task_1) TextView task1;
  @Bind(R.id.task_2) TextView task2;
  @Bind(R.id.task_3) TextView task3;
  private CollectionListAdapter mAdapter;
  @Inject AdvertisementService mAdvService;
  @Bind(R.id.list_view) SuperRecyclerView mListView;

  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    loadData(true);
  }

  @Override public boolean initializeTitleBar() {

    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override public int getLayoutId() {
    return R.layout.activity_task;
  }

  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
  }

  private void loadData(boolean showProgress) {
    if (showProgress) {
      showProgressDialog("", true);
    }
    manageRpcCall(mAdvService.getAdvTaskList(
        new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
        new UiRpcSubscriber<MyTaskModel>(this) {
          @Override protected void onSuccess(MyTaskModel data) {
            if ("0".equals(data.getType())) {
              setMiddleTitle(R.string.drawer_btn_task);
              task.setText(R.string.activity_task_title);
              task1.setText(R.string.activity_task_title_1);
              task2.setText(R.string.activity_task_title_2);
              task3.setVisibility(View.VISIBLE);
              task3.setText(R.string.activity_task_title_3);
            } else {
              setMiddleTitle(R.string.drawer_btn_task);
              task.setText(R.string.activity_task_title_4);
              task1.setText(R.string.activity_task_title_5);
              task2.setText(R.string.activity_task_title_6);
              task3.setVisibility(View.GONE);
            }
            mListView.setLayoutManager(new LinearLayoutManager(TaskActivity.this));
            mListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
              @Override public void onRefresh() {
                loadData(false);
              }
            });
            mAdapter = new CollectionListAdapter(data.getList());
            mListView.setAdapter(mAdapter);
          }

          @Override protected void onEnd() {
            closeProgressDialog();
            mListView.setLoadingMore(false);
            mListView.setRefreshing(false);
          }
        });
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.num) TextView num;
    @Bind(R.id.ask_title) TextView askTitle;
    @Bind(R.id.ask_content) TextView askContent;
    @Bind(R.id.linearLayout) LinearLayout linearLayout;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class CollectionListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<MyTaskModel.ListEntity> mData = new ArrayList<>();

    public CollectionListAdapter(List<MyTaskModel.ListEntity> list) {
      addAll(list);
    }

    public void addAll(List<MyTaskModel.ListEntity> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(TaskActivity.this)
          .inflate(R.layout.activity_ask_list_item, parent, false);
      return new ItemViewHolder(view);
    }

    @Override public void onBindViewHolder(final ItemViewHolder holder, final int position) {
      final MyTaskModel.ListEntity list = mData.get(position);

      holder.askTitle.setText(list.getTitle());
      holder.askContent.setText(list.getDesc());
      holder.num.setText(String.valueOf(list.getOrderNo()));
      if ("0".equals(list.getState())) {
        holder.linearLayout.setEnabled(true);
        holder.linearLayout.setClickable(true);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {

            switch (mData.get(position).getTarget()) {
              case "a":
                startActivity(new Intent(TaskActivity.this, ProfileDetailActivity.class));
                break;
              case "b":
                startActivity(new Intent(MainTabActivity.ACTION_ADV_LIST));
                break;
              case "c":
                startActivity(new Intent(MyAccountActivity.INTENT_ACTION_WITHDRAW));
                break;
              case "d":
                startActivity(new Intent(MainTabActivity.ACTION_BI));
                break;
              case "e":
                break;
              case "f":
                break;
              case "h":
                startActivity(new Intent(TaskActivity.this, DetailSysQuestionActivity.class));
                break;
              default:
                break;
            }
            return;
          }
        });
      } else {
        holder.linearLayout.setEnabled(false);
        holder.linearLayout.setClickable(false);
      }
    }

    @Override public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }
  }
}
