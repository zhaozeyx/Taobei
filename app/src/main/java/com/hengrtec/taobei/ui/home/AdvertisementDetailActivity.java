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
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.hengrtec.taobei.net.rpc.model.AdvPlayInfo;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.AdvOperationParams;
import com.hengrtec.taobei.net.rpc.service.params.AdvPlayParams;
import com.hengrtec.taobei.net.rpc.service.params.DoMyCommentParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementDetailParams;
import com.hengrtec.taobei.net.rpc.service.params.GetBenefitParams;
import com.hengrtec.taobei.net.rpc.service.params.GetCommentListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetUserAdvStateParams;
import com.hengrtec.taobei.net.rpc.service.params.LikeCommentParams;
import com.hengrtec.taobei.net.rpc.service.params.RecordUserPlayDurationParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.basic.scrollview.InterceptScrollView;
import com.hengrtec.taobei.ui.home.event.AwardReceiveClickedEvent;
import com.hengrtec.taobei.ui.home.event.PlayNotCompletedEvent;
import com.hengrtec.taobei.ui.home.event.SubmitQuestionAnswerEvent;
import com.hengrtec.taobei.ui.home.view.AwardViewController;
import com.hengrtec.taobei.ui.login.LoginWayActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.AdvertisementValueBindUtils;
import com.hengrtec.taobei.utils.DateUtils;
import com.hengrtec.taobei.utils.ShareUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.squareup.otto.Subscribe;
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
import rx.subscriptions.CompositeSubscription;

/**
 * 广告详情界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/21]
 */
public class AdvertisementDetailActivity extends BasicTitleBarActivity {
  private static final String TAG = "AdvertisementDetailActivity";
  private static final int MAX_COUNT = 20;
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
  InterceptScrollView mScrollView;
  @Inject
  AdvertisementService mAdvertisementService;
  @Bind(R.id.award_info_container)
  FrameLayout mAwardInfoContainer;
  @Bind(R.id.view_more)
  TextView mLoadMoreView;

  private int mAdvId;
  private AdvertisementDetail mDetail;
  private CommentListAdapter mListAdapter;
  private AdvPlayInfo mAdvPlayInfo;

  private AlertDialog mProfitDialog;

  private int mCurrentPage = 1;
  private int mCurrentOrder = ORDER_TIME;
  private int mServerOrderFlag = mCurrentOrder + 1;
  private boolean mIsLoading = false;
  private boolean mHasMore = false;

