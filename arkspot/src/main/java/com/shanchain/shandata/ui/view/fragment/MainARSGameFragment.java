package com.shanchain.shandata.ui.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyNewCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ARSGamneRoomAdapter;
import com.shanchain.shandata.base.AppResponseDispatcher;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;
import com.zhangke.websocket.response.ErrorResponse;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//import com.koushikdutta.async.http.AsyncHttpClient;

/**
 * Created by WealChen
 * Date : 2019/5/22
 * Describe :
 */
public class MainARSGameFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycler_view_arsgame)
    RecyclerView recyclerViewArsgame;
    @Bind(R.id.refresh_layout_arsgame)
    SwipeRefreshLayout refreshLayoutArsgame;
    @Bind(R.id.tb_coupon)
    ArthurToolBar toolBar;
    private String localVersion;
    private int totalPage, size = 10, pageNo = 0;
    private boolean last, disconnect = false;
    //    private HotChatRoomAdapter mQuickAdapter;
    private ARSGamneRoomAdapter mQuickAdapter;
    private WebSocketManager mWebSocketManager;
    private View footerView;
    private com.shanchain.data.common.ui.widgets.CustomDialog sureDialog;
    private com.shanchain.shandata.widgets.dialog.CustomDialog mCustomDialog;
    private SocketListener mSocketListener = new SimpleListener() {
        @Override
        public <T> void onMessage(final String message, T data) {
//                super.onMessage(message, data);
            LogUtils.d("WebSocketHandler", "接收到的message:" + message);
            String webSocketCode = SCJsonUtils.parseString(message, "code");
            String webSocketData = SCJsonUtils.parseString(message, "data");
            String type = SCJsonUtils.parseString(message, "type");
            if ("00000000".equals(webSocketCode)) {
                switch (type) {
                    case "ARSPaySuccess"://支付成功进入首层
                        String userId = SCJsonUtils.parseString(webSocketData, "userId");
                        if (userId.equals(SCCacheUtils.getCacheUserId())) {
                            MainARSGameFragment.this.showLoadingDialog(false);
                            enterChatRoom();
                        }
                        break;
                    case "makeSure": //确认进入下一层
//                        if (sureDialog != null) {
//                            sureDialog.dismiss();
//                        }
//                        String joinNextUse = SCJsonUtils.parseString(webSocketData, "userId");
//                        if (joinNextUse.equals(SCCacheUtils.getCacheUserId())) {
//                            showLoadingDialog(false);
//                            String s = SCCacheUtils.getCacheUserId();
//                            enterNextRoom();
                        refreshRoomList(pageNo, size, refreshLayoutArsgame);
//                        }
//                        mCustomDialog1.show();
//                        mCustomDialog1.setCancelable(false);
                        break;
                    case "confirmNum": //确认的人数
                        String num = SCJsonUtils.parseString(webSocketData, "num");
                        String confirmString = SCJsonUtils.parseString(webSocketData, "confirmList");
                        List<String> confirmList = SCJsonUtils.parseArr(confirmString, String.class);
                        //是否确认
                        isConfirm(num, confirmList);
                        break;
                    case "SysMessage": //显示确认进入
                        final String endTime = SCJsonUtils.parseString(webSocketData, "endTime");
                        String list = SCJsonUtils.parseString(webSocketData, "list");
                        final long overTime = Long.valueOf(endTime);//结束时间
                        final long currentTime = System.currentTimeMillis(); //当前时间
                        List<String> users = SCJsonUtils.parseArr(list, String.class);
                        if (users.indexOf("" + SCCacheUtils.getCacheUserId()) != -1) {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Activity topActivity = ActivityStackManager.getInstance().getTopActivity() != null ?
                                            ActivityStackManager.getInstance().getTopActivity() : getActivity();
                                    sureDialog = new CustomDialog(topActivity, false, 1.0,
                                            R.layout.common_dialog_costom, new int[]{R.id.btn_dialog_task_detail_sure});
                                    sureDialog.setDialogTitle("下一层元社区已开启");
                                    sureDialog.setMessageContent("点击确认进入元社区，否则您将被踢出聊天室");
                                    sureDialog.setSureText(getString(R.string.str_sure));
                                    sureDialog.setMessageContentSize(16);
                                    sureDialog.show();
                                    sureDialog.setCanceledOnTouchOutside(false);
                                    if (currentTime < overTime) {
                                        TextView countDown = sureDialog.findViewById(R.id.text_count_down);
                                        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(countDown, "确认倒计时：", overTime - currentTime, 1000);
                                        countDownTimeUtils.setContext(getActivity());
                                        countDownTimeUtils.start();
                                    }
                                    sureDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                        @Override
                                        public void OnItemClick(CustomDialog dialog, View view) {
                                            switch (view.getId()) {
                                                case R.id.btn_dialog_task_detail_sure:
                                                    Map dataMap = new HashMap();
                                                    dataMap.put("userId", SCCacheUtils.getCacheUserId());
                                                    dataMap.put("charcterId", SCCacheUtils.getCacheCharacterId());
                                                    dataMap.put("type", "makeSure");
                                                    final String dataString = JSONObject.toJSONString(dataMap);
                                                    WebSocketHandler.getDefault().send(dataString);
                                                    if (currentTime > overTime) {
                                                        sureDialog.dismiss();
                                                        ThreadUtils.runOnMainThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ToastUtils.showToast(getContext(), "进入下一层时间已过期");
                                                            }
                                                        });
                                                    } else {
                                                        //进入下一层
                                                        MainARSGameFragment.this.showLoadingDialog(false);
                                                        enterNextRoom();
                                                    }
                                                    sureDialog.dismiss();
                                                    break;
                                                case R.id.linear_layout:
                                                    break;
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        break;
                }
            }
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            super.onSendDataError(errorResponse);
            LogUtils.d("WebSocketHandler", "发送消息失败:" + errorResponse.toString());
        }

        @Override
        public void onConnected() {
            super.onConnected();
            if (disconnect = true) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(MainARSGameFragment.this.getContext(), R.string.service_on_success);
                    }
                });
                disconnect = false;
            }
            closeLoadingDialog();
        }

        @Override
        public void onDisconnect() {
            super.onDisconnect();
            disconnect = true;
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(MainARSGameFragment.this.getContext(), R.string.rebuild_server);
                }
            });
        }

        @Override
        public void onConnectFailed(Throwable e) {
            super.onConnectFailed(e);
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(MainARSGameFragment.this.getContext(), R.string.server_error_reset);
                }
            });
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            super.onMessage(bytes, data);
            LogUtils.d("WebSocketHandler", "接收到的bytes:");
        }
    };
    private CustomDialog mShowPasswordDialog;

    //是否点击确认
    private void isConfirm(String num, List<String> confirmList) {
        if (!num.equals("0")) {
            if (confirmList.indexOf(SCCacheUtils.getCacheUserId()) != -1) {
                if (sureDialog != null) {
                    sureDialog.dismiss();
                }
            }
        } else {
//            if (sureDialog != null) {
//                sureDialog.dismiss();
//            }
        }
    }

    public static MainARSGameFragment newInstance() {
        MainARSGameFragment fragment = new MainARSGameFragment();
        return fragment;
    }

    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_main_arsgame, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取WebSocket管理对象
        final WebSocketSetting socketSetting = new WebSocketSetting();
