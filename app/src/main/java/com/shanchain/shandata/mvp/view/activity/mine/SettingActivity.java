package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.DataCleanManager;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;

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
    @Bind(R.id.btn_setting_quit)
    Button mBtnSettingQuit;
    @Bind(R.id.activity_setting)
    LinearLayout mActivitySetting;
    @Bind(R.id.tv_setting_cache)
    TextView mTvSettingCache;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        try {
            File externalCacheDir = getExternalCacheDir();
            LogUtils.d("外部缓存目录:"+externalCacheDir.getAbsolutePath());
            File cacheDir = getCacheDir();
            LogUtils.d("本地缓存目录:" + cacheDir.getAbsolutePath());
            String cacheSize = DataCleanManager.getCacheSize(cacheDir);
            mTvSettingCache.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolBar() {
        mToolbarSetting = (ArthurToolBar) findViewById(R.id.toolbar_setting);
        mToolbarSetting.setBtnVisibility(true, false);
        mToolbarSetting.setBtnEnabled(true, false);
        mToolbarSetting.setOnLeftClickListener(this);
    }

    @OnClick({R.id.ll_setting_msg, R.id.ll_setting_account, R.id.ll_setting_shielding_person, R.id.ll_setting_clear_cache, R.id.ll_setting_feedback, R.id.ll_setting_about, R.id.btn_setting_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_msg:
                readyGo(MessageSettingActivity.class);
                break;
            case R.id.ll_setting_account:
                readyGo(AccountAndSafeActivity.class);
                break;
            case R.id.ll_setting_shielding_person:
                readyGo(ShieldActivity.class);
                break;
            case R.id.ll_setting_clear_cache:
                DataCleanManager.deleteFolderFile(getCacheDir().getAbsolutePath(),true);
                initData();
                break;
            case R.id.ll_setting_feedback:
                readyGo(FeedbackActivity.class);
                break;
            case R.id.ll_setting_about:
                readyGo(AboutActivity.class);
                break;
            case R.id.btn_setting_quit:
                loginOut();
                break;
        }
    }

    private void loginOut() {
        //退出登录

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
