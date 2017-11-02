package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class ReportActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    @Bind(R.id.tb_report)
    ArthurToolBar mTbReport;
    @Bind(R.id.rb_pornographic)
    RadioButton mRbPornographic;
    @Bind(R.id.rb_harmful_info)
    RadioButton mRbHarmfulInfo;
    @Bind(R.id.rb_sham_content)
    RadioButton mRbShamContent;
    @Bind(R.id.rb_personal_attack)
    RadioButton mRbPersonalAttack;
    @Bind(R.id.rb_break_law)
    RadioButton mRbBreakLaw;
    @Bind(R.id.rb_other)
    RadioButton mRbOther;
    @Bind(R.id.activity_report)
    LinearLayout mActivityReport;
    @Bind(R.id.et_report_content)
    EditText mEtReportContent;
    private boolean isCommit = false;
    private String mStoryId;
    private int mCharacterId;
    private int mPosition;
    private String[] mReportList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_report;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mStoryId = intent.getStringExtra("storyId");
        mCharacterId = intent.getIntExtra("characterId", 0);
        mReportList = getResources().getStringArray(R.array.reportList);
        initToolBar();
    }

    private void initToolBar() {
        mTbReport.setOnLeftClickListener(this);
        mTbReport.setOnRightClickListener(this);
    }

    @OnClick({R.id.rb_pornographic, R.id.rb_harmful_info, R.id.rb_sham_content, R.id.rb_personal_attack, R.id.rb_break_law, R.id.rb_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_pornographic:
                setRadioButtonChecked(0);
                break;
            case R.id.rb_harmful_info:
                setRadioButtonChecked(1);
                break;
            case R.id.rb_sham_content:
                setRadioButtonChecked(2);
                break;
            case R.id.rb_personal_attack:
                setRadioButtonChecked(3);
                break;
            case R.id.rb_break_law:
                setRadioButtonChecked(4);
                break;
            case R.id.rb_other:
                setRadioButtonChecked(5);
                break;
        }
    }

    boolean[] isChecked = {false, false, false, false, false, false};

    private void setRadioButtonChecked(int position) {
        for (int i = 0; i < isChecked.length; i++) {
            if (i == position) {
                isChecked[i] = true;
            } else {
                isChecked[i] = false;
            }
        }
        mRbPornographic.setChecked(isChecked[0]);
        mRbHarmfulInfo.setChecked(isChecked[1]);
        mRbShamContent.setChecked(isChecked[2]);
        mRbPersonalAttack.setChecked(isChecked[3]);
        mRbBreakLaw.setChecked(isChecked[4]);
        mRbOther.setChecked(isChecked[5]);
        isCommit = true;
        mPosition = position;
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {


        if (isCommit) {
            String content = mEtReportContent.getText().toString().trim();
            //提交举报信息
            showLoadingDialog();
            final String reason = "举报类型:" + mReportList[mPosition] +";举报内容:"+content;
            SCHttpUtils.post()
                    .url(HttpApi.STORY_REPORT)
                    .addParams("storyId",mStoryId.substring(1))
                    .addParams("characterId",mCharacterId + "")
                    .addParams("reason",reason)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            closeLoadingDialog();
                            LogUtils.i("举报失败！");
                            ToastUtils.showToast(mContext,"举报失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.i("举报成功" + response);
                            closeLoadingDialog();
                            ToastUtils.showToast(mContext,"感谢你的举报，我们会尽快处理~");
                            finish();
                        }
                    });

        }else {
            ToastUtils.showToast(this,"请选择举报类型！");
        }
    }

}
