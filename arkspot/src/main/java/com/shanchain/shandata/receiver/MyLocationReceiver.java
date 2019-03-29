package com.shanchain.shandata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.Coordinates;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/3/27
 * Describe :百度地图广播接收器
 */
public class MyLocationReceiver extends BroadcastReceiver implements BDLocationListener {
    private LatLng currentLatLng;
    private String roomID;

    @Override
    public void onReceive(Context context, Intent intent) {
//        ToastUtils.showToastLong(context, "存储roomId:" + roomID);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        currentLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        //获取聊天室信息
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", currentLatLng.longitude + "")
                .addParams("latitude", currentLatLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            Coordinates coordinates = JSONObject.parseObject(data, Coordinates.class);
                            if (coordinates == null) {
                                return;
                            }
                            //房间roomId
                            roomID = coordinates.getRoomId();
                            RoleManager.switchRoleCacheRoomId(roomID);
                        }
                    }
                });
    }
}
