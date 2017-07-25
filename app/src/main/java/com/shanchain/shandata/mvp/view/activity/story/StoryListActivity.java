package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.StoryListInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StoryListActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarStoryList;
    @Bind(R.id.xrv_story_list)
    XRecyclerView mXrvStoryList;
    @Bind(R.id.activity_story_list)
    LinearLayout mActivityStoryList;
    private List<StoryListInfo> mDatas;
    private View mHeadView;
    private TextView mTvHeadStoryTag;
    private TextView mTvHeadStoryCount;
    private TextView mTvHeadStoryShow;
    private PopupWindow mPopupWindow;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_list;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            StoryListInfo storyListInfo = new StoryListInfo();
            mDatas.add(storyListInfo);
        }
    }

    private void initRecyclerView() {
        initHeadView();
        mXrvStoryList.setLayoutManager(new LinearLayoutManager(this));
        mXrvStoryList.setLoadingMoreEnabled(false);
        mXrvStoryList.setPullRefreshEnabled(false);
        mXrvStoryList.addHeaderView(mHeadView);
        mXrvStoryList.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        StoryListAdapter listAdapter = new StoryListAdapter(this, R.layout.item_story_list, mDatas);
        mXrvStoryList.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(StoryListActivity.this,StoryHomePagerActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                return false;
            }
        });

    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_story_list, (ViewGroup) findViewById(android.R.id.content), false);

        mTvHeadStoryTag = (TextView) mHeadView.findViewById(R.id.tv_head_story_tag);
        mTvHeadStoryCount = (TextView) mHeadView.findViewById(R.id.tv_head_story_count);
        mTvHeadStoryShow = (TextView) mHeadView.findViewById(R.id.tv_head_story_show);

        mTvHeadStoryShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = View.inflate(StoryListActivity.this, R.layout.pop_show, null);
                if (mPopupWindow == null){
                    mPopupWindow = new PopupWindow(view,
                            DensityUtils.dip2px(StoryListActivity.this,146),
                            DensityUtils.dip2px(StoryListActivity.this,52));
                }
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDialogBtn)));
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.showAsDropDown(mTvHeadStoryShow,0,-DensityUtils.dip2px(StoryListActivity.this,1));
                mTvHeadStoryShow.setBackgroundResource(R.drawable.shape_bg_box_story_pressed);
                final TextView tvPopShowAll = (TextView) view.findViewById(R.id.tv_pop_show_all);
                final TextView tvPopShowFinish = (TextView) view.findViewById(R.id.tv_pop_show_finish);
                //显示全部文本的点击事件
                tvPopShowAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTvHeadStoryShow.setText(tvPopShowAll.getText().toString());
                        mPopupWindow.dismiss();
                    }
                });
                //点击显示未完成的点击事件
                tvPopShowFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTvHeadStoryShow.setText(tvPopShowFinish.getText().toString());
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        LogUtils.d("pop消失了！！！！！！");
                        mTvHeadStoryShow.setBackgroundResource(R.drawable.shape_bg_box_reservation);
                    }
                });

            }
        });

    }

    private void initToolBar() {
        mToolbarStoryList = (ArthurToolBar) findViewById(R.id.toolbar_story_list);
        mToolbarStoryList.setBtnEnabled(true);
        mToolbarStoryList.setOnLeftClickListener(this);
        mToolbarStoryList.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

}
