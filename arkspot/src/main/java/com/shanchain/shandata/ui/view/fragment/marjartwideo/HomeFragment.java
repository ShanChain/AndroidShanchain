package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.SCInputDialog;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.BuildConfig;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.receiver.MessageReceiver;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.presenter.HomePresenter;
import com.shanchain.shandata.ui.presenter.impl.HomePresenterImpl;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.square.MyGroupActivity;
import com.shanchain.shandata.ui.view.activity.square.PayforSuccessActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.HomeView;
import com.shanchain.shandata.utils.CoordinateTransformUtil;
import com.shanchain.shandata.utils.PermissionHelper;
import com.shanchain.shandata.utils.PermissionInterface;
import com.tinkerpatch.sdk.TinkerPatch;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.shanchain.data.common.base.Constants.CACHE_DEVICE_TOKEN_STATUS;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :地图界面
 */
public class HomeFragment extends BaseFragment implements PermissionInterface, HomeView {
    @Bind(R.id.map_view_home)
    MapView mapView;
    @Bind(R.id.image_view_info)
    ImageView imgInfo;
    @Bind(R.id.linear_reset_location)
    LinearLayout linearReset;
    @Bind(R.id.image_view_history)
    ImageButton imageViewHistory;
    @Bind(R.id.linear_hot)
    LinearLayout linearHot;
    @Bind(R.id.img_view_hide)
    ImageView imgViewHide;
    @Bind(R.id.button_join)
    LinearLayout buttonJoin;
    @Bind(R.id.text_view_location)
    TextView tvLocation;
    @Bind(R.id.relative_hideOrShow)
    RelativeLayout relativeHideOrShow;
    @Bind(R.id.iv_my_team)
    ImageView ivMyTeam;
    @Bind(R.id.im_person)
    ImageView imPerson;

