package com.shanchain.shandata.mvp.view.activity.challenge;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class WalkingMoreActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    ArthurToolBar mToolbarWalkingMore;
    @Bind(R.id.tv_walking_more_counts)
    TextView mTvWalkingMoreCounts;
    @Bind(R.id.tv_walking_more_rules)
    TextView mTvWalkingMoreRules;
    @Bind(R.id.tv_walking_champion_time)
    TextView mTvWalkingChampionTime;
    @Bind(R.id.tv_walkinge_mine_time)
    TextView mTvWalkingeMineTime;
    @Bind(R.id.tv_walking_challenge_champion_name1)
    TextView mTvWalkingChallengeChampionName1;
    @Bind(R.id.tv_walking_challenge_champion_name2)
    TextView mTvWalkingChallengeChampionName2;
    @Bind(R.id.tv_walking_challenge_time)
    TextView mTvWalkingChallengeTime;
    @Bind(R.id.tv_walking_challenge_info)
    TextView mTvWalkingChallengeInfo;
    @Bind(R.id.csb_walking)
    CustomSeekBar mCsbWalking;
    @Bind(R.id.tv_walking_pay)
    TextView mTvWalkingPay;
    @Bind(R.id.btn_walking_quit)
    Button mBtnWalkingQuit;
    @Bind(R.id.activity_walking_more)
    LinearLayout mActivityWalkingMore;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_walking_more;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        mCsbWalking.setProgress(60);
        mCsbWalking.setSeekBarEnable(false);
    }

    private void initToolBar() {
        mToolbarWalkingMore = (ArthurToolBar) findViewById(R.id.toolbar_walking_more);
        mToolbarWalkingMore.setBtnEnabled(true,false);
        mToolbarWalkingMore.setBtnVisibility(true,false);
        mToolbarWalkingMore.setOnLeftClickListener(this);
    }

    @OnClick({R.id.tv_walking_more_rules, R.id.btn_walking_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_walking_more_rules:
                CustomDialog customDialog = new CustomDialog(this,R.layout.dialog_walk_more,null);
                customDialog.show();
                break;
            case R.id.btn_walking_quit:
                CustomDialog dialog = new CustomDialog(this,false,0.7,R.layout.dialog_quit_countdown,new int[]{R.id.tv_dialog_countdown_quit,R.id.tv_dialog_countdown_continue});
                dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.tv_dialog_countdown_quit:
                                readyGo(MainActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                break;
                            case R.id.tv_dialog_countdown_continue:

                                break;
                        }
                    }
                });
                dialog.show();

                break;
        }

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
