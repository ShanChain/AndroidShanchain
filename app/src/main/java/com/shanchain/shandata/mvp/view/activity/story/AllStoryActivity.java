package com.shanchain.shandata.mvp.view.activity.story;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.StoryListInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AllStoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarAllStory;
    @Bind(R.id.xrv_all_story)
    XRecyclerView mXrvAllStory;
    private List<StoryListInfo> mDatas;
    private View mHeadView;
    private TextView mTvHeadAllStory;
    private TextView mTvHeadAllStoryCount;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_all_story;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        mXrvAllStory.setLayoutManager(new LinearLayoutManager(this));
        mXrvAllStory.setLoadingMoreEnabled(false);
        mXrvAllStory.setPullRefreshEnabled(false);
        mXrvAllStory.addHeaderView(mHeadView);
        mXrvAllStory.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        StoryListAdapter listAdapter = new StoryListAdapter(this, R.layout.item_story_list, mDatas);

        mXrvAllStory.setAdapter(listAdapter);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_all_story, (ViewGroup) findViewById(android.R.id.content), false);

        mTvHeadAllStory = (TextView) mHeadView.findViewById(R.id.tv_head_all_story);
        mTvHeadAllStoryCount = (TextView) mHeadView.findViewById(R.id.tv_head_all_story_count);

    }

    private void initData() {
        mDatas = new ArrayList<>();
        StoryListInfo storyListInfo1 = new StoryListInfo();
        storyListInfo1.setStoryName("地图步数");
        storyListInfo1.setStoryDes("坚持不放弃");
        storyListInfo1.setStoryResult("10条路线");
        mDatas.add(storyListInfo1);

        StoryListInfo storyListInfo2 = new StoryListInfo();
        storyListInfo2.setStoryName("走路的意义");
        storyListInfo2.setStoryDes("步数");
        storyListInfo2.setStoryResult("2条路线");
        mDatas.add(storyListInfo2);

        StoryListInfo storyListInfo3 = new StoryListInfo();
        storyListInfo3.setStoryName("走路的故事");
        storyListInfo3.setStoryDes("百变的我");
        storyListInfo3.setStoryResult("10条路线");
        mDatas.add(storyListInfo3);

    }

    private void initToolBar() {
        mToolbarAllStory = (ArthurToolBar) findViewById(R.id.toolbar_all_story);
        mToolbarAllStory.setBtnEnabled(true);
        mToolbarAllStory.setOnLeftClickListener(this);
        mToolbarAllStory.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
