package com.hengrtec.taobei.ui.profile.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CardQueryModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GetAddCardParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.net.rpc.service.params.GetRemoveCardParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.hengrtec.taobei.ui.profile.fragments.adapter.UserAdapter;
import com.hengrtec.taobei.ui.profile.fragments.bean.AddMyAccountavtivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.jtech.listener.OnItemClickListener;
import com.jtech.listener.OnItemLongClickListener;
import com.jtech.listener.OnItemTouchToMove;
import com.jtech.listener.OnItemViewMoveListener;
import com.jtech.listener.OnItemViewSwipeListener;
import com.jtech.listener.OnLoadListener;
import com.jtech.view.JRecyclerView;
import com.jtech.view.RecyclerHolder;
import com.jtech.view.RefreshLayout;
import javax.inject.Inject;

public class OneFragment extends BasicTitleBarFragment
    implements OnLoadListener, RefreshLayout.OnRefreshListener, OnItemClickListener,
    OnItemLongClickListener, OnItemTouchToMove, OnItemViewMoveListener, OnItemViewSwipeListener {
  @Inject UserService mAdvService;
  public static final String TYPE = "type";
  @Bind(R.id.empty_mycount) LinearLayout emptyMycount;
  @Bind(R.id.add_my_account) Button addMyAccount;
  private UserAdapter userAdapter;
  @Bind(R.id.txt) TextView txt;
  @Bind(R.id.txt_content) TextView txtContent;
  @Bind(R.id.jrecyclerview) JRecyclerView jrecyclerview;
  @Bind(R.id.refreshlayout) RefreshLayout refreshlayout;

  private View parentView;
  public static final int REQUSET = 1;
  private String nick = "1";
  private String name = null;
  private String cardnum = null;
  private String bank = null;
  private String bankcode = null;

  public OneFragment() {
    // Required empty public constructor
  }

  @Override protected void onCreateViewCompleted(View view) {
    ButterKnife.bind(this, view);
    inject();
    initVIew();
    initData();
  }

  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getActivity().getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }

  @Override public int getLayoutId() {
    return R.layout.fragment_one;
  }

  private void initData() {
    manageRpcCall(mAdvService.getAdvCardQueryList(
        new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
        new UiRpcSubscriber<CardQueryModel>(getActivity()) {
          @Override protected void onSuccess(CardQueryModel cardQueryModels) {
            if (null != cardQueryModels) {
              refreshlayout.setVisibility(View.VISIBLE);
              emptyMycount.setVisibility(View.GONE);
              //设置页码
              //userAdapter.setpage(true);
              //设置数据
              userAdapter.setDatas(cardQueryModels.getAccounts());

              txtContent.setText(String.valueOf(cardQueryModels.getMoney()) + "元");
            } else {
              refreshlayout.setVisibility(View.GONE);
              emptyMycount.setVisibility(View.VISIBLE);
            }
            refreshlayout.refreshingComplete();
            jrecyclerview.setLoadCompleteState();
          }

          @Override protected void onEnd() {
            refreshlayout.refreshingComplete();
          }
        });
  }

  private void initVIew() { //是否开启加载更多功能
    jrecyclerview.setLoadMore(true);
    //设置可以滑动删除
    jrecyclerview.setSwipeEnd(true, this);
    //设置可以上下拖动换位
    //jrecyclerview.setMoveUpDown(true, (OnItemViewMoveListener) getActivity());
    //设置layoutmanager(也可以换成其余两种)
    jrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    //实例化适配器
    userAdapter = new UserAdapter(getActivity());
    //设置适配器.

    jrecyclerview.setAdapter(userAdapter);
    //设置监听
    jrecyclerview.setOnLoadListener(this);//加载更多的监听
    userAdapter.setOnItemTouchToMove(this);//触摸拖动的监听，同时也需要在适配器中添加要监听的视图
    refreshlayout.setOnRefreshListener(this);//下拉刷新的监听
    jrecyclerview.setOnItemClickListener(this);//item点击监听
    jrecyclerview.setOnItemLongClickListener(this);//item长点击监听
    //发起下拉刷新
    refreshlayout.startRefreshing();
  }

  public static OneFragment newInstance(String text) {
    OneFragment fragment = new OneFragment();
    Bundle bundle = new Bundle();
    bundle.putString(TYPE, text);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onItemClick(RecyclerHolder holder, View view, int position) {
    Toast.makeText(getActivity(), "侧滑删除银行卡", Toast.LENGTH_SHORT).show();
  }

  @Override public boolean onItemLongClick(RecyclerHolder holder, View view, int position) {
    //长点击跟长按拖动换位有冲突，正常情况下不要同时使用
    //Toast.makeText(getActivity(), "item " + position + " 长点击", Toast.LENGTH_SHORT).show();
    return true;
  }

  @Override public void onRefresh() {
    initData();
  }

  @Override public void loadMore() {
    initData();
  }

  /**
   * 触摸拖动
   */
  @Override public void itemTouchToMove(RecyclerHolder holder) {
    //触摸的时候拖动
    jrecyclerview.startDrag(holder);
  }

  /**
   * 长点击拖动 换位
   */
  @Override public boolean onItemViewMove(RecyclerView recyclerView,
      RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
    userAdapter.moveData(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }
  protected void dialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("确认退出吗？");

    builder.setTitle("提示");

    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

        getActivity().finish();
      }
    });

    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    builder.create().show();
  }
  /**
   * 滑动删除
   */
  @Override public void onItemViewSwipe(final RecyclerView.ViewHolder viewHolder,  int direction) {
    //jrecyclerview.setSwipeStart(true,this);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("确定要删除提现账号吗？");

    builder.setTitle("提示");

    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        manageRpcCall(mAdvService.getAdvRemoveCardList(

            new GetRemoveCardParams(userAdapter.getItem(viewHolder.getAdapterPosition()).getAccountId())),
            new UiRpcSubscriber<CardQueryModel>(getActivity()) {
              @Override protected void onSuccess(CardQueryModel cardQueryModel) {
                showShortToast("删除成功");
                userAdapter.removeData(viewHolder.getAdapterPosition());

              }

              @Override protected void onEnd() {

              }

              @Override public void onApiError(RpcApiError apiError) {
                super.onApiError(apiError);
                showShortToast("添加失败");
              }
            });

      }
    });

    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        userAdapter.notifyDataSetChanged();
        //initData();
      }
    });

    builder.create().show();

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // TODO: inflate a fragment view
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @OnClick(R.id.add_my_account) public void onClick() {
    Intent intent = new Intent(getActivity(), AddMyAccountavtivity.class);
    //发送意图标示为REQUSET=1
    startActivityForResult(intent, REQUSET);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUSET && resultCode == getActivity().RESULT_OK) {
      name = data.getStringExtra(AddMyAccountavtivity.KEY_USER_ID);
      cardnum = data.getStringExtra(AddMyAccountavtivity.KEY_USER);
      bank = data.getStringExtra(AddMyAccountavtivity.KEY);
      if (bank != null) {
        if (bank.equals("建设银行")) {
          bankcode = "CCB";
        } else if (bank.equals("中国农业银行")) {
          bankcode = "ABC";
        } else if (bank.equals("中国工商银行")) {
          bankcode = "ICBC";
        } else if (bank.equals("中国银行")) {
          bankcode = "BOC";
        } else if (bank.equals("中国民生银行")) {
          bankcode = "CMBC";
        } else if (bank.equals("招商银行")) {
          bankcode = "CMB";
        } else if (bank.equals("中信银行")) {
          bankcode = "ChinaCITICBank";
        } else if (bank.equals("交通银行")) {
          bankcode = "BCM";
        } else if (bank.equals("华夏银行")) {
          bankcode = "HXB";
        } else if (bank.equals("中国邮政储蓄银行")) {
          bankcode = "PSBC";
        }
      }

      manageRpcCall(mAdvService.getAdvAddCardList(
          new GetAddCardParams(String.valueOf(getComponent().loginSession().getUserId()), nick,
              bankcode, name, cardnum, bank)), new UiRpcSubscriber<CardQueryModel>(getActivity()) {
        @Override protected void onSuccess(CardQueryModel cardQueryModel) {
          showShortToast("添加成功");
          initData();
        }

        @Override protected void onEnd() {

        }

        @Override public void onApiError(RpcApiError apiError) {
          super.onApiError(apiError);
          showShortToast("添加失败");
        }
      });
    }
  }
}

