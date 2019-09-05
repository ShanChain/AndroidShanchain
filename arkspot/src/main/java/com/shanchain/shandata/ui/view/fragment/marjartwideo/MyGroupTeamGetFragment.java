package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupTeamAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.presenter.impl.MyGroupTeamPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :我的小分队
 */
public class MyGroupTeamGetFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MyGroupTeamView {
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.tv_create)
    TextView tvCreate;
    @Bind(R.id.tv_join)
    TextView tvJoin;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

    private GroupTeamAdapter mGroupTeamAdapter;
    private List<GroupTeamBean> mList = new ArrayList<>();
    private MyGroupTeamPresenter mPresenter;
    private int pageIndex = 1;
    private boolean isLast = false;
    private int currentType = 1;//1我创建的矿区；2我加入的矿区

    public static MyGroupTeamGetFragment getInstance() {
        MyGroupTeamGetFragment fragment = new MyGroupTeamGetFragment();
        return fragment;
    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_get_my_team, null);
    }

    @Override
    public void initData() {
        mPresenter = new MyGroupTeamPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        mGroupTeamAdapter = new GroupTeamAdapter(R.layout.group_team_item, mList);
        mGroupTeamAdapter.setType(2);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color), getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
        mGroupTeamAdapter.notifyDataSetChanged();


        initLoadMoreListener();
    }

    //获取我的矿区数据
    private void getMyMiningData(int type,int pullType){
        if(type ==1){
            mPresenter.queryGroupTeam("", SCCacheUtils.getCacheUserId(),"",pageIndex, Constants.pageSize,pullType,0);
        }else {
            mPresenter.queryGroupTeam(SCCacheUtils.getCacheUserId(), "","",pageIndex, Constants.pageSize,pullType,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyMiningData(currentType,Constants.pullRefress);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getMyMiningData(currentType,Constants.pullRefress);
    }

    //我创建的
    @OnClick(R.id.tv_create)
    void myCreate(){
        currentType = 1;
        pageIndex = 1;
        changeBackgroup(currentType);
        getMyMiningData(currentType,Constants.pullRefress);
    }

    //我参加的
    @OnClick(R.id.tv_join)
    void myJoined(){
        currentType = 2;
        pageIndex = 1;
        changeBackgroup(currentType);
        getMyMiningData(currentType,Constants.pullRefress);
    }

    //点击变化背景和字体颜色
    private void changeBackgroup(int type){
        if(type==1){
            tvCreate.setTextColor(getResources().getColor(R.color.white));
            tvCreate.setBackgroundColor(getResources().getColor(R.color.login_marjar_color));
            tvJoin.setTextColor(getResources().getColor(R.color.colorBtnBg));
            tvJoin.setBackgroundColor(getResources().getColor(R.color.white));
        }else {
            tvCreate.setTextColor(getResources().getColor(R.color.colorBtnBg));
            tvCreate.setBackgroundColor(getResources().getColor(R.color.white));
            tvJoin.setTextColor(getResources().getColor(R.color.white));
            tvJoin.setBackgroundColor(getResources().getColor(R.color.login_marjar_color));
        }
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
    public void setQuearyMygoupTeamResponse(String response, int pullType) {
        refreshLayout.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            List<GroupTeamBean> listDara = JSONObject.parseArray(list,GroupTeamBean.class);
            if(listDara.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                mList.clear();
                mList.addAll(listDara);
                recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
                mGroupTeamAdapter.notifyDataSetChanged();
            }else {
                mGroupTeamAdapter.addData(listDara);
            }
            if(mList!=null && mList.size()>0){
                llNotdata.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }else {
                llNotdata.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setCheckPasswResponse(String response) {

    }

    @Override
    public void setCheckPassFaile() {

    }

    @Override
    public void setAddMinigRoomResponse(String response) {

    }

    @Override
    public void setdeleteDigiRoomIdResponse(String response) {

    }

    @Override
    public void setUpdateMiningRoomResponse(String response) {

    }

    @Override
    public void setCheckUserHasWalletResponse(String response) {

    }

    //上拉加载监听
    private void initLoadMoreListener() {
        //上拉加载
        recyclerViewCoupon.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mGroupTeamAdapter.getItemCount()){
                    pageIndex ++;
                    getMyMiningData(currentType,Constants.pillLoadmore);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        mGroupTeamAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupTeamBean groupTeamBean = (GroupTeamBean) adapter.getItem(position);
                if(groupTeamBean!=null){
                    LogUtils.d("----->>enter room "+groupTeamBean.toString());
                    gotoMessageRoom(groupTeamBean);
                }
            }
        });
    }
    //进入聊天室
    private void gotoMessageRoom(GroupTeamBean groupTeamBean){
        if(TextUtils.isEmpty(groupTeamBean.getRoomId()))return;
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("roomId", "" + groupTeamBean.getRoomId());
        intent.putExtra("roomName", "" + groupTeamBean.getRoomName());
        startActivity(intent);
    }
}
