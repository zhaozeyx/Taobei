/*
 * 文件名: CouponListActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/31
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.hengrtec.taobei.net.rpc.model.CouponModel;
import com.hengrtec.taobei.net.rpc.service.NetConstant;
import com.hengrtec.taobei.net.rpc.service.UserService;
import com.hengrtec.taobei.net.rpc.service.params.CouponListParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.DateUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/31]
 */
public class CouponListActivity extends BasicTitleBarActivity {
  @Bind(R.id.list_view)
  SuperRecyclerView mListView;

  @Inject
  UserService mUserService;
  private CouponListAdapter mAdapter = new CouponListAdapter();

  @Override
  public int getLayoutId() {
    return R.layout.activity_coupon_list;
  }

  private void initView() {
    mListView.setLayoutManager(new LinearLayoutManager(this));
    mListView.setAdapter(mAdapter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    inject();
    initView();
    loadData();
  }

  @Override
  public boolean initializeTitleBar() {
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    setMiddleTitle(R.string.activity_coupon_list_title);
    return true;
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void loadData() {
    manageRpcCall(mUserService.couponList(new CouponListParams(getComponent().loginSession()
            .getUserId(), Integer.MAX_VALUE)),
        new UiRpcSubscriber<List<CouponModel>>(this) {
          @Override
          protected void onSuccess(List<CouponModel> couponModels) {
            mAdapter.clear();
            mAdapter.addAll(couponModels);
            mAdapter.notifyDataSetChanged();
          }

          @Override
          protected void onEnd() {

          }
        });
  }

  class CouponListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    public List<CouponModel> mData = new ArrayList();

    public void addAll(List<CouponModel> models) {
      if (null == models) {
        return;
      }
      mData.addAll(models);
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view;
      if (viewType == ItemType.NORMAL.ordinal()) {
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout
            .activity_coupon_list_normal, parent, false);
      } else {
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout
            .activity_coupon_list_overdue, parent, false);
      }
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
      final CouponModel model = mData.get(position);
      if (getItemViewType(position) == ItemType.OVERDUE.ordinal()) {
        ImageLoader.loadOptimizedHttpImage(getApplicationContext(), NetConstant.BASE_URL_LOCATION
            + model.getImg()).placeholder(R.mipmap.icon_shop_logo_default)
            .bitmapTransform(new GrayscaleTransformation(getApplicationContext())).into(holder
            .mLogoView);
      } else {
        ImageLoader.loadOptimizedHttpImage(getApplicationContext(), NetConstant.BASE_URL_LOCATION
            + model.getImg()).placeholder(R.mipmap.icon_shop_logo_default)
            .into(holder.mLogoView);
      }
      holder.mShopNameView.setText(getResources().getString(R.string
          .activity_coupon_list_item_shop_name, model.getSellerName()));
      setStatus(holder.mStatusView, model);
      holder.mShareInfoView.setVisibility(View.GONE);
      holder.mValidityView.setText(getString(R.string.activity_coupon_list_item_validity,
          DateUtils.getSimpleDateTime(model.getEndTime())));
      switch (model.getType()) {
        case CouponModel.TYPE_COUPON:
          holder.type.setText(model.getCouponName());
          holder.mCouponValueView.setText(model.getCouponDesc());
          break;
        case CouponModel.TYPE_DISCOUNT:
          holder.type.setText(model.getCouponName());
          holder.mCouponValueView.setText(model.getCouponDesc());
          break;
      }

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          startActivity(CouponDetailActivity.makeIntent(CouponListActivity.this, model));
        }
      });
    }

    private void setStatus(TextView view, CouponModel model) {
      if (TextUtils.equals(model.getExpired(), CouponModel.EXPIRED_OVERDUE)) {
        view.setText(R.string.activity_coupon_list_item_overdue);
        view.setTextColor(getResources().getColor(R.color.font_color_secondary));
        view.setVisibility(View.VISIBLE);
        return;
      } else {
        if (DateUtils.isTomorrow(model.getEndTime())) {
          view.setTextColor(getResources().getColor(R.color.font_color_red_dark));
          view.setText(R.string.activity_coupon_list_item_left_time);
          view.setVisibility(View.VISIBLE);
        } else {
          view.setVisibility(View.GONE);
        }
      }
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
      CouponModel model = mData.get(position);
      switch (model.getExpired()) {
        case CouponModel.EXPIRED_NORMAL:
          return ItemType.NORMAL.ordinal();
        case CouponModel.EXPIRED_OVERDUE:
          return ItemType.OVERDUE.ordinal();
      }
      return super.getItemViewType(position);
    }
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.logo)
    CircleImageView mLogoView;
    @Bind(R.id.shop_name)
    TextView mShopNameView;
    @Bind(R.id.status)
    TextView mStatusView;
    @Bind(R.id.coupon_value)
    TextView mCouponValueView;
    @Bind(R.id.share_info)
    TextView mShareInfoView;
    @Bind(R.id.validity)
    TextView mValidityView;
    @Bind(R.id.type)
    TextView type;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  enum ItemType {
    NORMAL, OVERDUE
  }

}
