package com.shanchain.data.common.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by flyye on 2017/10/16.
 */

public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
           //TODO 处理网络状态改变
        }
    }
}
