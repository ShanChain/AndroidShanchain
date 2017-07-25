package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryPagerAdapter;
import com.shanchain.shandata.adapter.StoryPagerListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.StoryPagerInfo;
import com.shanchain.shandata.mvp.model.StoryPagerListInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.other.AutoHeightListView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class StoryHomePagerActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarStory;
    @Bind(R.id.iv_story_home_icon)
    ImageView mIvStoryHomeIcon;
    @Bind(R.id.tv_story_home_name)
    TextView mTvStoryHomeName;
    @Bind(R.id.tv_story_home_des)
    TextView mTvStoryHomeDes;
    @Bind(R.id.tv_story_home_result)
    TextView mTvStoryHomeResult;
    @Bind(R.id.iv_story_home_collection)
    ImageView mIvStoryHomeCollection;
    @Bind(R.id.tv_story_home_route)
    TextView mTvStoryHomeRoute;
    @Bind(R.id.vp_story_home_pager)
    ViewPager mVpStoryHomePager;
    @Bind(R.id.tv_story_home_things_record)
    TextView mTvStoryHomeThingsRecord;
    @Bind(R.id.lv_story_home_pager)
    AutoHeightListView mLvStoryHomePager;
    @Bind(R.id.tv_story_home_roadmap)
    TextView mTvStoryHomeRoadmap;
    @Bind(R.id.iv_story_home_roadmap)
    ImageView mIvStoryHomeRoadmap;
    @Bind(R.id.iv_story_home_protagonist)
    ImageView mIvStoryHomeProtagonist;
    @Bind(R.id.tv_story_home_protagonist)
    TextView mTvStoryHomeProtagonist;
    @Bind(R.id.tv_story_home_protagonist_des)
    TextView mTvStoryHomeProtagonistDes;
    private List<StoryPagerListInfo> mListDatas;
    private boolean isCollected;
    private ArrayList<StoryPagerInfo> mStoryPagerDatas;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_home_pager;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initView();

    }

    private void initView() {
        Glide.with(this)
                .load(R.drawable.photo_water)
                .transform(new GlideCircleTransform(this))
                .into(mIvStoryHomeIcon);

        initViewPager();
        initListView();
    }

    private void initListView() {
        StoryPagerListAdapter adapter = new StoryPagerListAdapter(this,mListDatas);
        mLvStoryHomePager.setAdapter(adapter);
        mLvStoryHomePager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoryHomePagerActivity.this,RecordStoryFragmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewPager() {
        mVpStoryHomePager.setOffscreenPageLimit(3);
        StoryPagerAdapter storyPagerAdapter = new StoryPagerAdapter(mStoryPagerDatas);
        mVpStoryHomePager.setPageMargin(DensityUtils.dip2px(this,15));
        mVpStoryHomePager.setAdapter(storyPagerAdapter);
    }

    private void initData() {
        isCollected = false;
        mIvStoryHomeCollection.setImageResource(isCollected?R.mipmap.storyhomepage_btn_collection_selected:R.mipmap.storyhomepage_btn_collection_default);
        mListDatas = new ArrayList<>();

        StoryPagerListInfo storyPagerListInfo1 = new StoryPagerListInfo();
        storyPagerListInfo1.setTime("12月31日");
        storyPagerListInfo1.setType("支持");
        storyPagerListInfo1.setDes("小船由路人丙往火星推行");
        mListDatas.add(storyPagerListInfo1);

        StoryPagerListInfo storyPagerListInfo2 = new StoryPagerListInfo();
        storyPagerListInfo2.setTime("4月10日");
        storyPagerListInfo2.setType("分岔");
        storyPagerListInfo2.setDes("小船在路人乙的拦截下放弃了那个谁！");
        mListDatas.add(storyPagerListInfo2);

        StoryPagerListInfo storyPagerListInfo3 = new StoryPagerListInfo();
        storyPagerListInfo3.setTime("4月10日");
        storyPagerListInfo3.setType("支持");
        storyPagerListInfo3.setDes("小船由路人丙再次往去大理路线了的");
        mListDatas.add(storyPagerListInfo3);

        StoryPagerListInfo storyPagerListInfo4 = new StoryPagerListInfo();
        storyPagerListInfo4.setTime("4月10日");
        storyPagerListInfo4.setType("支持");
        storyPagerListInfo4.setDes("小船由路人丙往火星推行");
        mListDatas.add(storyPagerListInfo4);

        StoryPagerListInfo storyPagerListInfo5 = new StoryPagerListInfo();
        storyPagerListInfo5.setTime("4月10日");
        storyPagerListInfo5.setType("支持");
        storyPagerListInfo5.setDes("小船由路人丙往火星推行");
        mListDatas.add(storyPagerListInfo5);


        mStoryPagerDatas = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            StoryPagerInfo storyPagerInfo = new StoryPagerInfo();
            mStoryPagerDatas.add(storyPagerInfo);
        }


    }

    private void initToolBar() {
        mToolbarStory = (ArthurToolBar) findViewById(R.id.toolbar_story_home_pager);
        mToolbarStory.setBtnEnabled(true,false);
        mToolbarStory.setBtnVisibility(true,false);
        mToolbarStory.setOnLeftClickListener(this);
    }

    @OnClick({R.id.iv_story_home_collection, R.id.tv_story_home_route, R.id.tv_story_home_things_record, R.id.tv_story_home_roadmap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_story_home_collection:
                //收藏
                isCollected = ! isCollected;
                mIvStoryHomeCollection.setImageResource(isCollected?R.mipmap.storyhomepage_btn_collection_selected:R.mipmap.storyhomepage_btn_collection_default);
                break;
            case R.id.tv_story_home_route:
                //路线
                readyGo(CurrentRouteActivity.class);
                break;
            case R.id.tv_story_home_things_record:
                //大事记
                readyGo(ThingsRecordActivity.class);
                break;
            case R.id.tv_story_home_roadmap:
                //路线图

                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
