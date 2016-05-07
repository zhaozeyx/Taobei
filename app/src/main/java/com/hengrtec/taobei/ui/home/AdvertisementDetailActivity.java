/*
 * 文件名: AdvertisementDetail
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/21
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.UiRpcSubscriber1;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.model.Question;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvQuestionListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementDetailParams;
import com.hengrtec.taobei.net.rpc.service.params.GetUserAdvStateParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.home.view.DetailProfitInfoView;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.AdvertisementValueBindUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.sevenheaven.segmentcontrol.SegmentControl;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apmem.tools.layouts.FlowLayout;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 广告详情界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/21]
 */
public class AdvertisementDetailActivity extends BasicTitleBarActivity {
  private static final String TAG = "AdvertisementDetailActivity";
  private static final int ORDER_TIME = 0;
  private static final int ORDER_HOT = 1;
  private static final int REQUEST_CODE_PLAY = 0;
  /**
   * 参数，广告ID
   */
  private static final String BUNDLE_KEY_ADV_ID = "adv_id";

  /**
   * 广告缩略图
   */
  @Bind(R.id.adv_snapshot)
  ImageView mAdvSnapshot;
  /**
   * 没有播放次数的界面
   */
  @Bind(R.id.no_play_time)
  ViewStub mNoPlayTime;
  @Bind(R.id.layout_can_play)
  ImageView mLayoutCanPlay;
  @Bind(R.id.play_count_info)
  TextView mPlayCountInfo;
  @Bind(R.id.adv_title)
  TextView mAdvTitle;
  @Bind(R.id.adv_sub_title)
  TextView mAdvSubTitle;
  @Bind(R.id.adv_profit)
  TextView mAdvProfit;
  TextView mVirtualProfitGet;
  @Bind(R.id.btn_share_moments)
  TextView mBtnShareMoments;
  @Bind(R.id.btn_share_friends)
  TextView mBtnShareFriends;
  @Bind(R.id.activity_description)
  TextView mActivityDescription;
  @Bind(R.id.comment_list)
  RecyclerView mCommentListView;
  @Bind(R.id.edit_text)
  AppCompatEditText mEditText;
  @Bind(R.id.edit_disable_text)
  TextView mEditDisableView;
  @Bind(R.id.btn_praise)
  ImageView mBtnPraise;
  @Bind(R.id.btn_collect)
  ImageView mBtnCollect;
  @Bind(R.id.btn_comment)
  ImageView mBtnComment;
  @Bind(R.id.icon_new_comment)
  ImageView mIconNewComment;
  @Bind(R.id.adv_rule)
  TextView mRuleView;
  @Bind(R.id.tag_layout)
  FlowLayout mTagLayout;
  @Bind(R.id.order)
  SegmentControl mSegmentControl;
  @Bind(R.id.comment_operation_container)
  View mOperationView;
  @Bind(R.id.btn_send)
  View mBtnSend;
  @Bind(R.id.scrollView)
  ScrollView mScrollView;
  @Bind(R.id.view_more)
  View mLoadMoreView;

  @Inject
  AdvertisementService mAdvertisementService;
  @Bind(R.id.detail_profit_info)
  DetailProfitInfoView mDetailProfitInfoView;

  private int mAdvId;
  private AdvertisementDetail mDetail;
  private CommentListAdapter mListAdapter;


  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    mAdvId = getIntent().getIntExtra(BUNDLE_KEY_ADV_ID, -1);
    if (-1 == mAdvId) {
      Logger.w(TAG, "mAdvId is null");
      finish();
      return;
    }
    inject();
    initRecyclerView();
    initSegmentControl();
    initData();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void initRecyclerView() {
    mCommentListView.setLayoutManager(new LinearLayoutManager(this));
    mListAdapter = new CommentListAdapter(this);
    mCommentListView.setAdapter(mListAdapter);
  }

  private void initSegmentControl() {
    mSegmentControl.setText(getResources().getStringArray(R.array.adv_detail_order));
    mSegmentControl.setOnSegmentControlClickListener(new SegmentControl
        .OnSegmentControlClickListener() {


      @Override
      public void onSegmentControlClick(int index) {
        List<AdvertisementDetail.Comment> list;
        switch (index) {
          case ORDER_TIME:
            list = mDetail.getCommentsTimeList();
            break;
          case ORDER_HOT:
            list = mDetail.getCommentsHotList();
            break;
          default:
            list = mDetail.getCommentsTimeList();
            break;
        }
        mListAdapter.clear();
        mListAdapter.addAll(list);
        mListAdapter.notifyDataSetChanged();
      }
    });
  }