    private HomePresenter mHomePresenter;
    public static BaiduMap baiduMap;
    public LocationClient locationClient;
    private UiSettings uiSettings;
    public static BDLocationListener bdLocationListener;
    private Coordinates coordinates;
    private List pointList = new ArrayList();
    private BaiduMap.OnMapClickListener MapListener;
    private boolean isAddRoom = false;
    private CoordinateConverter coordinateConverter;
    private LatLng myFocusPoint;
    private SCInputDialog mScInputDialog;
    private String roomID = "";
    private String roomName;
    public static LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位
    private BDLocation myLocation;
    private LatLng gpsLatLng;
    private List<Coordinates> coordinatesList;
    private List roomList = new ArrayList();
    private PermissionHelper mPermissionHelper;
    private MessageReceiver mMessageReceiver;
    private boolean isHide = true; //是否隐藏
    private boolean isClickMap = false;//是否是点击地图

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.activity_home_new, null);
    }

    @Override
    public void initData() {
        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(getActivity(), this);
        mPermissionHelper.requestPermissions();
        mHomePresenter = new HomePresenterImpl(this);
        setInitData();

        setDeviceToken();
        //通知栏
        setStyleCustom();
        initBaiduMap();
        //注册自定义消息广播
        registerMessageReceiver();
    }
    //初始化数据
    private void setInitData(){
        //每次进入主界面自动查询是否有补丁文件更新
        if(BuildConfig.TINKER_ENABLE){
            TinkerPatch.with().fetchPatchUpdate(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
    }
    //设置设备的token
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
            return;
        }
        mHomePresenter.getDeviceToken(token,PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""));
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MessageReceiver.MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, filter);
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
    }

    @Override
    public void requestPermissionsFail() {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //隐藏或者显示底部按钮
    @OnClick(R.id.img_view_hide)
    void hideView(){
        if (isHide) {
            imgViewHide.setBackground(getResources().getDrawable(R.mipmap.home_show));
            buttonJoin.setVisibility(View.GONE);
            linearReset.setVisibility(View.GONE);
            linearHot.setVisibility(View.GONE);
            tvLocation.setVisibility(View.GONE);
            isHide = false;
        } else {
            imgViewHide.setBackground(getResources().getDrawable(R.mipmap.home_hide));
            buttonJoin.setVisibility(View.VISIBLE);
            linearReset.setVisibility(View.VISIBLE);
            linearHot.setVisibility(View.VISIBLE);
            tvLocation.setVisibility(View.VISIBLE);
            isHide = true;
        }
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);//加载动画资源文件
        imgViewHide.setAnimation(shake);
    }

    //我的小分队
    @OnClick(R.id.iv_my_team)
    void goToMyGroup(){
        startActivity(new Intent(getActivity(), MyGroupActivity.class));
    }

    @OnClick(R.id.im_person)
    void gotoMyGetGroup(){
        startActivity(new Intent(getActivity(), PayforSuccessActivity.class));
    }

    /**
     * 通知栏适配 - 定义通知栏渠道
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
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
                    break;
                case 1001:
                    Bundle bundle = msg.getData();
                    LatLng point = bundle.getParcelable("point");
                    String url = bundle.getString("url");
                    EditText etContent = mScInputDialog.getEtContent();
                    String customRoomName = etContent.getText().toString();
                    mHomePresenter.createChatRoom(point,customRoomName,url,getActivity());
                    break;
            }

        }
    };

    /*
     * 初始化定位
     * */
    private void initBaiduMap() {
        baiduMap = mapView.getMap();
        locationClient = new LocationClient(getActivity().getApplicationContext());//创建LocationClient对象
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式,LocationMode.Hight_Accuracy：高精度；
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        option.setScanSpan(3 * 60 * 1000);//可选，设置发起定位请求的间隔，int类型，单位ms,需设置1000ms以上才有效
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
            public void onMapClick(final LatLng point) {
                if (isAddRoom == true) {
                    //绘制方区
//                    drawCubeMap(point);
                } else {
                    isClickMap = true;
                    initCubeMap(point);
                    locationClient.requestLocation();
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        };
    }


    //点击请求方区
    public void initCubeMap(LatLng point) {
        //获取聊天室信息
        mHomePresenter.getCurrentPoint(point);
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    /**
     * 设置devicetoken
     * @param response
     */
    @Override
    public void setDeviceTokenInfo(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String userId = SCCacheUtils.getCacheUserId();
            CommonCacheHelper.getInstance().setCache(userId, CACHE_DEVICE_TOKEN_STATUS, "true");
        }
    }

    @Override
    public void setCurrentPoint(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            LogUtils.d(TAG, "####### " + "获取聊天室地理位置信息" + " ########");
            String data = JSONObject.parseObject(response).getString("data");
            coordinates = JSONObject.parseObject(data, Coordinates.class);
            if (coordinates == null) {
                return;
            }
            roomID = coordinates.getRoomId();
            String latLang = coordinates.getRoomName();
            //设置经纬度显示
            setCurrentAddress(latLang);

        }
    }
    //获取附近的聊天室
    @Override
    public void setAroundChatRoome(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            LogUtils.d("####### " + "获取聊天室信息" + " ########");
            String data = JSONObject.parseObject(response).getString("data");
            String room = JSONObject.parseObject(data).getString("room");
            coordinatesList = JSONObject.parseArray(room, Coordinates.class);
            if (coordinatesList == null) {
                return;
            }
            setMapViewPointView();

        }
    }

    @Override
    public void setCoordinateInfoResponse(String response) {

    }

    @Override
    public void setCreateChatRoomResponse(String string) {
        final String code = JSONObject.parseObject(string).getString("code");
        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
            String data = SCJsonUtils.parseData(string);
            String coordinateInfo = SCJsonUtils.parseString(data, "coordinateInfo");
            String ChatRoomInfo = SCJsonUtils.parseString(data, "hotChatRoom");
            Coordinates coordas = SCJsonUtils.parseObj(coordinateInfo, Coordinates.class);
            HotChatRoom hotChatRoom = SCJsonUtils.parseObj(ChatRoomInfo, HotChatRoom.class);
            //刷新热门聊天室
            EventBus.getDefault().post(new EventMessage(NetErrCode.ADD_ROOM_SUCCESS));
            //进入聊天室
            Intent intent = new Intent(getActivity(), MessageListActivity.class);
            intent.putExtra("roomId", "" + hotChatRoom.getRoomId());
            intent.putExtra("roomName", "" + hotChatRoom.getRoomName());
            startActivity(intent);
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(getActivity(), getResources().getString(R.string.internet_error));
                }
            });
        }
    }

    @Override
    public void setUploadImageToOSSResponse(String url, boolean isSucess,LatLng point) {
        LogUtils.d("--->> oss issuccess: "+isSucess);
        if(isSucess){
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("url",url);
            bundle.putParcelable("point",point);
            message.what = 1001;
            message.setData(bundle);
            handler.sendMessage(message);
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(getActivity(), getResources().getString(R.string.internet_error));
                }
            });
        }
    }

    /**
     * 绘制附近的方区
     */
    private void setMapViewPointView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (coordinatesList != null) {
                    for (int i = 0; i < coordinatesList.size(); i++) {
                        if (coordinatesList.get(i) != null
                                && coordinatesList.get(i).getFocusLatitude() != null
                                && coordinatesList.get(i).getFocusLongitude() != null) {
                            LatLng srcLatLang = new LatLng(Double.valueOf(coordinatesList.get(i).getFocusLatitude()), Double.valueOf(coordinatesList.get(i).getFocusLongitude()));
                            LatLng focusPoint = coordinateConverter.from(CoordinateConverter.CoordType.GPS).coord(srcLatLang).convert();
                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.home_new_icon);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(srcLatLang)
                                    .perspective(true)
                                    .animateType(MarkerOptions.MarkerAnimateType.jump)
                                    .icon(bitmap);

                            //在地图上添加Marker，并显示
                            baiduMap.addOverlay(option);
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
            }
        }).start();
    }

    /**
     * 点击地图后显示当前地址
     * @param latLang
     */
    private void setCurrentAddress(String latLang){
        String[] strings = latLang.split(",");
        final double lat = Double.valueOf(strings[0]);
        final double lang = Double.valueOf(strings[1]);
        final String latLocation, langLocation;
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat enDf = new DecimalFormat("#.00");
        String sta = MyApplication.systemLanguge;
        if (lat < 0) {
            latLocation = sta.equals("zh") ? "南纬" + df.format(lat) : enDf.format(lat) + "'S";

        } else {
            latLocation = sta.equals("zh") ? "北纬" + df.format(lat) : enDf.format(lat) + "'N";
        }
        if (lang > 0) {
            langLocation = sta.equals("zh") ? "东经" + df.format(lang) : enDf.format(lang) + "'E";

        } else {
            langLocation = sta.equals("zh") ? "西经" + df.format(lang) : enDf.format(lang) + "'W";
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLocation.setText(langLocation + " °," + latLocation + " °");
            }
        });
        Message roomMessage = new Message();
        roomMessage.what = 1;
        roomMessage.obj = langLocation + "°," + latLocation + "°";
        try {
            roomMessage.arg1 = Integer.valueOf(roomID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //getRoomIdHandle.sendMessage(roomMessage);

        drawCurrentAddress();
    }

    /**
     * 绘制所在位置的方区
     */
    private void drawCurrentAddress(){
        ArrayList<LatLng> clickPointList = new ArrayList<>();
        for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
            double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
            double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
            LatLng point = new LatLng(pointLatitude, pointLongitude);
            // 将GPS设备采集的原始GPS坐标转换成百度坐标
            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
            coordinateConverter.coord(point);
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
        final MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(myFocusPoint); //未转换的坐标
        baiduMap.setMapStatus(mapStatusUpdate);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
//                            baiduMap.setBaiduHeatMapEnabled(true);

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

        if(!isClickMap)return;
        //弹出显示添加元社区
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mScInputDialog = new SCInputDialog(getActivity(), getString(R.string.add_meta_c),
                        getString(R.string.enter_m_name));
                //显示输入元社区
                mScInputDialog.setCallback(new Callback() {//确定
                    @Override
                    public void invoke() {
                        if(TextUtils.isEmpty(mScInputDialog.getEtContent().getText())){
                            ToastUtils.showToast(getActivity(), R.string.enter_char_room);
                            return;
                        }
                        addCustomChatRoom(baiduMap, gpsLatLng);
                    }
                }, new Callback() {//取消
                    @Override
                    public void invoke() {

                    }
                });
                //显示输入元社区弹窗
                mScInputDialog.show();
            }
        });
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

                getCurrentDta(latLng);
