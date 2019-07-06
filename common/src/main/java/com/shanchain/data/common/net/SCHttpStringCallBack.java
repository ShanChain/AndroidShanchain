package com.shanchain.data.common.net;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.shanchain.common.R;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Response;

/**
 * Created by zhoujian on 2017/11/22.
 */

public abstract class SCHttpStringCallBack extends Callback<String> {
    private Context mContext;
    public CustomDialog mCustomDialog;
    public StandardDialog mStandardDialog;


    public SCHttpStringCallBack() {

    }

    public SCHttpStringCallBack(Context mContext) {
        this.mContext = mContext;
    }

    public SCHttpStringCallBack(Context mContext, CustomDialog dialog) {
        this.mContext = mContext;
        this.mCustomDialog = dialog;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        LogUtils.d("Resonse: ", result);
        final String code = SCJsonUtils.parseCode(result);
//        final String code = NetErrCode.WALLET_NOT_CREATE_PASSWORD;
        final String msg = SCJsonUtils.parseMsg(result);
        if (mContext != null) {

        }
        switch (code) {
            case NetErrCode.COMMON_SUC_CODE:
                if (mContext != null) {
//                    ThreadUtils.runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                           ToastUtils.showToastLong(mContext, "全局处理,成功");
//                        }
//                    });
                }
                break;
            case NetErrCode.COMMON_TOKEN_OVERDUE_CODE:
                if (mContext != null) {
//                    Class clazz = Class.forName("com.shanchain.shandata.ui.view.activity.login.LoginActivity");
//                    Intent intent = new Intent(mContext, clazz);
//                    mContext.startActivity(intent);
//                    ActivityStackManager.getInstance().finishAllActivity();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }

                break;
            case NetErrCode.COMMON_ERR_CODE:
                if (mContext != null && !TextUtils.isEmpty(msg)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });

                }
                break;
            case NetErrCode.UN_VERIFIED_CODE:
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mStandardDialog = new StandardDialog(mContext);
                            mStandardDialog.setStandardTitle("  ");
                            mStandardDialog.setStandardMsg(mContext.getString(R.string.not_vertifu)+"\n" +
                                    mContext.getString(R.string.uses_fun));
                            mStandardDialog.setCancelText(mContext.getString(R.string.str_cancel));
                            mStandardDialog.setSureText(mContext.getString(R.string.go_kt));
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
                }
                break;
            case NetErrCode.BIND_PONE_ERR_CODE:
                if (mContext != null && !TextUtils.isEmpty(msg)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });

                }
                break;
            case NetErrCode.ACCOUNT_HAS_BINDED:
                if (mContext != null && !TextUtils.isEmpty(msg)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + "" + msg);
                        }
                    });

                }
                break;
            case NetErrCode.COMMON_UNOPENED_CODE:
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(msg)) {
                                ToastUtils.showToast(mContext, code + "" + msg);
                            } else {
                                ToastUtils.showToast(mContext, R.string.not_support);
                            }
                        }
                    });
                }
                break;
            case NetErrCode.WALLET_NOT_CREATE_PASSWORD:
                if (mContext != null) {
                    Intent ScWebView = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                    mContext.startActivity(ScWebView);
                    Activity activity = (Activity) mContext;
                }
                break;
            case NetErrCode.WALLET_PASSWORD_INVALID:
                if (mContext != null) {
                    //上传密码图片弹窗
                    EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, mCustomDialog);
                    EventBus.getDefault().postSticky(busObject);
                }
                break;
            case NetErrCode.WALLET_NOT_CREATE:
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mStandardDialog = new StandardDialog(mContext);
                            mStandardDialog.setStandardTitle("  ");
                            mStandardDialog.setStandardMsg(mContext.getString(R.string.open_marjar_wallet));
                            mStandardDialog.setCancelText(mContext.getString(R.string.str_cancel));
                            mStandardDialog.setSureText(mContext.getString(R.string.go_kt));
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
                }
                break;
            case NetErrCode.COUPON_INVALID_QRCODE:
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            final CustomDialog invalidCode = new CustomDialog(mContext, R.layout.common_dialog_costom, new int[]{R.id.even_message_content});
                            invalidCode.setDialogTitle("");
                            invalidCode.setMessageContentSize(14);
                            invalidCode.setMessageContent(mContext.getString(R.string.not_user_majar));
                            invalidCode.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                @Override
                                public void OnItemClick(CustomDialog dialog, View view) {
                                    if (view.getId() == R.id.even_message_content) {
                                        invalidCode.dismiss();
                                    }
                                }
                            });
                            invalidCode.show();
                        }
                    });
                }
                break;
            case NetErrCode.TRANSACTION_FAILURE:
                if (mContext != null) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + "：" + msg);
                        }
                    });
                }
                break;
            default:
//                closeLoadingDialog();
                break;
        }
        return result;
    }

    //打开相册
    private void selectImage() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Activity activity = (Activity) mContext;
            activity.startActivityForResult(intent, NetErrCode.WALLET_PHOTO);
        }
    }


//    protected void closeLoadingDialog() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//    }

}
