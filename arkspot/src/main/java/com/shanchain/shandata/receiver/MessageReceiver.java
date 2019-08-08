package com.shanchain.shandata.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.shanchain.shandata.R;
import com.shanchain.shandata.push.ExampleUtil;
import com.shanchain.shandata.ui.view.activity.HomeActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by WealChen
 * Date : 2019/8/6
 * Describe :消息通知栏
 */
public class MessageReceiver extends BroadcastReceiver {
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MESSAGE_RECEIVED_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                //设置点击通知栏的动作为启动另外一个广播
                Intent broadcastIntent = new Intent(context, MyReceiver.class);
                PendingIntent pendingIntent = PendingIntent.
                        getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(context, "subscribe")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle(context.getString(R.string.receive_message))
                        .setContentText(showMsg.toString())
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.app_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notification);
            }
        } catch (Exception e) {
        }
    }
}
