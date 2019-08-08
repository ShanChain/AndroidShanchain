package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupTeamAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.GroupTeamBean;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :可加入矿区
 */
public class MyGroupTeamFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private GroupTeamAdapter mGroupTeamAdapter;
    private List<GroupTeamBean> mList = new ArrayList<>();

    public static MyGroupTeamFragment getInstance() {
        MyGroupTeamFragment fragment = new MyGroupTeamFragment();
        return fragment;
    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_my_team, null);
    }

    @Override
    public void initData() {
        refreshLayout.setOnRefreshListener(this);
        mGroupTeamAdapter = new GroupTeamAdapter(R.layout.group_team_item,mList);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        for (int i = 0; i <5; i++) {
            GroupTeamBean b = new GroupTeamBean();
            b.setId(i+1);
            b.setName(i+"");
            mList.add(b);
        }
        recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
        mGroupTeamAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

}
