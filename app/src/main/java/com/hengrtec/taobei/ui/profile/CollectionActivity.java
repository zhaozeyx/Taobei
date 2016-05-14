/*
 * 文件名: CollectionProfile
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/13
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CollectAdvModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.AdvOperationParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCollectionsParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 收藏列表界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/13]
 */
public class CollectionActivity extends BasicTitleBarActivity {
  private static final int PAGE_SIZE = 20;
  @Bind(R.id.list_view)
  SuperRecyclerView mListView;
  private int mCurrentPage = 1;

  @Inject
  AdvertisementService mAdvService;
  private CollectionListAdapter mAdapter;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initListView();
    loadData(true);
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_collection_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_collection;
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void initListView() {
    mListView.setLayoutManager(new LinearLayoutManager(this));
    mListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        loadData(false);
      }
    });
    mAdapter = new CollectionListAdapter(null);
    mListView.setAdapter(mAdapter);
  }

  private void loadData(boolean showProgress) {
    if (showProgress) {
      showProgressDialog("", true);
    }
    manageRpcCall(mAdvService.getAdvCollectionList(new GetCollectionsParams(getComponent()
        .loginSession().getUserId(), mCurrentPage, PAGE_SIZE)), new
        UiRpcSubscriber<List<CollectAdvModel>>(this) {


          @Override
          protected void onSuccess(List<CollectAdvModel> advertisements) {
            mListView.getEmptyView().setVisibility(null == advertisements || advertisements.size
                () == 0 ? View.VISIBLE : View.GONE);
            if (null == advertisements || advertisements.size() <= PAGE_SIZE) {
              mListView.removeMoreListener();
            } else {
              mCurrentPage++;
              mListView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int
                    maxLastVisiblePosition) {
                  if (mListView.isLoadingMore()) {
                    return;
                  }
                  loadData(false);
                }
              }, 3);
            }
            mAdapter.clear();
            mAdapter.addAll(advertisements);
          }

          @Override
          protected void onEnd() {
            closeProgressDialog();
            mListView.setLoadingMore(false);
            mListView.setRefreshing(false);
          }
        });
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.collection_info)
    TextView mCollectionInfoView;
    @Bind(R.id.btn_delete)
    TextView mBtnDelete;
    @Bind(R.id.adv_snapshot)
    ImageView mAdvSnapshotView;
    @Bind(R.id.adv_name)
    TextView mAdvNameView;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class CollectionListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<CollectAdvModel> mData = new ArrayList<>();

    public CollectionListAdapter(List<CollectAdvModel> list) {
      addAll(list);
    }

    public void addAll(List<CollectAdvModel> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(CollectionActivity.this).inflate(R.layout
          .activity_collection_list_item, parent, false);
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
      final CollectAdvModel collection = mData.get(position);
      ImageLoader.loadOptimizedHttpImage(CollectionActivity.this, collection.getAdv()
          .getThumbnail()).into(holder
          .mAdvSnapshotView);
      String imgType = getResources().getString(R.string.activity_collection_adv_type_video);
      switch (collection.getAdv().getAdvType()) {
        case AdvertisementConstant.ADV_TYPE_VIDEO:
          imgType = getResources().getString(R.string.activity_collection_adv_type_video);
          break;
        case AdvertisementConstant.ADV_TYPE_PIC:
          imgType = getResources().getString(R.string.activity_collection_adv_type_image);
          break;
        case AdvertisementConstant.ADV_TYPE_H5:
          imgType = getResources().getString(R.string.activity_collection_adv_type_h5);
      }
      holder.mAdvNameView.setText(collection.getAdv().getName());
      holder.mCollectionInfoView.setText(getString(R.string.activity_collection_label, imgType));
      holder.mBtnDelete.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
      holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mData.remove(position);
          notifyDataSetChanged();
          delete(collection.getAdvId());
        }
      });
    }

    @Override
    public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }
  }

  private void delete(int advId) {
    manageRpcCall(mAdvService.collectadv(new AdvOperationParams(AdvOperationParams.FLAG_FALSE,
        String.valueOf(advId), getComponent().loginSession().getUserId())), new
        UiRpcSubscriber<String>(this) {


          @Override
          protected void onSuccess(String s) {

          }

          @Override
          protected void onEnd() {

          }
        });
  }

}
