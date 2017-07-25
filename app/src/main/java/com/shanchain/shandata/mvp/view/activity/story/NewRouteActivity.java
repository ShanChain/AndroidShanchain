package com.shanchain.shandata.mvp.view.activity.story;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class NewRouteActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarNewRoute;
    @Bind(R.id.tv_new_route_story_name)
    TextView mTvNewRouteStoryName;
    @Bind(R.id.tv_new_route_story_des)
    TextView mTvNewRouteStoryDes;
    @Bind(R.id.iv_new_route_author_avatar)
    ImageView mIvNewRouteAuthorAvatar;
    @Bind(R.id.tv_new_route_author_nick)
    TextView mTvNewRouteAuthorNick;
    @Bind(R.id.tv_new_route_dynamic_time)
    TextView mTvNewRouteDynamicTime;
    @Bind(R.id.tv_new_route_dynamic_address)
    TextView mTvNewRouteDynamicAddress;
    @Bind(R.id.iv_new_route_vote)
    ImageView mIvNewRouteVote;
    @Bind(R.id.iv_new_route_dynamic)
    ImageView mIvNewRouteDynamic;
    @Bind(R.id.tv_new_route_dynamic_des)
    TextView mTvNewRouteDynamicDes;
    @Bind(R.id.tv_new_route_dynamic_days)
    TextView mTvNewRouteDynamicDays;
    @Bind(R.id.tv_new_route_look_dynamic)
    TextView mTvNewRouteLookDynamic;
    @Bind(R.id.rl_new_route_dynamic)
    RelativeLayout mRlNewRouteDynamic;
    @Bind(R.id.tv_new_route_order)
    TextView mTvNewRouteOrder;
    @Bind(R.id.et_new_route_Introduction)
    EditText mEtNewRouteIntroduction;
    @Bind(R.id.et_new_route_name)
    EditText mEtNewRouteName;
    @Bind(R.id.activity_new_route)
    LinearLayout mActivityNewRoute;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_new_route;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        Glide.with(this)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(this))
                .into(mIvNewRouteAuthorAvatar);
    }

    private void initToolBar() {
        mToolbarNewRoute = (ArthurToolBar) findViewById(R.id.toolbar_new_route);
        mToolbarNewRoute.setBtnEnabled(true);
        mToolbarNewRoute.setBtnVisibility(true);
        mToolbarNewRoute.setOnLeftClickListener(this);
        mToolbarNewRoute.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(RecordStoryFragmentActivity.class);
    }
}
