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

import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.ui.SetWalletPasswordActivity;
import com.shanchain.data.common.ui.widgets.CustomDialog;
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


    public SCHttpStringCallBack() {

    }

    public SCHttpStringCallBack(Context mContext, CustomDialog dialog) {
        this.mContext = mContext;
        this.mCustomDialog = dialog;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        final String code = SCJsonUtils.parseCode(result);
        final String msg = SCJsonUtils.parseMsg(result);
//        if (mContext != null) {
//            String data = SCJsonUtils.parseData(result);
//            if (!TextUtils.isEmpty(data)) {
//                String characterInfo = SCJsonUtils.parseString(data, "characterInfo");
//                if (!TextUtils.isEmpty(characterInfo)) {
//                    boolean isBindPwd = SCJsonUtils.parseBoolean(characterInfo, "isBindPwd");
//                    boolean allowNotify = SCJsonUtils.parseBoolean(characterInfo, "allowNotify");
//                    if (isBindPwd == false) {
//                        code = NetErrCode.WALLET_PASSWORD_INVALID;
//                    }
//                }
//            }
//        }
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
                            ToastUtils.showToast(mContext, code + "" + msg);
                        }
                    });
                }

                break;
            case NetErrCode.COMMON_ERR_CODE:
                if (mContext != null && !TextUtils.isEmpty(msg)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, code + "" + msg);
                        }
                    });

                }
                break;
            case NetErrCode.UN_VERIFIED_CODE:
                Class clazz = null;
                try {
                    clazz = Class.forName("com.shanchain.shandata.ui.view.activity.jmessageui.VerifiedActivity");
                    Intent intent = new Intent(mContext, clazz);
                    mContext.startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
                                ToastUtils.showToast(mContext, "暂未支持该功能");
                            }
                        }
                    });
                }
                break;
            case NetErrCode.WALLET_NOT_CREATE_PASSWORD:
                if (mContext != null) {
                    Intent setWalletPassword = new Intent(mContext, SetWalletPasswordActivity.class);
                    mContext.startActivity(setWalletPassword);
                    Activity activity = (Activity) mContext;
                }

                break;
            case NetErrCode.WALLET_PASSWORD_INVALID:
                if (mContext != null) {
                    //上传密码图片弹窗
//                    final CustomDialog showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(mContext, true, 1.0,
//                            R.layout.dialog_bottom_wallet_password,
//                            new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                    EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, mCustomDialog);
                    EventBus.getDefault().post(busObject);
                }
                break;
            case NetErrCode.WALLET_NOT_CREATE:
                if (mContext != null) {
                    Intent ScWebView = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                    mContext.startActivity(ScWebView);
                    Activity activity = (Activity) mContext;
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
