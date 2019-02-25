package com.shanchain.data.common.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.ui.SetWalletPasswordActivity;
import com.shanchain.data.common.ui.widgets.CustomDialog;
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
        String code = SCJsonUtils.parseCode(result);
        String msg = SCJsonUtils.parseMsg(result);
        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
            responseDoParse(result);
        } else if (NetErrCode.WALLET_NOT_CREATE.equals(code)) {
            if (mContext != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("activity://qianqianshijie:80/webview"));
                mContext.startActivity(intent);
                Activity activity = (Activity) mContext;
            }
        } else if (NetErrCode.WALLET_PASSWORD_INVALID.equals(code)) {
            if (mContext != null) {
                //上传密码图片弹窗
//                final CustomDialog showPasswordDialog = new CustomDialog(ActivityStackManager.getInstance().getTopActivity(), true, 1.0,
//                        R.layout.dialog_bottom_wallet_password,
//                        new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
//                showPasswordDialog.setCancelable(true);
                EventBusObject busObject = new EventBusObject(NetErrCode.WALLET_PHOTO, mCustomDialog);
                EventBus.getDefault().post(busObject);
            }
        } else if (NetErrCode.WALLET_NOT_CREATE_PASSWORD.equals(code)) {
            if (mContext != null) {
                Intent intent = new Intent(mContext, SetWalletPasswordActivity.class);
                mContext.startActivity(intent);
                Activity activity = (Activity) mContext;
            }
        } else if (NetErrCode.BALANCE_NOT_ENOUGH.equals(code)) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(mContext, "您的余额不足");
                }
            });
        } else if (NetErrCode.UN_VERIFIED_CODE.equals(code)) {
            if (mContext != null) {

                Class clazz = null;
                try {
                    clazz = Class.forName("com.shanchain.shandata.ui.view.activity.jmessageui.VerifiedActivity");
                    Intent intent = new Intent(mContext, clazz);
                    mContext.startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public abstract void responseDoParse(String string) throws IOException;

}
