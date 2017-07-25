package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ShareStoryActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarShareStory;
    @Bind(R.id.iv_share_story_img)
    ImageView mIvShareStoryImg;
    @Bind(R.id.tv_share_story_name)
    TextView mTvShareStoryName;
    @Bind(R.id.tv_share_story_result)
    TextView mTvShareStoryResult;
    @Bind(R.id.iv_share_story_shan)
    ImageView mIvShareStoryShan;
    @Bind(R.id.tv_share_story_shan)
    TextView mTvShareStoryShan;
    @Bind(R.id.tv_other_login)
    TextView mTvOtherLogin;
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
    @Bind(R.id.activity_share_story)
    LinearLayout mActivityShareStory;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_share_story;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initToolBar() {
        mToolbarShareStory = (ArthurToolBar) findViewById(R.id.toolbar_share_story);
        mToolbarShareStory.setBtnEnabled(false,true);
        mToolbarShareStory.setBtnVisibility(false,true);
        mToolbarShareStory.setOnRightClickListener(this);
    }

    private void initData() {
        Glide.with(this)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(this))
                .into(mIvShareStoryImg);
        Glide.with(this)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(this))
                .into(mIvShareStoryShan);
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

        ActivityManager.getInstance().finishAllActivity();

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fragmentId",1);
        startActivity(intent);
        finish();
    }
}
