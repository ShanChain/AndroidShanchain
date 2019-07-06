package com.shanchain.shandata.ui.view.activity.coupon;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MyCreateDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {


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
    @Bind(R.id.srl_coupon_list)
    BGARefreshLayout srlCouponList;
    @Bind(R.id.iv_invalid)
    ImageView ivInvalid;
    @Bind(R.id.frame_coupon_check)
    FrameLayout frameCouponCheck;

    private int pageNo = 1, pageSize = 10;


    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 10;
    private List<CouponSubInfo> couponList = new ArrayList();
    private List<CouponSubInfo> clientList = new ArrayList();
    private BaseQuickAdapter adapter;
    private String couponsId, subCoupId;
    private Integer currentPage;
    private String last;
    private String symbol;
    private List<CouponSubInfo> loadMore;

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
        tbCouponDetail.setTitleText(getString(R.string.nav_coupon_details));
        tbCouponDetail.setOnLeftClickListener(this);
//        tbCouponDetail.setOnRightClickListener(this);
        srlCouponList.setDelegate(this);
        srlCouponList.beginLoadingMore();
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(MyCreateDetailsActivity.this, true);//微博效果
        srlCouponList.setRefreshViewHolder(refreshViewHolder);
        // 设置正在加载更多时不显示加载更多控件
        srlCouponList.setIsShowLoadingMoreView(true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText(getString(R.string.Load_more));
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
                                if (couponSubInfo != null) {
                                    String name = couponSubInfo.getName() != null ? couponSubInfo.getName() : "";
                                    tvItemStoryName.setText(name + "");
                                }
                                if (couponSubInfo.getTokenStatus() == CouponSubInfo.CREATE_INVALID) {
                                    ivInvalid.setVisibility(View.VISIBLE);
                                }
                                tvItemCode.setText(couponSubInfo.getTokenSymbol() + "");
                                evenMessageBounty.setText(couponSubInfo.getPrice() + "");
                                tvCouponNum.setText(getString(R.string.picker_image_folder_info,couponSubInfo.getAmount()));
                                tvRuleDetails.setText(couponSubInfo.getDetail());
                                tvCouponWaitingCheckNum.setText(couponSubInfo.getUnusedNum() + "");
                                tvCouponCheckedNum.setText(couponSubInfo.getUsedNum() + "");
                                tvCouponSurplusNum.setText(couponSubInfo.getRemainAmount() + "");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String createTime = sdf.format(new Date(Long.valueOf(couponSubInfo.getCreateTime())));
                                tvCreateTime.setText(createTime + "");
                                String expiration = sdf.format(new Date(Long.valueOf(couponSubInfo.getDeadline())));
                                tvCouponDetailsExpiration.setText(getString(R.string.express_time_to) + expiration + "");
//                                tvCreateTime.setText(couponSubInfo.getCreateTime() + "");
                                //获取领劵人员的列表
                                getClientList(couponSubInfo);

                            }
                        }
                    });
        }
    }

    private void getClientList(final CouponSubInfo couponSubInfo) {
        symbol = couponSubInfo.getTokenSymbol().substring(0, 3);
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
                            last = JSONObject.parseObject(data).getString("last");
                            String next = JSONObject.parseObject(data).getString("next");
                            String pageNum = JSONObject.parseObject(data).getString("pageNo");
                            currentPage = Integer.valueOf(pageNum);
                            page = Integer.valueOf(next);
                            adapter = new BaseQuickAdapter<CouponSubInfo, BaseViewHolder>(R.layout.item_common_simple, clientList) {
                                @Override
                                protected void convert(BaseViewHolder helper, CouponSubInfo item) {
                                    helper.setText(R.id.tv_item_story_name, item.getUserId() + "");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    switch (item.getTokenStatus()) {
                                        case CouponSubInfo.RECEIVER:
                                            helper.setText(R.id.tv_item_coupon_status, getString(R.string.get_yi));
                                            if (item.getGetTime() == null) return;
                                            String getTime = sdf.format(new Date(Long.valueOf(item.getGetTime())));
                                            helper.setText(R.id.tv_item_receive_time, "" + getTime);
                                            break;
                                        case CouponSubInfo.RECEIVER_UN_USE:
                                            helper.setText(R.id.tv_item_coupon_status, "" + item.getTokenStatus());
                                            break;
                                        case CouponSubInfo.RECEIVER_USE:
                                            helper.setText(R.id.tv_item_coupon_status, R.string.yihexiao);
                                            if (item.getUseTime() == null) return;
                                            String useTime = sdf.format(new Date(Long.valueOf(item.getUseTime())));
                                            helper.setText(R.id.tv_item_receive_time, "" + useTime);
                                            break;
                                        case CouponSubInfo.RECEIVER_INVALID:
                                            helper.setText(R.id.tv_item_coupon_status, getString(R.string.expired));
                                            break;
                                    }
                                }
                            };
                            recyclerViewCouponCheckList.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (page != currentPage && symbol != null) {
            if (last != null && pageNo < Integer.valueOf(last)) {
                pageNo++;
            }
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
                                last = JSONObject.parseObject(data).getString("last");
                                String next = JSONObject.parseObject(data).getString("next");
                                String pageNum = JSONObject.parseObject(data).getString("pageNo");
                                currentPage = Integer.valueOf(pageNum);
                                page = Integer.valueOf(next);
                                if (currentPage <= Integer.valueOf(last)) {
                                    last = JSONObject.parseObject(data).getString("last");
                                    page = Integer.valueOf(next);
                                    loadMore = JSONObject.parseArray(list, CouponSubInfo.class);
                                    adapter.addData(loadMore);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
        if (page == currentPage || pageNo < Integer.valueOf(last)) {
            return true;
        }
        return false;
    }
}
