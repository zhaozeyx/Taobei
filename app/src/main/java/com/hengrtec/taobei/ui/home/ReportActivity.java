/*
 * 文件名: ReportActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.BibiReport;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.constant.UserConstant;
import com.hengrtec.taobei.net.rpc.service.params.BibiReportParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import javax.inject.Inject;
import org.apmem.tools.layouts.FlowLayout;

/**
 * 贝贝报告界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/10]
 */
public class ReportActivity extends BasicTitleBarActivity {
  private static final long HOUR = 60 * 60l;
  private static final long MINUTE = 60l;

  /**
   * TODO 前两张图片是不是应该替换成别的，游民，长工
   */
  private static final int[] ICON_LEVEL_MALE = new int[]{R.mipmap.icon_pinnong, R.mipmap
      .icon_pinnong
      , R.mipmap.icon_pinnong, R.mipmap.icon_zhongnong, R.mipmap.icon_funong, R.mipmap.icon_tucaizhu
      , R.mipmap.icon_tuhaozha, R.mipmap.icon_dafuweng};
  private static final int[] ICON_LEVEL_FEMALE = new int[]{R.mipmap.icon_pinnong, R.mipmap
      .icon_pinnong
      , R.mipmap.icon_pinnong, R.mipmap.icon_zhongnong, R.mipmap.icon_funong, R.mipmap.icon_tucaizhu
      , R.mipmap.icon_baifumei, R.mipmap.icon_dafuweng};

  private static final int[][] LEVEL = new int[][]{
      {0, 200}, {201, 1000}, {1001, 2000}, {2001, 10000}, {10001, 40000}, {40001, 1000000},
      {1000001, Integer.MAX_VALUE}
  };

  @Bind(R.id.report_wheel)
  ImageView mReportWheel;
  @Bind(R.id.level_icon)
  TextView mLevelIconText;
  @Bind(R.id.adv_view_count)
  TextView mAdvViewCount;
  @Bind(R.id.total_money_get)
  TextView mTotalMoneyGet;
  @Bind(R.id.total_view_time)
  TextView mTotalViewTime;
  @Bind(R.id.predict_value)
  TextView mPredictValue;
  @Bind(R.id.medal_container)
  FlowLayout mMedalContainer;
  @Bind(R.id.question_list)
  RecyclerView mQuestionList;

  @Inject
  AdvertisementService mAdvService;
  @Bind(R.id.rank_list)
  RecyclerView mRankList;

  private String[] mLevelLabels;
  private int[] mLevelIcon = ICON_LEVEL_MALE;

  @Override
  protected void afterCreate(Bundle savedInstance) {
    ButterKnife.bind(this);
    initLevelRes();
    initQuestionListView();
    initRankListView();
    inject();
    initData();
  }

  private void initLevelRes() {
    if (TextUtils.equals(getComponent().loginSession().getUserInfo().getGender(), UserConstant
        .GENDER_MALE)) {
      mLevelLabels = getResources().getStringArray(R.array.label_report_level_male);
      mLevelIcon = ICON_LEVEL_MALE;
    } else {
      mLevelLabels = getResources().getStringArray(R.array.label_report_level_female);
      mLevelIcon = ICON_LEVEL_FEMALE;
    }
  }

