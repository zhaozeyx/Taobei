/*
 * 文件名: AdvQuestionListActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/5
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.RpcApiError;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.AdvertisementDetail;
import com.hengrtec.taobei.net.rpc.model.Question;
import com.hengrtec.taobei.net.rpc.model.ResponseModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.AdvertisementConstant;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvQuestionListParams;
import com.hengrtec.taobei.net.rpc.service.params.GetAdvertisementDetailParams;
import com.hengrtec.taobei.net.rpc.service.params.SubAdvQuestionAnswerParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.home.event.SubmitQuestionAnswerEvent;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import com.hengrtec.taobei.utils.AdvertisementValueBindUtils;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 答题界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/5]
 */
public class AdvQuestionListActivity extends BasicTitleBarActivity {
  private static final String BUNDLE_KEY_ADV_ID = "adv_id";
  private static final String BUNDLE_KEY_WATCH_ID = "watch_id";
  @Bind(R.id.adv_snapshot)
  ImageView mAdvSnapshot;
  @Bind(R.id.no_play_time)
  ViewStub mNoPlayTime;
  @Bind(R.id.layout_can_play)
  ImageView mLayoutCanPlay;
  @Bind(R.id.play_count_info)
  TextView mPlayCountInfo;
  @Bind(R.id.adv_info)
  RelativeLayout mAdvInfo;
  @Bind(R.id.adv_title)
  TextView mAdvTitle;
  @Bind(R.id.adv_sub_title)
  TextView mAdvSubTitle;
  @Bind(R.id.adv_profit)
  TextView mAdvProfit;
  @Bind(R.id.question_list)
  ExpandableListView mQuestionListView;
  @Bind(R.id.btn_commit)
  TextView mBtnCommit;

  @Inject
  AdvertisementService mAdvService;

  private int mAdvId;
  private String mWatchId;
  private QuestionListAdapter mListAdapter;

