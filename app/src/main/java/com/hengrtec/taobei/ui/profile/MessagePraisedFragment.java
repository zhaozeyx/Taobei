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
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Date;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/20]
 */
public class MessagePraisedFragment extends BaseMessageListFragment {

  public static MessagePraisedFragment newInstance(int type) {

    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY_MSG_TYPE, type);
    MessagePraisedFragment fragment = new MessagePraisedFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected RecyclerView.Adapter getAdapter() {
    return new PraisedListAdapter();
  }

  class PraisedListAdapter extends BaseMessageAdapter<ItemViewHolder> {

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout
          .fragment_message_praised_list_item, parent, false);
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindData(ItemViewHolder holder, int position) {
      final GrabMyMessageModel.MessageBean bean = JsonConverter.jsonToObject(GrabMyMessageModel
          .MessageBean.class, mData.get(position).getMsgContent());
      ImageLoader.loadOptimizedHttpImage(getActivity(), bean.getMsgImg()).placeholder(R.mipmap
          .src_avatar_default_drawer).into(holder.avatarView);
      holder.dateView.setText(DateUtils.getFormatDateTime(new Date(bean.getMsgTime()), DateUtils
          .FORMAT_YEAR_MONTH_DAY));
      holder.contentView.setText(bean.getMsgContent());
      holder.userNameView.setText(bean.getMsgTitle());
    }

    @Override
    protected ImageView getBtnChoose(ItemViewHolder holder) {
      return holder.btnChoose;
    }

  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.btn_choose)
    ImageView btnChoose;
    @Bind(R.id.avatar)
    CircleImageView avatarView;
    @Bind(R.id.user_name)
    TextView userNameView;
    @Bind(R.id.date)
    TextView dateView;
    @Bind(R.id.content)
    TextView contentView;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
