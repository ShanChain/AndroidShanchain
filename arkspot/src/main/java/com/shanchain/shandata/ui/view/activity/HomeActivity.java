package com.shanchain.shandata.ui.view.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.L;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.receiver.MessageReceiver;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.IsFavBean;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.utils.CoordinateTransformUtil;
import com.shanchain.shandata.utils.ImageUtils;
import com.shanchain.shandata.utils.MyOrientationListener;
import com.shanchain.shandata.utils.PermissionHelper;
import com.shanchain.shandata.utils.PermissionInterface;
import com.shanchain.shandata.utils.RequestCode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.model.AccessTokenInfo;
import cn.jiguang.share.android.model.BaseResponseInfo;
import cn.jiguang.share.android.model.UserInfo;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;
//import me.shaohui.shareutil.ShareUtil;
//import me.shaohui.shareutil.share.ShareListener;
//import me.shaohui.shareutil.share.SharePlatform;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_DEVICE_TOKEN_STATUS;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;

public class HomeActivity extends BaseActivity implements PermissionInterface {
    private BaiduMap baiduMap;
    private long downloadId;
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;
    private PermissionHelper mPermissionHelper;
    private MapStatusUpdate mapstatusupdate;
    private LatLng markerPoint;
    private double[] WGS84point;
    private Runnable runnable;
    private Handler handler, getRoomIdHandle;
    private BDLocation myLocation;
    private double[] gcj02point;
    private UiSettings uiSettings;
    private MyOrientationListener myOrientationListener;
    private Coordinates coordinates;
    private List pointList = new ArrayList();
    private List<Coordinates> coordinatesList;
    private List roomList = new ArrayList();
    private boolean isFirstLoc = true; // 是否首次定位
    private String roomID = "", clickRoomID = "";
    private String roomName;
    private int joinRoomId;
    private ProgressDialog mDialog;
    private LatLng myFocusPoint;
    private boolean isIn;

    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(HomeActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            closeLoadingDialog();
        }
    };
    private PlatActionListener mPlatActionListener;
    List<String> platformList = JShareInterface.getPlatformList();
    List<String> dataList = new ArrayList<String>();


    TextView tvLocation;
    ImageView imgInfo;
    ImageView imgHistory;
    MapView mapView;
    LinearLayout btJoin;
    LinearLayout linearRule;
    public static LatLng latLng;
    private CoordinateConverter coordinateConverter;
    private LatLng gpsLatLng;
    private LatLng bd09LatLng;
    private View.OnClickListener onClickListener;
    BaiduMap.OnMapClickListener MapListener;
    private File imgeFile;
    private Bitmap cropScreenBitmap;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEvents() {
        tvLocation = findViewById(R.id.text_view_location);
        mapView = findViewById(R.id.map_view_home);
        btJoin = findViewById(R.id.button_join);
        imgHistory = findViewById(R.id.image_view_history);
        imgInfo = findViewById(R.id.image_view_info);
        linearRule = findViewById(R.id.linear_rule); //活动规则
        String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);

        RNGDataBean rngDataBean = new RNGDataBean();
        rngDataBean.setUserId(uId);
        rngDataBean.setToken(token);
        rngDataBean.setSpaceId(spaceId);
        rngDataBean.setCharacterId(characterId);
        String jsonGData = JSON.toJSONString(rngDataBean);
        SCCacheUtils.setCache(uId, Constants.CACHE_GDATA, jsonGData);
        String cacheGData = SCCacheUtils.getCache(uId, Constants.CACHE_GDATA);
        LogUtils.i("缓存的gdata = " + cacheGData);


        getRoomIdHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        joinRoomId = msg.arg1;
//                        if (roomID.equals(clickRoomID)) {
//                            isIn = true;
//                        } else {
//                            isIn = false;
//                        }
                        roomID = String.valueOf(joinRoomId);
                        roomName = (String) msg.obj;
                        btJoin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, MessageListActivity.class);
                                intent.putExtra("roomId", roomID);
                                intent.putExtra("roomName", roomName);
