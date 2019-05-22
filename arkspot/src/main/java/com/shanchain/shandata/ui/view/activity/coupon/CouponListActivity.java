package com.shanchain.shandata.ui.view.activity.coupon;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.VerifiedActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;

public class CouponListActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener,
        ArthurToolBar.OnRightClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private ArthurToolBar toolBar;
    private LinearLayout addCoupon;
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private int pageNo = 1, pageSize = 10;
    private String roomid;
    private String subUserId = SCCacheUtils.getCacheCharacterId();
    private List<CouponSubInfo> couponInfoList = new ArrayList<>();
    private String last;
    private List<CouponSubInfo> loadMore;
    private CouponListAdapter adapter;
    private CouponListAdapter couponListAdapter;
    private int currentPage = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_coupon_list;
    }

    @Override
    protected void initViewsAndEvents() {
        roomid = getIntent() != null ? getIntent().getStringExtra("roomId") : SCCacheUtils.getCacheRoomId() + "";
        initToolBar();
        initView();
        initData();
    }

    //初始化视图
    private void initView() {
        refreshLayout = findViewById(R.id.refresh_layout);
        addCoupon = findViewById(R.id.linear_add_coupon);
        recyclerView = findViewById(R.id.recycler_view_coupon);
        refreshLayout.setDelegate(this);
        refreshLayout.beginLoadingMore();
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);//微博效果
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
//         设置正在加载更多时不显示加载更多控件
        refreshLayout.setIsShowLoadingMoreView(true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("加载更多");

        //添加卡劵
        addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(CouponListActivity.this, CreateCouponActivity.class);
                detailIntent.putExtra("roomId", roomid);
                startActivity(detailIntent);
            }
        });
    }

    //初始化标题栏
    private void initToolBar() {
        toolBar = findViewById(R.id.tb_coupon);
        toolBar.setTitleText(getResources().getString(R.string.nav_coupon));
        toolBar.setRightText("我的");
        toolBar.setOnLeftClickListener(this);
        toolBar.setOnRightClickListener(this);
    }

    //初始化数据
    private void initData() {
        showLoadingDialog(true);
        SCHttpUtils.getAndToken()
//                .url(HttpApi.COUPONS_LIST)
                .url(HttpApi.COUPONS_ALL_LIST)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("subuserId", subUserId + "")
//                .addParams("roomId", roomid + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, final int id) {
                        closeLoadingDialog();
                        String code = JSONObject.parseObject(response).getString("code");
                        final String msg = JSONObject.parseObject(response).getString("msg");
                        closeLoadingDialog();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
//                                ToastUtils.showToast(CouponListActivity.this, "" + msg);
                            }
                        });
                        if (NetErrCode.SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String list = JSONObject.parseObject(data).getString("list");
                            String next = JSONObject.parseObject(data).getString("next");
                            String last = JSONObject.parseObject(data).getString("last");
                            String pageNum = JSONObject.parseObject(data).getString("pageNo");
                            currentPage = Integer.valueOf(pageNum);
                            pageNo = Integer.valueOf(next);
                            if (TextUtils.isEmpty(list)) {
                                return;
                            }
                            pageNo = Integer.valueOf(next);
                            couponInfoList = JSONObject.parseArray(list, CouponSubInfo.class);
                            couponListAdapter = new CouponListAdapter(CouponListActivity.this, couponInfoList, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two});
                            LinearLayoutManager layoutManager = new LinearLayoutManager(CouponListActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(couponListAdapter);

                        } else if (NetErrCode.UN_VERIFIED_CODE.equals(code)) {
                            Intent intent = new Intent(CouponListActivity.this, VerifiedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(CouponListActivity.this, MyCouponListActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCouponList(EventMessage eventMessage) {
        if (eventMessage.getCode() == 0) {
            initData();
        }

    }

    /* 下拉刷新 */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        initData();
        refreshLayout.endRefreshing();
    }

    /* 上拉加载 */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        refreshLayout.setIsShowLoadingMoreView(true);
        if (pageNo != currentPage) {
            SCHttpUtils.get()
//                    .url(HttpApi.COUPONS_LIST)
                    .url(HttpApi.COUPONS_ALL_LIST)
                    .addParams("pageNo", pageNo + "")
                    .addParams("pageSize", pageSize + "")
                    .addParams("subuserId", subUserId + "")
//                    .addParams("roomId", roomid + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            closeLoadingDialog();
                            refreshLayout.setIsShowLoadingMoreView(false);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            closeLoadingDialog();
                            String code = JSONObject.parseObject(response).getString("code");
                            final String msg = JSONObject.parseObject(response).getString("msg");
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
//                                ToastUtils.showToast(CouponListActivity.this, "" + msg);
                                }
                            });
                            if (NetErrCode.SUC_CODE.equals(code)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String list = JSONObject.parseObject(data).getString("list");
                                last = JSONObject.parseObject(data).getString("last");
                                String next = JSONObject.parseObject(data).getString("next");
                                String pageNum = JSONObject.parseObject(data).getString("pageNo");
                                currentPage = Integer.valueOf(pageNum);
                                if (TextUtils.isEmpty(list)) {
                                    return;
                                }
                                if (pageNo <= Integer.valueOf(last)) {
                                    last = JSONObject.parseObject(data).getString("last");
                                    pageNo = Integer.valueOf(next);
                                    loadMore = JSONObject.parseArray(list, CouponSubInfo.class);
                                    couponListAdapter.addData(loadMore);
                                    couponListAdapter.notifyDataSetChanged();
                                }
//                                refreshLayout.endLoadingMore();
                                refreshLayout.setIsShowLoadingMoreView(false);
                            } else if (NetErrCode.UN_VERIFIED_CODE.equals(code)) {
                                Intent intent = new Intent(CouponListActivity.this, VerifiedActivity.class);
                                startActivity(intent);

                            }
                        }
                    });
        }
        if (pageNo == currentPage) {
            refreshLayout.setIsShowLoadingMoreView(false);
            return true;
        }
        return false;
    }
}
