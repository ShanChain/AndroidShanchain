package com.shanchain.shandata.ui.view.activity.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MyCreateDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.tb_coupon_detail)
    ArthurToolBar tbCouponDetail;
    @Bind(R.id.iv_item_story_avatar)
    CircleImageView ivItemStoryAvatar;
    @Bind(R.id.even_message_bounty)
    TextView evenMessageBounty;
    @Bind(R.id.tv_item_story_name)
    TextView tvItemStoryName;
    @Bind(R.id.tv_item_code)
    TextView tvItemCode;
    @Bind(R.id.tv_use_rule)
    TextView tvUseRule;
    @Bind(R.id.tv_coupon_num)
    TextView tvCouponNum;
    @Bind(R.id.tv_create_time)
    TextView tvCreateTime;
    @Bind(R.id.tv_rule_details)
    TextView tvRuleDetails;
    @Bind(R.id.linear_show_details)
    RelativeLayout linearShowDetails;
    @Bind(R.id.tv_coupon_details_expiration)
    TextView tvCouponDetailsExpiration;
    @Bind(R.id.linear_coupon_detail)
    LinearLayout linearCouponDetail;
    @Bind(R.id.tv_coupon_waiting_check_num)
    TextView tvCouponWaitingCheckNum;
    @Bind(R.id.tv_coupon_checked_num)
    TextView tvCouponCheckedNum;
    @Bind(R.id.tv_coupon_surplus_num)
    TextView tvCouponSurplusNum;
    @Bind(R.id.linear_coupon_check)
    LinearLayout linearCouponCheck;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.recycler_view_coupon_check_list)
    RecyclerView recyclerViewCouponCheckList;

    private int pageNo = 0, pageSize = 10;


    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 10;
    private List<CouponSubInfo> couponList = new ArrayList();
    private List<CouponSubInfo> clientList = new ArrayList();
    private CouponListAdapter adapter;
    private String couponsId, subCoupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_create_details;
    }

    @Override
    protected void initViewsAndEvents() {
        couponsId = getIntent().getStringExtra("couponsId");
        subCoupId = getIntent().getStringExtra("subCoupId");
        tbCouponDetail.setTitleText("马甲劵详情");
        tbCouponDetail.setOnLeftClickListener(this);
//        tbCouponDetail.setOnRightClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCreateDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCouponCheckList.setLayoutManager(linearLayoutManager);
        initData();
    }

    private void initData() {
        if (!TextUtils.isEmpty(couponsId)) {
            SCHttpUtils.get()
                    .url(HttpApi.MY_CREATE_COUPONS_INFO)
                    .addParams("couponsId", "" + couponsId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d(TAG, "网络异常");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String code = JSONObject.parseObject(response).getString("code");
                            if (NetErrCode.SUC_CODE.equals(code)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                CouponSubInfo couponSubInfo = JSONObject.parseObject(data, CouponSubInfo.class);
                                RequestOptions options = new RequestOptions();
                                try {
                                    options.placeholder(R.mipmap.aurora_headicon_default);
                                    Glide.with(MyCreateDetailsActivity.this).load(couponSubInfo.getPhotoUrl()).apply(options).into(ivItemStoryAvatar);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                tvItemStoryName.setText(couponSubInfo.getName() + "");
                                tvItemCode.setText(couponSubInfo.getTokenSymbol() + "");
                                evenMessageBounty.setText(couponSubInfo.getPrice() + "");
                                tvCouponNum.setText("共 " + couponSubInfo.getAmount() + " 张");
                                tvRuleDetails.setText(couponSubInfo.getDetail());
                                tvCouponWaitingCheckNum.setText(couponSubInfo.getUnusedNum() + "");
                                tvCouponCheckedNum.setText(couponSubInfo.getUsedNum() + "");
                                tvCouponSurplusNum.setText(couponSubInfo.getRemainAmount() + "");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String createTime = sdf.format(new Date(Long.valueOf(couponSubInfo.getCreateTime())));
                                tvCreateTime.setText(createTime + "");
                                String expiration = sdf.format(new Date(Long.valueOf(couponSubInfo.getDeadline())));
                                tvCouponDetailsExpiration.setText("有效期至：" + expiration + "");
//                                tvCreateTime.setText(couponSubInfo.getCreateTime() + "");
                                //获取领劵人员的列表
                                getClientList(couponSubInfo);

                            }
                        }
                    });
        }
    }

    private void getClientList(final CouponSubInfo couponSubInfo) {
        String symbol = couponSubInfo.getTokenSymbol().substring(0, 3);
        SCHttpUtils.get()
                .url(HttpApi.COUPON_CLIENT_LIST)
                .addParams("pageNo", "" + pageNo)
                .addParams("pageSize", "" + pageSize)
                .addParams("tokenSymbol", "" + symbol)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String list = JSONObject.parseObject(data).getString("list");
                            clientList = JSONObject.parseArray(list, CouponSubInfo.class);
//                            for (int i = 0; i < 10; i++) {
//                                CouponSubInfo couponSubInfo1 = new CouponSubInfo();
//                                couponSubInfo1.setUseTime("" + i);
//                                couponSubInfo1.setUserId(i);
//                                couponSubInfo1.setTokenStatus(i % 2);
//                                clientList.add(couponSubInfo1);
//                            }
                            recyclerViewCouponCheckList.setAdapter(new BaseQuickAdapter<CouponSubInfo, BaseViewHolder>(R.layout.item_common_simple, clientList) {
                                @Override
                                protected void convert(BaseViewHolder helper, CouponSubInfo item) {
                                    helper.setText(R.id.tv_item_story_name, item.getUserId() + "");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                    switch (item.getTokenStatus()) {
                                        case CouponSubInfo.RECEIVER:
                                            helper.setText(R.id.tv_item_coupon_status, "已领取");
                                            if (item.getGetTime() == null) return;
                                            String getTime = sdf.format(new Date(Long.valueOf(item.getGetTime())));
                                            helper.setText(R.id.tv_item_receive_time, "" + getTime);
                                            break;
                                        case CouponSubInfo.RECEIVER_UN_USE:
                                            helper.setText(R.id.tv_item_coupon_status, "" + item.getTokenStatus());
                                            break;
                                        case CouponSubInfo.RECEIVER_USE:
                                            helper.setText(R.id.tv_item_coupon_status, "已使用");
                                            if (item.getUseTime() == null) return;
                                            String useTime = sdf.format(new Date(Long.valueOf(item.getUseTime())));
                                            helper.setText(R.id.tv_item_receive_time, "" + useTime);
                                            break;
                                        case CouponSubInfo.RECEIVER_INVALID:
                                            helper.setText(R.id.tv_item_coupon_status, "已失效");
                                            break;
                                    }
                                }
                            });
                        }
                    }
                });
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
