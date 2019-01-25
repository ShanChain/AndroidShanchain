package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
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
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
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

public class MyCreateCouponFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {


    @Bind(R.id.rv_coupon_list)
    RecyclerView rvCouponList;
    @Bind(R.id.srl_coupon_list)
    BGARefreshLayout srlCouponList;

    private List<CouponSubInfo> couponList = new ArrayList();
    private ProgressDialog mDialog;

    private CouponListAdapter adapter;
    private String characterId = SCCacheUtils.getCacheCharacterId();
    private String userId = SCCacheUtils.getCacheUserId();
    private String roomId;
    private int page = 0;
    private int size = 10;
    private List<CouponSubInfo> loadMore = new ArrayList<>();
    private String last;
    private int currentPage;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_coupon_list, null);
    }

    @Override
    public void initData() {
        srlCouponList.setDelegate(this);
        srlCouponList.beginLoadingMore();
//        srlCouponList.beginRefreshing();
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);//微博效果
        srlCouponList.setRefreshViewHolder(refreshViewHolder);
        // 设置正在加载更多时不显示加载更多控件
        srlCouponList.setIsShowLoadingMoreView(true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("加载更多");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCouponList.setLayoutManager(linearLayoutManager);
        getMycreateCouponList();

    }

    private void getMycreateCouponList() {
        SCHttpUtils.get()
                .url(HttpApi.COUPONS_CREATE_LIST)
                .addParams("pageNo", page + "")
                .addParams("pageSize", size + "")
                .addParams("subuserId", characterId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String list = JSONObject.parseObject(data).getString("list");
                            couponList = JSONObject.parseArray(list, CouponSubInfo.class);
                            last = JSONObject.parseObject(data).getString("last");
                            String next = JSONObject.parseObject(data).getString("next");
                            String pageNum = JSONObject.parseObject(data).getString("pageNo");
                            currentPage = Integer.valueOf(pageNum);
                            page = Integer.valueOf(next);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            rvCouponList.setLayoutManager(layoutManager);
                            adapter = new CouponListAdapter(getContext(), couponList, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two});
                            rvCouponList.setAdapter(adapter);
                        }
                    }
                });
    }

    public void showProgress() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /* 下拉刷新 */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 0;
        getMycreateCouponList();
        refreshLayout.endRefreshing();
    }

    /* 上拉加载 */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        if (page != currentPage) {
            SCHttpUtils.get()
                    .url(HttpApi.COUPONS_CREATE_LIST)
                    .addParams("pageNo", page + "")
                    .addParams("pageSize", size + "")
                    .addParams("subuserId", characterId + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d(TAG, "网络异常");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String code = JSONObject.parseObject(response).getString("code");
                            if (NetErrCode.SUC_CODE.equals(code)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String list = JSONObject.parseObject(data).getString("list");
                                last = JSONObject.parseObject(data).getString("last");
                                String next = JSONObject.parseObject(data).getString("next");
                                String pageNo = JSONObject.parseObject(data).getString("pageNo");
                                page = Integer.valueOf(next);
                                int lastPage = Integer.valueOf(last);
                                currentPage = Integer.valueOf(pageNo);
                                if (currentPage <= Integer.valueOf(last)) {
                                    last = JSONObject.parseObject(data).getString("last");
                                    page = Integer.valueOf(next);
                                    loadMore = JSONObject.parseArray(list, CouponSubInfo.class);
                                    adapter.addData(loadMore);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
        if (page == currentPage) {
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
