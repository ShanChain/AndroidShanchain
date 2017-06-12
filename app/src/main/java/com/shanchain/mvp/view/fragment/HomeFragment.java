package com.shanchain.mvp.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HomeAdapter;
import com.shanchain.base.BaseFragment;
import com.shanchain.mvp.model.DynamicInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/16.
 * 主页页面
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.xrv_home)
    XRecyclerView mXrvHome;
    private List<DynamicInfo> datas;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_home, null);
    }

    @Override
    public void initData() {
        getData();
        mXrvHome.setLayoutManager(new LinearLayoutManager(mActivity));
        View headView = View.inflate(mActivity,R.layout.headview_home,null);
        mXrvHome.addHeaderView(headView);
        mXrvHome.setPullRefreshEnabled(false);
        mXrvHome.setLoadingMoreEnabled(false);
        HomeAdapter homeAdapter = new HomeAdapter(mActivity,R.layout.item_home,datas);
        mXrvHome.setAdapter(homeAdapter);

    }

    private void getData() {
        datas= new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            DynamicInfo dynamicInfo = new DynamicInfo();
            dynamicInfo.setLeft1("什么鬼" + i);
            datas.add(dynamicInfo);
        }
    }


}
