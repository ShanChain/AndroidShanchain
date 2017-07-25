package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.JoinRouteAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.StoryPagerInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class JoinRouteActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarJoinRoute;
    @Bind(R.id.xrv_join_route)
    XRecyclerView mXrvJoinRoute;
    private List<StoryPagerInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_join_route;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mXrvJoinRoute.setLayoutManager(new LinearLayoutManager(this));
        mXrvJoinRoute.setPullRefreshEnabled(false);
        mXrvJoinRoute.setLoadingMoreEnabled(false);

        mXrvJoinRoute.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 20),
                getResources().getColor(R.color.colorWhite)));

        JoinRouteAdapter joinRouteAdapter = new JoinRouteAdapter(this,R.layout.item_join_route,mDatas);
        mXrvJoinRoute.setAdapter(joinRouteAdapter);

        joinRouteAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(JoinRouteActivity.this,RecordStoryFragmentActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 12; i ++) {
            StoryPagerInfo storyPagerInfo = new StoryPagerInfo();
            mDatas.add(storyPagerInfo);
        }
    }

    private void initToolBar() {
        mToolbarJoinRoute = (ArthurToolBar) findViewById(R.id.toolbar_join_route);
        mToolbarJoinRoute.setBtnVisibility(true,false);
        mToolbarJoinRoute.setBtnEnabled(true,false);
        mToolbarJoinRoute.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