//        socketSetting.setConnectUrl("ws://test.qianqianshijie.com:8081/websocket/" + SCCacheUtils.getCacheUserId());
        socketSetting.setConnectUrl("ws://api.qianqianshijie.com:8081/websocket/" + SCCacheUtils.getCacheUserId());
        //设置连接超时时间，单位：毫秒
        socketSetting.setConnectTimeout(20 * 1000);
        //心跳包
//        socketSetting.setConnectionLostTimeout(60 * 3);
        socketSetting.setResponseProcessDispatcher(new AppResponseDispatcher());
//        接收到数据后是否放入子线程处理，只有设置了 ResponseProcessDispatcher 才有意义
        socketSetting.setProcessDataOnBackground(true);
        //网络状态发生变化后是否重连，
//        需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        socketSetting.setReconnectWithNetworkChanged(true);
        //在子线程开启WebSocket
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                mWebSocketManager = WebSocketHandler.init(socketSetting);
                mWebSocketManager.addListener(mSocketListener);
            }
        });
        //注册网路连接状态变化广播
        try {
            WebSocketHandler.registerNetworkChangedReceiver(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map webSocket = WebSocketHandler.getAllWebSocket();
        LogUtils.d("webSocketSize", webSocket.size() + "");

    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitleText(getResources().getString(R.string.wheel_seat));
        toolBar.setLeftTitleLayoutView(View.GONE);
        if (mQuickAdapter != null && refreshLayoutArsgame != null) {
            refreshRoomList(pageNo, size, refreshLayoutArsgame);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketHandler.getDefault().removeListener(mSocketListener);
    }

    @Override
    public void initData() {
        refreshLayoutArsgame.setOnRefreshListener(this);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        getChatRoomList(pageNo, size, refreshLayoutArsgame);
        if (mQuickAdapter != null) {
            //初始化上拉加载
            initLoadMoreListener();
        }
    }

    //获取首页列表
    private void getChatRoomList(int pageNo, int size, final SwipeRefreshLayout refreshLayout) {
        localVersion = VersionUtils.getVersionName(getContext());
        SCHttpUtils.get()
                .url(HttpApi.ARS_ROOM_LIST)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("charaterId", "" + SCCacheUtils.getCacheCharacterId())
//                .addParams("version", localVersion + "")
//                .addParams("page", pageNo + "")
//                .addParams("size", size + "")
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
                            final String data = JSONObject.parseObject(response).getString("data");
//                            String content = SCJsonUtils.parseString(data, "content");
//                            totalPage = SCJsonUtils.parseInt(data, "totalPage");
//                            String isLast = JSONObject.parseObject(response).getString("last");
//                            last = Boolean.valueOf(isLast);
                            List<HotChatRoom> hotChatRoomList = JSONArray.parseArray(data, HotChatRoom.class);
                            if (hotChatRoomList == null) {
                                return;
                            }
                            //                                intent.putExtra("isInCharRoom", isIn);
                            mQuickAdapter = new ARSGamneRoomAdapter(getContext(), hotChatRoomList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerViewArsgame.setLayoutManager(layoutManager);
//                            mQuickAdapter.setLoadMoreView(new MyLoadMoreView());
//                            mQuickAdapter.setFooterView(footerView);
                            recyclerViewArsgame.setAdapter(mQuickAdapter);
                            //item点击事件
                            mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                                    HotChatRoom item = (HotChatRoom) adapter.getItem(position);
                                    if (position == 0 && !item.isPay()) {
//                                    if (position == 0) {
//                                        //构造webSocket消息
//                                        Map dataMap = new HashMap();
//                                        dataMap.put("userId", SCCacheUtils.getCacheUserId());
//                                        dataMap.put("type", "ARSPaySuccess");
//                                        final String dataString = JSONObject.toJSONString(dataMap);
//                                        WebSocketHandler.getDefault().send("" + dataString);

                                        final StandardDialog standardDialog = new StandardDialog(getContext());
                                        standardDialog.setStandardTitle(" ");
//                                        standardDialog.setStandardMsg("支付100个 SEAT 参与ARS活动\n" +
//                                                "      (公测期间0.001个SEAT)");
                                        standardDialog.setStandardMsg(getString(R.string.payfor_join, HttpApi.PAYFOR_MINING_MONEY));

                                        standardDialog.setSureText(getString(R.string.commit_payfor));
                                        standardDialog.setCallback(new Callback() {
                                            @Override
                                            public void invoke() {//确认
                                                standardDialog.dismiss();
                                                ThreadUtils.runOnMainThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //显示上传密码弹窗
                                                        mShowPasswordDialog = new CustomDialog(getContext(), true, 1.0,
                                                                R.layout.dialog_bottom_wallet_password,
                                                                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                                                        mShowPasswordDialog.setPasswordBitmap(null);
                                                        mShowPasswordDialog.show();
                                                        mShowPasswordDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                                            @Override
                                                            public void OnItemClick(CustomDialog dialog, View view) {
                                                                switch (view.getId()) {
                                                                    case R.id.iv_dialog_add_picture:
                                                                        selectImage(getContext());
                                                                        break;
                                                                    case R.id.tv_dialog_sure:
                                                                        if(mShowPasswordDialog.getPasswordBitmap() == null){
                                                                            ToastUtils.showToast(getActivity(), R.string.upload_qr_code);
                                                                        }else {
                                                                            payForARS(new File(photoPath), HttpApi.PAYFOR_MINING_MONEY);
                                                                        }
                                                                        break;
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }, new Callback() {//取消
                                            @Override
                                            public void invoke() {
                                                standardDialog.dismiss();
                                            }
                                        });
                                        ThreadUtils.runOnMainThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                standardDialog.show();
                                                TextView msgTextView = standardDialog.findViewById(R.id.dialog_msg);
                                                msgTextView.setTextSize(18);
                                            }
                                        });

                                    } else {
                                        if (item.isLitUp()) {
                                            Intent intent = new Intent(getContext(), MessageListActivity.class);
                                            intent.putExtra("roomId", item.getRoomId());
                                            intent.putExtra("roomName", item.getRoomName());
                                            intent.putExtra("hotChatRoom", item);
                                            intent.putExtra("isHotChatRoom", true);
                                            startActivity(intent);
                                        } else {
                                            ToastUtils.showToast(getContext(), R.string.not_peimition);
                                        }
                                    }
                                }
                            });

                            if (refreshLayout != null) {
                                refreshLayout.setRefreshing(false);
                            }
                        }
                        closeLoadingDialog();
                    }
                });
    }

    private void payForARS(File file, String money) {
        //创建requestBody
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .addFormDataPart("value", "" + money)//支付金额
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.PAY_FOR_ARS + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new SCHttpPostBodyNewCallBack(getContext(), null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                LogUtils.d("----->>>ars: "+string);
                String code = SCJsonUtils.parseCode(string);
                String msg = SCJsonUtils.parseMsg(string);
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    String data = SCJsonUtils.parseData(string);
                    if (mShowPasswordDialog != null) {
                        mShowPasswordDialog.dismiss();
                    }
                    Map dataMap = new HashMap();
                    dataMap.put("userId", SCCacheUtils.getCacheUserId());
                    dataMap.put("type", "ARSPaySuccess");
                    final String dataString = JSONObject.toJSONString(dataMap);
                    WebSocketHandler.getDefault().send("" + dataString);
                }
            }

            @Override
            public void responseDoFaile(String string) throws IOException {

            }
        });
    }

    //刷新首页列表
    private void refreshRoomList(int pageNo, int size, final SwipeRefreshLayout refreshLayout) {
        localVersion = VersionUtils.getVersionName(getContext());
        SCHttpUtils.get()
                .url(HttpApi.ARS_ROOM_LIST)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("charaterId", "" + SCCacheUtils.getCacheCharacterId())
//                .addParams("version", localVersion + "")
//                .addParams("page", pageNo + "")
//                .addParams("size", size + "")
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
                            final String data = JSONObject.parseObject(response).getString("data");
//                            String content = SCJsonUtils.parseString(data, "content");
//                            totalPage = SCJsonUtils.parseInt(data, "totalPage");
//                            String isLast = JSONObject.parseObject(response).getString("last");
//                            last = Boolean.valueOf(isLast);
                            List<HotChatRoom> hotChatRoomList = JSONArray.parseArray(data, HotChatRoom.class);
                            if (hotChatRoomList != null && mQuickAdapter != null) {
                                mQuickAdapter.replaceData(hotChatRoomList);
                            }
                        }
                        refreshLayout.setRefreshing(false);
                        closeLoadingDialog();
                    }
                });
    }

    //进入首层
    private void enterChatRoom() {
        SCHttpUtils.post()
                .url(HttpApi.ENTER_CHAT_ROOM)
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .build()
                .execute(new SCHttpStringCallBack(getContext()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MainARSGameFragment.this.closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("进入首页", "response:" + response);
                        String code = SCJsonUtils.parseCode(response);
                        String msg = SCJsonUtils.parseMsg(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            MainARSGameFragment.this.closeLoadingDialog();
                            HotChatRoom item = mQuickAdapter.getItem(0);
                            Intent intent = new Intent(getContext(), MessageListActivity.class);
                            intent.putExtra("roomId", item.getRoomId());
                            intent.putExtra("roomName", item.getRoomName());
                            intent.putExtra("hotChatRoom", item);
                            intent.putExtra("isHotChatRoom", true);
                            startActivity(intent);
                        }
//                        if (mCustomDialog != null) {
//                            mCustomDialog.dismiss();
//                        }
                    }
                });
    }

    //进入下一层
    private void enterNextRoom() {
        SCHttpUtils.post()
                .url(HttpApi.ENTER_NEXT_CHAT_ROOM)
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mCustomDialog1.dismiss();
                        MainARSGameFragment.this.closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("进入下一层", "response:" + response);
//                        mCustomDialog1.dismiss();
                        MainARSGameFragment.this.closeLoadingDialog();
                        if (sureDialog != null) {
                            sureDialog.dismiss();
                        }
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
                                    String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                                    if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                        String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
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
                                        MainARSGameFragment.this.closeLoadingDialog();
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
                                        ToastUtils.showToast(getContext(), R.string.delete_by_manager);
                                    }
                                });
                            }


                        }
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
    protected void showLoadingDialog(boolean isShow) {
        mCustomDialog = new com.shanchain.shandata.widgets.dialog.CustomDialog(getContext(), 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCanceledOnTouchOutside(isShow);
    }

    @Override
    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 0;
        refreshRoomList(pageNo, size, refreshLayoutArsgame);
        refreshLayoutArsgame.setRefreshing(false);
    }

    private String photoPath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            if (requestCode == NetErrCode.WALLET_PHOTO) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                photoPath = cursor.getString(columnIndex);
                com.shanchain.data.common.utils.LogUtils.d("----->MainARS: select image path is "+photoPath);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                if(mShowPasswordDialog!=null){
                    mShowPasswordDialog.setPasswordBitmap2(bitmap);
                }
            }
        }
    }
}
