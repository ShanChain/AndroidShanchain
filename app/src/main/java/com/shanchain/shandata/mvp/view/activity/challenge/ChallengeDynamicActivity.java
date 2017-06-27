package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.model.ContactInfo;
import com.shanchain.shandata.mvp.model.TopicInfo;
import com.shanchain.shandata.mvp.view.activity.AiteContactsActivity;
import com.shanchain.shandata.mvp.view.activity.TopicActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ChallengeDynamicActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int TYPE_WALKMORE = 1;
    private static final int TYPE_SLEEP_EARLIER = 2;
    private static final int AITE_REQUEST_CODE = 100;
    private static final int TOPIC_REQUEST_CODE = 200;

    ArthurToolBar mToolbarChallengeDynamic;
    @Bind(R.id.iv_challenge_dynamic_type)
    ImageView mIvChallengeDynamicType;
    @Bind(R.id.tv_challenge_dynamic_type)
    TextView mTvChallengeDynamicType;
    @Bind(R.id.et_challenge_dynamic_content)
    EditText mEtChallengeDynamicContent;
    @Bind(R.id.iv_challenge_dynamic_aite)
    ImageView mIvChallengeDynamicAite;
    @Bind(R.id.iv_challenge_dynamic_theme)
    ImageView mIvChallengeDynamicTheme;
    @Bind(R.id.activity_challenge_dynamic)
    LinearLayout mActivityChallengeDynamic;
    private int mType;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_challenge_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);

        initToolBar();
        initData();
    }

    private void initData() {
        if (mType == TYPE_WALKMORE) {
            mIvChallengeDynamicType.setImageResource(R.mipmap.home_icon_walkmore_default);
            mTvChallengeDynamicType.setText("多走走");
        } else if (mType == TYPE_SLEEP_EARLIER) {
            mIvChallengeDynamicType.setImageResource(R.mipmap.home_icon_sleep_default);
            mTvChallengeDynamicType.setText("早点睡");
        }
    }

    private void initToolBar() {
        mToolbarChallengeDynamic = (ArthurToolBar) findViewById(R.id.toolbar_challenge_dynamic);
        mToolbarChallengeDynamic.setBtnEnabled(true);
        mToolbarChallengeDynamic.setBtnVisibility(true);
        mToolbarChallengeDynamic.setOnLeftClickListener(this);
        mToolbarChallengeDynamic.setOnRightClickListener(this);
    }

    @OnClick({R.id.iv_challenge_dynamic_aite, R.id.iv_challenge_dynamic_theme})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_challenge_dynamic_aite:
                readyGoForResult(AiteContactsActivity.class, AITE_REQUEST_CODE);
                break;
            case R.id.iv_challenge_dynamic_theme:
                readyGoForResult(TopicActivity.class, TOPIC_REQUEST_CODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AITE_REQUEST_CODE) {
            if (data != null) {
                ContactInfo contactInfo =
                        (ContactInfo) data.getSerializableExtra("aiteReturn");
                LogUtils.d(contactInfo.getName());
            }
        } else if (requestCode == TOPIC_REQUEST_CODE) {
            if (data != null) {
                TopicInfo topicInfo = (TopicInfo) data.getSerializableExtra("topicReturn");
                String topic = topicInfo.getTopic();
                LogUtils.d(topic);

            }
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("challengeType",mType);
        readyGo(ShareActivity.class,bundle);
        ActivityManager.getInstance().finishAllActivity();
    }
}
