package com.shanchain.shandata.base;

import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import javax.annotation.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by WealChen
 * Date : 2019/5/29
 * Describe :
 */
public class MyWebSocketListener extends WebSocketListener {

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        LogUtils.d("webSocket", "打开webSocket连接： ");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        LogUtils.d("webSocket", "已经关闭webSocket连接： ");
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        LogUtils.d("webSocket", "正在关闭webSocket连接： ");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        LogUtils.d("webSocket", "发送消息失败： ");
    }


}
