package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/7/22
 * Describe :广场列表
 */
public class SquareFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_square, null);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRefresh() {

    }
}
