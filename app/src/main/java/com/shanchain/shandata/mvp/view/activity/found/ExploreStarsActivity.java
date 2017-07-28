package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.mine.BackgroundActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.VersionUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ExploreStarsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarExploreStars;
    @Bind(R.id.iv_explore_stars_bg)
    ImageView mIvExploreStarsBg;
    @Bind(R.id.tv_explore_stars_title)
    TextView mTvExploreStarsTitle;
    @Bind(R.id.tv_explore_stars_collection)
    TextView mTvExploreStarsCollection;
    @Bind(R.id.tv_explore_stars_share)
    TextView mTvExploreStarsShare;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_explore_stars;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initToolBar();

    }

    private void initData() {
        Glide.with(this).load(R.drawable.photo_stars).into(mIvExploreStarsBg);
    }

    private void initToolBar() {
        mToolbarExploreStars = (ArthurToolBar) findViewById(R.id.toolbar_explore_stars);
        mToolbarExploreStars.setBtnEnabled(true);
        mToolbarExploreStars.setBtnVisibility(true);
        mToolbarExploreStars.setOnLeftClickListener(this);
        mToolbarExploreStars.setOnRightClickListener(this);
    }

    @OnClick({R.id.tv_explore_stars_collection, R.id.tv_explore_stars_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_explore_stars_collection:
                //收藏
                String apiVersion = VersionUtils.getApiVersion();
                LogUtils.d("安卓版本："+apiVersion );
                break;
            case R.id.tv_explore_stars_share:
                //分享

                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(this,BackgroundActivity.class);
        intent.putExtra("isBg",false);
        startActivity(intent);
    }
}
