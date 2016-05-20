/*
 * 文件名: CommentListActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/19
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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CommentModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.CommentParams;
import com.hengrtec.taobei.net.rpc.service.params.DeleteCommentParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.DateUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 评论列表<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/19]
 */
public class CommentListActivity extends BasicTitleBarActivity {
  private static final int PAGE_COUNT = 20;
  private static final int LOAD_MORE_COUNT = 3;
  @Bind(R.id.list_view)
  SuperRecyclerView mListView;
  @Inject
  UserService mUserService;
  private CommentListAdapter mAdapter;
  private int mCurrentPage = 1;

  private OnMoreListener onMoreListener = new OnMoreListener() {
    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int
        maxLastVisiblePosition) {
      if (!mListView.isLoadingMore()) {
        loadData();
      }
    }
  };

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initView();
    showProgressDialog("", true);
    loadData();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build()
        .inject(this);
  }

  private void initView() {
    mListView.setLayoutManager(new LinearLayoutManager(this));
    mListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mCurrentPage = 1;
        loadData();
      }
    });

    mListView.setupMoreListener(onMoreListener, LOAD_MORE_COUNT);
    mAdapter = new CommentListAdapter();
    mListView.setAdapter(mAdapter);
  }

  private void loadData() {
    manageRpcCall(mUserService.myCommentList(new CommentParams(getComponent().loginSession()
        .getUserId(), mCurrentPage, PAGE_COUNT)), new UiRpcSubscriber<List<CommentModel>>(this) {


      @Override
      protected void onSuccess(List<CommentModel> commentModels) {
        if (mCurrentPage == 1) {
          mAdapter.clear();
        }
        if (null == commentModels || commentModels.size() < PAGE_COUNT) {
          mListView.removeMoreListener();
        } else {
          mListView.setOnMoreListener(onMoreListener);
          mCurrentPage++;
        }
        mAdapter.addAll(commentModels);
        mAdapter.notifyDataSetChanged();
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_comment_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  private void performDelete(String commentId) {
    manageRpcCall(mUserService.deleteComment(new DeleteCommentParams(commentId)), new
        UiRpcSubscriber<String>(this) {


          @Override
          protected void onSuccess(String s) {

          }

          @Override
          protected void onEnd() {

          }
        });
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_comment_list;
  }

  private class CommentListAdapter extends RecyclerView.Adapter<CommentViewHolder> {


    private List<CommentModel> mData = new ArrayList<>();

    public CommentListAdapter() {
    }

    public void addAll(List<CommentModel> list) {
      if (null != list) {
        mData.addAll(list);
      }
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(CommentListActivity.this).inflate(R.layout
          .activity_comment_list_item, parent, false);
      return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
      final CommentModel model = mData.get(position);
      holder.mAdvNameView.setText(model.getAdvName());
      holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mData.remove(position);
          notifyDataSetChanged();
          performDelete(model.getCommentId());
        }
      });
      holder.mCommentView.setText(model.getContents());
      holder.mPraiseNumberView.setText(getString(R.string.activity_comment_be_praised, model
          .getHearts()));
      ImageLoader.loadOptimizedHttpImage(CommentListActivity.this, model.getThumbnail()).into
          (holder.mAdvSnapshotView);
      holder.mTimeView.setText(DateUtils.getDisplayTime(model.getCommentTime(),
          CommentListActivity.this));
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }
  }

  class CommentViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.btn_delete)
    ImageView mBtnDelete;
    @Bind(R.id.comment)
    TextView mCommentView;
    @Bind(R.id.time)
    TextView mTimeView;
    @Bind(R.id.praise_number)
    TextView mPraiseNumberView;
    @Bind(R.id.adv_snapshot)
    ImageView mAdvSnapshotView;
    @Bind(R.id.adv_name)
    TextView mAdvNameView;

    public CommentViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
