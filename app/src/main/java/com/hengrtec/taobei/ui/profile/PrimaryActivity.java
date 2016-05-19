package com.hengrtec.taobei.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hengrtec.taobei.CustomApp;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.injection.GlobalModule;
import com.hengrtec.taobei.net.UiRpcSubscriber;
import com.hengrtec.taobei.net.rpc.model.CollectAdvModel;
import com.hengrtec.taobei.net.rpc.service.AdvertisementService;
import com.hengrtec.taobei.net.rpc.service.params.GetSettingsParams;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.ui.serviceinjection.DaggerServiceComponent;
import com.hengrtec.taobei.ui.serviceinjection.ServiceModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiao on 2016/5/16.
 */
public class PrimaryActivity extends BasicTitleBarActivity {
    private static final int PAGE_SIZE = 20;
    private String questionone = "";
    private String questiontwo = "";
    @Inject
    AdvertisementService mAdvService;
    @Bind(R.id.question_one_1)
    RelativeLayout questionOne1;
    @Bind(R.id.question_one_2)
    RelativeLayout questionOne2;
    @Bind(R.id.question_one_3)
    RelativeLayout questionOne3;
    @Bind(R.id.question_two_1)
    RelativeLayout questionTwo1;
    @Bind(R.id.question_two_2)
    RelativeLayout questionTwo2;
    @Bind(R.id.question_two_3)
    RelativeLayout questionTwo3;
    @Bind(R.id.et_question)
    EditText etQuestion;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.bt_exit)
    Button btExit;
    private int mCurrentPage = 1;

    @Override
    protected void afterCreate(Bundle savedInstance) {
        ButterKnife.bind(this);
        inject();
        initView();
        loadData(true);
    }

    private void inject() {
        DaggerServiceComponent.builder().globalModule(new GlobalModule((CustomApp) getApplication()))
                .serviceModule(new ServiceModule()).build().inject(PrimaryActivity.this);
    }

    private void loadData(boolean showProgress) {


    }


    private void initView() {


    }

    @Override
    public boolean initializeTitleBar() {
        setMiddleTitle(R.string.activity_settings_title);
        setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_primary;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.question_one_1, R.id.question_one_2, R.id.question_one_3, R.id.question_two_1, R.id.question_two_2, R.id.question_two_3, R.id.bt_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_one_1:
                questionOne1.setSelected(true);
                questionOne2.setSelected(false);
                questionOne3.setSelected(false);
                questionone = "1";
                break;
            case R.id.question_one_2:
                questionOne1.setSelected(false);
                questionOne2.setSelected(true);
                questionOne3.setSelected(false);
                questionone = "2";
                break;
            case R.id.question_one_3:
                questionOne1.setSelected(false);
                questionOne2.setSelected(false);
                questionOne3.setSelected(true);
                questionone = "3";
                break;
            case R.id.question_two_1:
                questionTwo1.setSelected(true);
                questionTwo2.setSelected(false);
                questionTwo3.setSelected(false);
                questiontwo = "1";
                break;
            case R.id.question_two_2:
                questionTwo1.setSelected(false);
                questionTwo2.setSelected(true);
                questionTwo3.setSelected(false);
                questiontwo = "2";
                break;
            case R.id.question_two_3:
                questionTwo1.setSelected(false);
                questionTwo2.setSelected(false);
                questionTwo3.setSelected(true);
                questiontwo = "3";
                break;
            case R.id.bt_exit:

                manageRpcCall(mAdvService.getAdvSettingsList(new GetSettingsParams(getComponent()
                        .loginSession().getUserId(), questionone, questiontwo, etQuestion.getText().toString(), etPhone.getText().toString())), new
                        UiRpcSubscriber<List<CollectAdvModel>>(this) {
                            @Override
                            protected void onSuccess(List<CollectAdvModel> advertisements) {
                                finish();
                                showShortToast(R.string.activity_settings_primary_success);
                            }

                            @Override
                            protected void onEnd() {


                            }
                        });
                break;
        }
    }
}
