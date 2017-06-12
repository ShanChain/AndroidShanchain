package com.shanchain.mvp.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HomeAdapter;
import com.shanchain.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/16.
 * 主页页面
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.xrv_home)
    XRecyclerView mXrvHome;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_home, null);
    }

    @Override
    public void initData() {
        mXrvHome.setLayoutManager(new LinearLayoutManager(mActivity));
        View headView = View.inflate(mActivity,R.layout.headview_home,null);
        mXrvHome.addHeaderView(headView);
        HomeAdapter homeAdapter = new HomeAdapter(mActivity,R.layout.item_home,datas);
        mXrvHome.setAdapter();

    }


}
