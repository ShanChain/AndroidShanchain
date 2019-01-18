package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
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

    private String name, code;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_verified;
    }

    @Override
    protected void initViewsAndEvents() {
        tbMain.setTitleText(getResources().getString(R.string.nav_real_identity));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(this);
        name = editCouponName.getText().toString();
        code = editCouponCode.getText().toString();
        btnVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(code)) {
                    ToastUtils.showToast(VerifiedActivity.this, "请输入完整信息");
                    return;
                }
                SCHttpUtils.get()
                        .url(HttpApi.VERIFIED)
                        .addParams("cardno", "" + code)
                        .addParams("name", "" + name)
                        .addParams("token",SCCacheUtils.getCacheToken())
                        .addParams("userId",SCCacheUtils.getCacheUserId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                LogUtils.d(TAG,"网络异常");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                StandardDialog standardDialog = new StandardDialog(VerifiedActivity.this);
                                standardDialog.setSureText("确定");
                                standardDialog.setCancelText("取消");
                                if (NetErrCode.COMMON_SUC_CODE.equals(code)){
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String desc = JSONObject.parseObject(data).getString("desc");
                                    standardDialog.setStandardTitle("实名认证成功");
                                    standardDialog.setStandardMsg("实名认证成功！");
                                    standardDialog.show();

                                }else {
                                    standardDialog.setStandardTitle("实名认证失败");
                                    standardDialog.setStandardMsg("实名认证失败请检查姓名、身份证号");
                                    standardDialog.show();
                                }
                            }
                        });
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
