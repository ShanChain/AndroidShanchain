package com.shanchain.shandata.mvp.view.activity.challenge;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class NotPartInSleepEarlierActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarNotPartSleepEarlier;
    @Bind(R.id.tv_not_part_sleep_counts)
    TextView mTvNotPartSleepCounts;
    @Bind(R.id.tv_not_part_sleep_rules)
    TextView mTvNotPartSleepRules;
    @Bind(R.id.iv_not_part_sleep_img)
    ImageView mIvNotPartSleepImg;
    @Bind(R.id.tv_not_part_sleep_name1)
    TextView mTvNotPartSleepName1;
    @Bind(R.id.tv_not_part_sleep_name2)
    TextView mTvNotPartSleepName2;
    @Bind(R.id.tv_not_part_sleep_time)
    TextView mTvNotPartSleepTime;
    @Bind(R.id.tv_not_part_sleep_info)
    TextView mTvNotPartSleepInfo;
    @Bind(R.id.csb_not_part_sleep)
    CustomSeekBar mCsbNotPartSleep;
    @Bind(R.id.tv_not_part_sleep_pay)
    TextView mTvNotPartSleepPay;
    @Bind(R.id.btn_not_part_sleep_in)
    Button mBtnNotPartSleepIn;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_not_part_in_sleep_earlier;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarNotPartSleepEarlier = (ArthurToolBar) findViewById(R.id.toolbar_not_part_sleep_earlier);
        mToolbarNotPartSleepEarlier.setBtnEnabled(true,false);
        mToolbarNotPartSleepEarlier.setBtnVisibility(true,false);
        mToolbarNotPartSleepEarlier.setOnLeftClickListener(this);
    }


    @OnClick({R.id.tv_not_part_sleep_rules, R.id.btn_not_part_sleep_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_not_part_sleep_rules:
                CustomDialog customDialog = new CustomDialog(this,R.layout.dialog_sleep_rules,null);
                customDialog.show();
                break;
            case R.id.btn_not_part_sleep_in:
                readyGo(HasPartInSleepEarlierActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();

    }
}