//                                intent.putExtra("isInCharRoom", isIn);
                                startActivity(intent);
                            }
                        });
                        break;
                    case 2:
                        joinRoomId = msg.arg1;
                        roomID = String.valueOf(joinRoomId);
                        roomName = (String) msg.obj;
                        btJoin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, MessageListActivity.class);
                                intent.putExtra("roomId", roomID);
                                intent.putExtra("roomName", roomName);
                                startActivity(intent);
                            }
                        });
                        break;
                }
            }
        }

        ;

        //        UMConfigure.setLogEnabled(true); //显示友盟log日记
        //检查apk版本
        checkApkVersion();

        initDeviceToken();

        initView();

        initBaiduMap();

    }

    //点击请求方区
    public void initCubeMap(LatLng point) {
        //获取聊天室信息
        final ArrayList<LatLng> clickPointList = new ArrayList<>();
        showProgress();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", point.longitude + "")
                .addParams("latitude", point.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                        closeProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            coordinates = JSONObject.parseObject(data, Coordinates.class);
//                            clickRoomID = coordinates.getRoomId();
                            roomID = coordinates.getRoomId();
                            String latLang = coordinates.getRoomName();
                            String[] strings = latLang.split(",");
                            final double lat = Double.valueOf(strings[0]);
                            final double lang = Double.valueOf(strings[1]);
                            final String latLocation, langLocation;
                            DecimalFormat df = new DecimalFormat("#.00");
                            if (lat < 0) {
                                latLocation = "南纬" + df.format(lat);

                            } else {
                                latLocation = "北纬" + df.format(lat);
                            }
                            if (lang > 0) {
                                langLocation = "东经" + df.format(lang);

                            } else {
                                langLocation = "西经" + df.format(lang);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvLocation.setText(langLocation + "°," + latLocation + "°");
                                }
                            });

                            Message roomMessage = new Message();
                            roomMessage.what = 1;
                            roomMessage.obj = langLocation + "°," + latLocation + "°";
//                            roomMessage.arg1 = Integer.valueOf(clickRoomID);
                            roomMessage.arg1 = Integer.valueOf(roomID);
                            getRoomIdHandle.sendMessage(roomMessage);
                            /*
                             * 绘制所在位置的方区
                             *
                             * */
                            for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                                double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                                double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                                LatLng point = new LatLng(pointLatitude, pointLongitude);
                                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                coordinateConverter.coord(point);
                                LatLng desLatLng = coordinateConverter.convert();
//                                pointList.add(desLatLng);

                                clickPointList.add(point);
                            }

                            double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                            double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                            myFocusPoint = new LatLng(focusLatitude, focusLongitude);
                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                            coordinateConverter.coord(myFocusPoint);
                            LatLng desLatLng = coordinateConverter.convert();
                            //设置显示地图中心点
//                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(desLatLng); //转换后的坐标
                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(myFocusPoint); //未转换的坐标
                            baiduMap.setMapStatus(mapStatusUpdate);

                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.home_location);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(myFocusPoint)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
