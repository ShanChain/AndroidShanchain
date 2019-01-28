package com.shanchain.data.common.net;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.shanchain.common.R;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by zhoujian on 2017/11/22.
 */

public abstract class SCHttpStringCallBack extends Callback<String> {
    private Context mContext;
    private CustomDialog dialog;

    public SCHttpStringCallBack() {

    }

    public SCHttpStringCallBack(Context mContext, CustomDialog dialog) {
        this.mContext = mContext;
        this.dialog = dialog;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        String code = SCJsonUtils.parseCode(result);
        String msg = SCJsonUtils.parseMsg(result);
        switch (code) {
            case NetErrCode.COMMON_TOKEN_OVERDUE_CODE:
                AppManager.getInstance().logout();
                break;
            case NetErrCode.COMMON_ERR_CODE:
                if (mContext != null && TextUtils.isEmpty(msg)) {
                    ToastUtils.showToast(mContext, msg + "");
                }
                break;
            case NetErrCode.UN_VERIFIED_CODE:
                if (mContext != null && TextUtils.isEmpty(msg)) {
                    ToastUtils.showToast(mContext, msg + "");
                }
                break;
            case NetErrCode.ACCOUNT_HAS_BINDED:
                if (mContext != null && TextUtils.isEmpty(msg)) {
                    ToastUtils.showToast(mContext, msg + "");
                }
                break;
            case NetErrCode.COMMON_UNOPENED_CODE:
                if (mContext != null) {
                    if (TextUtils.isEmpty(msg)) {
                        ToastUtils.showToast(mContext, msg + "");
                    } else {
                        ToastUtils.showToast(mContext, "暂未支持该功能");
                    }
                }
                break;
            default:
                closeLoadingDialog();
                break;
        }
        return result;
    }

    protected void showLoadingDialog(boolean cancelable) {
        if (dialog != null) {
            dialog.show();
            dialog.setCancelable(cancelable);
        }
    }

    protected void closeLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