  private Subscription mCheckBtnStateSubscription;


  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    mAdvId = getIntent().getIntExtra(BUNDLE_KEY_ADV_ID, -1);
    mWatchId = getIntent().getStringExtra(BUNDLE_KEY_WATCH_ID);
    inject();
    initListView();
    initData();
  }

  private void inject() {
    DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
        .serviceModule(new ServiceModule()).build().inject(this);
  }

  private void initData() {
    showProgressDialog("", true);
    Observable<Response<ResponseModel<List<Question>>>> listQuestionObservable =
        mAdvService.getAdvertisement(new GetAdvertisementDetailParams
            (getComponent().appPreferences().getUserId(),
                mAdvId)).observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Action1<Response<ResponseModel<AdvertisementDetail>>>() {
              @Override
              public void call(Response<ResponseModel<AdvertisementDetail>> responseModelResponse) {
                refreshUI(responseModelResponse.body().getData());
              }
            }).observeOn(Schedulers.newThread())
            .flatMap(new Func1<Response<ResponseModel<AdvertisementDetail>>,
                Observable<Response<ResponseModel<List<Question>>>>>() {
              @Override
              public Observable<Response<ResponseModel<List<Question>>>> call
                  (Response<ResponseModel<AdvertisementDetail>>
                       responseModelResponse) {
                return mAdvService.getAdvQuestionList(new GetAdvQuestionListParams(String.valueOf
                    (getComponent().loginSession().getUserId()), String.valueOf(mAdvId)));
              }
            });
    manageRpcCall(listQuestionObservable, new UiRpcSubscriber<List<Question>>(this) {
      @Override
      protected void onSuccess(List<Question> questions) {
        mListAdapter.addAll(questions);
        mListAdapter.notifyDataSetChanged();
        for (int i = 0; i < questions.size(); i++) {
          mQuestionListView.expandGroup(i);
        }
        setListViewHeightBasedOnChildren(mQuestionListView);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  private void initListView() {
    mQuestionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
      }
    });
    mListAdapter = new QuestionListAdapter(this, null);
    View footerView = LayoutInflater.from(this).inflate(R.layout.list_space,
        mQuestionListView, false);
    mQuestionListView.addFooterView(footerView);
    mQuestionListView.setAdapter(mListAdapter);
    setListViewHeightBasedOnChildren(mQuestionListView);
  }

  private void setListViewHeightBasedOnChildren(ExpandableListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }

  private void refreshUI(AdvertisementDetail detail) {
    if (null == detail) {
      return;
    }
    updateCommitStatus();
    ImageLoader.loadOptimizedHttpImage(AdvQuestionListActivity.this, detail.getThumbnail())
        .into(mAdvSnapshot);
    mAdvTitle.setText(detail.getName());
    mAdvSubTitle.setText(detail.getSubTitle());

    mPlayCountInfo.setText(getResources().getString(R.string.adv_detail_play_number_info, detail
        .getPlayCounts(), detail.getPlanCounts()));
    if (0 == detail.getPlayCounts()) {
      mLayoutCanPlay.setVisibility(View.GONE);
      mNoPlayTime.setVisibility(View.VISIBLE);
    } else {
      mLayoutCanPlay.setVisibility(View.VISIBLE);
      mNoPlayTime.setVisibility(View.GONE);
    }
    mPlayCountInfo.setText(getResources().getString(R.string.adv_detail_play_number_info, detail
        .getPlayCounts(), detail.getPlanCounts()));
    setProfitInfoByStatus(detail);
  }

  private void setProfitInfoByStatus(AdvertisementDetail detail) {
    String benefitType = detail.getBenefitType();
    AdvertisementValueBindUtils.setProfit(mAdvProfit, benefitType, detail.getPrice(), R.string
        .adv_detail_profit_info_real, R.string.adv_detail_profit_info_virtual);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_adv_question_list;
  }

  @Override
  public boolean initializeTitleBar() {
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    return super.initializeTitleBar();
  }

  @OnClick(R.id.btn_commit)
  public void onClick() {
    showProgressDialog("", true);
    HashMap<String, String> qMap = new HashMap<>();
    for (Question question : mListAdapter.getData()) {
      qMap.put(question.getQId(), question.getMyAnswer().toString());
    }
    manageRpcCall(mAdvService.subAnswer(new SubAdvQuestionAnswerParams(mWatchId, String.valueOf
        (getComponent().loginSession().getUserId()), String.valueOf(mAdvId), qMap)), new
        UiRpcSubscriber<String>(this) {
          @Override
          protected void onSuccess(String s) {
            getComponent().getGlobalBus().post(new SubmitQuestionAnswerEvent(mWatchId));
            finish();
          }

          @Override
          protected void onEnd() {
            closeProgressDialog();
          }

          @Override
          public void onApiError(RpcApiError apiError) {
            super.onApiError(apiError);
            showShortToast(R.string.activity_adv_toast_submit_failure);
          }
        });
  }

  private void updateCommitStatus() {
    mCheckBtnStateSubscription = Observable.just(mListAdapter.getData()).subscribeOn(Schedulers
        .computation()).observeOn
        (Schedulers.newThread())
        .map(new Func1<List<Question>, Boolean>() {
          @Override
          public Boolean call(List<Question> questions) {
            if (null == questions || questions.size() == 0) {
              return false;
            }
            boolean allCorrect = true;
            for (Question question : questions) {
              Object myAnswer = question.getMyAnswer();
              if (null == myAnswer || TextUtils.isEmpty(myAnswer.toString())) {
                allCorrect = false;
                break;
              }
              if (TextUtils.equals(question.getType(), AdvertisementConstant
                  .ADV_QUESTION_TYPE_CHOICE_NO_ANSWER)
                  || TextUtils.equals(question.getType(), AdvertisementConstant
                  .ADV_QUESTION_TYPE_MULTY_CHOICE)) {
                continue;
              }
              if (myAnswer instanceof Integer) {
                int intAnswer = (int) myAnswer;
                if (!TextUtils.equals(String.valueOf(intAnswer), question.getAnswer())) {
                  allCorrect = false;
                  break;
                }
              } else if (myAnswer instanceof String) {
                if (TextUtils.equals(myAnswer.toString(), question
                    .getAnswer())) {
                  allCorrect = false;
                  break;
                }
              }
            }
            return allCorrect;
          }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Action1<Boolean>() {
          @Override
          public void call(Boolean aBoolean) {
            mBtnCommit.setEnabled(aBoolean);
          }
        }).subscribe();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != mCheckBtnStateSubscription && mCheckBtnStateSubscription.isUnsubscribed()) {
      mCheckBtnStateSubscription.unsubscribe();
    }
  }

  public static Intent makeIntent(Context context, int advId, String watchId) {
    Intent intent = new Intent(context, AdvQuestionListActivity.class);
    intent.putExtra(BUNDLE_KEY_ADV_ID, advId);
    intent.putExtra(BUNDLE_KEY_WATCH_ID, watchId);
    return intent;
  }

  private class QuestionListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "QuestionListAdapter";
    private List<Question> mQuestionList = new ArrayList<>();
    private Context mContext;
    /**
     * 问题答案的缓存 key是问题索引号 value 是答案 可能是选项，可能是字符串
     */
    private HashMap<Integer, Object> mAnswerCache = new HashMap<>();

    public QuestionListAdapter(Context context, List<Question> questionList) {
      addAll(questionList);
      mContext = context;
    }

    public void addAll(List<Question> questionList) {
      if (null != questionList) {
        mQuestionList.addAll(questionList);
      }
    }

    public List<Question> getData() {
      return mQuestionList;
    }

    @Override
    public int getGroupCount() {
      return null == mQuestionList ? 0 : mQuestionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      if (TextUtils.equals(mQuestionList.get(groupPosition).getType(), AdvertisementConstant
          .ADV_QUESTION_TYPE_EASY_QUESTION)) {
        return 1;
      }
      return mQuestionList.get(groupPosition).getQItemList().size();
    }

    @Override
    public Question getGroup(int groupPosition) {
      return mQuestionList.get(groupPosition);
    }

    @Override
    public Question.QuestionOptions getChild(int groupPosition, int childPosition) {
      return mQuestionList.get(groupPosition).getQItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public boolean hasStableIds() {
      return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
        parent) {
      QuestionTitleHolder holder;
      if (null == convertView) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout
            .activity_adv_question_list_item_question_title, parent, false);
        holder = new QuestionTitleHolder(convertView);
        convertView.setTag(holder);
      }
      holder = (QuestionTitleHolder) convertView.getTag();
      Question question = mQuestionList.get(groupPosition);
      holder.questionText.setText(question.getName());
      return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
        convertView, ViewGroup parent) {
      int type = getChildType(groupPosition, childPosition);
      if (type == ChildType.CHOICE_QUESTION.ordinal()) {
        // 加载选择题布局
        return loadChoiceView(groupPosition, childPosition, isLastChild, convertView, parent);
      } else if (type == ChildType.EASY_QUESTION.ordinal()) {
        // 加载问答题布局
        Logger.d(TAG, "load easy question ");
        return loadEasyQuestionView(groupPosition, childPosition, isLastChild, convertView, parent);
      }
      return null;
    }

    private View loadChoiceView(int groupPosition, final int childPosition, boolean isLastChild,
                                View convertView, ViewGroup parent) {
      QuestionOptionHolder holder;
      if (null == convertView) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout
            .activity_adv_question_list_item_question_child_option, parent, false);
        holder = new QuestionOptionHolder(convertView);
        convertView.setTag(holder);
      }
      holder = (QuestionOptionHolder) convertView.getTag();
      final ImageView stateView = holder.answerState;
      final View containerView = holder.containerView;
      final Question question = getGroup(groupPosition);
      final Question.QuestionOptions options = getChild(groupPosition, childPosition);
      holder.containerView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Object myAnswer = question.getMyAnswer();
          if (null != myAnswer && (myAnswer.toString().contains(String.valueOf(childPosition)) ||
              myAnswer.toString().contains(childPosition + ","))) {
            question.setMyAnswer(myAnswer.toString().replace(childPosition + ",", "").replace
                (String.valueOf(childPosition), ""));
          } else {
            if (TextUtils.equals(AdvertisementConstant.ADV_QUESTION_TYPE_MULTY_CHOICE, question
                .getType())) {
              // 如果是多选题，拼接类型为1，2，3，
              question.setMyAnswer(null != myAnswer && myAnswer
                  instanceof String ? (question.getMyAnswer().toString() + childPosition + ",")
                  : childPosition + ",");
            } else {
              question.setMyAnswer(childPosition);
            }
          }
          setViewStateByAnswer(question, stateView, containerView, childPosition);
          updateCommitStatus();
          notifyDataSetChanged();
        }
      });

      setViewStateByAnswer(question, stateView, containerView, childPosition);
      holder.questionOptionText.setText(options.getItem());

      return convertView;
    }

    private View loadEasyQuestionView(int groupPosition, final int childPosition, boolean
        isLastChild, View convertView, ViewGroup parent) {
      QuestionInputHolder holder;
      if (null == convertView) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout
            .activity_adv_question_list_item_question_child_input, parent, false);
        holder = new QuestionInputHolder(convertView);
        convertView.setTag(holder);
      }
      final Question question = getGroup(groupPosition);
      holder = (QuestionInputHolder) convertView.getTag();
      holder.questionInput.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
          question.setMyAnswer(s.toString());
          updateCommitStatus();
        }
      });
      holder.questionInput.setText(null == question.getMyAnswer() ? "" : (String) question
          .getMyAnswer());
      return convertView;
    }

    private void setViewStateByAnswer(Question question, ImageView stateView, View containerView,
                                      int childPosition) {

      Object myAnswer = question.getMyAnswer();
      boolean isAnswerCorrect = false;
      boolean hasCorrectQuestion = TextUtils.equals(AdvertisementConstant
              .ADV_QUESTION_TYPE_CHOICE_WITH_ANSWER
          , question.getType());
      if (null != myAnswer && !TextUtils.isEmpty(myAnswer.toString())) {
        if (myAnswer instanceof Integer) {
          int intAnswer = (int) myAnswer;
          if (intAnswer != childPosition) {
            stateView.setVisibility(View.GONE);
            containerView.setBackground(null);
            return;
          }
          isAnswerCorrect = TextUtils.equals(String.valueOf(intAnswer), question
              .getAnswer());

        } else if (myAnswer instanceof String) {
          if (TextUtils.equals(AdvertisementConstant.ADV_QUESTION_TYPE_MULTY_CHOICE, question
              .getType())) {
            if (!question.getMyAnswer().toString().contains(childPosition + ",")) {
              stateView.setVisibility(View.GONE);
              containerView.setBackground(null);
              return;
            }
          }
          isAnswerCorrect = TextUtils.equals(myAnswer.toString(), question
              .getAnswer());
        }
        // 如果没有正确答案 或者答案是正确的
        if (!hasCorrectQuestion || isAnswerCorrect) {
          stateView.setVisibility(View.VISIBLE);
          stateView.setImageResource(R.drawable.icon_answer_correct);
          containerView.setBackground(null);
        } else {
          stateView.setVisibility(View.VISIBLE);
          stateView.setImageResource(R.drawable.icon_answer_wrong);
          containerView.setBackgroundResource(R.drawable.bg_adv_question_answer_wrong);
        }
      } else {
        stateView.setVisibility(View.GONE);
        containerView.setBackground(null);
      }
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
      Question question = mQuestionList.get(groupPosition);
      Logger.d(TAG, "question type groupPosition is %d values is %s ", groupPosition, question
          .getType());
      switch (question.getType()) {
        case AdvertisementConstant.ADV_QUESTION_TYPE_CHOICE_NO_ANSWER:
        case AdvertisementConstant.ADV_QUESTION_TYPE_CHOICE_WITH_ANSWER:
        case AdvertisementConstant.ADV_QUESTION_TYPE_MULTY_CHOICE:
          return ChildType.CHOICE_QUESTION.ordinal();
        case AdvertisementConstant.ADV_QUESTION_TYPE_EASY_QUESTION:
          return ChildType.EASY_QUESTION.ordinal();
        default:
          Logger.d(TAG, "Unknown Question Type");
          break;
      }
      return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getChildTypeCount() {
      return ChildType.values().length;
    }
  }

  static class QuestionTitleHolder {
    @Bind(R.id.question_title)
    TextView questionText;

    QuestionTitleHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class QuestionOptionHolder {
    @Bind(R.id.question_option)
    TextView questionOptionText;
    @Bind(R.id.answer_states)
    ImageView answerState;
    @Bind(R.id.container)
    View containerView;

    QuestionOptionHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class QuestionInputHolder {
    @Bind(R.id.question_input)
    EditText questionInput;

    QuestionInputHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  private enum ChildType {
    /**
     * 选择题
     */
    CHOICE_QUESTION,

    /**
     * 问答题
     */
    EASY_QUESTION
  }
}
