package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.presenter.CouponListPresenter;
import com.shanchain.shandata.ui.presenter.impl.CouponListPresenterImpl;
import com.shanchain.shandata.ui.view.activity.coupon.CreateCouponActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.VerifiedActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.CounponListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :马甲券界面
 */
public class CouponFragment extends BaseFragment implements CounponListView,
        ArthurToolBar.OnRightClickListener, SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.tb_coupon)
    ArthurToolBar toolBar;
    @Bind(R.id.linear_add_coupon)
    LinearLayout linearAddCoupon;
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayoutArsgame;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;
    private View footerView;
    private String roomid;
    private int pageNo = 1;
    private String subUserId = SCCacheUtils.getCacheCharacterId();
    private List<CouponSubInfo> couponInfoList = new ArrayList<>();
    private CouponListAdapter couponListAdapter;
    private CouponListPresenter mCouponListPresenter;
    private boolean isLast = false;
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.activity_coupon_list_new, null);
    }

    public static CouponFragment newInstance() {
        CouponFragment fragment = new CouponFragment();
        return fragment;
    }

    @Override
    public void initData() {
        roomid = SCCacheUtils.getCacheRoomId() + "";
        refreshLayoutArsgame.setOnRefreshListener(this);
        refreshLayoutArsgame.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        initToolBar();

        mCouponListPresenter = new CouponListPresenterImpl(this);

    }
    //初始化标题栏
    private void initToolBar() {
        toolBar.setTitleText(getResources().getString(R.string.nav_coupon));
        toolBar.setRightText(getString(R.string.my_));
        toolBar.setLeftTitleLayoutView(View.INVISIBLE);
        toolBar.setOnRightClickListener(this);

        couponListAdapter = new CouponListAdapter(getActivity(), couponInfoList, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two});
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(couponListAdapter);
        couponListAdapter.notifyDataSetChanged();

        initLoadMoreListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCouponListPresenter.getCounponList(subUserId,pageNo, Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(getActivity(), MyCouponListActivity.class);
        startActivity(intent);
    }

    /**
     * 添加马甲券
     */
    @OnClick(R.id.linear_add_coupon)
    void addCoupon(){
        Intent detailIntent = new Intent(getActivity(), CreateCouponActivity.class);
        detailIntent.putExtra("roomId", roomid);
        startActivity(detailIntent);
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isLast = false;
        mCouponListPresenter.getCounponList(subUserId,pageNo,Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog(true);
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setCounponList(String response,int pullType) {
        refreshLayoutArsgame.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (NetErrCode.SUC_CODE.equals(code)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            if (list==null) {
                return;
            }
            List<CouponSubInfo> counpList = JSONObject.parseArray(list, CouponSubInfo.class);
            if(couponInfoList.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                couponInfoList.clear();
                couponInfoList.addAll(counpList);
                recyclerViewCoupon.setAdapter(couponListAdapter);
                couponListAdapter.notifyDataSetChanged();
            }else {
                couponListAdapter.addData(counpList);
            }
            if(couponInfoList!=null && couponInfoList.size()>0){
                llNotdata.setVisibility(View.GONE);
                refreshLayoutArsgame.setVisibility(View.VISIBLE);
            }else {
                llNotdata.setVisibility(View.VISIBLE);
                refreshLayoutArsgame.setVisibility(View.GONE);
            }
        } else if (NetErrCode.UN_VERIFIED_CODE.equals(code)) {
            Intent intent = new Intent(getActivity(), VerifiedActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void setMyGetCounponList(String response, int pullType) {

    }

    @Override
    public void setMyCreateCounponList(String response, int pullType) {

    }

    //上拉加载
    private void initLoadMoreListener() {
        recyclerViewCoupon.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == couponListAdapter.getItemCount()){
                    pageNo ++;
                    mCouponListPresenter.getCounponList(subUserId,pageNo,Constants.pageSize,Constants.pullRefress);
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
