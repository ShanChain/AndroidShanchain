package com.shanchain.shandata.ui.view.activity.settings;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ClientListAdapter;
import com.shanchain.shandata.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.DefaultUser;

/**
 * Created by WealChen
 * Date : 2019/9/6
 * Describe :
 */
public class ClientListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        ArthurToolBar.OnLeftClickListener {
    @Bind(R.id.tb_about)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private ClientListAdapter mListAdapter;
    private List<DefaultUser> mList = new ArrayList<>();
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_client_service;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        mListAdapter = new ClientListAdapter(R.layout.activity_client_item,mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mListAdapter);

        for (int i = 0; i <3; i++) {
            DefaultUser defaultUser = new DefaultUser(i,"客服"+i+1,"");
            defaultUser.setHxUserId("11111");
            defaultUser.setSignature("");
            mList.add(defaultUser);
        }
        mListAdapter.notifyDataSetChanged();
    }

    private void initToolBar() {
        tbTaskComment.setTitleText(getString(R.string.client_center));
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setOnLeftClickListener(this);

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