//                           baiduMap.addOverlay(option);
                            //绘制虚线（需要多添加一个起点坐标，形成矩形）
                            clickPointList.add(clickPointList.get(0));
                            OverlayOptions ooPolyline = new PolylineOptions().width(4)
                                    .color(0xAA121518).points(clickPointList);
                            Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                            mPolyline.setDottedLine(true);
                        }
                        closeProgress();
                    }

                });
    }

    //请求接口数据
    private void initData(LatLng gpsLatLng) {
//        LatLng myLatLang = new LatLng(20.045082, 110.32447);
        //获取周边
        showProgress();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_COORDINATE)
                .addParams("longitude", gpsLatLng.longitude + "")
                .addParams("latitude", gpsLatLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                        closeProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeProgress();
                        LogUtils.d("####### USER_COORDINATE 请求成功 #######");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            String room = JSONObject.parseObject(data).getString("room");
                            coordinatesList = JSONObject.parseArray(room, Coordinates.class);

                            /*
                             * 绘制附近的方区
                             *
                             * */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < coordinatesList.size(); i++) {
                                        LatLng srcLatLang = new LatLng(Double.valueOf(coordinatesList.get(i).getFocusLatitude()), Double.valueOf(coordinatesList.get(i).getFocusLongitude()));
                                        LatLng focusPoint = coordinateConverter.from(CoordinateConverter.CoordType.GPS).coord(srcLatLang).convert();
                                        //构建Marker图标
                                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                                .fromResource(R.mipmap.home_location);
                                        //构建MarkerOption，用于在地图上添加Marker
                                        OverlayOptions option = new MarkerOptions()
                                                .position(srcLatLang)
                                                .icon(bitmap);
                                        //在地图上添加Marker，并显示
//                                baiduMap.addOverlay(option);
                                        for (int j = 0; j < coordinatesList.get(i).getCoordinates().size(); j++) {
                                            double pointLatitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(j).getLatitude());
                                            double pointLongitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(j).getLongitude());
                                            LatLng point = new LatLng(pointLatitude, pointLongitude);

                                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                            coordinateConverter.coord(point);
                                            LatLng desLatLng = coordinateConverter.convert();

//                                    roomList.add(desLatLng);
                                            roomList.add(point);//未转换的坐标


                                        }
                                        //
                                        double firstLatitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(0).getLatitude());
                                        double firstLongitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(0).getLongitude());
                                        LatLng indexPoint = new LatLng(firstLatitude, firstLongitude);
                                        // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                        coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                        coordinateConverter.coord(indexPoint);
                                        LatLng firstLatLng = coordinateConverter.convert();
//                                roomList.add(firstLatLng);
                                        //绘制虚线（需要多添加一个起点坐标，形成矩形）
                                        roomList.add(indexPoint);
                                        OverlayOptions roomOoPolyline = new PolylineOptions().width(4)
                                                .color(0xAA121518).points(roomList);
                                        Polyline roomPolyline = (Polyline) baiduMap.addOverlay(roomOoPolyline);
                                        roomPolyline.setDottedLine(true);
                                        roomList.clear();
                                    }
                                }
                            }).start();


                        }
                        closeProgress();
                    }
                });

        //获取聊天室信息
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", gpsLatLng.longitude + "")
                .addParams("latitude", gpsLatLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            coordinates = JSONObject.parseObject(data, Coordinates.class);
                            //房间roomId
                            roomID = coordinates.getRoomId();
                            RoleManager.switchRoleCacheRoomId(roomID);
//                            roomID = "12826211";

                            /*
                             * 绘制所在位置的方区
                             *
                             * */
                            for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                                double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                                double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                                LatLng point = new LatLng(pointLatitude, pointLongitude);
                                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                coordinateConverter.coord(point);
                                LatLng desLatLng = coordinateConverter.convert();
//                                pointList.add(desLatLng);
                                pointList.add(point);
                            }
                            double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                            double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                            LatLng point = new LatLng(focusLatitude, focusLongitude);
                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                            coordinateConverter.coord(point);
                            LatLng desLatLng = coordinateConverter.convert();
                            //设置显示地图中心点
//                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(desLatLng); //转换后的坐标
                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(point); //未转换的坐标
                            baiduMap.setMapStatus(mapStatusUpdate);

                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.map_maker_my);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