  private List<AdvertisementDetail.Comment> mMyCommentsCache = new ArrayList<>();
  private AwardViewController mAwardViewController;
  private CompositeSubscription mSubscriptions = new CompositeSubscription();
  private String mWatchId;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    mAdvId = getIntent().getIntExtra(BUNDLE_KEY_ADV_ID, -1);
    if (-1 == mAdvId) {
      Logger.w(TAG, "mAdvId is null");
      finish();
      return;
    }
    mScrollView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        disabledEditText();
        return false;
      }
    });
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

    mScrollView.setOnOverScrolledListener(new InterceptScrollView.OnOverScrolledListener() {
      @Override
      public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mIsLoading && mHasMore) {
          loadMore();
        }
      }
    });
  }

  private void loadMore() {
    mIsLoading = true;
    manageRpcCall(mAdvertisementService.getCommentList(new GetCommentListParams(mAdvId,
        getComponent().loginSession().getUserId(), mCurrentPage, MAX_COUNT,
        mServerOrderFlag)), new UiRpcSubscriber<List<AdvertisementDetail.Comment>>
        (AdvertisementDetailActivity.this) {

      @Override
      protected void onSuccess(List<AdvertisementDetail.Comment> comments) {
        if (null == comments || comments.size() < MAX_COUNT) {
          mHasMore = false;
          mLoadMoreView.setVisibility(View.GONE);
        } else {
          mHasMore = true;
          mCurrentPage++;
          mLoadMoreView.setVisibility(View.VISIBLE);
        }
        mDetail.getCommentsTimeList().addAll(0, comments);
        mDetail.getCommentsHotList().addAll(mDetail.getCommentsHotList().size() -
            mMyCommentsCache.size(), comments);
        mListAdapter.notifyDataSetChanged();
      }

      @Override
      protected void onEnd() {
        mIsLoading = false;
      }
    });
  }

  @Subscribe
  public void onAwardReceiveBtnClickedEvent(final AwardReceiveClickedEvent event) {
    showProgressDialog("", true);
    refreshUserInfo();
    manageRpcCall(mAdvertisementService.getBenefit(new GetBenefitParams(event.watchId,
        getComponent().loginSession().getUserId(), mAdvId, Integer.parseInt(mDetail
        .getBenefitType()))), new
        UiRpcSubscriber<String>(this) {


          @Override
          protected void onSuccess(String s) {
            showBenefitDialog(mDetail.getBenefitType());
            refreshDetailInfoView(mDetail, Integer.parseInt(mAdvPlayInfo.getBenefitFinal()),
                true, true, event
                    .watchId);
            initData();
          }

          @Override
          protected void onEnd() {
            closeProgressDialog();
          }
        });
  }

  private void showBenefitDialog(String benefitType) {
    switch (benefitType) {
      case AdvertisementConstant.ADV_BENEFIT_TYPE_REALITY_CURRENCY:
        if (getComponent().isLogin()) {
          View childView = LayoutInflater.from(AdvertisementDetailActivity.this).inflate(R.layout
              .dialog_red_bag_get, null);
          mProfitDialog = new AlertDialog.Builder(AdvertisementDetailActivity.this).setView
              (childView).create();
          bindDataProfitDialog(childView);
        } else {
          View childView = LayoutInflater.from(AdvertisementDetailActivity.this).inflate(R.layout
              .dialog_red_bag_get_unlogin, null);
          mProfitDialog = new AlertDialog.Builder(AdvertisementDetailActivity.this).setView
              (childView).create();
          bindDataUnLoginProfitDialog(childView);
        }
        if (null != mProfitDialog) {
          if (mProfitDialog.isShowing()) {
            mProfitDialog.cancel();
          }
          mProfitDialog.show();
        }
        break;
      case AdvertisementConstant.ADV_BENEFIT_TYPE_COUPON:
        if (getComponent().isLogin()) {
          View childView = LayoutInflater.from(AdvertisementDetailActivity.this).inflate(R.layout
              .dialog_coupon_get, null);
          mProfitDialog = new AlertDialog.Builder(AdvertisementDetailActivity.this).setView
              (childView).create();
          bindDataProfitDialog(childView);
        } else {
          View childView = LayoutInflater.from(AdvertisementDetailActivity.this).inflate(R.layout
              .dialog_coupon_get_un_login, null);
          mProfitDialog = new AlertDialog.Builder(AdvertisementDetailActivity.this).setView
              (childView).create();
          bindDataUnLoginProfitDialog(childView);
        }
        if (null != mProfitDialog) {
          if (mProfitDialog.isShowing()) {
            mProfitDialog.cancel();
          }
          mProfitDialog.show();
        }
        break;
    }
  }

  private void initSegmentControl() {
    mSegmentControl.setText(getResources().getStringArray(R.array.adv_detail_order));
    mSegmentControl.setOnSegmentControlClickListener(new SegmentControl
        .OnSegmentControlClickListener() {


      @Override
      public void onSegmentControlClick(int index) {
        List<AdvertisementDetail.Comment> list;
        mCurrentOrder = index;
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
        // 服务器接口 1 是按照时间排序 2 是按照热度排序
        mServerOrderFlag = mCurrentOrder + 1;
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

  }

  private void refreshUI(AdvertisementDetail detail) {
    if (null == detail) {
      return;
    }
    mAwardViewController = new AwardViewController(mAwardInfoContainer);

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

    mBtnCollect.setSelected(mDetail.isCollected());
    mBtnPraise.setSelected(mDetail.isHeartsClicked());

    mListAdapter.clear();
    mListAdapter.addAll(detail.getCommentsTimeList());
    mHasMore = null != detail.getCommentsTimeList() && detail.getCommentsTimeList().size() >=
        MAX_COUNT;
    mLoadMoreView.setVisibility(mHasMore ? View.VISIBLE : View.GONE);
    mListAdapter.notifyDataSetChanged();

    //refreshDetailInfoView(mDetail, null == mAdvPlayInfo ? 0 : Integer.parseInt(mAdvPlayInfo
    //    .getBenefitFinal()), true, true, null);
  }

  private void refreshDetailInfoView(AdvertisementDetail detail, int profitCount, boolean
      viewCompleted, boolean hasGotten, String watchId) {
    mWatchId = watchId;
    mAwardViewController.loadUi(detail, viewCompleted, profitCount, hasGotten, watchId);
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
      .btn_collect, R.id.btn_comment, R.id.btn_send, R.id.edit_disable_text, R.id
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
        performLikeAdvBtnClicked();
        break;
      case R.id.btn_collect:
        performCollectAdvBtnClicked();
        break;
      case R.id.btn_comment:
        mScrollView.scrollTo(0, mSegmentControl.getScrollY());
        break;
      case R.id.btn_send:
        if (TextUtils.isEmpty(mEditText.getEditableText())) {
          return;
        }
        performBtnSendClicked();
        break;
      case R.id.adv_info:
        performAdvInfoClicked();
        break;
    }
  }

  private void performBtnSendClicked() {
    disabledEditText();
    manageRpcCall(mAdvertisementService.doMycomments(new DoMyCommentParams(mAdvId, getComponent()
        .loginSession().getUserId(), mEditText.getText().toString())), new
        UiRpcSubscriber<List<AdvertisementDetail.Comment>>(this) {

          @Override
          protected void onSuccess(List<AdvertisementDetail.Comment> comments) {
            mMyCommentsCache.addAll(comments);
            mDetail.getCommentsHotList().addAll(comments);
            mDetail.getCommentsTimeList().addAll(0, comments);
            mListAdapter.clear();
            if (mCurrentOrder == ORDER_TIME) {
              mListAdapter.addAll(mDetail.getCommentsTimeList());
            } else if (mCurrentOrder == ORDER_HOT) {
              mListAdapter.addAll(mDetail.getCommentsHotList());
            }
            mListAdapter.notifyDataSetChanged();
          }

          @Override
          protected void onEnd() {

          }
        });
  }

  private void performLikeAdvBtnClicked() {
    mDetail.setHeartsClicked(!mDetail.isHeartsClicked());
    mBtnPraise.setSelected(mDetail.isHeartsClicked());
    manageRpcCall(mAdvertisementService.likeAdv(new AdvOperationParams(mDetail.isHeartsClicked()
        ? AdvOperationParams.FLAG_TRUE : AdvOperationParams.FLAG_FALSE
        , String.valueOf(mAdvId), getComponent().loginSession().getUserId())), new
        UiRpcSubscriber<String>(this) {


          @Override
          protected void onSuccess(String s) {

          }

          @Override
          protected void onEnd() {

          }
        });
  }

  private void performCollectAdvBtnClicked() {
    mDetail.setCollected(!mDetail.isCollected());
    mBtnCollect.setSelected(mDetail.isCollected());
    manageRpcCall(mAdvertisementService.collectadv(new AdvOperationParams(mDetail.isCollected()
        ? AdvOperationParams.FLAG_TRUE : AdvOperationParams.FLAG_FALSE, String.valueOf(mAdvId),
        getComponent().loginSession().getUserId())), new UiRpcSubscriber<String>(this) {

      @Override
      protected void onSuccess(String s) {

      }

      @Override
      protected void onEnd() {

      }
    });
  }

  /**
   * 处理广告点击事件
   * 需要先获取广告状态,判断其是否发生改变
   * 如果改变,重新获取详情信息
   * 如果未改变,根据是否播放完毕回答问题进行处理
   */
  private void performAdvInfoClicked() {

    showProgressDialog("", false);
    Observable<AdvPlayInfo> advInfoClickedObservable = mAdvertisementService.getUserAdvState(new
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
    }).flatMap(new Func1<String, Observable<Response<ResponseModel<AdvPlayInfo>>>>() {
      @Override
      public Observable<Response<ResponseModel<AdvPlayInfo>>> call(String s) {
        return mAdvertisementService.advPlay(new AdvPlayParams(String.valueOf(getComponent()
            .loginSession().getUserId()), String.valueOf(mAdvId)));
      }
    }).map(new Func1<Response<ResponseModel<AdvPlayInfo>>, AdvPlayInfo>() {
      @Override
      public AdvPlayInfo call(Response<ResponseModel<AdvPlayInfo>> responseModelResponse) {
        mAdvPlayInfo = responseModelResponse.body().getData();
        mDetail.setBenefitType(mAdvPlayInfo.getBenefitType());
        return mAdvPlayInfo;
      }
    }).filter(new Func1<AdvPlayInfo, Boolean>() {
      @Override
      public Boolean call(AdvPlayInfo advPlayInfo) {
        // 判断用户是否是看完未回答问题
        boolean hasAnswerQuestion = !TextUtils.equals(mDetail.getUserAdvState(),
            AdvertisementConstant.ADV_USER_ADV_STATE_HAS_VIEWED_NOT_COMMIT_QUESTION);
        if (!hasAnswerQuestion) {
          // 如果未完成问题回答，直接跳转到回答问题界面
          startActivity(AdvQuestionListActivity.makeIntent(AdvertisementDetailActivity.this,
              mAdvId, advPlayInfo.getWatchId()));
        }
        return hasAnswerQuestion;
      }
    }).doOnNext(new Action1<AdvPlayInfo>() {
      @Override
      public void call(AdvPlayInfo info) {
        // 如果已完成问题回答，跳转到播放界面
        Intent intent = AdvertisementPlayActivity.makeIntent(AdvertisementDetailActivity.this,
            mDetail.getAdvId()
            , mDetail.getAdvType(), mDetail.getFilePath(), mDetail.getBenefitType(), info
                .getBenefitFinal(), info.getWatchId());
        closeProgressDialog();
        startActivityForResult(intent, REQUEST_CODE_PLAY);
      }
    });

    manageRpcCall(advInfoClickedObservable, new UiRpcSubscriber1<AdvPlayInfo>(this) {
      @Override
      protected void onSuccess(AdvPlayInfo info) {

      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
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
        if (RESULT_OK == resultCode) {
          if (null != data) {
            dispatchPlayFinish(data.getStringExtra(AdvertisementPlayActivity.RESULT_KEY_WATCH_ID));
          }
        }
        break;
    }
  }


  @Subscribe
  public void performAdvQustionSubmitEvent(SubmitQuestionAnswerEvent event) {
    refreshUserInfo();
    showBenefitDialog(mDetail.getBenefitType());
    initData();
    refreshDetailInfoView(mDetail, Integer.parseInt(mAdvPlayInfo.getBenefitFinal()), true, true,
        event.watchId);
  }

  private void dispatchPlayFinish(String watchId) {
    refreshDetailInfoView(mDetail, Integer.parseInt(mAdvPlayInfo.getBenefitFinal()), true,
        false, watchId);
  }

  private void refreshUserInfo() {
    mSubscriptions.add(getComponent().loginSession().loadUserInfo());
  }

  private void bindDataProfitDialog(@NonNull View contentView) {
    ImageLoader.loadOptimizedHttpImage(this, getComponent().loginSession().getUserInfo().getAvart
        ()).into((ImageView) contentView.findViewById(R.id.user_avatar));
    contentView.findViewById(R.id.btn_share_moments).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO 分享到朋友圈 根据类型不同分享内容不同？
        ShareUtils.showShareWechatMoments(AdvertisementDetailActivity.this,"","");
      }
    });
    contentView.findViewById(R.id.btn_share_friends).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO 分享到微信 根据类型不同分享内容不同？
        ShareUtils.showShareWechat(AdvertisementDetailActivity.this,"","");
      }
    });
    contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mProfitDialog) {
          mProfitDialog.cancel();
        }
      }
    });
    if (null != mAdvPlayInfo) {
      ((TextView) contentView.findViewById(R.id.profit_number)).setText(mAdvPlayInfo
          .getBenefitFinal());
    }
    TextView congratulationsView = ((TextView) contentView.findViewById(R.id
        .dialog_congratulations));
    congratulationsView.setText(getString(R.string.adv_detail_dialog_congratulations,
        getComponent().loginSession().getUserInfo().getUserName()));
  }

  private void bindDataUnLoginProfitDialog(@NonNull View contentView) {
    ImageLoader.loadOptimizedHttpImage(this, getComponent().loginSession().getUserInfo().getAvart
        ()).into((ImageView) contentView.findViewById(R.id.user_avatar));
    contentView.findViewById(R.id.btn_login_qq).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO QQ登录
        startActivity(new Intent(AdvertisementDetailActivity.this, LoginWayActivity.class));
      }
    });
    contentView.findViewById(R.id.btn_login_web_chat).setOnClickListener(new View.OnClickListener
        () {
      @Override
      public void onClick(View v) {
        // TODO 微信登录？
        startActivity(new Intent(AdvertisementDetailActivity.this, LoginWayActivity.class));
      }
    });
    contentView.findViewById(R.id.btn_login_phone).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO 手机登录
        startActivity(new Intent(AdvertisementDetailActivity.this, LoginWayActivity.class));
      }
    });
    if (null != mAdvPlayInfo) {
      ((TextView) contentView.findViewById(R.id.profit_number)).setText(mAdvPlayInfo
          .getBenefitFinal());
    }
    contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mProfitDialog) {
          mProfitDialog.cancel();
        }
      }
    });
    TextView congratulationsView = ((TextView) contentView.findViewById(R.id
        .dialog_congratulations));
    congratulationsView.setText(getString(R.string.adv_detail_dialog_congratulations,
        getComponent().loginSession().getUserInfo().getUserName()));
  }


  @Subscribe
  public void performPlayNotCompletedEvent(PlayNotCompletedEvent event) {
    manageRpcCall(mAdvertisementService.recordUserPlayDuration(new RecordUserPlayDurationParams
        (getComponent().loginSession().getUserId(), mAdvId, event.playTime)), new
        UiRpcSubscriber<String>(this) {


          @Override
          protected void onSuccess(String s) {
          }

          @Override
          protected void onEnd() {

          }
        });
  }

  class CommentListAdapter extends RecyclerView.Adapter<ViewHolder> {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
      final AdvertisementDetail.Comment comment = mData.get(position);
      ImageLoader.loadOptimizedHttpImage(mContext, comment.getAvart()).placeholder(R.mipmap
          .src_avatar_default_drawer).into(holder.userAvatar);
      holder.userName.setText(comment.getUserName());
      holder.time.setText(DateUtils.getDisplayTime(comment.getCommentTime(), mContext));
      holder.btnPraise.setText(String.valueOf(comment.getHearts()));
      holder.btnPraise.setSelected(comment.isClickHearts());
      holder.btnPraise.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (comment.isClickHearts()) {
            return;
          }
          comment.setClickHearts(true);
          holder.btnPraise.setSelected(comment.isClickHearts());
          holder.btnPraise.setText(String.valueOf(comment.getHearts() + 1));
          likeComment(comment.getCommentId());
        }
      });
      holder.content.setText(comment.getContents());
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    private void likeComment(String commentId) {
      manageRpcCall(mAdvertisementService.likeComments(new LikeCommentParams(commentId,
          getComponent().loginSession().getUserId())), new UiRpcSubscriber<String>(mContext) {


        @Override
        protected void onSuccess(String s) {
        }

        @Override
        protected void onEnd() {

        }
      });
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
