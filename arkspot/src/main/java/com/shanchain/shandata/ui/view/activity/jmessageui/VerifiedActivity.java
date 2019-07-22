package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
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
    @Bind(R.id.iv_user_head)
    CircleImageView ivＵserＨead;
    @Bind(R.id.iv_user_head_1)
    CircleImageView ivＵserＨead1;
    @Bind(R.id.im_back)
    ImageView imBack;


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

        //设置头像
        UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo != null && userInfo.getAvatarFile() != null) {
            Glide.with(this).load(userInfo.getAvatarFile().getAbsolutePath())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(ivＵserＨead);
            Glide.with(this).load(userInfo.getAvatarFile().getAbsolutePath())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(ivＵserＨead1);
        } else {
            Glide.with(this).load(SCCacheUtils.getCacheHeadImg())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(ivＵserＨead);
            Glide.with(this).load(SCCacheUtils.getCacheHeadImg())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(ivＵserＨead1);
        }

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
                        LogUtils.d(TAG, getString(R.string.network_wrong));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        LogUtils.d("------realName--",response);
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

    //提交认证信息
    @OnClick(R.id.verified_sure)
    void commitVertifyInfo(){
        name = editCouponName.getText().toString();
        code = editCouponCode.getText().toString();
        if (TextUtils.isEmpty(name) ) {
            ToastUtils.showToast(VerifiedActivity.this, R.string.real_name_not_entity);
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(VerifiedActivity.this, R.string.id_not_entity);
            return;
        }
        String sta = MyApplication.systemLanguge;
        String language;
        if("zh".equals(sta)){
            language = "zh";
        }else {
            language = "en";
        }
        SCHttpUtils.get()
                .url(HttpApi.VERIFIED)
                .addParams("cardno", "" + code)
                .addParams("name", "" + name)
                .addParams("language",language)
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
                        standardDialog.setSureText(getString(R.string.btn_send));
                        standardDialog.setCancelText(getString(R.string.cancel));
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                                    /*String data = JSONObject.parseObject(response).getString("data");
                                    String desc = JSONObject.parseObject(data).getString("desc");*/
                            standardDialog.setStandardTitle(getString(R.string.vertify_success));
                            standardDialog.setStandardMsg(getString(R.string.pass_verdify));
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
                            standardDialog.setStandardTitle(getString(R.string.realname_vdf_failed));
                            if (!TextUtils.isEmpty(msg)) {
                                standardDialog.setStandardMsg("" + msg);
                            } else {
                                standardDialog.setStandardMsg(getString(R.string.check_information));
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

    @OnClick(R.id.im_back)
    void finished(){
        finish();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
