package com.shanchain.shandata.mvp.view.activity.challenge;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ChooseChallengeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarChooseChallenge;
    @Bind(R.id.et_choose_challenge)
    EditText mEtChooseChallenge;
    @Bind(R.id.et_choose_content)
    EditText mEtChooseContent;
    @Bind(R.id.iv_choose_walk)
    ImageView mIvChooseWalk;
    @Bind(R.id.iv_choose_sleep)
    ImageView mIvChooseSleep;
    @Bind(R.id.activity_choose_challenge)
    LinearLayout mActivityChooseChallenge;

    private boolean isSleep = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_challenge;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {

    }

    private void initToolBar() {
        mToolbarChooseChallenge = (ArthurToolBar) findViewById(R.id.toolbar_choose_challenge);
        mToolbarChooseChallenge.setBtnVisibility(true, true);
        mToolbarChooseChallenge.setBtnEnabled(true);
        mToolbarChooseChallenge.setOnLeftClickListener(this);
        mToolbarChooseChallenge.setOnRightClickListener(this);
    }

    @OnClick({R.id.iv_choose_walk, R.id.iv_choose_sleep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose_walk:
                isSleep = false;
                chooseWalk(true);
                break;
            case R.id.iv_choose_sleep:
                isSleep = true;
                chooseWalk(false);
                break;
        }
    }

    private void chooseWalk(boolean isWalk) {

        mIvChooseWalk.setImageResource(isWalk ? R.mipmap.setup_icon_walkmore_default : R.mipmap.select_icon_walkmore_default);
        mIvChooseSleep.setImageResource(isWalk ? R.mipmap.select_icon_sleep_default : R.mipmap.select_icon_sleep_selected);
        mEtChooseChallenge.setHint(isWalk ? "多走走" : "早点睡");
        mEtChooseContent.setHint(isWalk?"找个好天气，看谁走的步数多！":"年轻人，早点睡！");

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

        readyGo(isSleep?SleepEarlierSettingActivity.class:WalkMoreSettingActivity.class);
    }
}
