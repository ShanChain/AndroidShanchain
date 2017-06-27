package com.shanchain.shandata.mvp.view.activity.challenge;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ChallengeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarChallenge;
    @Bind(R.id.et_challenge_search)
    TextView mEtChallengeSearch;
    @Bind(R.id.iv_challenge_icon1)
    ImageView mIvChallengeIcon1;
    @Bind(R.id.tv_challenge_type1)
    TextView mTvChallengeType1;
    @Bind(R.id.tv_challenge_state1)
    TextView mTvChallengeState1;
    @Bind(R.id.iv_challenge_icon2)
    ImageView mIvChallengeIcon2;
    @Bind(R.id.tv_challenge_type2)
    TextView mTvChallengeType2;
    @Bind(R.id.tv_challenge_state2)
    TextView mTvChallengeState2;
    @Bind(R.id.tv_challenge_more)
    TextView mTvChallengeMore;
    @Bind(R.id.iv_challenge_happy_counts)
    ImageView mIvChallengeHappyCounts;
    @Bind(R.id.tv_challenge_happy_counts)
    TextView mTvChallengeHappyCounts;
    @Bind(R.id.iv_challenge_focuson_counts)
    ImageView mIvChallengeFocusonCounts;
    @Bind(R.id.tv_challenge_focuson_counts)
    TextView mTvChallengeFocusonCounts;
    @Bind(R.id.iv_challenge_walk_counts)
    ImageView mIvChallengeWalkCounts;
    @Bind(R.id.tv_challenge_walk_counts)
    TextView mTvChallengeWalkCounts;
    @Bind(R.id.iv_challenge_sleep_counts)
    ImageView mIvChallengeSleepCounts;
    @Bind(R.id.tv_challenge_sleep_counts)
    TextView mTvChallengeSleepCounts;
    @Bind(R.id.btn_challenge_new)
    TextView mBtnChallengeNew;
    @Bind(R.id.tv_challenge_new)
    TextView mTvChallengeNew;
    @Bind(R.id.activity_challenge)
    LinearLayout mActivityChallenge;
    public static Typeface typeFace;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_challenge;
    }

    @Override
    protected void initViewsAndEvents() {
        typeFace = Typeface.createFromAsset(getAssets(), "font/hanzipen_sc.ttf");
        mBtnChallengeNew.setTypeface(typeFace);
        initToolBar();
    }

    private void initToolBar() {
        mToolbarChallenge = (ArthurToolBar) findViewById(R.id.toolbar_challenge);
        mToolbarChallenge.setBtnEnabled(true,false);
        mToolbarChallenge.setBtnVisibility(true,false);
        mToolbarChallenge.setOnLeftClickListener(this);
    }


    @OnClick({R.id.et_challenge_search,R.id.iv_challenge_icon1, R.id.iv_challenge_icon2, R.id.tv_challenge_more, R.id.iv_challenge_happy_counts, R.id.iv_challenge_focuson_counts, R.id.iv_challenge_walk_counts, R.id.iv_challenge_sleep_counts, R.id.btn_challenge_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_challenge_search:
                readyGo(ChallengeSearchActivity.class);
                break;
            case R.id.iv_challenge_icon1:
                readyGo(WalkingMoreActivity.class);
                break;
            case R.id.iv_challenge_icon2:
                readyGo(HasPartInSleepEarlierActivity.class);
                break;
            case R.id.tv_challenge_more:
                readyGo(ChallengeOnGoingActivity.class);
                break;
            case R.id.iv_challenge_happy_counts:
                readyGo(HappierActivity.class);
                break;
            case R.id.iv_challenge_focuson_counts:
                readyGo(NoBowActivity.class);
                break;
            case R.id.iv_challenge_walk_counts:
                readyGo(WalkedMoreActivity.class);
                break;
            case R.id.iv_challenge_sleep_counts:
                readyGo(SleepEarlierActivity.class);
                break;
            case R.id.btn_challenge_new:
                readyGo(ChooseChallengeActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
