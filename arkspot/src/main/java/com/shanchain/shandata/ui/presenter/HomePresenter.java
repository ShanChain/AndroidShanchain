package com.shanchain.shandata.ui.presenter;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :
 */
public interface HomePresenter {
    void getDeviceToken(String token,String deviceToken);
    void getCurrentPoint(LatLng point);
    void getCurrentChatRoom(LatLng point);
    void getCoordinateInfo(LatLng point);
    void createChatRoom(LatLng point, String roomName, String urlPath, Context context);
    void updoadImageToOSS(Context context,String urlPath,LatLng point);
}
