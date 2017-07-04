package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class CommonProblemActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarCommonProblem;
    @Bind(R.id.tv_common_problem_shan)
    TextView mTvCommonProblemShan;
    @Bind(R.id.tv_common_problem_card)
    TextView mTvCommonProblemCard;
    @Bind(R.id.tv_common_problem_challenge_rule)
    TextView mTvCommonProblemChallengeRule;
    @Bind(R.id.tv_common_problem_custom_challenge)
    TextView mTvCommonProblemCustomChallenge;
    @Bind(R.id.tv_common_problem_record_story)
    TextView mTvCommonProblemRecordStory;
    @Bind(R.id.tv_common_problem_unique_story)
    TextView mTvCommonProblemUniqueStory;
    @Bind(R.id.tv_common_problem_new_story)
    TextView mTvCommonProblemNewStory;
    @Bind(R.id.tv_common_problem_shan_use)
    TextView mTvCommonProblemShanUse;
    @Bind(R.id.tv_common_problem_difference)
    TextView mTvCommonProblemDifference;
    @Bind(R.id.tv_common_problem_friend)
    TextView mTvCommonProblemFriend;
    @Bind(R.id.tv_common_problem_invitation)
    TextView mTvCommonProblemInvitation;
    @Bind(R.id.tv_common_problem_more)
    TextView mTvCommonProblemMore;
    @Bind(R.id.activity_common_problem)
    LinearLayout mActivityCommonProblem;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_common_problem;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarCommonProblem = (ArthurToolBar) findViewById(R.id.toolbar_common_problem);
        mToolbarCommonProblem.setBtnEnabled(true,false);
        mToolbarCommonProblem.setBtnVisibility(true,false);
        mToolbarCommonProblem.setOnLeftClickListener(this);

    }


    @OnClick({R.id.tv_common_problem_shan, R.id.tv_common_problem_card, R.id.tv_common_problem_challenge_rule, R.id.tv_common_problem_custom_challenge, R.id.tv_common_problem_record_story, R.id.tv_common_problem_unique_story, R.id.tv_common_problem_new_story, R.id.tv_common_problem_shan_use, R.id.tv_common_problem_difference, R.id.tv_common_problem_friend, R.id.tv_common_problem_invitation, R.id.tv_common_problem_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_common_problem_shan:
                break;
            case R.id.tv_common_problem_card:
                break;
            case R.id.tv_common_problem_challenge_rule:
                break;
            case R.id.tv_common_problem_custom_challenge:
                break;
            case R.id.tv_common_problem_record_story:
                break;
            case R.id.tv_common_problem_unique_story:
                break;
            case R.id.tv_common_problem_new_story:
                break;
            case R.id.tv_common_problem_shan_use:
                break;
            case R.id.tv_common_problem_difference:
                break;
            case R.id.tv_common_problem_friend:
                break;
            case R.id.tv_common_problem_invitation:
                break;
            case R.id.tv_common_problem_more:
                readyGo(FeedbackActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