  private void initQuestionListView() {
    mQuestionList.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initRankListView() {
    mRankList.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initData() {
    showProgressDialog("", true);
    manageRpcCall(mAdvService.bibiReport(new BibiReportParams(String.valueOf(getComponent()
        .loginSession().getUserId()))), new UiRpcSubscriber<BibiReport>(this) {


      @Override
      protected void onSuccess(BibiReport bibiReport) {
        bindData(bibiReport);
      }

      @Override
      protected void onEnd() {
        closeProgressDialog();
      }
    });
  }

  private void bindData(BibiReport bibiReport) {
    if (null == bibiReport) {
      return;
    }
    mAdvViewCount.setText(bibiReport.getAdvQuantity() + getString(R.string.unit_adv_view_count));
    mTotalMoneyGet.setText(bibiReport.getTotalBenefit() + getString(R.string.unit_cash));
    mTotalViewTime.setText(getResources().getString(R.string.activity_report_total_view_duration,
        bibiReport.getPlayDuration() / HOUR, (bibiReport.getPlayDuration() % HOUR) / MINUTE));
    mPredictValue.setText(bibiReport.getExpectedBenefit() + getString(R.string.unit_cash));

    setLevel(bibiReport.getTotalBenefit());

    bindDataToQuestionList(bibiReport.getQList());
    bindDataToRankList(bibiReport.getFriendsRanking());

    initRotationAnim(getRotation(bibiReport.getTotalBenefit()));
  }

  private float getRotation(int money) {

    if (money <= 200) {
      return computeRadian(200, money, 4);
    } else if (money <= 1000 & money > 200) {
      return computeRadian(1000, money, 8);
    } else if (money <= 2000 & money > 1000) {
      return computeRadian(2000, money, 10);
    } else if (money <= 10000 & money > 2000) {
      return computeRadian(10000, money, 16);
    } else if (money <= 40000 & money > 10000) {
      return computeRadian(40000, money, 20);
    } else if (money <= 100000 & money > 40000) {
      return computeRadian(100000, money, 30);
    } else if (money > 100000) {
      return computeRadian(100000, money, 8);
    }
    return computeRadian(200, money, 4);
  }

  private float computeRadian(int max, int money, int scaleCount) {
    float perScale = 360.0f / 96.0f;
    return 270 + (money / max) * (scaleCount * perScale) - perScale;
  }

  private void bindDataToQuestionList(List<BibiReport.QListBean> list) {
    QuestionListAdapter adapter = new QuestionListAdapter(list);
    mQuestionList.setAdapter(adapter);
  }

  private void bindDataToRankList(List<BibiReport.FriendsRankingBean> list) {
    RankListAdapter adapter = new RankListAdapter(list);
    mRankList.setAdapter(adapter);
  }

  private void inject() {
    DaggerServiceComponent.builder().serviceModule(new ServiceModule()).globalModule(new
        GlobalModule((CustomApp) getApplication())).build()
        .inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_report;
  }

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_report_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    return true;
  }

  private void initRotationAnim(float toDegrees) {
    RotateAnimation animation = new RotateAnimation(0f, toDegrees, Animation.RELATIVE_TO_SELF,
        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    animation.setDuration(3000);
    animation.setFillAfter(true);
    animation.setInterpolator(new DecelerateInterpolator());
    mReportWheel.setAnimation(animation);
    animation.startNow();
  }

  private void setLevel(int money) {
    int level = computeLevel(money);
    mLevelIconText.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable
            (mLevelIcon[level]),
        null, null);
    mLevelIconText.setText(mLevelLabels[level]);
  }

  private int computeLevel(int money) {
    int level = 0;
    for (int i = 0; i < LEVEL.length; i++) {
      if (money >= LEVEL[i][0] && money <= LEVEL[i][1]) {
        level = i;
      }
    }
    return level;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
  }

  public class ItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.question_name)
    TextView mQuestionName;
    @Bind(R.id.divider)
    View mDivider;
    @Bind(R.id.item_container)
    View mContainer;

    public ItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class QuestionListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private List<BibiReport.QListBean> mData;

    public QuestionListAdapter(List<BibiReport.QListBean> questions) {
      mData = questions;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout
              .report_question_list_item, parent,
          false);
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
      holder.mQuestionName.setText(mData.get(position).getName());
      holder.mDivider.setVisibility(position == mData.size() - 1 ? View.GONE : View.VISIBLE);
      holder.mContainer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // TODO 回答问题界面
          startActivity(ReportSysQuestionActivity.makeReportSysQuestionIntent(ReportActivity
              .this, mData.get(position).getQId(), true));
        }
      });
    }

    @Override
    public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }
  }

  public class RankListAdapter extends RecyclerView.Adapter<RankViewHolder> {
    private static final int HIGH_RANK = 3;
    private List<BibiReport.FriendsRankingBean> mData;

    public RankListAdapter(List<BibiReport.FriendsRankingBean> list) {
      mData = list;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout
          .activity_report_rank_list_item, parent, false);
      return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
      BibiReport.FriendsRankingBean bean = mData.get(position);
      if (position <= HIGH_RANK) {
        holder.mContentContainer.setBackgroundResource(R.drawable.bg_rank_content_dark);
        holder.mRankNumberView.setBackgroundResource(R.mipmap.bg_rank_number_dark);
        holder.mRankNumberView.setTextColor(getResources().getColor(R.color
            .report_rank_color_primary));
        holder.mUserNameView.setTextColor(getResources().getColor(R.color.font_color_primary));
        holder.mBenefitView.setTextColor(getResources().getColor(R.color
            .report_rank_color_primary));
      } else {
        holder.mContentContainer.setBackgroundResource(R.drawable.bg_rank_content_light);
        holder.mRankNumberView.setBackgroundResource(R.mipmap.bg_rank_number_light);
        holder.mRankNumberView.setTextColor(getResources().getColor(R.color
            .report_rank_color_secondary));
        holder.mUserNameView.setTextColor(getResources().getColor(R.color.font_color_dark_gray));
        holder.mBenefitView.setTextColor(getResources().getColor(R.color
            .report_rank_color_secondary));
      }
      holder.mLevelIconView.setImageResource(TextUtils.equals(UserConstant.GENDER_MALE, bean
          .getGender()) ? ICON_LEVEL_MALE[computeLevel(bean.getMoney())]
          : ICON_LEVEL_FEMALE[computeLevel(bean.getMoney())]);
      holder.mRankNumberView.setText(String.valueOf(position + 1));
      holder.mUserNameView.setText(bean.getUserName());
      holder.mBenefitView.setText("￥" + bean.getMoney());
    }

    @Override
    public int getItemCount() {
      return null == mData ? 0 : mData.size();
    }
  }

  public class RankViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.rank_number)
    TextView mRankNumberView;
    @Bind(R.id.level_icon)
    CircleImageView mLevelIconView;
    @Bind(R.id.user_name)
    TextView mUserNameView;
    @Bind(R.id.benefit)
    TextView mBenefitView;
    @Bind(R.id.rank_content_container)
    View mContentContainer;

    public RankViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
