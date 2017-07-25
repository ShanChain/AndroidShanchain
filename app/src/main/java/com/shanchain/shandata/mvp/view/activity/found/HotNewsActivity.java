package com.shanchain.shandata.mvp.view.activity.found;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HotNewsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.HotNewsInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HotNewsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, XRecyclerView.LoadingListener {

    ArthurToolBar mToolbarHotNews;
    @Bind(R.id.xrv_hot_news)
    XRecyclerView mXrvHotNews;
    private List<HotNewsInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_hot_news;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mXrvHotNews.setLayoutManager(layoutManager);
        mXrvHotNews.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvHotNews.setLoadingMoreEnabled(false);

        mXrvHotNews.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        HotNewsAdapter hotNewsAdapter = new HotNewsAdapter(this, R.layout.item_hot_news, mDatas);
        mXrvHotNews.setAdapter(hotNewsAdapter);
        mXrvHotNews.setLoadingListener(this);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            HotNewsInfo hotNewsInfo = new HotNewsInfo();
            mDatas.add(hotNewsInfo);
        }
    }

    private void initToolBar() {
        mToolbarHotNews = (ArthurToolBar) findViewById(R.id.toolbar_hot_news);
        mToolbarHotNews.setBtnEnabled(true, false);
        mToolbarHotNews.setBtnVisibility(true, false);
        mToolbarHotNews.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mXrvHotNews.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }
}
