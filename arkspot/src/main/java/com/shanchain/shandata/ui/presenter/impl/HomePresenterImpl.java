package com.shanchain.shandata.ui.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpPostBodyNewCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
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
import com.shanchain.shandata.ui.model.PhotoBean;
import com.shanchain.shandata.ui.presenter.HomePresenter;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.HomeView;
import com.zhangke.websocket.WebSocketHandler;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
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
//        mHomeView.showProgressStart();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", point.longitude + "")
                .addParams("latitude", point.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mHomeView.showProgressEnd();
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
        requestBody.put("isProbeCoin","true");
        requestBody.put("thumbnails", urlPath);
        SCHttpUtils.postByBody(HttpApi.ADD_HOT_ROOM, JSONObject.toJSONString(requestBody), new SCHttpPostBodyNewCallBack(context, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mHomeView.showProgressEnd();
                mHomeView.setCreateChatRoomResponse(string);
            }

            @Override
            public void responseDoFaile(String string) throws IOException {
                mHomeView.showProgressEnd();
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
    public void checkPasswordToServer(final Context mContext,String file, String value) {
        LogUtils.d("Resonse file: ", file);
        mHomeView.showProgressStart();
        final File f = new File(file);
        String userId = SCCacheUtils.getCache("0", "curUser");
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, f);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", f.getName(), fileBody)
                .addFormDataPart("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .addFormDataPart("value", "" + value)//支付金额
                .addFormDataPart("token",token)
                .addFormDataPart("memo","payMining")//挖矿支付标识
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
//        LogUtils.d("------>>>>checkPassword para: "+SCCacheUtils.getCacheCharacterId()+"--"+userId+"---"+value);
        SCHttpUtils.postByBody(HttpApi.PAY_FOR_ARS + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new SCHttpPostBodyNewCallBack(mContext, null) {
            @Override
            public void responseDoParse(String string) throws IOException {
                mHomeView.showProgressEnd();
                mHomeView.setCheckPasswResponse(string);
            }

            @Override
            public void responseDoFaile(String string) throws IOException {
                mHomeView.showProgressEnd();
            }
        });

    }

    @Override
    public void addMiningRoom(String userId, String roomId) {
//        LogUtils.d("------>>>userid:"+userId+"---"+roomId);
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

    @Override
    public void checkIsJoinMining(String userId, String diggingsId) {
        SCHttpUtils.post()
                .url(HttpApi.CHECK_ADD_MMINING_ROOM)
                .addParams("userId",userId)
                .addParams("diggingsId",diggingsId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setCheckIsJoinMiningRsponse(response);
                    }
                });
    }

    @Override
    public void insertMiningRoomByOther(String userId, String diggingsId) {
        mHomeView.showProgressStart();
        SCHttpUtils.post()
                .url(HttpApi.ADD_MMINING_ROOM)
                .addParams("userId",userId)
                .addParams("diggingsId",diggingsId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mHomeView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.showProgressEnd();
                        mHomeView.setAddMinigRoomResponse(response);
                    }
                });
    }

    @Override
    public void checkNameIsExit(String name) {
        SCHttpUtils.post()
                .url(HttpApi.CHECK_NAME_MMINING_ROOM)
                .addParams("roomName",name)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setMiningNameExit(response);
                    }
                });
    }

    @Override
    public void checkUserHasWallet(Context context) {
        CustomDialog showPasswordDialog = new CustomDialog(context, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        SCHttpUtils.getAndToken()
                .url(HttpApi.WALLET_INFO)
                .addParams("characterId", SCCacheUtils.getCacheCharacterId())
                .addParams("userId", SCCacheUtils.getCacheUserId())
                .build()
                .execute(new SCHttpStringCallBack(context, showPasswordDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setCheckUserHasWalletResponse(response);
                    }
                });
    }

    @Override
    public void insertCheckinRecord(String userId) {
        SCHttpUtils.postNoToken()
                .url(HttpApi.INSERTCHECKIN)
                .addParams("userId", userId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setInsertCheckinResponse(response);
                    }
                });
    }

    @Override
    public void queryMonthCheckinRecord(String querymonth) {
        SCHttpUtils.post()
                .url(HttpApi.QUERY_MONTH_CHECKIN)
                .addParams("loginDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .addParams("userId", SCCacheUtils.getCacheUserId())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHomeView.setQueryMonthCheckinRecordResponse(response);
                    }
                });
    }
}
