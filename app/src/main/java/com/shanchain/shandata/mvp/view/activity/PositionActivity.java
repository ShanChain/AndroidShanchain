package com.shanchain.shandata.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.PositionAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.mvp.model.PositionInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ThreadUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class PositionActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private static final int RESULT_CODE = 20;
    @Bind(R.id.et_position_search)
    EditText mEtPositionSearch;
    @Bind(R.id.rv_position_list)
    RecyclerView mRvPositionList;
    @Bind(R.id.activity_position)
    LinearLayout mActivityPosition;
    private ArthurToolBar mPositionToolBar;
    private List<PositionInfo> datas;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_position;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPositionList.setLayoutManager(linearLayoutManager);

        mRvPositionList.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        PositionAdapter adapter = new PositionAdapter(this, R.layout.item_position, datas);
        mRvPositionList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //返回位置信息
                returnPosition(position);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void returnPosition(int position) {
        Intent intent = new Intent();
        intent.putExtra("positionInfo", datas.get(position));
        setResult(RESULT_CODE, intent);
        finish();
    }

    private void initDatas() {
        datas = new ArrayList<>();
        mLocationClient = new LocationClient(MyApplication.getContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocationOption();
        mLocationClient.start();
    }

    private void initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    private void initToolBar() {
        mPositionToolBar = (ArthurToolBar) findViewById(R.id.toolbar_position);
        mPositionToolBar.setBtnEnabled(true, false);
        mPositionToolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(mMyLocationListener);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\n addr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\n城市 : ");               //获取城市
                sb.append(location.getCity());

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息


                LogUtils.i("BaiduLocationApiDem", "============================");

                LogUtils.i("BaiduLocationApiDem",
                        " 省... : " + location.getProvince()

                                + " 城市 ...: " + location.getCity()

                                + " 县/区 ..: " + location.getDistrict()

                                + " 街道 : " + location.getStreet()

                                + " 街道号码 : " + location.getStreetNumber()

                                + " 楼层 : " + location.getFloor());

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");

                sb.append("离线定位成功，离线定位结果也是有效的");
                sb.append("\n addressstr地址 : ");
                sb.append(location.getAddrStr());

                sb.append("\n address");
                sb.append(location.getAddress());
            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            LogUtils.i("BaiduLocationApiDem", sb.toString());

            if (location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeGpsLocation) {
                //网络定位成功或者gps定位成功
                PositionInfo positionInfo = new PositionInfo();
                positionInfo.setAddress(location.getProvince() + "," + location.getCity() + "," + location.getDistrict());
                positionInfo.setDetails(location.getLocationDescribe());
                datas.add(positionInfo);
                List<Poi> pois = location.getPoiList();
                for (int i = 0; i < pois.size(); i++) {
                    PositionInfo info = new PositionInfo();
                    info.setAddress(location.getProvince() + "," + location.getCity() + "," + location.getDistrict());
                    info.setDetails(pois.get(i).getName());
                    datas.add(info);
                }
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView();
                    }
                });

            }
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
