package com.shanchain.shandata.ui.presenter;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :
 */
public interface HomePresenter {
    //获取设备token
    void getDeviceToken(String token,String deviceToken);
    //获取当前地址聊天室信息
    void getCurrentPoint(LatLng point);
    //附近矿区信息
    void getCurrentChatRoom(LatLng point);
    //获取当前点击位置是否可加入的状态
    void getCoordinateInfo(LatLng point);
    //创建矿区
    void createChatRoom(LatLng point, String roomName, String urlPath, Context context);
    //上传图片到oss
    void updoadImageToOSS(Context context,String urlPath,LatLng point);
    //验证密码图片
    void checkPasswordToServer(Context context,String file,String value);
    //加入矿区表
    void addMiningRoom(String userId,String roomId);
    //判断是否已加入矿区接口
    void checkIsJoinMining(String userId,String diggingsId);
    //支付成功后调用加入矿区接口
    void insertMiningRoomByOther(String userId,String diggingsId);
}
