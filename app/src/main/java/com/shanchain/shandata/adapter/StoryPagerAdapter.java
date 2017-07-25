package com.shanchain.shandata.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.mvp.model.StoryPagerInfo;
import com.shanchain.shandata.mvp.view.activity.DetailsActivity;
import com.shanchain.shandata.mvp.view.activity.PersonalHomePagerActivity;
import com.shanchain.shandata.mvp.view.activity.story.CurrentRouteActivity;
import com.shanchain.shandata.mvp.view.activity.story.JoinRouteActivity;
import com.shanchain.shandata.mvp.view.activity.story.NewRouteActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhoujian on 2017/7/18.
 */

public class StoryPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private TextView mTvItemVpStoryName;
    private TextView mTvItemVpStoryDes;
    private ImageView mIvItemVpStoryAuthorAvatar;
    private TextView mTvItemVpStoryAuthorNick;
    private TextView mTvItemVpStoryDynamicTime;
    private TextView mTvItemVpStoryDynamicAddress;
    private ImageView mIvItemVpStoryDynamic;
    private TextView mTvItemVpStoryDynamicDes;
    private TextView mTvItemVpStoryDynamicDays;
    private TextView mTvItemVpStoryLookDynamic;
    private TextView mTvItemVpStoryOrder;
    private TextView mTvItemVpStoryJoin;
    private TextView mTvItemVpStoryBifurcation;
    private TextView mTvItemVpStorySupport;
    private RelativeLayout mRlItemVpStoryDynamic;


    private ArrayList<StoryPagerInfo> mPagerInfos;
    private LinearLayout mLlItemVpStory;
    private TextView mTvItemVpStoryRoute;

    public StoryPagerAdapter(ArrayList<StoryPagerInfo> pagerInfos) {
        mPagerInfos = pagerInfos;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.item_vp_story,null);

        mLlItemVpStory = (LinearLayout) view.findViewById(R.id.ll_item_vp_story);
        mTvItemVpStoryName = (TextView) view.findViewById(R.id.tv_item_vp_story_name);
        mTvItemVpStoryDes = (TextView) view.findViewById(R.id.tv_item_vp_story_des);
        mIvItemVpStoryAuthorAvatar = (ImageView) view.findViewById(R.id.iv_item_vp_story_author_avatar);
        mTvItemVpStoryAuthorNick = (TextView) view.findViewById(R.id.tv_item_vp_story_author_nick);
        mRlItemVpStoryDynamic = (RelativeLayout) view.findViewById(R.id.rl_item_vp_story_dynamic);
        mTvItemVpStoryDynamicTime = (TextView) view.findViewById(R.id.tv_item_vp_story_dynamic_time);
        mTvItemVpStoryDynamicAddress = (TextView) view.findViewById(R.id.tv_item_vp_story_dynamic_address);
        mIvItemVpStoryDynamic = (ImageView) view.findViewById(R.id.iv_item_vp_story_dynamic);
        mTvItemVpStoryDynamicDes = (TextView) view.findViewById(R.id.tv_item_vp_story_dynamic_des);
        mTvItemVpStoryDynamicDays = (TextView) view.findViewById(R.id.tv_item_vp_story_dynamic_days);
        mTvItemVpStoryLookDynamic = (TextView) view.findViewById(R.id.tv_item_vp_story_look_dynamic);
        mTvItemVpStoryOrder = (TextView) view.findViewById(R.id.tv_item_vp_story_order);
        mTvItemVpStoryJoin = (TextView) view.findViewById(R.id.tv_item_vp_story_join);
        mTvItemVpStoryBifurcation = (TextView) view.findViewById(R.id.tv_item_vp_story_bifurcation);
        mTvItemVpStorySupport = (TextView) view.findViewById(R.id.tv_item_vp_story_support);
        mTvItemVpStoryRoute = (TextView) view.findViewById(R.id.tv_item_vp_story_route);
        Glide.with(container.getContext())
                .load(R.drawable.photo_bear)
                .transform(new GlideCircleTransform(container.getContext()))
                .into(mIvItemVpStoryAuthorAvatar);

        mLlItemVpStory.setOnClickListener(this);
        mIvItemVpStoryAuthorAvatar.setOnClickListener(this);
        mTvItemVpStoryAuthorNick.setOnClickListener(this);
        mRlItemVpStoryDynamic.setOnClickListener(this);
        mTvItemVpStoryLookDynamic.setOnClickListener(this);
        mTvItemVpStoryJoin.setOnClickListener(this);
        mTvItemVpStoryBifurcation.setOnClickListener(this);
        mTvItemVpStorySupport.setOnClickListener(this);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item_vp_story:
                Intent intentStory = new Intent(v.getContext(), CurrentRouteActivity.class);
                v.getContext().startActivity(intentStory);
                break;
            case R.id.iv_item_vp_story_author_avatar:
                Intent avatarIntent = new Intent(v.getContext(), PersonalHomePagerActivity.class);
                v.getContext().startActivity(avatarIntent);
                break;
            case R.id.tv_item_vp_story_author_nick:
                Intent nickIntent = new Intent(v.getContext(), PersonalHomePagerActivity.class);
                v.getContext().startActivity(nickIntent);
                break;

            case R.id.rl_item_vp_story_dynamic:
                //查看动态布局
                Intent intentDynamic = new Intent(v.getContext(), DetailsActivity.class);
                v.getContext().startActivity(intentDynamic);
                break;
            case R.id.tv_item_vp_story_look_dynamic:
                //查看动态按钮
                Intent intentLook = new Intent(v.getContext(), DetailsActivity.class);
                v.getContext().startActivity(intentLook);
                break;
            case R.id.tv_item_vp_story_join:
                //并入故事
                Intent intentJoin = new Intent(v.getContext
                        (),JoinRouteActivity.class);
                v.getContext().startActivity(intentJoin);
                break;
            case R.id.tv_item_vp_story_bifurcation:
                //故事分岔
                Intent intentBifurcation = new Intent(v.getContext(), NewRouteActivity.class);
                v.getContext().startActivity(intentBifurcation);
                break;
            case R.id.tv_item_vp_story_support:
                //支持故事
                ToastUtils.showToast(v.getContext(),"支持");
                break;
        }
    }


}
