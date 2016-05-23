/*
 * 文件名: MessageSystemFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/20
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.model.GrabMyMessageModel;
import com.hengrtec.taobei.utils.DateUtils;
import com.hengrtec.taobei.utils.JsonConverter;
import java.util.Date;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/20]
 */
public class MessagePromotionFragment extends BaseMessageListFragment {

  public static MessagePromotionFragment newInstance(int type) {

    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY_MSG_TYPE, type);
    MessagePromotionFragment fragment = new MessagePromotionFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected RecyclerView.Adapter getAdapter() {
    return new MessageListAdapter();
  }

  class MessageListAdapter extends BaseMessageAdapter<ItemViewHolder> {

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout
          .fragment_message_promotion_list_item, parent, false);
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindData(ItemViewHolder holder, int position) {
      final GrabMyMessageModel.MessageBean bean = JsonConverter.jsonToObject(GrabMyMessageModel
          .MessageBean.class, mData.get(position).getMsgContent());
      holder.mDateView.setText(DateUtils.getFormatDateTime(new Date(bean.getMsgTime()), DateUtils
          .FORMAT_YEAR_MONTH_DAY));
      holder.mContentView.setText(bean.getMsgContent());
      holder.mTitleView.setText(bean.getMsgTitle());
    }

    @Override
    protected ImageView getBtnChoose(ItemViewHolder holder) {
      return holder.mBtnChoose;
    }

  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.btn_choose)
    ImageView mBtnChoose;
    @Bind(R.id.date)
    TextView mDateView;
    @Bind(R.id.title)
    TextView mTitleView;
    @Bind(R.id.content)
    TextView mContentView;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
