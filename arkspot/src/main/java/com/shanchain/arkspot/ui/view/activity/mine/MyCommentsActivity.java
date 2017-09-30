package com.shanchain.arkspot.ui.view.activity.mine;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.MyCommentsAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.MyCommentsInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.other.SCEmptyView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MyCommentsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.tb_my_comments)
    ArthurToolBar mTbMyComments;
    @Bind(R.id.rv_comments)
    RecyclerView mRvComments;
    @Bind(R.id.srl_my_comments)
    SwipeRefreshLayout mSrlMyComments;
    private List<MyCommentsInfo> datas;
    private MyCommentsAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_comments;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 12; i ++) {
            MyCommentsInfo info = new MyCommentsInfo();
            datas.add(info);
        }
    }

    private void initRecyclerView() {

        mSrlMyComments.setOnRefreshListener(this);

        View emptyView = new SCEmptyView(this,R.string.str_empty_my_comments,R.mipmap.abs_comment_icon_thumbsup_default_1);
        mRvComments.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyCommentsAdapter(R.layout.item_my_comments,datas);
        mRvComments.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 1), getResources().getColor(R.color.colorDivider)));
        mRvComments.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
    }



    private void initToolBar() {
        mTbMyComments.setBtnEnabled(true,false);
        mTbMyComments.setOnLeftClickListener(this);
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
                datas.clear();
                mAdapter.notifyDataSetChanged();
                mSrlMyComments.setRefreshing(false);
            }
        }, 1000);
    }
}
