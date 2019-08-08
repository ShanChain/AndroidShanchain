package com.shanchain.shandata.ui.view.fragment.marjartwideo.view;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :
 */
public interface HomeView {
    void showProgressStart();
    void showProgressEnd();
    void setDeviceTokenInfo(String response);
    void setCurrentPoint(String response);
    void setAroundChatRoome(String response);
    void setCoordinateInfoResponse(String response);
    void setCreateChatRoomResponse(String response);
    void setUploadImageToOSSResponse(String url, boolean isSucess, LatLng point);


}
