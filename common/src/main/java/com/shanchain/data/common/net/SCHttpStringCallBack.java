package com.shanchain.data.common.net;

import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by zhoujian on 2017/11/22.
 */

public abstract class SCHttpStringCallBack extends Callback<String>{

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String result = response.body().string();
        String code = SCJsonUtils.parseCode(result);
        switch (code) {
            case NetErrCode.COMMON_TOKEN_OVERDUE_CODE:
                AppManager.getInstance().logout();
                break;
            case NetErrCode.COMMON_ERR_CODE:

                break;
        }
        return result;
    }

}