//                                baiduMap.addOverlay(option);
                            //绘制虚线（需要多添加一个起点坐标，形成矩形）
                            pointList.add(pointList.get(0));
                            OverlayOptions ooPolyline = new PolylineOptions().width(4)
                                    .color(0xAA121518).points(pointList);
                            Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                            mPolyline.setDottedLine(true);
                            String latLang = coordinates.getRoomName();
                            String[] strings = latLang.split(",");
                            final double lat = Double.valueOf(strings[0]);
                            final double lang = Double.valueOf(strings[1]);
                            final String latLocation, langLocation;
                            DecimalFormat df = new DecimalFormat("#.00");
                            if (lat < 0) {
                                latLocation = "南纬" + df.format(lat);

                            } else {
                                latLocation = "北纬" + df.format(lat);
                            }
                            if (lang > 0) {
                                langLocation = "东经" + df.format(lang);

                            } else {
                                langLocation = "西经" + df.format(lang);
                            }

                            roomName = langLocation + "°," + latLocation + "°";
                            Message roomMessage = new Message();
                            roomMessage.what = 2;
                            roomMessage.obj = roomName;
                            roomMessage.arg1 = Integer.valueOf(roomID);
                            getRoomIdHandle.sendMessage(roomMessage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvLocation.setText(langLocation + "," + latLocation);
                                }
                            });
                        }
                        closeProgress();
                    }
                });
    }

    private void initDeviceToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setDeviceToken();
            }
        });
        thread.start();
    }

    private void checkApkVersion() {
        final String localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.postNoToken()
                .url(HttpApi.OSS_APK_GET_LASTEST)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取版本信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到版本信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                String forceUpdate = JSONObject.parseObject(data).getString("forceUpdate");
                                boolean force = Boolean.parseBoolean(forceUpdate);
                                String url = JSONObject.parseObject(data).getString("url");
                                String version = JSONObject.parseObject(data).getString("version");
                                boolean isUpdata = VersionUtils.compareVersion(localVersion, version);
                                if (isUpdata) {
                                    showUpdateDialog(url, force, version);
                                } else {
                                    return;
                                }

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //版本更新提示弹窗
    private void showUpdateDialog(final String url, final boolean force, String version) {
        String msg = "";
        if (force) {
            msg = "新版本有较大改进，马上更新吧";
        } else {
            msg = "确定要更新吗？";
        }
        final StandardDialog dialog = new StandardDialog(this);
        dialog.setStandardTitle("发现新版本 (" + version + ")");
        dialog.setStandardMsg(msg);
        dialog.setSureText("确定");
        dialog.setCancelText("取消");
        dialog.setCallback(new Callback() {
            @Override
            public void invoke() {  //确定
                downLoadApk(url);
            }
        }, new Callback() {
            @Override
            public void invoke() {  //取消
                if (force) {
                    dialog.dismiss();
                    ActivityStackManager.getInstance().finishAllActivity();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.setCancelable(!force);
        dialog.setCanceledOnTouchOutside(!force);
    }

    //下载APK版本
    private void downLoadApk(String url) {
        DownloadManager manager;

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
        Cursor c = manager.query(query);
        if (c.moveToNext()) {
        } else {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            down.setVisibleInDownloadsUi(true);
            down.setTitle("马甲");
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            //down.setDestinationInExternalFilesDir(this, null, "arkspot-release.apk");
//            String filePath = getCacheDir().getAbsoluteFile() + "arkspot-release.apk";
//            File apkFile = new File(filePath);
//            Uri.withAppendedPath(Uri.fromFile(getCacheDir()),"arkspot-release.apk");
            down.setDestinationUri(Uri.withAppendedPath(Uri.fromFile(getExternalCacheDir()), "arkspot-release.apk"));
            downloadId = manager.enqueue(down);
        }
    }

    private void setDeviceToken() {
        final String userId = SCCacheUtils.getCacheUserId();

        if (!TextUtils.isEmpty(CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS)) && CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS).equalsIgnoreCase("true")) {
            return;
        }
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        if (!TextUtils.isEmpty(PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))) {
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
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                CommonCacheHelper.getInstance().setCache(userId, CACHE_DEVICE_TOKEN_STATUS, "true");
                            }

                        }
                    });
        }
    }

    private void initView() {
        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();
        linearRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog ruleDialog = new CustomDialog(HomeActivity.this, false, 1.0, R.layout.common_dialog_costom, new int[]{R.id.btn_dialog_task_detail_sure});
                ruleDialog.show();
                ruleDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.btn_dialog_task_detail_sure:
                                ruleDialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });
        //通知栏
        setStyleCustom();
    }

    private void initBaiduMap() {
        /*
         * 初始化定位
         * */
        baiduMap = mapView.getMap();
        locationClient = new LocationClient(getApplicationContext());//创建LocationClient对象
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式,LocationMode.Hight_Accuracy：高精度；
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        option.setScanSpan(60 * 1000);//可选，设置发起定位请求的间隔，int类型，单位ms,需设置1000ms以上才有效
        option.setOpenGps(true);//使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);//可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        locationClient.setLocOption(option);

        /*
         * 初始化地图显示
         * */
//        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        //baiduMap.getUiSettings().setAllGesturesEnabled(false);//设置禁用所以有手势
        baiduMap.setMyLocationEnabled(true);//开启定位
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker); //定位图标
        int accuracyCircleFillColor = 0xAAFFFF88;//精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00; //精度圈边框颜色
