package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.widgets.SCInputDialog;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupTeamAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.presenter.impl.MyGroupTeamPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :可加入矿区
 */
public class MyGroupTeamFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MyGroupTeamView {
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

    private GroupTeamAdapter mGroupTeamAdapter;
    private List<GroupTeamBean> mList = new ArrayList<>();
    private MyGroupTeamPresenter mPresenter;
    private int pageIndex = 0;
    private boolean isLast = false;
    private SCInputDialog mScInputDialog;
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
        mPresenter = new MyGroupTeamPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        mGroupTeamAdapter = new GroupTeamAdapter(R.layout.group_team_item,mList);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
        mGroupTeamAdapter.notifyDataSetChanged();

        mPresenter.queryGroupTeam("","",pageIndex, Constants.pageSize,Constants.pullRefress);

        initLoadMoreListener();
    }

    @Override
    public void onRefresh() {
        pageIndex=0;
        mPresenter.queryGroupTeam("","",pageIndex, Constants.pageSize,Constants.pullRefress);

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
                    mPresenter.queryGroupTeam("","",pageIndex, Constants.pageSize,Constants.pillLoadmore);
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
                    if(groupTeamBean.getCreateUser().equals(SCCacheUtils.getCacheUserId())){//自己创建的直接进入聊天室
                        gotoMessageRoom(groupTeamBean);
                    }else {
                        //不是自己创建的
                        if(groupTeamBean.getUserCount() ==4){//如果人数已满，则直接进入聊天室，但不能发言
                            gotoMessageRoom(groupTeamBean);
                        }else {//人数未满则弹窗提示支付进入
                            isJoinMiningTip();
                        }
                    }
                }
            }
        });

    }

    //进入聊天室
    private void gotoMessageRoom(GroupTeamBean groupTeamBean){
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("roomId", "" + groupTeamBean.getRoomId());
        intent.putExtra("roomName", "" + groupTeamBean.getRoomName());
        startActivity(intent);
    }
    //弹窗提示支付进入矿区
    private void isJoinMiningTip(){
        mScInputDialog = new SCInputDialog(getActivity(), getString(R.string.add_meta_c),
                getString(R.string.enter_m_name));
        //显示输入元社区
        mScInputDialog.setCallback(new Callback() {//确定
            @Override
            public void invoke() {
                if(TextUtils.isEmpty(mScInputDialog.getEtContent().getText())){
                    ToastUtils.showToast(getActivity(), R.string.enter_char_room);
                    return;
                }
                mScInputDialog.dismiss();
                //先支付
//                createRoomPayforTip();
            }
        }, new Callback() {//取消
            @Override
            public void invoke() {

            }
        });
        //显示输入元社区弹窗
        mScInputDialog.show();
    }
}
