package com.shanchain.data.common.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by WealChen
 * Date : 2019/2/21
 * Describe :
 */
public abstract class SCHttpPostBodyCallBack implements Callback {
    Context mContext;
    CustomDialog mCustomDialog;
    StandardDialog mStandardDialog;

    public SCHttpPostBodyCallBack() {

    }

    public SCHttpPostBodyCallBack(Context context, CustomDialog customDialog) {
        this.mContext = context;
        this.mCustomDialog = customDialog;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(mContext, "网络异常");
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        final String code = SCJsonUtils.parseCode(result);
//        final String code = NetErrCode.WALLET_NOT_CREATE_PASSWORD;
        final String msg = SCJsonUtils.parseMsg(result);
        if (mContext != null) {

        }
        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
            responseDoParse(result);
        } else if (NetErrCode.WALLET_NOT_CREATE.equals(code)) {
            if (mContext != null) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mStandardDialog = new StandardDialog(mContext);
                        mStandardDialog.setStandardTitle("  ");
                        mStandardDialog.setStandardMsg("您尚未开通马甲钱包，开通后方可使用该功能");
                        mStandardDialog.setCancelText("返回");
                        mStandardDialog.setSureText("去开通");
                        mStandardDialog.setCancelable(true);
                        mStandardDialog.setCanceledOnTouchOutside(true);
                        mStandardDialog.setOnItemClickListener(null);
                        mStandardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
                            @Override
                            public void invoke() {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                                mContext.startActivity(intent);
                                Activity activity = (Activity) mContext;
                                mStandardDialog.dismiss();
                            }
                        }, new com.shanchain.data.common.base.Callback() {
                            @Override
                            public void invoke() {
                                mStandardDialog.dismiss();
                            }
                        });

                        mStandardDialog.show();
                    }
                });
            }
        } else if (NetErrCode.WALLET_PASSWORD_INVALID.equals(code)) {
            if (mContext != null) {
                //上传密码图片弹窗
                EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, mCustomDialog);
                EventBus.getDefault().post(busObject);
            }
        } else if (NetErrCode.WALLET_NOT_CREATE_PASSWORD.equals(code)) {
            if (mContext != null) {
                Intent ScWebView = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                mContext.startActivity(ScWebView);
                Activity activity = (Activity) mContext;
            }
        } else if (NetErrCode.BALANCE_NOT_ENOUGH.equals(code)) {
            if (mContext != null) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "您的余额不足");
                    }
                });
            }
        } else if (NetErrCode.UN_VERIFIED_CODE.equals(code)) {
            if (mContext != null) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mStandardDialog = new StandardDialog(mContext);
                        mStandardDialog.setStandardTitle("  ");
                        mStandardDialog.setStandardMsg("您尚未开通马甲钱包，开通后方可使用该功能");
                        mStandardDialog.setCancelText("返回");
                        mStandardDialog.setSureText("去开通");
                        mStandardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
                            @Override
                            public void invoke() {
                                Intent ScWebView = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                                mContext.startActivity(ScWebView);
                                Activity activity = (Activity) mContext;
                            }
                        }, new com.shanchain.data.common.base.Callback() {
                            @Override
                            public void invoke() {
                                mStandardDialog.dismiss();
                            }
                        });
                        mStandardDialog.show();
                    }
                });
            } else {
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }
            }
        }

    }

    public abstract void responseDoParse(String string) throws IOException;

}
