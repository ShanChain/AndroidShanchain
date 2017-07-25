package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryTagAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.WebActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class StoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.toolbar_story)
    ArthurToolBar mToolbarStory;
    @Bind(R.id.tv_story_search)
    TextView mTvStorySearch;
    @Bind(R.id.iv_story_icon1)
    ImageView mIvStoryIcon1;
    @Bind(R.id.tv_story_name1)
    TextView mTvStoryName1;
    @Bind(R.id.tv_story_state1)
    TextView mTvStoryState1;
    @Bind(R.id.iv_story_icon2)
    ImageView mIvStoryIcon2;
    @Bind(R.id.tv_story_name2)
    TextView mTvStoryName2;
    @Bind(R.id.tv_story_state2)
    TextView mTvStoryState2;
    @Bind(R.id.tv_story_more)
    TextView mTvStoryMore;
    @Bind(R.id.flowlayout_story)
    TagFlowLayout mFlowlayoutStory;
    @Bind(R.id.iv_story_hot_icon1)
    ImageView mIvStoryHotIcon1;
    @Bind(R.id.tv_story_hot_name1)
    TextView mTvStoryHotName1;
    @Bind(R.id.tv_story_hot_state1)
    TextView mTvStoryHotState1;
    @Bind(R.id.iv_story_hot_icon2)
    ImageView mIvStoryHotIcon2;
    @Bind(R.id.tv_story_hot_name2)
    TextView mTvStoryHotName2;
    @Bind(R.id.tv_story_hot_state2)
    TextView mTvStoryHotState2;
    @Bind(R.id.iv_story_hot_icon3)
    ImageView mIvStoryHotIcon3;
    @Bind(R.id.tv_story_hot_name3)
    TextView mTvStoryHotName3;
    @Bind(R.id.tv_story_hot_state3)
    TextView mTvStoryHotState3;
    @Bind(R.id.iv_story_hot_icon4)
    ImageView mIvStoryHotIcon4;
    @Bind(R.id.tv_story_hot_name4)
    TextView mTvStoryHotName4;
    @Bind(R.id.tv_story_hot_state4)
    TextView mTvStoryHotState4;
    @Bind(R.id.btn_story_new)
    TextView mBtnStoryNew;
    @Bind(R.id.tv_story_new)
    TextView mTvStoryNew;
    @Bind(R.id.btn_story_all)
    TextView mBtnStoryAll;
    @Bind(R.id.tv_story_all)
    TextView mTvStoryAll;

    private Intent intent;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        final String[] tags = {"热门标签", "标签一呜", "标签二月", "标签三", "标签四五", "标签五呜", "更多"};
        StoryTagAdapter tagAdapter = new StoryTagAdapter(tags);
        mFlowlayoutStory.setAdapter(tagAdapter);
        mFlowlayoutStory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position == 0){
                    return false;
                }else if (position == tags.length-1){
                     intent = new Intent(StoryActivity.this,TagActivity.class);
                    startActivity(intent);
                    return true;
                }else {
                     intent= new Intent(StoryActivity.this, StoryListActivity.class);
                    startActivity(intent);
                    return true;
                }

            }
        });
    }

    private void initToolBar() {
        mToolbarStory = (ArthurToolBar) findViewById(R.id.toolbar_story);
        mToolbarStory.setBtnEnabled(true);
        mToolbarStory.setOnLeftClickListener(this);
        mToolbarStory.setOnRightClickListener(this);

    }

    @OnClick({R.id.iv_story_icon1, R.id.tv_story_name1, R.id.iv_story_icon2, R.id.tv_story_name2, R.id.tv_story_more,R.id.iv_story_hot_icon1, R.id.tv_story_hot_name1, R.id.iv_story_hot_icon2, R.id.tv_story_hot_name2, R.id.iv_story_hot_icon3, R.id.tv_story_hot_name3, R.id.iv_story_hot_icon4, R.id.tv_story_hot_name4, R.id.btn_story_new, R.id.tv_story_new, R.id.btn_story_all, R.id.tv_story_all})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_story_icon1:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_name1:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_story_icon2:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_name2:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_more:
                readyGo(CollectionStoryActivity.class);
                break;
            case R.id.iv_story_hot_icon1:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_hot_name1:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_story_hot_icon2:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_hot_name2:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_story_hot_icon3:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_hot_name3:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_story_hot_icon4:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_story_hot_name4:
                intent = new Intent(this,StoryHomePagerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_story_new:
                readyGo(CreationActivity.class);
                break;
            case R.id.tv_story_new:
                readyGo(CreationActivity.class);
                break;
            case R.id.btn_story_all:
                readyGo(AllStoryActivity.class);
                break;
            case R.id.tv_story_all:
                readyGo(AllStoryActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }


}
