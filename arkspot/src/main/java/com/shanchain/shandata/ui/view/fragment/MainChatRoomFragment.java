package com.shanchain.shandata.ui.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HotChatRoomAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.widgets.other.MyLoadMoreView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/5/22
 * Describe :
 */
public class MainChatRoomFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycler_view_arsgame)
    RecyclerView recyclerViewArsgame;
    @Bind(R.id.refresh_layout_arsgame)
    SwipeRefreshLayout refreshLayoutArsgame;
    @Bind(R.id.search_view)
    SearchView searchView;

    private String localVersion;
    private int totalPage, size = 10, pageNo = 0, searchPage = 0;
    private boolean last;
    private List<HotChatRoom> searchRoomList = new ArrayList<>(),
            adapterChatRoomList = new ArrayList<>();
    private HotChatRoomAdapter mQuickAdapter;
    private View footerView;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_main_hot_room, null);
    }

    @Override
    public void initData() {
        refreshLayoutArsgame.setOnRefreshListener(this);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        localVersion = VersionUtils.getVersionName(getContext());
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("version", localVersion + "")
                .addParams("page", pageNo + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = SCJsonUtils.parseString(data, "content");
                            totalPage = SCJsonUtils.parseInt(data, "totalPage");
                            String isLast = JSONObject.parseObject(response).getString("last");
                            last = Boolean.valueOf(isLast);
                            List<HotChatRoom> hotChatRoomList = JSONArray.parseArray(content, HotChatRoom.class);
                            if (hotChatRoomList == null) {
                                return;
                            }
                            adapterChatRoomList.addAll(hotChatRoomList);
                            //                                intent.putExtra("isInCharRoom", isIn);
                            mQuickAdapter = new HotChatRoomAdapter(getContext(), hotChatRoomList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerViewArsgame.setLayoutManager(layoutManager);
                            mQuickAdapter.setLoadMoreView(new MyLoadMoreView());
                            mQuickAdapter.setFooterView(footerView);
                            initLoadMoreListener();
                            recyclerViewArsgame.setAdapter(mQuickAdapter);
                            mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    //获取当前item的对象
                                    HotChatRoom hotChatRoom = (HotChatRoom) adapter.getItem(position);
                                    showLoadingDialog();
                                    isInBlacklist(hotChatRoom.getRoomId(), hotChatRoom);
                                }
                            });
                        }
                        closeLoadingDialog();
                    }
                });

        //搜索逻辑
        searchView.setIconifiedByDefault(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mQuickAdapter != null && adapterChatRoomList != null) {
                    mQuickAdapter.replaceData(adapterChatRoomList);
                }
                searchView.clearFocus();
                pageNo = 0;
                last = false;
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query) && mQuickAdapter != null) {
                    mQuickAdapter.getData().clear();//清楚列表所有数据
                    SCHttpUtils.getAndToken()
                            .url(HttpApi.SEARCH_ROOM)
                            .addParams("page", searchPage + "")
                            .addParams("size", size + "")
                            .addParams("roomName", query + "")
                            .build()
                            .execute(new SCHttpStringCallBack(getContext()) {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    closeLoadingDialog();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = SCJsonUtils.parseCode(response);
                                    if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                                        String data = SCJsonUtils.parseData(response);
                                        searchRoomList = SCJsonUtils.parseArr(data, HotChatRoom.class);
//                                        String content = SCJsonUtils.parseString(data, "content");
//                                        searchRoomList = SCJsonUtils.parseArr(content, HotChatRoom.class);
                                        if (searchRoomList != null && mQuickAdapter != null) {
                                            mQuickAdapter.replaceData(searchRoomList);
                                        }
                                        mQuickAdapter.getFooterLayout().setVisibility(View.GONE);
                                    }
                                    closeLoadingDialog();
                                }
                            });
//                    }
//                }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 1) {
                    pageNo = 0;
                    last = false;
                    if (mQuickAdapter != null && adapterChatRoomList != null) {
                        mQuickAdapter.replaceData(adapterChatRoomList);
                    }
                }
                return false;
            }
        });
    }

    //初始化上拉加载
    private void initLoadMoreListener() {
        recyclerViewArsgame.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (last == true) {
                    return;

                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mQuickAdapter.getItemCount()) {
                    mQuickAdapter.getFooterLayout().setVisibility(View.VISIBLE);
                    pageNo++;
                    SCHttpUtils.get()
                            .url(HttpApi.HOT_CHAT_ROOM)
                            .addParams("token", SCCacheUtils.getCacheToken() + "")
                            .addParams("version", localVersion + "")
                            .addParams("page", pageNo + "")
                            .addParams("size", size + "")
                            .build()
                            .execute(new SCHttpStringCallBack() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    closeLoadingDialog();
                                    LogUtils.d("网络异常");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = JSONObject.parseObject(response).getString("code");
                                    if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                        String data = JSONObject.parseObject(response).getString("data");
                                        String content = SCJsonUtils.parseString(data, "content");
                                        String allPage = JSONObject.parseObject(data).getString("totalPages");
                                        totalPage = Integer.valueOf(allPage);
//                                last = SCJsonUtils.parseBoolean(data, "last");
                                        String isLast = JSONObject.parseObject(data).getString("last");
                                        last = Boolean.valueOf(isLast);
                                        List<HotChatRoom> roomList = JSONArray.parseArray(content, HotChatRoom.class);
                                        mQuickAdapter.addData(roomList);
                                        mQuickAdapter.notifyDataSetChanged();
                                        mQuickAdapter.getFooterLayout().setVisibility(View.GONE);
                                        closeLoadingDialog();
//                                        mQuickAdapter.loadMoreEnd(true);
                                    }
                                }
                            });
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

    }

    //判断是否在群黑名单里
    private void isInBlacklist(String roomID, final HotChatRoom item) {
        //判断是否在群黑名单里
        final boolean[] isBlackMember = new boolean[1];
        SCHttpUtils.get()
                .url(HttpApi.IS_BLACK_MEMBER)
                .addParams("roomId", roomID)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            boolean isBlackMember = Boolean.valueOf(data);
                            if (!isBlackMember) {
                                Intent intent = new Intent(getContext(), MessageListActivity.class);
                                intent.putExtra("roomId", item.getRoomId());
                                intent.putExtra("roomName", item.getRoomName());
                                intent.putExtra("hotChatRoom", item);
                                intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                getContext().startActivity(intent);
                            } else {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToast(getContext(), "您已被该聊天室管理员删除");
                                    }
                                });
                            }


                        }
                        closeLoadingDialog();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        JMessageClient.registerEventReceiver(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        pageNo = 0;
        reFreshRoomList(pageNo, size, refreshLayoutArsgame);
        refreshLayoutArsgame.setRefreshing(false);
    }

    private void reFreshRoomList(int pageNo, int size, final SwipeRefreshLayout refreshLayout) {
        localVersion = VersionUtils.getVersionName(getContext());
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("version", localVersion + "")
                .addParams("page", pageNo + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = SCJsonUtils.parseString(data, "content");
                            totalPage = SCJsonUtils.parseInt(data, "totalPage");
                            String isLast = JSONObject.parseObject(response).getString("last");
                            last = Boolean.valueOf(isLast);
                            List<HotChatRoom> hotChatRoomList = JSONArray.parseArray(content, HotChatRoom.class);
                            if (hotChatRoomList != null && mQuickAdapter != null) {
                                mQuickAdapter.replaceData(hotChatRoomList);
                            }

                        }
                        refreshLayout.setRefreshing(false);
                        closeLoadingDialog();
                    }
                });
    }
}
