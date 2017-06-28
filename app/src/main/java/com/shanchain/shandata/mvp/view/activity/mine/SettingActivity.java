package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarSetting;
    @Bind(R.id.ll_setting_msg)
    RelativeLayout mLlSettingMsg;
    @Bind(R.id.ll_setting_account)
    RelativeLayout mLlSettingAccount;
    @Bind(R.id.ll_setting_shielding_person)
    RelativeLayout mLlSettingShieldingPerson;
    @Bind(R.id.ll_setting_clear_cache)
    RelativeLayout mLlSettingClearCache;
    @Bind(R.id.ll_setting_feedback)
    RelativeLayout mLlSettingFeedback;
    @Bind(R.id.ll_setting_about)
    RelativeLayout mLlSettingAbout;
    @Bind(R.id.ll_setting_nice_praise)
    RelativeLayout mLlSettingNicePraise;
    @Bind(R.id.btn_setting_quit)
    Button mBtnSettingQuit;
    @Bind(R.id.activity_setting)
    LinearLayout mActivitySetting;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarSetting = (ArthurToolBar) findViewById(R.id.toolbar_setting);
        mToolbarSetting.setBtnVisibility(true,false);
        mToolbarSetting.setBtnEnabled(true,false);
        mToolbarSetting.setOnLeftClickListener(this);
    }


    @OnClick({R.id.ll_setting_msg, R.id.ll_setting_account, R.id.ll_setting_shielding_person, R.id.ll_setting_clear_cache, R.id.ll_setting_feedback, R.id.ll_setting_about, R.id.ll_setting_nice_praise, R.id.btn_setting_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_msg:
                readyGo(MessageSettingActivity.class);
                break;
            case R.id.ll_setting_account:
                break;
            case R.id.ll_setting_shielding_person:
                break;
            case R.id.ll_setting_clear_cache:
                break;
            case R.id.ll_setting_feedback:
                break;
            case R.id.ll_setting_about:
                break;
            case R.id.ll_setting_nice_praise:
                break;
            case R.id.btn_setting_quit:
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
