package com.shanchain.shandata.ui.view.activity.coupon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.BaseViewHolder;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jmessage.support.qiniu.android.utils.Json;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CouponListActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {
    private ArthurToolBar toolBar;
    private LinearLayout addCoupon;
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private int pageNo = 0, pageSize = 10;
    private String roomid;
    private List<CouponInfo> couponInfoList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_coupon_list;
    }

    @Override
    protected void initViewsAndEvents() {
        roomid = getIntent() != null ? getIntent().getStringExtra("roomId") : "0";
        initToolBar();
        initView();
//        initData();
    }

    private void initData() {

        Map reqeust = new HashMap();
        reqeust.put("pageNo", pageNo + "");
        reqeust.put("pageSize", pageSize + "");
        reqeust.put("roomid", roomid + "");
        LogUtils.d("mapJson", JSONArray.toJSONString(reqeust) + "");
        SCHttpUtils.postByBody(HttpApi.COUPONS_LIST, "" + JSONArray.toJSONString(reqeust), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("获取卡劵失败");
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(CouponListActivity.this, "网络异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.toString();
//                int code1 = JSONObject.parseObject(response.body().string()).getIntValue("code");
                final String code = JSONObject.parseObject(response.body().string()).getString("code");
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(CouponListActivity.this, "" + code);
                    }
                });

                if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                    String data = JSONObject.parseObject(response.toString()).getString("data");
                }

            }
        });

    }

    private void initView() {
        refreshLayout = findViewById(R.id.refresh_layout);
        addCoupon = findViewById(R.id.linear_add_coupon);
        recyclerView = findViewById(R.id.recycler_view_coupon);
        for (int i = 0; i < 20; i++) {
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setName("肯定基饭店" + i);
            couponInfo.setPrice("$" + i);
            couponInfo.setUserStatus("领取" + i);
            couponInfo.setRemainAmount("剩余" + i);
            couponInfoList.add(couponInfo);
        }
        CouponListAdapter adapter = new CouponListAdapter(CouponListActivity.this, couponInfoList, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two});
        LinearLayoutManager layoutManager = new LinearLayoutManager(CouponListActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CouponListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder, int position) {
                Intent intent = new Intent(CouponListActivity.this,MyCouponListActivity.class);
                startActivity(intent);
            }
        });


        //添加卡劵
        addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(CouponListActivity.this,"添加马甲劵");
            }
        });

    }

    private void initToolBar() {
        toolBar = findViewById(R.id.tb_coupon);
        toolBar.setTitleText("马甲卷");
        toolBar.setRightText("我的");
        toolBar.setOnLeftClickListener(this);
        toolBar.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(CouponListActivity.this, MyCouponListActivity.class);
        startActivity(intent);
    }
}