//        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker,accuracyCircleFillColor,accuracyCircleStrokeColor));

        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
        uiSettings = baiduMap.getUiSettings();
        uiSettings.setOverlookingGesturesEnabled(false);


        bdLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(bdLocationListener);//注册监听函数
        locationClient.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    /*
                     * 绘制所在位置的方区
                     *
                     * */
                    for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                        double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                        double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                        double[] pointGCJ02 = CoordinateTransformUtil.wgs84togcj02(pointLongitude, pointLatitude);
                        LatLng point = new LatLng(pointGCJ02[1], pointGCJ02[0]);
                        pointList.add(point);
                    }
                    //设置地图中心
                    double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                    double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                    double[] focusGCJ02 = CoordinateTransformUtil.wgs84togcj02(focusLongitude, focusLatitude);
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(focusGCJ02[1], focusGCJ02[0]));
                    baiduMap.setMapStatus(mapStatusUpdate);
                    //绘制虚线（需要多添加一起起点坐标，形成矩形）
                    pointList.add(pointList.get(0));
                    OverlayOptions ooPolyline = new PolylineOptions().width(4)
                            .color(0xAA121518).points(pointList);
                    Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                    mPolyline.setDottedLine(true);

                }
            }
        };
        BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            public boolean onMarkerClick(Marker marker) {
//                ToastUtils.showToast(HomeActivity.this, "点击MapMarkr");

                marker.getPosition();
                return false;
            }
        };
        baiduMap.setOnMarkerClickListener(listener);

        MapListener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param point 点击的地理坐标
             */
            public void onMapClick(LatLng point) {
                initCubeMap(point);
                locationClient.requestLocation();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        };
    }

    //设置通知栏自定义消息
   /* private void setCostomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }*/


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(HomeActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.app_logo;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(HomeActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sharedChatRoom(EventMessage eventMessage) {
        if (eventMessage.getCode() == RequestCode.SCREENSHOT) {
//            ToastUtils.showToast(HomeActivity.this, "分享元社区");
            LogUtils.d("sharedChatRoom:", "分享元社区");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap captureScreen = ImageUtils.captureScreen(HomeActivity.this);
                    cropScreenBitmap = ImageUtils.cropScreenBitmap(captureScreen);
                }
            }).start();
            //第三平台授权
            final AuthListener mAuthListener = new AuthListener() {
                @Override
                public void onComplete(Platform platform, int action, BaseResponseInfo data) {
                    Logger.dd(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
                    String toastMsg = null;
                    switch (action) {
                        case Platform.ACTION_AUTHORIZING:
                            if (data instanceof AccessTokenInfo) {        //授权信息
                                String token = ((AccessTokenInfo) data).getToken();//token
                                long expiration = ((AccessTokenInfo) data).getExpiresIn();//token有效时间，时间戳
                                String refresh_token = ((AccessTokenInfo) data).getRefeshToken();//refresh_token
                                String openid = ((AccessTokenInfo) data).getOpenid();//openid
                                //授权原始数据，开发者可自行处理
                                String originData = data.getOriginData();
                                toastMsg = "授权成功:" + data.toString();
                                Logger.dd(TAG, "openid:" + openid + ",token:" + token + ",expiration:" + expiration + ",refresh_token:" + refresh_token);
                                Logger.dd(TAG, "originData:" + originData);
                            }
                            break;
                        case Platform.ACTION_REMOVE_AUTHORIZING:
                            toastMsg = "删除授权成功";
                            break;
                        case Platform.ACTION_USER_INFO:
                            if (data instanceof UserInfo) {      //第三方个人信息
                                String openid = ((UserInfo) data).getOpenid();  //openid
                                String name = ((UserInfo) data).getName();  //昵称
                                String imageUrl = ((UserInfo) data).getImageUrl();  //头像url
                                int gender = ((UserInfo) data).getGender();//性别, 1表示男性；2表示女性
                                //个人信息原始数据，开发者可自行处理
                                String originData = data.getOriginData();
                                toastMsg = "获取个人信息成功:" + data.toString();
                                Logger.dd(TAG, "openid:" + openid + ",name:" + name + ",gender:" + gender + ",imageUrl:" + imageUrl);
                                Logger.dd(TAG, "originData:" + originData);
                            }
                            break;
                    }
                    if (handler != null) {
                        Message msg = handler.obtainMessage(1);
                        msg.obj = toastMsg;
                        msg.sendToTarget();
                    }
                }

                @Override
                public void onError(Platform platform, int action, int errorCode, Throwable error) {
                    Logger.dd(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
                    String toastMsg = null;
                    switch (action) {
                        case Platform.ACTION_AUTHORIZING:
                            toastMsg = "授权失败";
                            break;
                        case Platform.ACTION_REMOVE_AUTHORIZING:
                            toastMsg = "删除授权失败";
                            break;
                        case Platform.ACTION_USER_INFO:
                            toastMsg = "获取个人信息失败";
                            break;
                    }
                    if (handler != null) {
                        Message msg = handler.obtainMessage(1);
                        msg.obj = toastMsg + (error != null ? error.getMessage() : "") + "---" + errorCode;
                        msg.sendToTarget();
                    }
                }

                @Override
                public void onCancel(Platform platform, int action) {
                    Logger.dd(TAG, "onCancel:" + platform + ",action:" + action);
                    String toastMsg = null;
                    switch (action) {
                        case Platform.ACTION_AUTHORIZING:
                            toastMsg = "取消授权";
                            break;
                        // TODO: 2017/6/23 删除授权不存在取消
                        case Platform.ACTION_REMOVE_AUTHORIZING:
                            break;
                        case Platform.ACTION_USER_INFO:
                            toastMsg = "取消获取个人信息";
                            break;
                    }
                    if (handler != null) {
                        Message msg = handler.obtainMessage(1);
                        msg.obj = toastMsg;
                        msg.sendToTarget();
                    }
                }
            };

            final ShareParams shareParams = new ShareParams();//分享参数
            final CustomDialog shareBottomDialog = new CustomDialog(HomeActivity.this, true, true, 1.0, R.layout.layout_bottom_share, new int[]{R.id.share_image, R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlWeibo, R.id.share_close});
            ImageView shareImage = (ImageView) shareBottomDialog.getView(HomeActivity.this, R.id.share_image);
            shareImage.setImageBitmap(cropScreenBitmap);
            try {
                imgeFile = ImageUtils.saveFile(cropScreenBitmap, "cropScreen.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareBottomDialog.show();
            mPlatActionListener = new PlatActionListener() {
                @Override
                public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
                    if (handler != null) {
                        Message message = handler.obtainMessage();
                        message.obj = "分享成功";
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onError(Platform platform, int action, int errorCode, Throwable error) {
                    if (handler != null) {
                        Message message = handler.obtainMessage();
                        message.obj = "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode;
                        Logger.dd(TAG, message.obj + "");
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onCancel(Platform platform, int action) {
                    if (handler != null) {
                        Message message = handler.obtainMessage();
                        message.obj = "分享取消";
                        handler.sendMessage(message);
                    }
                }
            };
            shareBottomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                @Override
                public void OnItemClick(CustomDialog dialog, View view) {
                    switch (view.getId()) {
                        case R.id.mRlWechat:
                            shareParams.setShareType(Platform.SHARE_TEXT);
                            shareParams.setText("分享的文本！！");
                            shareParams.setTitle("分享的标题！！");
                            //调用分享接口share ，分享到QQ平台。
                            JShareInterface.share(Wechat.Name, shareParams, mPlatActionListener);
                            break;
                        case R.id.mRlWeixinCircle:
                            shareParams.setShareType(Platform.SHARE_TEXT);
                            shareParams.setText("分享的文本！！");
                            shareParams.setTitle("分享的标题！！");
                            //调用分享接口share ，分享到QQ平台。
                            JShareInterface.share(WechatMoments.Name, shareParams, mPlatActionListener);
                            break;
                        case R.id.mRlQQ:
                            if (!JShareInterface.isAuthorize(QQ.Name)) {
                                JShareInterface.authorize(QQ.Name, mAuthListener);
                            }
//                            shareParams.setShareType(Platform.SHARE_TEXT);
//                            shareParams.setText("身边的人都在这");
//                            shareParams.setTitle("马甲App");
                            Bitmap drawableBitmap = ImageUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.app_logo));
                            try {
//                                imgeFile = ImageUtils.readBitmap()
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            shareParams.setShareType(Platform.SHARE_WEBPAGE);
                            shareParams.setTitle("马甲APP");
                            shareParams.setText("身边的人都在这");
                            shareParams.setUrl(SCCacheUtils.getCacheHeadImg());
                            //调用分享接口share ，分享到QQ平台。
                            JShareInterface.share(QQ.Name, shareParams, mPlatActionListener);
                            break;
                        case R.id.mRlWeibo:
                            shareParams.setShareType(Platform.SHARE_TEXT);
                            shareParams.setShareType(Platform.SHARE_IMAGE);
                            shareParams.setText("身边的人都在这");
                            //调用分享接口share ，分享到QQ平台。
                            JShareInterface.share(SinaWeibo.Name, shareParams, mPlatActionListener);
                            break;
                        case R.id.share_close:
                            shareBottomDialog.dismiss();
                            break;
                    }
                }
            });
        }
    }


    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
    }

    @Override
    public void requestPermissionsSuccess() {
        initBaiduMap();
        locationClient.restart();

    }

    @Override
    public void requestPermissionsFail() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
//            if (requestCode==10002){
//                mLocationClient.restart();
//            }

            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        locationClient.stop();
//        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                LogUtils.d("leaveChatRoom","离开聊天室");
//            }
//        });
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @OnClick({R.id.image_view_history})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_history:
                readyGo(FootPrintActivity.class);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeProgress();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            // 构造定位数据
            //构建MarkerOption，用于在地图上添加Marker
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.mipmap.map_marker);
            //设置定位跟随
            MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
            //显示定位图标
            baiduMap.setMyLocationConfiguration(config);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //baiduMap.setMyLocationEnabled(false);

            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                bdLocation
                    initCubeMap(latLng);
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(myFocusPoint);
                    baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
                    baiduMap.setMapStatus(mapStatusUpdate);
                }
            });
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                myLocation = bdLocation;

                coordinateConverter = new CoordinateConverter();
                //百度转为gps坐标
                gpsLatLng = coordinateConverter.from(CoordinateConverter.CoordType.BD09LL).coord(latLng).convert();
                //服务端请求数据
//                handler.sendEmptyMessage(1);
                //设置显示地图中心点
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.setMapStatus(mapStatusUpdate);

                initData(latLng);
                baiduMap.setOnMapClickListener(MapListener);

//                LogUtils.d("百度地图定位回调 " + bdLocation.getLocType() + "百度地图坐标 " + bdLocation.getCoorType() + "类型 " + bdLocation.getLongitude() + " " + bdLocation.getLatitude());
//                LogUtils.d(bdLocation.getLocType() + "百度地图坐标 转换为 WGS84类型 经度" + WGS84point[0] + " 纬度： " + WGS84point[1]);
                if (bdLocation.getLocationWhere() == BDLocation.LOCATION_WHERE_OUT_CN) {
                    //判断位置是否为国外
                }


            }

        }

    }

    public void showProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }
}
