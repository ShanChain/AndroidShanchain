package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.DetailsActivity;
import com.shanchain.shandata.mvp.view.activity.PersonalHomePagerActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class CurrentRouteActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarCurrentRoute;
    @Bind(R.id.tv_current_route_story_name)
    TextView mTvCurrentRouteStoryName;
    @Bind(R.id.tv_current_route_story_des)
    TextView mTvCurrentRouteStoryDes;
    @Bind(R.id.iv_current_route_author_avatar)
    ImageView mIvCurrentRouteAuthorAvatar;
    @Bind(R.id.tv_current_route_author_nick)
    TextView mTvCurrentRouteAuthorNick;
    @Bind(R.id.tv_current_route_dynamic_time)
    TextView mTvCurrentRouteDynamicTime;
    @Bind(R.id.tv_current_route_dynamic_address)
    TextView mTvCurrentRouteDynamicAddress;
    @Bind(R.id.iv_current_route_vote)
    ImageView mIvCurrentRouteVote;
    @Bind(R.id.iv_current_route_dynamic)
    ImageView mIvCurrentRouteDynamic;
    @Bind(R.id.tv_current_route_dynamic_des)
    TextView mTvCurrentRouteDynamicDes;
    @Bind(R.id.tv_current_route_dynamic_days)
    TextView mTvCurrentRouteDynamicDays;
    @Bind(R.id.tv_current_route_look_dynamic)
    TextView mTvCurrentRouteLookDynamic;
    @Bind(R.id.rl_current_route_dynamic)
    RelativeLayout mRlCurrentRouteDynamic;
    @Bind(R.id.tv_current_route_order)
    TextView mTvCurrentRouteOrder;
    @Bind(R.id.tv_current_route_join)
    TextView mTvCurrentRouteJoin;
    @Bind(R.id.tv_current_route_bifurcation)
    TextView mTvCurrentRouteBifurcation;
    @Bind(R.id.tv_current_route_support)
    TextView mTvCurrentRouteSupport;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_current_route;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        Glide.with(this)
                .load(R.drawable.photo_shenzhen)
                .transform(new GlideCircleTransform(this))
                .into(mIvCurrentRouteAuthorAvatar);
    }

    private void initToolBar() {
        mToolbarCurrentRoute = (ArthurToolBar) findViewById(R.id.toolbar_current_route);
        mToolbarCurrentRoute.setBtnVisibility(true,false);
        mToolbarCurrentRoute.setBtnEnabled(true,false);
        mToolbarCurrentRoute.setOnLeftClickListener(this);
    }

    @OnClick({R.id.iv_current_route_author_avatar, R.id.tv_current_route_author_nick, R.id.iv_current_route_vote, R.id.tv_current_route_look_dynamic, R.id.rl_current_route_dynamic, R.id.tv_current_route_join, R.id.tv_current_route_bifurcation, R.id.tv_current_route_support})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_current_route_author_avatar:
                //头像
                Intent intentAvatar = new Intent(this, PersonalHomePagerActivity.class);
                startActivity(intentAvatar);
                break;
            case R.id.tv_current_route_author_nick:
                //昵称
                Intent nickAvatar = new Intent(this, PersonalHomePagerActivity.class);
                startActivity(nickAvatar);
                break;
            case R.id.iv_current_route_vote:
                //投票
                vote();
                break;
            case R.id.tv_current_route_look_dynamic:
                //查看动态
                Intent intentLook = new Intent(this, DetailsActivity.class);
                startActivity(intentLook);
                break;
            case R.id.rl_current_route_dynamic:
                //查看动态
                Intent intentDynamic = new Intent(this, DetailsActivity.class);
                startActivity(intentDynamic);
                break;
            case R.id.tv_current_route_join:
                //并入
                Intent intentJoin = new Intent(this,JoinRouteActivity.class);
                startActivity(intentJoin);
                break;
            case R.id.tv_current_route_bifurcation:
                //分岔

                break;
            case R.id.tv_current_route_support:
                //支持

                break;
        }
    }

    private void vote() {
        CustomDialog customDialog = new CustomDialog(mContext,false,0.8,R.layout.dialog_vote,new int[]{R.id.tv_dialog_vote_cancle,R.id.tv_dialog_vote_sure});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                if (view.getId() == R.id.tv_dialog_vote_sure){
                    //投票
                    ToastUtils.showToast(CurrentRouteActivity.this,"投票成功");



                }else if (view.getId() == R.id.tv_dialog_vote_cancle){

                }
            }
        });
        customDialog.show();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
