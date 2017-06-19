package com.shanchain.mvp.view.activity.challenge;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.view.activity.MainActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener {


    private static final int TYPE_WALKMORE = 1;
    private static final int TYPE_SLEEP_EARLIER = 2;
    ArthurToolBar mToolbarShare;
    @Bind(R.id.iv_share_type)
    ImageView mIvShareType;
    @Bind(R.id.tv_share_type)
    TextView mTvShareType;
    @Bind(R.id.iv_share_wechat)
    ImageView mIvShareWechat;
    @Bind(R.id.iv_share_circleoffriends)
    ImageView mIvShareCircleoffriends;
    @Bind(R.id.iv_share_qq)
    ImageView mIvShareQq;
    @Bind(R.id.iv_share_qzone)
    ImageView mIvShareQzone;
    @Bind(R.id.iv_share_weibo)
    ImageView mIvShareWeibo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_share;
    }

    @Override
    protected void initViewsAndEvents() {

        initToolBar();
        iniData();
    }

    private void iniData() {
        Intent intent = getIntent();
        int challengeType = intent.getIntExtra("challengeType", 0);
        if (challengeType == TYPE_WALKMORE){
            mIvShareType.setImageResource(R.mipmap.home_icon_walkmore_default);
            mTvShareType.setText("多走走");
        }else if (challengeType == TYPE_SLEEP_EARLIER){
            mIvShareType.setImageResource(R.mipmap.home_icon_sleep_default);
            mTvShareType.setText("早点睡");
        }
    }

    private void initToolBar() {
        mToolbarShare = (ArthurToolBar) findViewById(R.id.toolbar_share);
        mToolbarShare.setBtnEnabled(false, true);
        mToolbarShare.setBtnVisibility(false, true);
        mToolbarShare.setOnRightClickListener(this);
    }


    @OnClick({R.id.iv_share_wechat, R.id.iv_share_circleoffriends, R.id.iv_share_qq, R.id.iv_share_qzone, R.id.iv_share_weibo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_share_wechat:

                break;
            case R.id.iv_share_circleoffriends:

                break;
            case R.id.iv_share_qq:

                break;
            case R.id.iv_share_qzone:

                break;
            case R.id.iv_share_weibo:

                break;
        }
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fragmentId",1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fragmentId",1);
        startActivity(intent);
        finish();
    }
}
