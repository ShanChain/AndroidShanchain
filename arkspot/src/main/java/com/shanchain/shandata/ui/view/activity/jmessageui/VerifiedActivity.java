package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class VerifiedActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {


    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.edit_coupon_name)
    EditText editCouponName;
    @Bind(R.id.edit_coupon_code)
    EditText editCouponCode;
    @Bind(R.id.verified_sure)
    Button btnVerified;
    @Bind(R.id.tv_real_name_info)
    TextView tvRealNameInfo;
    @Bind(R.id.tv_certificates_type_info)
    TextView tvCertificatesTypeInfo;
    @Bind(R.id.tv_tv_identity_code_info)
    TextView tvTvIdentityCodeInfo;
    @Bind(R.id.relative_verified_info)
    RelativeLayout relativeVerifiedInfo;
    @Bind(R.id.relative_hint)
    RelativeLayout relativeHint;

    private String name, code;
    private boolean idcard;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_verified;
    }

    @Override
    public void onResume() {
        super.onResume();
//        ActivityStackManager.getInstance().finishActivity(CouponListActivity.class);
    }

    @Override
    protected void initViewsAndEvents() {
        idcard = getIntent().getBooleanExtra("idcard", false);
        tbMain.setTitleText(getResources().getString(R.string.nav_real_identity));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(this);
        isRealName();
        if (MyApplication.isRealName() || idcard) {
            initData();
            relativeVerifiedInfo.setVisibility(View.GONE);
            relativeHint.setVisibility(View.VISIBLE);
        } else {
            relativeVerifiedInfo.setVisibility(View.VISIBLE);
            relativeHint.setVisibility(View.GONE);
        }
        btnVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editCouponName.getText().toString();
                code = editCouponCode.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(code)) {
                    ToastUtils.showToast(VerifiedActivity.this, "请输入完整信息");
                    return;
                }
                SCHttpUtils.get()
                        .url(HttpApi.VERIFIED)
                        .addParams("cardno", "" + code)
                        .addParams("name", "" + name)
                        .addParams("token", SCCacheUtils.getCacheToken())
                        .addParams("userId", SCCacheUtils.getCacheUserId())
                        .build()
                        .execute(new SCHttpStringCallBack(VerifiedActivity.this) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.d(TAG, "网络异常");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                String msg = SCJsonUtils.parseMsg(response);
                                final StandardDialog standardDialog = new StandardDialog(VerifiedActivity.this);
                                standardDialog.setSureText("确定");
                                standardDialog.setCancelText("取消");
                                if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String desc = JSONObject.parseObject(data).getString("desc");
                                    standardDialog.setStandardTitle("实名认证成功");
                                    standardDialog.setStandardMsg("实名认证成功！");
                                    standardDialog.setCallback(new Callback() {
                                        @Override
                                        public void invoke() {
                                            finish();
                                        }
                                    }, new Callback() {
                                        @Override
                                        public void invoke() {
                                            finish();
                                        }
                                    });
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            standardDialog.show();
                                        }
                                    });

                                } else {
                                    standardDialog.setStandardTitle("实名认证失败");
                                    if (!TextUtils.isEmpty(msg)) {
                                        standardDialog.setStandardMsg("" + msg);
                                    } else {
                                        standardDialog.setStandardMsg("实名认证失败请检查姓名、身份证号");
                                    }

                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            standardDialog.show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });


    }

    private void initData() {
        CustomDialog showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(VerifiedActivity.this, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        SCHttpUtils.get()
                .url(HttpApi.VERIFIED_DETAILS)
                .build()
                .execute(new SCHttpStringCallBack(mContext, showPasswordDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            final String cardType = SCJsonUtils.parseString(data, "cardType");
                            final String idCard = SCJsonUtils.parseString(data, "idCard");
                            final String realName = SCJsonUtils.parseString(data, "realName");
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvRealNameInfo.setText(realName + "");
                                    tvCertificatesTypeInfo.setText(cardType + "");
                                    tvTvIdentityCodeInfo.setText(idCard + "");
                                }
                            });

                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
