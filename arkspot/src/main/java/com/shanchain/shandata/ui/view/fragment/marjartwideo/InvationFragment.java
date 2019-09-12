package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.InvationDataAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.InvationRecordBean;
import com.shanchain.shandata.ui.presenter.ReturnInvationPresenter;
import com.shanchain.shandata.ui.presenter.impl.ReturnInvationPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.view.ReturnInvationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/8/23
 * Describe :
 */
public class InvationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ReturnInvationView {

    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

    private int type;
    private int pageIndex = 1;
    private boolean isLast = false;
    private ReturnInvationPresenter mPresenter;
    private InvationDataAdapter mAdapter;
    private List<InvationRecordBean> mRecordBeanList = new ArrayList<>();
    public static InvationFragment getInstance(int type) {
        InvationFragment fragment = new InvationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_invation, null);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");

        mPresenter = new ReturnInvationPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        mAdapter = new InvationDataAdapter(R.layout.frament_invation_item,mRecordBeanList);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.not_data_footer_view, null);
        mAdapter.addFooterView(view);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getData();
        initLoadMoreListener();
    }

    private void getData(){
        pageIndex =1;
        if(type ==1){
            mPresenter.queryUserInvationRecord(SCCacheUtils.getCacheUserId(),pageIndex, Constants.pageSize,Constants.pullRefress);
        }else {
            mPresenter.queryUserInvationRecord(SCCacheUtils.getCacheUserId(),pageIndex, Constants.pageSize,Constants.pullRefress);
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void showProgressStart() {

    }

    @Override
    public void showProgressEnd() {

    }

    @Override
    public void setInvationDataResponse(String response) {

    }

    @Override
    public void setQuearyInvatRecordResponse(String response, int pullType) {
        refreshLayout.setRefreshing(false);
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            List<InvationRecordBean> listDara = JSONObject.parseArray(list,InvationRecordBean.class);
            if(listDara.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                mRecordBeanList.clear();
                mRecordBeanList.addAll(listDara);
                recyclerViewCoupon.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else {
                mAdapter.addData(listDara);
            }
            if(mRecordBeanList!=null && mRecordBeanList.size()>0){
                llNotdata.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }else {
                llNotdata.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }

    //上拉加载监听
    private void initLoadMoreListener() {
        //上拉加载
        recyclerViewCoupon.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isLast) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    pageIndex++;
                    mPresenter.queryUserInvationRecord(SCCacheUtils.getCacheUserId(),pageIndex, Constants.pageSize,Constants.pillLoadmore);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
