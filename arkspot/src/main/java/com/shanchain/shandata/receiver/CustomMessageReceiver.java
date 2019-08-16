package com.shanchain.shandata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MyMessageActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by WealChen
 * Date : 2019/3/25
 * Describe :
 */
public class CustomMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String jsonString = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String android = SCJsonUtils.parseString(jsonString, "android");
//            String extras = SCJsonUtils.parseString(jsonString, "extras");
            String extra = SCJsonUtils.parseString(jsonString, "extra");
            String sysPage = SCJsonUtils.parseString(jsonString, "sysPage");
            if (sysPage.equals("messageList")) {
                Intent customIntent = new Intent(context, MyMessageActivity.class);
                customIntent.putExtras(bundle);
                customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(customIntent);
            } else {
                Intent customIntent = new Intent(context, FootPrintNewActivity.class);
                customIntent.putExtras(bundle);
                customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(customIntent);
            }
        }
    }
}