//                transYear(latLng);//todo
                baiduMap.setOnMapClickListener(MapListener);

//                LogUtils.d("百度地图定位回调 " + bdLocation.getLocType() + "百度地图坐标 " + bdLocation.getCoorType() + "类型 " + bdLocation.getLongitude() + " " + bdLocation.getLatitude());
//                LogUtils.d(bdLocation.getLocType() + "百度地图坐标 转换为 WGS84类型 经度" + WGS84point[0] + " 纬度： " + WGS84point[1]);
                if (bdLocation.getLocationWhere() == BDLocation.LOCATION_WHERE_OUT_CN) {
                    //判断位置是否为国外
                }


            }

        }

    }

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(getActivity(), R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.app_logo;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(HomeActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }

    //添加自定义社区
    private File screenshotFile = null;
    private void addCustomChatRoom(BaiduMap map, final LatLng myFocusPoint) {
        //截图
        final int rectTop = (mScreenHeight / 2 - mScreenWidth / 2);
        final int rectBottom = rectTop + mScreenWidth + 10;
        map.snapshotScope(new Rect(0, rectTop, mScreenWidth, rectBottom), new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                FileOutputStream out;
                try {
                    String filePath = ImageUtils.getSDPath() + File.separator + "shanchain";
                    //创建文件夹
                    File fPath = new File(filePath);
                    if (!fPath.exists()) {
                        fPath.mkdir();
                    }
                    screenshotFile = new File(filePath + File.separator + ImageUtils.getTempFileName() + ".png");
                    out = new FileOutputStream(screenshotFile);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtils.d("--->>capture image path: ",screenshotFile.getAbsolutePath());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //上传图片到oss
                        mHomePresenter.updoadImageToOSS(getActivity(),screenshotFile.getAbsolutePath(),myFocusPoint);
                    }
                },1000);
            }
        });
    }

    //请求接口数据
    private void getCurrentDta(LatLng gpsLatLng) {
        //获取周边聊天室
        mHomePresenter.getCurrentChatRoom(gpsLatLng);
        //获取当前地址聊天室信息
        mHomePresenter.getCurrentPoint(gpsLatLng);

    }
}
