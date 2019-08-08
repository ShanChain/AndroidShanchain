package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.presenter.CouponListPresenter;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.CouponListPresenterImpl;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.CounponListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class MyCreateCouponFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CounponListView {


    @Bind(R.id.rv_coupon_list)
    RecyclerView rvCouponList;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

    private List<CouponSubInfo> couponList = new ArrayList();
    private CouponListAdapter adapter;
    private String characterId = SCCacheUtils.getCacheCharacterId();
    private int page = 0;
    private int size = Constants.pageSize;
    private CouponListPresenter mPresenter;
    private boolean isLast = false;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_coupon_list, null);
    }

    @Override
    public void initData() {
        adapter = new CouponListAdapter(getContext(), couponList, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two});
        mPresenter = new CouponListPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCouponList.setLayoutManager(linearLayoutManager);
        rvCouponList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getMycreateCouponList();
        initLoadMoreListener();
    }

    private void getMycreateCouponList() {
        mPresenter.getMyCreateCounponList(characterId,page,size,Constants.pullRefress);
    }

    //相关监听
    private void initLoadMoreListener() {
        //上拉加载
        rvCouponList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isLast) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    page++;
                    getMycreateCouponList();
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


    @Override
    public void onRefresh() {
        page = 0;
        getMycreateCouponList();
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setCounponList(String response, int pullType) {

    }

    @Override
    public void setMyGetCounponList(String response, int pullType) {

    }

    @Override
    public void setMyCreateCounponList(String response, int pullType) {
        refreshLayout.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (NetErrCode.SUC_CODE.equals(code)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            List<CouponSubInfo> mList = JSONObject.parseArray(list, CouponSubInfo.class);
            if(mList.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                couponList.clear();
                couponList.addAll(mList);
                rvCouponList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                adapter.addData(mList);
            }
            if(couponList!=null && couponList.size()>0){
                llNotdata.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }else {
                llNotdata.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }
}
