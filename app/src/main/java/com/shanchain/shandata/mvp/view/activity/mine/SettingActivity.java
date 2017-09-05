package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.DataCleanManager;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
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
    @Bind(R.id.btn_setting_quit)
    Button mBtnSettingQuit;
    @Bind(R.id.activity_setting)
    LinearLayout mActivitySetting;
    @Bind(R.id.tv_setting_cache)
    TextView mTvSettingCache;
    @Bind(R.id.shs_setting_story)
    ShSwitchView mShsSettingStory;
    @Bind(R.id.shs_setting_style_introduce)
    ShSwitchView mShsSettingStyleIntroduce;

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
        //自动收藏参加过的故事
        boolean mShsSettingStoryOn = PrefUtils.getBoolean(this, "mShsSettingStoryOn", true);
        mShsSettingStory.setOn(mShsSettingStoryOn);

        boolean mShsSettingStyleIntroduceOn = PrefUtils.getBoolean(this, "mShsSettingStyleIntroduceOn", true);
        mShsSettingStyleIntroduce.setOn(mShsSettingStyleIntroduceOn);

        try {
            File externalCacheDir = getExternalCacheDir();
            String cacheSizeExternal = DataCleanManager.getCacheSize(externalCacheDir);

            LogUtils.d("外部缓存目录:" + externalCacheDir.getAbsolutePath() + "外部缓存大小 : " + cacheSizeExternal);
            File cacheDir = getCacheDir();

            String cacheSize = DataCleanManager.getCacheSize(cacheDir);
            LogUtils.d("本地缓存目录:" + cacheDir.getAbsolutePath() + "本地缓存大小 : " + cacheSize );
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

    @OnClick({R.id.ll_setting_msg, R.id.ll_setting_account, R.id.ll_setting_shielding_person,
            R.id.ll_setting_clear_cache, R.id.ll_setting_feedback, R.id.btn_setting_quit})
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
                clearDiskCache();
                break;
            case R.id.ll_setting_feedback:
                readyGo(FeedbackActivity.class);
                break;
            case R.id.btn_setting_quit:
                loginOut();
                break;
        }
    }

    private void clearDiskCache() {
        CustomDialog customDialog = new CustomDialog(this, true, true, 0.95, R.layout.dialog_clear_cache, new int[]{R.id.btn_dialog_clear_cache, R.id.btn_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_dialog_clear_cache:
                        DataCleanManager.deleteFolderFile(getCacheDir().getAbsolutePath(), true);
                        initData();
                        break;
                    case R.id.btn_dialog_cancel:
                        break;
                }
            }
        });
        customDialog.show();
    }

    private void loginOut() {
        //退出登录

    }

    @Override
    public void onLeftClick(View v) {
        SaveState();
        finish();
    }

    private void SaveState() {
        //检测按钮设置是否改变
        boolean mShsSettingStoryOn = mShsSettingStory.isOn();
        boolean mShsSettingStoryOnBefore = PrefUtils.getBoolean(this, "mShsSettingStoryOn", true);
        if (mShsSettingStoryOn != mShsSettingStoryOnBefore) {
            PrefUtils.putBoolean(this, "mShsSettingStoryOn", mShsSettingStoryOn);
            LogUtils.d("自动收藏改变");
        }

        boolean mShsSettingStyleIntroduceOn = mShsSettingStyleIntroduce.isOn();
        boolean mShsSettingStyleIntroduceOnBefore = PrefUtils.getBoolean(this, "mShsSettingStyleIntroduceOn", true);
        if (mShsSettingStyleIntroduceOn != mShsSettingStyleIntroduceOnBefore) {
            PrefUtils.putBoolean(this,"mShsSettingStyleIntroduceOn",mShsSettingStyleIntroduceOn);
            LogUtils.d("方式介绍改变");
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SaveState();
        finish();
    }
}