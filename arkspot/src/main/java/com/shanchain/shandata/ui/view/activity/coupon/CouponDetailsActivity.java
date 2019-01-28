package com.shanchain.shandata.ui.view.activity.coupon;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.WriterException;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.EncodingHandler;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CouponListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.view.CircleImageView;
import okhttp3.Call;

public class CouponDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_coupon_detail)
    ArthurToolBar tbCouponDetail;
    @Bind(R.id.tv_coupon_name)
    TextView tvCouponName;
    @Bind(R.id.linear_show_QRcode)
    LinearLayout linearShowQRcode;
    @Bind(R.id.tv_coupon_currency)
    TextView tvCouponCurrency;
    @Bind(R.id.tv_coupon_currency_num)
    TextView tvCouponCurrencyNum;
    @Bind(R.id.tv_coupon_seller_name)
    TextView tvCouponSellerName;
    @Bind(R.id.tv_coupon_code)
    TextView tvCouponCode;
    @Bind(R.id.tv_use_rule)
    TextView tvUseRule;
    @Bind(R.id.tv_coupon_details_expiration)
    TextView tvCouponDetailsExpiration;
    @Bind(R.id.linear_show_details)
    LinearLayout linearShowDetails;
    @Bind(R.id.linear_coupon_detail)
    LinearLayout linearCouponDetail;
    @Bind(R.id.tv_use_num)
    TextView tvUseNum;
    @Bind(R.id.btn_coupon_details)
    Button btnCouponDetails;
    @Bind(R.id.iv_show_QRcode)
    ImageView ivShowQRcode;
    @Bind(R.id.iv_coupon_details_avatar)
    CircleImageView ivCouponDetailsAvatar;
    @Bind(R.id.tv_use_rule_details)
    TextView tvUseRuleDetails;
    @Bind(R.id.tv_coupon_token_name)
    TextView tvCouponTokenName;

    private List<CouponSubInfo> couponList = new ArrayList();
    private ProgressDialog mDialog;
    private CouponListAdapter adapter;
    private String characterId = SCCacheUtils.getCacheCharacterId();
    private String userId = SCCacheUtils.getCacheUserId();
    private String couponsId, subCoupId;
    private String roomId;
    private boolean checkCoupon = false;
    private int page = 0;
    private int size = 10;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void initViewsAndEvents() {
        subCoupId = getIntent().getStringExtra("subCoupId");
        couponsId = getIntent().getStringExtra("couponsId");
        checkCoupon = getIntent().getBooleanExtra("checkCoupon", false);
        tbCouponDetail.setTitleText(getResources().getString(R.string.nav_coupon_details));
        tbCouponDetail.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbCouponDetail.setOnLeftClickListener(this);
        customDialog = new CustomDialog(CouponDetailsActivity.this, R.layout.common_dialog_costom,
                new int[]{R.id.tv_input_dialog_title, R.id.even_message_content, R.id.btn_dialog_task_detail_sure});
        initData();
        if (checkCoupon == true) {
            CheckCoupon(subCoupId);
        }

//
    }

    private void initData() {
        //获取子卡劵信息
        if (!TextUtils.isEmpty(subCoupId)) {
            SCHttpUtils.get()
                    .url(HttpApi.SUB_COUPONS_INFO)
                    .addParams("subCoupId", "" + subCoupId)
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
                                if (TextUtils.isEmpty(data)) {
                                    return;
                                }
                                final CouponSubInfo couponSubInfo = JSONObject.parseObject(data, CouponSubInfo.class);
                                if (couponSubInfo.getVendorUser() != 0) {
                                    getCouponUseName("" + couponSubInfo.getVendorUser());
                                }
                                RequestOptions options = new RequestOptions();
                                options.placeholder(R.mipmap.aurora_headicon_default);
                                Glide.with(CouponDetailsActivity.this).load(couponSubInfo.getPhotoUrl()).apply(options).into(ivCouponDetailsAvatar);
                                tvCouponCurrencyNum.setText(couponSubInfo.getPrice());
                                tvCouponSellerName.setText(couponSubInfo.getTokenName());
                                tvCouponCode.setText(couponSubInfo.getTokenSymbol());
                                tvUseRuleDetails.setText(couponSubInfo.getDetail());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String expiration = sdf.format(new Date(Long.valueOf(couponSubInfo.getDeadline())));
                                tvCouponDetailsExpiration.setText("有效期至：" + expiration);
                                switch (couponSubInfo.getTokenStatus()) {
                                    case CouponSubInfo.CREATE_INVALID: //创建方已失效
                                        btnCouponDetails.setText("已失效");
                                        btnCouponDetails.setTextColor(getResources().getColor(R.color.white));
                                        btnCouponDetails.setBackground(getResources().getDrawable(R.drawable.shape_coupon_btn_bg_gray));
                                        break;
                                    case CouponSubInfo.RECEIVER_USE: // 领取方已使用
                                        btnCouponDetails.setText("已使用");
                                        if (!TextUtils.isEmpty(couponSubInfo.getUseTime())) {
                                            String useTime = sdf.format(new Date(Long.valueOf(couponSubInfo.getUseTime())));
                                            btnCouponDetails.setText("已使用" + useTime);
                                        }
                                        btnCouponDetails.setTextColor(getResources().getColor(R.color.white));
                                        btnCouponDetails.setBackground(getResources().getDrawable(R.drawable.shape_coupon_btn_bg_gray));
                                        break;
                                    case CouponSubInfo.RECEIVER_INVALID: //领取方已失效
                                        btnCouponDetails.setText("已失效");
                                        btnCouponDetails.setTextColor(getResources().getColor(R.color.white));
                                        btnCouponDetails.setBackground(getResources().getDrawable(R.drawable.shape_coupon_btn_bg_gray));
                                        break;
                                    case CouponSubInfo.RECEIVER: //已领取
                                        if (checkCoupon == true) {
                                            CheckCoupon(couponSubInfo.getSubCoupId() + "");
                                        } else {
                                            btnCouponDetails.setText("立即使用");
                                        }

//                                    btnCouponDetails.setTextColor(getResources().getColor(R.color.white));
//                                    btnCouponDetails.setBackground(getResources().getDrawable(R.drawable.shape_coupon_btn_bg_gray));
                                        break;
                                }
                                //立即使用
                                if (couponSubInfo.getTokenStatus() == CouponSubInfo.RECEIVER &&
                                        couponSubInfo.getVendorUser() != Integer.valueOf(SCCacheUtils.getCacheUserId()) && checkCoupon == false) {
                                    btnCouponDetails.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            /*
                                             * 显示二维码
                                             * */
                                            // 生成二位码
                                            try {
                                                Bitmap bitmap = EncodingHandler.create2Code("" + couponSubInfo.getSubCoupId(), 800);
                                                ivShowQRcode.setImageBitmap(bitmap);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            linearShowDetails.setVisibility(View.GONE);
                                            linearShowQRcode.setVisibility(View.VISIBLE);
                                            tvCouponName.setVisibility(View.INVISIBLE);
                                            tvCouponTokenName.setVisibility(View.VISIBLE);
                                            tvCouponTokenName.setText(couponSubInfo.getTokenName() + "");
                                            Animation animation = AnimationUtils.loadAnimation(CouponDetailsActivity.this, R.anim.coupon_details_in);
                                            linearShowQRcode.startAnimation(animation);

                                        }
                                    });
                                }
                            }
                        }
                    });
        }

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
                                final CouponSubInfo couponSubInfo = JSONObject.parseObject(data, CouponSubInfo.class);
                                tvCouponSellerName.setText(couponSubInfo.getName() + "");
                                tvCouponCode.setText(couponSubInfo.getTokenSymbol() + "");
                                tvCouponCurrencyNum.setText(couponSubInfo.getPrice() + "");
                                tvUseNum.setVisibility(View.VISIBLE);
                                tvUseNum.setText("剩余 " + couponSubInfo.getRemainAmount() + " 张");
                                tvUseRuleDetails.setText(couponSubInfo.getDetail() + "");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String expiration = sdf.format(new Date(Long.valueOf(couponSubInfo.getDeadline())));
                                tvCouponDetailsExpiration.setText("有效期至：" + expiration);
                                if (couponSubInfo.getUserId() != 0) {
                                    getCouponUseName("" + couponSubInfo.getUserId());
                                }
                                switch (couponSubInfo.getTokenStatus()) {
                                    case CouponSubInfo.CREATE_INVALID:
                                        btnCouponDetails.setText("已失效" + expiration);
                                        break;
                                    case CouponSubInfo.CREATE_WAIT:
//                                        String useTime = sdf.format(new Date(Long.valueOf(couponSubInfo.getUseTime())));
                                        btnCouponDetails.setText("立即领取 ");
                                        btnCouponDetails.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                createSubCouponId(couponSubInfo);
                                            }
                                        });
                                        break;
                                }

                            }
                        }
                    });
        }

    }

    private void createSubCouponId(CouponSubInfo couponSubInfo) {
        showLoadingDialog(true);
        String symbol = couponSubInfo.getTokenSymbol().substring(0, 3);
        SCHttpUtils.get()
                .url(HttpApi.COUPON_GET_COUPONS)
                .addParams("userId", userId)
                .addParams("subuserId", characterId)
                .addParams("tokenSymbol", symbol)
                .addParams("quantities", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        String code = JSONObject.parseObject(response).getString("code");
                        final String msg = JSONObject.parseObject(response).getString("msg");

                        if (NetErrCode.SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            couponsId = JSONObject.parseObject(data).getString("couponsId");
                            subCoupId = JSONObject.parseObject(data).getString("subCoupId");
                            initData();
                            EventMessage eventMessage = new EventMessage(0);
                            EventBus.getDefault().post(eventMessage);
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(CouponDetailsActivity.this, "领取成功");
                                }
                            });
                        } else {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(CouponDetailsActivity.this, "" + msg);
                                }
                            });
                        }
                    }
                });
    }

    private void CheckCoupon(final String result) {
        btnCouponDetails.setText("核销马甲劵 ");
        btnCouponDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCHttpUtils.getAndToken()
                        .url(HttpApi.COUPON_CLIENT_USE)
                        .addParams("subCoupId", result)
                        .addParams("subuserId", SCCacheUtils.getCacheCharacterId() + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.d(TAG, "网络异常");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                final String msg = JSONObject.parseObject(response).getString("msg");
//                                ThreadUtils.runOnMainThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(CouponDetailsActivity.this, msg, Toast.LENGTH_LONG).show();
//                                    }
//                                });
                                if (NetErrCode.SUC_CODE.equals(code)) {
                                    String data = JSONObject.parseObject(response).getString("data");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String useTime = sdf.format(new Date(Long.valueOf(data)));
                                    btnCouponDetails.setText("已核销 " + useTime);
                                    btnCouponDetails.setBackground(getResources().getDrawable(R.drawable.shape_coupon_btn_bg_gray));
                                    EventMessage eventMessage = new EventMessage(0);
                                    EventBus.getDefault().post(eventMessage);
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CouponDetailsActivity.this, "核销成功", Toast.LENGTH_LONG).show();
                                            btnCouponDetails.setOnClickListener(null);
                                        }
                                    });
                                } else {
                                    customDialog.setDialogTitle("");
                                    customDialog.setMessageContentSize(14);
                                    customDialog.setMessageContent("很抱歉你无法核销他人创建的马甲劵,尝试创建自己的马甲劵吧");
                                    customDialog.show();
//                                    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
//                                        @Override
//                                        public void onTick(long millisUntilFinished) {
//
//                                        }
//
//                                        @Override
//                                        public void onFinish() {
//                                            customDialog.dismiss();
//                                        }
//                                    };
//                                    countDownTimer.start();
                                }
                            }
                        });
            }
        });
    }

    private void getCouponUseName(String userId) {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .addParams("userId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            if (data == null) {
                                return;
                            }
                            String character = JSONObject.parseObject(data).getString("characterInfo");
                            final CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                            final String headImg = characterInfo.getHeadImg();
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvCouponName.setText(characterInfo.getName());
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.aurora_headicon_default);
                                    Glide.with(CouponDetailsActivity.this).load(headImg).apply(options).into(ivCouponDetailsAvatar);
                                }
                            });
                        }
                    }
                });
    }


    @Override
    public void onLeftClick(View v) {
        if (linearShowQRcode.isShown()) {
            linearShowDetails.setVisibility(View.VISIBLE);
            linearShowQRcode.setVisibility(View.GONE);
            tvCouponName.setVisibility(View.VISIBLE);
            tvCouponTokenName.setVisibility(View.GONE);
            initData();
//            Animation animation = AnimationUtils.loadAnimation(CouponDetailsActivity.this, R.anim.coupon_details_exit);
//            linearShowQRcode.startAnimation(animation);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (linearShowQRcode.isShown()) {
                linearShowDetails.setVisibility(View.VISIBLE);
                linearShowQRcode.setVisibility(View.GONE);
                tvCouponName.setVisibility(View.VISIBLE);
                tvCouponTokenName.setVisibility(View.GONE);
                initData();
//                Animation animation = AnimationUtils.loadAnimation(CouponDetailsActivity.this, R.anim.coupon_details_exit);
//                linearShowQRcode.startAnimation(animation);
            } else {
                finish();
                return super.onKeyDown(keyCode, event);
            }
        }
        return false;
    }
}
