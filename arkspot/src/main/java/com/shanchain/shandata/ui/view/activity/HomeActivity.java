package com.shanchain.shandata.ui.view.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.square.FoodPrintActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryTitleActivity;
import com.shanchain.shandata.utils.CoordinateTransformUtil;
import com.shanchain.shandata.utils.PermissionHelper;
import com.shanchain.shandata.utils.PermissionInterface;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
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
    private LatLng point;
    private double[] WGS84point;
    private Runnable runnable;
    private Handler handler;


    @Bind(R.id.map_view_home)
    MapView mapView;
    @Bind(R.id.button_join)
    Button btJoin;
    @Bind(R.id.text_view_location)
    TextView tvLocation;
    @Bind(R.id.image_view_info)
    ImageView imgInfo;
    @Bind(R.id.image_view_history)
    ImageView imgHistory;
    private double[] gcj02point;
    private UiSettings uiSettings;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEvents() {

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

//        UMConfigure.setLogEnabled(true); //显示友盟log日记
        //检查apk版本
        checkApkVersion();
        initDeviceToken();
        initView();
        initBaiduMap();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 60 * 1000);
//                initData();
            }
        };

    }

    //请求接口数据
    private void initData() {
        //上传用户实时坐标
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_COORDINATE)
                .addParams("longitude", WGS84point[0] + "")
                .addParams("latitude", WGS84point[1] + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求成功 #######");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            LogUtils.d("####### " + data + " ########");

                        }
                    }
                });

        //获取聊天室信息
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", WGS84point[0] + "")
                .addParams("latitude", WGS84point[1] + "")
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
                            String data = JSONObject.parseObject(response).getString("data");
                            LogUtils.d("####### " + data + " ########");


                        }

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

    @OnClick({R.id.button_join,})
    public void buttonOnclick(View view) {
        switch (view.getId()) {
            case R.id.button_join:
                readyGo(MessageListActivity.class);
                break;
        }

    }

    private void initView() {
        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();


    }

    private void initBaiduMap() {
        /*
         * 初始化定位
         * */
        locationClient = new LocationClient(getApplicationContext());//创建LocationClient对象
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式,LocationMode.Hight_Accuracy：高精度；
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("gcj02");
        option.setScanSpan(10 * 1000);//可选，设置发起定位请求的间隔，int类型，单位ms,需设置1000ms以上才有效
        option.setOpenGps(true);//使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);//可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        locationClient.setLocOption(option);

        /*
         * 初始化地图显示
         * */
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        //baiduMap.getUiSettings().setAllGesturesEnabled(false);//设置禁用所以有手势
        baiduMap.setMyLocationEnabled(true);//开启定位
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker); //定位图标
        int accuracyCircleFillColor = 0xAAFFFF88;//精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00; //精度圈边框颜色
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker,
                accuracyCircleFillColor, accuracyCircleStrokeColor));
//        baiduMap.setMyLocationConfiguration();
        uiSettings = baiduMap.getUiSettings();
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别


        bdLocationListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {

                point = new LatLng(bdLocation.getLongitude(), bdLocation.getLatitude());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLocation.setText(bdLocation.getLongitude() + " " + bdLocation.getLatitude());
                        LogUtils.d(bdLocation.getLocType() + "百度地图坐标 " + bdLocation.getCoorType() + "类型 " + bdLocation.getLongitude() + " " + bdLocation.getLatitude());

                        WGS84point = CoordinateTransformUtil.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());
                        LogUtils.d(bdLocation.getLocType() + "百度地图坐标 转换为 WGS84类型 " + WGS84point[0] + " " + WGS84point[1]);
                        handler = new Handler();
                        handler.postDelayed(runnable, 1000);
                    }
                });
                // 构造定位数据
                gcj02point = CoordinateTransformUtil.wgs84togcj02(WGS84point[0], WGS84point[1]);
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(gcj02point[1])
                        .longitude(gcj02point[0]).build();
//                baiduMap.setMyLocationData(locData);// 设置定位数据

                //构建Marker图标
                LatLng point = new LatLng(gcj02point[1], gcj02point[0]);
//                LatLng point = new LatLng(20.045065, 110.324457);
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.map_marker);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions OverlayOption = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                baiduMap.addOverlay(OverlayOption);


            }
        };
        locationClient.registerLocationListener(bdLocationListener);//注册监听函数
        locationClient.start();
        //设置经纬度（参数一是纬度，参数二是经度）
//         MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(new LatLng(gcj02point[1], gcj02point[0]));
        mapstatusupdate = MapStatusUpdateFactory.newLatLng(new LatLng(20.045065, 110.324457));// 39.93923,116.357428(北京坐标)
        //对地图的中心点进行更新
        baiduMap.setMapStatus(mapstatusupdate);

        /*
         * 在地图上画一个矩形
         * */
        LatLng pt1 = new LatLng(20.047514, 110.323298);
        LatLng pt2 = new LatLng(20.047896, 110.327493);
        LatLng pt3 = new LatLng(20.045231,110.328652);
        LatLng pt4 = new LatLng(20.043957,110.323936);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);

        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);

        //在地图上添加多边形Option，用于显示
        baiduMap.addOverlay(polygonOption);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        mapView.onDestroy();
        locationClient.stop();
        JMessageClient.logout();
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
                readyGo(FoodPrintActivity.class);
                break;
        }

    }
}