  private void initData() {
    showProgressDialog("", true);
    manageRpcCall(mAdvertisementService.getAdvertisement(new GetAdvertisementDetailParams
        (getComponent().appPreferences().getUserId(),
            mAdvId)), new UiRpcSubscriber<AdvertisementDetail>(this) {
      @Override
      protected void onSuccess(AdvertisementDetail advertisementDetail) {
        mDetail = advertisementDetail;
        refreshUI(advertisementDetail);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });

    manageRpcCall(mAdvertisementService.getAdvQuestionList(new GetAdvQuestionListParams(
        String.valueOf(getComponent().loginSession().getUserId()), String.valueOf(mAdvId)
    )), new UiRpcSubscriber<List<Question>>(this) {
      @Override
      protected void onSuccess(List<Question> questions) {

      }

      @Override
      protected void onEnd() {

      }
    });
  }

  private void refreshUI(AdvertisementDetail detail) {
    if (null == detail) {
      return;
    }
    ImageLoader.loadOptimizedHttpImage(AdvertisementDetailActivity.this, detail.getThumbnail())
        .into(mAdvSnapshot);
    mAdvTitle.setText(detail.getName());
    mAdvSubTitle.setText(detail.getSubTitle());

    setProfitInfoByStatus();

    mPlayCountInfo.setText(getResources().getString(R.string.adv_detail_play_number_info, detail
        .getPlayCounts(), detail.getPlanCounts()));
    if (0 == detail.getPlayCounts()) {
      mLayoutCanPlay.setVisibility(View.GONE);
      mNoPlayTime.setVisibility(View.VISIBLE);
    } else {
      mLayoutCanPlay.setVisibility(View.VISIBLE);
      mNoPlayTime.setVisibility(View.GONE);
    }
    mRuleView.setText(getResources().getString(R.string.adv_detail_rule, detail.getMoneyPrice()));
    mActivityDescription.setText(detail.getActivities());
    addActivityTags(detail);

    mListAdapter.addAll(detail.getCommentsTimeList());
    mListAdapter.notifyDataSetChanged();

    refreshDetailInfoView(mDetail, 0, false, false, false);
  }

  private void refreshDetailInfoView(AdvertisementDetail detail, int profitCount, boolean
      viewCompleted, boolean hasGotten, boolean hasQuestion) {
    mDetailProfitInfoView.update(detail, profitCount, viewCompleted, hasGotten, hasQuestion);
  }

  private void setProfitInfoByStatus() {
    String benefitType = mDetail.getBenefitType();
    AdvertisementValueBindUtils.setProfit(mAdvProfit, benefitType, mDetail.getPrice(), R.string
        .adv_detail_profit_info_real, R.string.adv_detail_profit_info_virtual);
  }

  private void addActivityTags(AdvertisementDetail detail) {
    LayoutInflater inflater = LayoutInflater.from(this);
    List<String> tagsList = detail.getTagsArr();
    if (null == tagsList || tagsList.size() == 0) {
      mTagLayout.setVisibility(View.GONE);
      return;
    }
    mTagLayout.setVisibility(View.VISIBLE);
    for (String tag : tagsList) {
      TextView tagView = (TextView) inflater.inflate(R.layout.tag_item, mTagLayout, false);
      tagView.setText(tag);
      mTagLayout.addView(tagView);
    }
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_advertisement_detail_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    return true;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_advertisement_detail;
  }

  @OnClick({R.id.btn_share_moments, R.id.btn_share_friends, R.id.edit_text, R.id.btn_praise, R.id
      .btn_collect, R.id.btn_comment, R.id.btn_send, R.id.edit_disable_text, R.id.view_more, R.id
      .adv_info})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_share_moments:
        break;
      case R.id.btn_share_friends:
        break;
      case R.id.edit_disable_text:
        mEditText.setVisibility(View.VISIBLE);
        mEditDisableView.setVisibility(View.INVISIBLE);
        mEditText.requestFocus();
        mOperationView.setVisibility(View.GONE);
        mBtnSend.setVisibility(View.VISIBLE);
        showInputWindow(mEditText);
        break;
      case R.id.btn_praise:
        break;
      case R.id.btn_collect:
        break;
      case R.id.btn_comment:
        //mScrollView.smoothScrollTo(mSegmentControl.getScrollX(), (int) mSegmentControl.getRw);
        mCommentListView.scrollToPosition(0);
        break;
      case R.id.btn_send:
        disabledEditText();
        break;
      case R.id.view_more:
        // TODO 加载更多
        break;
      case R.id.adv_info:
        performAdvInfoClicked();
        break;
    }

  }

  private void performAdvInfoClicked() {

    Observable<String> advInfoClickedObservable = mAdvertisementService.getUserAdvState(new
        GetUserAdvStateParams(String.valueOf
        (getComponent()
            .loginSession().getUserId()), String.valueOf(mDetail.getAdvId()))).map(new Func1<Response<ResponseModel<String>>, String>() {

      @Override
      public String call(Response<ResponseModel<String>> responseModelResponse) {
        return responseModelResponse.body().getData();
      }
    }).observeOn(AndroidSchedulers.mainThread()).filter(new Func1<String, Boolean>() {
      @Override
      public Boolean call(String s) {
        boolean stateChanged = !TextUtils.equals(mDetail.getUserAdvState(), s);
        if (stateChanged) {
          // 如果状态发生改变，重新获取数据 提示用户数据发生改变
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              showShortToast(R.string.adv_detail_toast_info_state_changed);
              initData();
            }
          });
        }
        return !stateChanged;
      }
    }).filter(new Func1<String, Boolean>() {
      @Override
      public Boolean call(String s) {
        // 判断用户是否是看完未回答问题
        boolean hasAnswerQuestion = !TextUtils.equals(mDetail.getUserAdvState(),
            AdvertisementConstant.ADV_USER_ADV_STATE_HAS_VIEWED_NOT_COMMIT_QUESTION);
        if (!hasAnswerQuestion) {
          // 如果未完成问题回答，直接跳转到回答问题界面
          startActivity(AdvQuestionListActivity.makeIntent(AdvertisementDetailActivity.this,
              mAdvId));
        }
        return hasAnswerQuestion;
      }
    }).doOnNext(new Action1<String>() {
      @Override
      public void call(String s) {
        // 如果已完成问题回答，跳转到播放界面
        Intent intent = AdvertisementPlayActivity.makeIntent(AdvertisementDetailActivity.this,
            mDetail.getAdvId()
            , mDetail.getAdvType(), mDetail.getFilePath(), mDetail.getBenefitType());
        startActivityForResult(intent, REQUEST_CODE_PLAY);
      }
    });

    manageRpcCall(advInfoClickedObservable, new UiRpcSubscriber1<String>(this) {
      @Override
      protected void onSuccess(String s) {

      }

      @Override
      protected void onEnd() {

      }
    });

  }

  @Override
  public void onBackPressed() {
    if (mEditText.getVisibility() == View.VISIBLE) {
      disabledEditText();
      return;
    }
    super.onBackPressed();
  }

  private void disabledEditText() {
    mEditText.setVisibility(View.GONE);
    mEditDisableView.setVisibility(View.VISIBLE);
    mOperationView.setVisibility(View.VISIBLE);
    mBtnSend.setVisibility(View.GONE);
    hideInputWindow(mEditText);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_CODE_PLAY:
        if (RESULT_OK == resultCode && null != data) {
          dispatchPlayFinish(data);
        }
        break;
    }
  }

  private void dispatchPlayFinish(Intent data) {
    String benefitFinal = data.getStringExtra(AdvertisementPlayActivity
        .RESULT_DATA_KEY_BENEFIT_FINAL);
    String benefitType = data.getStringExtra(AdvertisementPlayActivity
        .RESULT_DATA_KEY_BENEFIT_TYPE);
    String watchId = data.getStringExtra(AdvertisementPlayActivity.RESULT_DATA_KEY_WATCH_ID);
    mDetail.setBenefitType(benefitType);

    // TODO 最后一个参数，代表是否要回答平台问题，如何判断？？
    mDetailProfitInfoView.update(mDetail, Integer.parseInt(benefitFinal), true, false, false);
  }

  static class CommentListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private List<AdvertisementDetail.Comment> mData = new ArrayList<>();

    public CommentListAdapter(Context context) {
      mContext = context;
    }

    public void addItem(AdvertisementDetail.Comment comment) {
      mData.add(comment);
    }

    public void addAll(List<AdvertisementDetail.Comment> list) {
      mData.addAll(list);
    }

    public void clear() {
      mData.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout
          .adv_detail_comment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      AdvertisementDetail.Comment comment = mData.get(position);
      ImageLoader.loadOptimizedHttpImage(mContext, comment.getAvart()).placeholder(R.mipmap
          .src_avatar_default_drawer).into(holder.userAvatar);
      holder.userName.setText(comment.getUserName());
      holder.time.setText(String.valueOf(comment.getCommentTime()));
      holder.btnPraise.setText(String.valueOf(comment.getHearts()));
      holder.content.setText(comment.getContents());
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.user_avatar)
    CircleImageView userAvatar;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.btn_praise)
    TextView btnPraise;
    @Bind(R.id.content)
    TextView content;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static Intent makeIntent(Context context, int advId) {
    Intent intent = new Intent(context, AdvertisementDetailActivity.class);
    intent.putExtra(BUNDLE_KEY_ADV_ID, advId);
    return intent;
  }
}
