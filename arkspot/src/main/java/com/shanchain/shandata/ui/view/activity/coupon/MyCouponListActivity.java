package com.shanchain.shandata.ui.view.activity.coupon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.fragment.MyCreateCouponFragment;
import com.shanchain.shandata.ui.view.fragment.MyReciverCouponFragment;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

public class MyCouponListActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {
    private ArthurToolBar toolBar;
    private ViewPager vpCoupon;
    private TabLayout tabLayoutCoupon;
    private static final int REQUEST_CODE = 100;
    private CustomDialog customDialog;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_coupon_list;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();
        initData();

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setName("肯定基饭店" + i);
            couponInfo.setPrice("" + i);
            couponInfo.setUserStatus("领取" + i);
            couponInfo.setRemainAmount("剩余" + i);
//            couponInfoList.add(couponInfo);
        }
    }

    private void initView() {
        String[] titles = {"我领取的", "我创建的"};
        List fragments = new ArrayList();
        fragments.add(new MyReciverCouponFragment());
        fragments.add(new MyCreateCouponFragment());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragments);
        vpCoupon = findViewById(R.id.vp_coupon);
        tabLayoutCoupon = findViewById(R.id.tab_coupon);
        vpCoupon.setOffscreenPageLimit(2);
        vpCoupon.setAdapter(adapter);
        tabLayoutCoupon.setupWithViewPager(vpCoupon);
        vpCoupon.setCurrentItem(0);
        customDialog = new CustomDialog(MyCouponListActivity.this, R.layout.common_dialog_costom,
                new int[]{R.id.tv_input_dialog_title, R.id.even_message_content, R.id.btn_dialog_task_detail_sure});
    }

    private void initToolBar() {
        toolBar = findViewById(R.id.tb_main);
        toolBar.setTitleText("我的马甲劵");
        toolBar.setRightImage(R.mipmap.scan);
        toolBar.setOnLeftClickListener(this);//左侧导航栏监听
        toolBar.setOnRightClickListener(this);//右侧导航栏监听
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        /*
         * 扫码
         * */
        int takePhotoPermission = ContextCompat.checkSelfPermission(MyCouponListActivity.this, Manifest.permission.CAMERA);
        if (takePhotoPermission == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MyCouponListActivity.this, ScanQRCodeActivity.class);
//            Intent intent = new Intent(MyCouponListActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MyCouponListActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCouponList(EventMessage eventMessage) {
        if (eventMessage.getCode() == 0) {
            initData();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    SCHttpUtils.get()
                            .url(HttpApi.SUB_COUPONS_INFO)
                            .addParams("subCoupId", "" + result)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    LogUtils.d(TAG, "网络异常");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                                    if (NetErrCode.SUC_CODE.equals(code)) {
                                        String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                                        String vendorUse = com.alibaba.fastjson.JSONObject.parseObject(data).getString("vendorUser");
                                        if (SCCacheUtils.getCacheUserId().equals(vendorUse)) {
                                            Intent intent = new Intent(MyCouponListActivity.this, CouponDetailsActivity.class);
                                            intent.putExtra("subCoupId", result);
                                            intent.putExtra("checkCoupon", true);
                                            startActivity(intent);
                                        } else {
                                            customDialog.setDialogTitle("");
                                            customDialog.setMessageContentSize(14);
                                            customDialog.setMessageContent("很抱歉，您无法核销他人创建的马甲券，尝试创建自己的马甲券吧～");
                                            customDialog.show();
                                        }

                                    } else {
                                        customDialog.setDialogTitle("");
                                        customDialog.setMessageContentSize(14);
                                        customDialog.setMessageContent("很抱歉，您无法核销他人创建的马甲券，尝试创建自己的马甲券吧～");
                                        customDialog.show();
                                    }
                                }
                            });

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MyCouponListActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
