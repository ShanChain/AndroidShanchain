package com.shanchain.shandata.base;

import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.zhangke.websocket.dispatcher.IResponseDispatcher;
import com.zhangke.websocket.dispatcher.ResponseDelivery;
import com.zhangke.websocket.response.ErrorResponse;

import org.java_websocket.framing.Framedata;

import java.nio.ByteBuffer;

/**
 * Created by WealChen
 * Date : 2019/5/23
 * Describe :
 */
public class AppResponseDispatcher implements IResponseDispatcher {
    @Override
    public void onConnected(ResponseDelivery delivery) {
        delivery.onConnected();
        LogUtils.d("webSocket", "webSocket的连接成功 ");

    }

    @Override
    public void onConnectFailed(Throwable cause, ResponseDelivery delivery) {
        delivery.onConnectFailed(cause);
        LogUtils.d("webSocket", "webSocket的连接失败 ---- " + cause.toString());
    }

    @Override
    public void onDisconnect(ResponseDelivery delivery) {
        delivery.onDisconnect();
        LogUtils.d("webSocket", "webSocket的连接断开 ");
    }

    @Override
    public void onMessage(String message, ResponseDelivery delivery) {
        //第二个参数根据业务要求可以进行封装message
        delivery.onMessage(message,message);
        LogUtils.d("webSocket", "接收到webSocket发送的String类型的消息： " + message);
    }

    @Override
    public void onMessage(ByteBuffer byteBuffer, ResponseDelivery delivery) {
        //第二个参数根据业务要求可以进行封装byteBuffer
        delivery.onMessage(byteBuffer,byteBuffer);
        LogUtils.d("webSocket", "接收到webSocket发送的byteBuffer类型的消息 ");
    }

    @Override
    public void onPing(Framedata framedata, ResponseDelivery delivery) {
        delivery.onPing(framedata);
        LogUtils.d("webSocket", "接收ping ");
    }

    @Override
    public void onPong(Framedata framedata, ResponseDelivery delivery) {
        delivery.onPong(framedata);
        LogUtils.d("webSocket", "接收pong ");
    }

    @Override
    public void onSendDataError(ErrorResponse error, ResponseDelivery delivery) {
        LogUtils.d("webSocket", "webSocket发送消息失败: " + error.getDescription() + " ---完整信息：" + error.toString());
    }
}
