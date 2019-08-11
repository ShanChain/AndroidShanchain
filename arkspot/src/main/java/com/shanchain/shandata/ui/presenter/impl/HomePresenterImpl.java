package com.shanchain.shandata.ui.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.presenter.HomePresenter;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.HomeView;
import com.zhangke.websocket.WebSocketHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :
 */
public class HomePresenterImpl implements HomePresenter {
    private HomeView mHomeView;

    public HomePresenterImpl(HomeView homeView){
        this.mHomeView = homeView;
    }

    @Override
    public void getDeviceToken(String token, String deviceToken) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.SET_DEVICE_TOKEN)
                .addParams("osType", "android")
                .addParams("token", token)
                .addParams("deviceToken", PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("设置DeviceToken失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setDeviceTokenInfo(response);
                    }
                });
    }

    @Override
    public void getCurrentPoint(LatLng point) {
        mHomeView.showProgressStart();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", point.longitude + "")
                .addParams("latitude", point.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mHomeView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.showProgressEnd();
                        mHomeView.setCurrentPoint(response);
                    }
                });
    }

    @Override
    public void getCurrentChatRoom(LatLng point) {
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_COORDINATE)
                .addParams("longitude", point.longitude + "")
                .addParams("latitude", point.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setAroundChatRoome(response);
                    }
                });
    }

    @Override
    public void getCoordinateInfo(LatLng point) {
        SCHttpUtils.getAndToken()
                .url(HttpApi.CUBE_INFO)
                .addParams("latitude", point.latitude + "")
                .addParams("longitude", point.longitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setCoordinateInfoResponse(response);
                    }
                });
    }

    @Override
    public void createChatRoom(LatLng myFocusPoint, String roomName, String urlPath, Context context) {
        mHomeView.showProgressStart();
        Map requestBody = new HashMap();
        requestBody.put("latitude", myFocusPoint.latitude);
        requestBody.put("longitude", myFocusPoint.longitude);
        requestBody.put("roomName", "" + roomName);
        requestBody.put("thumbnails", urlPath);
        SCHttpUtils.postByBody(HttpApi.ADD_HOT_ROOM, JSONObject.toJSONString(requestBody), new SCHttpPostBodyCallBack(context, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mHomeView.showProgressEnd();
                mHomeView.setCreateChatRoomResponse(string);
            }
        });

    }

    @Override
    public void updoadImageToOSS(final Context context, String urlPath,final LatLng point) {
        List screenshot = new ArrayList();
        screenshot.add(urlPath);
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(final List<String> urls) {
                if(urls.size()==0){
                    mHomeView.setUploadImageToOSSResponse("",false,point);
                }else {
                    mHomeView.setUploadImageToOSSResponse(urls.get(0),true,point);
                }


            }

            @Override
            public void error() {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(context, context.getResources().getString(R.string.internet_error));
                    }
                });
            }
        });
        helper.upLoadImg(context, screenshot);
    }

    @Override
    public void checkPasswordToServer(Context context,String file, String value) {
        mHomeView.showProgressStart();
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file, fileBody)
                .addFormDataPart("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .addFormDataPart("value", "" + value)//支付金额
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.PAY_FOR_ARS + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new SCHttpPostBodyCallBack(context, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mHomeView.showProgressEnd();
                mHomeView.setCheckPasswResponse(string);
            }
        });
    }

    @Override
    public void addMiningRoom(String userId, String roomId) {
        SCHttpUtils.post()
                .url(HttpApi.ADD_MINING_ROOM)
                .addParams("createUser",userId)
                .addParams("roomId",roomId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setAddMiningRoomResponse(response);
                    }
                });
    }
}
