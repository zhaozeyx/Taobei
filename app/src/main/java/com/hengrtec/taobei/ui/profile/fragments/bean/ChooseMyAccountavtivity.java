package com.hengrtec.taobei.ui.profile.fragments.bean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CardQueryModel;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.GetAddCardParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCardQueryParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.profile.fragments.adapter.UserAdapter;
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

/**
 * Created by jiao on 2016/5/23.
 */
public class ChooseMyAccountavtivity extends BasicTitleBarActivity  implements OnLoadListener, RefreshLayout.OnRefreshListener,
    OnItemClickListener, OnItemLongClickListener, OnItemTouchToMove, OnItemViewMoveListener,
    OnItemViewSwipeListener {
  @Inject UserService mAdvService;
  public static final String TYPE = "type";
  @Bind(R.id.empty_mycount) LinearLayout emptyMycount;

  private UserAdapter userAdapter;
  @Bind(R.id.txt) TextView txt;

  @Bind(R.id.jrecyclerview) JRecyclerView jrecyclerview;
  @Bind(R.id.refreshlayout) RefreshLayout refreshlayout;

  private View parentView;
  public static final int REQUSET = 1;
  private String nick = "1";
  private String name = null;
  private String cardnum = null;
  private String bank = null;
  private String bankcode = null;


  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.my_account_out_money);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    setRightTextButtonVisible(true);
    setRightTextButtonTextColor(R.color.font_color_white);
    setRightTextButton(R.string.my_account_out_addcard, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ChooseMyAccountavtivity.this, AddMyAccountavtivity.class);
        //发送意图标示为REQUSET=1
        startActivityForResult(intent, REQUSET);
      }
    });
    return true;
  }
  private void inject() {
    DaggerServiceComponent.builder()
        .globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule())
        .build()
        .inject(this);
  }

  @Override protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    inject();
    initVIew();
    initData();
  }

  @Override public int getLayoutId() {
    return R.layout.activity_out_one;
  }

  private void initData() {
    manageRpcCall(mAdvService.getAdvCardQueryList(
        new GetCardQueryParams(String.valueOf(getComponent().loginSession().getUserId()))),
        new UiRpcSubscriber<CardQueryModel>(this) {
          @Override protected void onSuccess(CardQueryModel cardQueryModels) {
            if (null != cardQueryModels) {
              refreshlayout.setVisibility(View.VISIBLE);
              emptyMycount.setVisibility(View.GONE);
              //设置页码
              //userAdapter.setpage(true);
              //设置数据
              userAdapter.setDatas(cardQueryModels.getAccounts());
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
    jrecyclerview.setLayoutManager(new LinearLayoutManager(this));
    //实例化适配器
    userAdapter = new UserAdapter(this);
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




  @Override public void onItemClick(RecyclerHolder holder, View view, int position) {

    Intent intent = new Intent(ChooseMyAccountavtivity.this, OutMyAccountavtivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("name",   userAdapter.getItem(position).getAccountUserName());
    bundle.putString("cardnum",   userAdapter.getItem(position).getAccountName());
    bundle.putString("bank",   userAdapter.getItem(position).getBankName());
    bundle.putString("bankcode",   userAdapter.getItem(position).getBankNameCode());
    intent.putExtras(bundle);
    //setResult(RESULT_OK, intent);
    startActivity(intent);
    Toast.makeText(this, "item " + position + " 点击", Toast.LENGTH_SHORT).show();
    finish();
  }

  @Override public boolean onItemLongClick(RecyclerHolder holder, View view, int position) {
    //长点击跟长按拖动换位有冲突，正常情况下不要同时使用
    Toast.makeText(this, "item " + position + " 长点击", Toast.LENGTH_SHORT).show();
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
    //jrecyclerview.startDrag(holder);
  }

  /**
   * 长点击拖动 换位
   */
  @Override public boolean onItemViewMove(RecyclerView recyclerView,
      RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
    userAdapter.moveData(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }

  /**
   * 滑动删除
   */
  @Override public void onItemViewSwipe(RecyclerView.ViewHolder viewHolder, int direction) {

    //userAdapter.removeData(viewHolder.getAdapterPosition());
  }




  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUSET && resultCode == this.RESULT_OK) {
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
              bankcode, name, cardnum, bank)), new UiRpcSubscriber<CardQueryModel>(this) {
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
