package com.shanchain.shandata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.shanchain.shandata.push.ExampleUtil;
import com.shanchain.shandata.ui.view.activity.HomeActivity;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (HomeActivity.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(HomeActivity.KEY_MESSAGE);
                String extras = intent.getStringExtra(HomeActivity.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(HomeActivity.KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(HomeActivity.KEY_EXTRAS + " : " + extras + "\n");
                }
                //设置自定义消息
//                setCostomMsg(showMsg.toString());
            }
        } catch (Exception e){
        }
    }
}
