package com.shanchain.shandata.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SystemUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.push.ExampleUtil;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CouponDetailsActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MyMessageActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private Conversation mConversation;
    private Message mSendCustomMessage;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtils.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                Intent customIntent = new Intent(context, MyMessageActivity.class);
//                customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(customIntent);
//                Intent broadcastIntent = new Intent(context, MyReceiver.class);
                String jsonString = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                createNotification(context, jsonString, bundle);
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                    JSONObject messageJson = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    if (!TextUtils.isEmpty(messageJson.toString()) && !messageJson.toString().equals("{}")) {
                        String jguserName = SCJsonUtils.parseString(messageJson.toString(), "jguserName");
                        String extra = SCJsonUtils.parseString(messageJson.toString(), "extra");
                        String sysPage = SCJsonUtils.parseString(messageJson.toString(), "sysPage");
                        String title = SCJsonUtils.parseString(messageJson.toString(), "title");
                        String msgContent = SCJsonUtils.parseString(messageJson.toString(), "msgContent");
//                    String android = messageJson.getString("android");
//                    String extras = SCJsonUtils.parseString(android, "extras");
//                    String extras = SCJsonUtils.parseString(messageJson.toString(), "extras");
//                    String jguserName = SCJsonUtils.parseString(extras, "jguserName");
//                    String extra = SCJsonUtils.parseString(extras, "extra");
//                    String sysPage = SCJsonUtils.parseString(extras, "sysPage");
                        if (!TextUtils.isEmpty(jguserName) && !TextUtils.isEmpty(extra)) {
                            mConversation = JMessageClient.getSingleConversation(jguserName);
                            if (mConversation == null) {
                                mConversation = Conversation.createSingleConversation(jguserName);
                            }
                            Map customMap = new HashMap();
                            customMap.put("taskId", 0 + "");
                            customMap.put("bounty", title + "");
                            customMap.put("dataString", "" + extra);
                            customMap.put("time", "" + System.currentTimeMillis());

                            mSendCustomMessage = mConversation.createSendCustomMessage(customMap, jguserName);
                            JMessageClient.sendMessage(mSendCustomMessage);
                        }
                    }
                }
//

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");
                if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                    JSONObject messageJson = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    if (!TextUtils.isEmpty(messageJson.toString()) && !messageJson.toString().equals("{}")) {
                        String jguserName = SCJsonUtils.parseString(messageJson.toString(), "jguserName");
                        String extra = SCJsonUtils.parseString(messageJson.toString(), "extra");
                        String sysPage = SCJsonUtils.parseString(messageJson.toString(), "sysPage");
                        String title = SCJsonUtils.parseString(messageJson.toString(), "title");
                        String msgContent = SCJsonUtils.parseString(messageJson.toString(), "msgContent");
                        if (!TextUtils.isEmpty(sysPage) && sysPage.equals("messageList")) {
                            Intent i = new Intent(context, MyMessageActivity.class);
                            i.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        } else if (!TextUtils.isEmpty(sysPage) && sysPage.equals("publishTaskList")) {
                            Intent i = new Intent(context, TaskListActivity.class);
                            i.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);

                    } else if (!TextUtils.isEmpty(sysPage) && sysPage.equals("receiveTaskList")) {
                            Intent i = new Intent(context, TaskDetailActivity.class);
                            i.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);

                        } else if (!TextUtils.isEmpty(sysPage) && sysPage.equals("couponsVendorList")) {
                            Intent i = new Intent(context, CouponListActivity.class);
                            i.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);

                        } else if (!TextUtils.isEmpty(sysPage) && sysPage.equals("couponsClientGet")) {
                            Intent i = new Intent(context, CouponDetailsActivity.class);
                            i.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        }
                    }
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    LogUtils.d(TAG, "" + json.toString());
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    private void createNotification(Context context, String jsonString, Bundle bundle) throws JSONException {

        String messageContent = null;
        String title = null;
        String sysPage = null;
        String extra = null;
        String alert = null;
        if (jsonString != null) {
//            String android = SCJsonUtils.parseString(jsonString, "android");
//            title = SCJsonUtils.parseString(android, "title");
//            alert = SCJsonUtils.parseString(android, "alert");
//            String extras = SCJsonUtils.parseString(jsonString, "extras");
            String jguserName = SCJsonUtils.parseString(jsonString, "jguserName");
            extra = SCJsonUtils.parseString(jsonString, "extra");
            sysPage = SCJsonUtils.parseString(jsonString, "sysPage");
            title = SCJsonUtils.parseString(jsonString, "title");
            alert = SCJsonUtils.parseString(jsonString, "msgContent");

            mConversation = JMessageClient.getSingleConversation(jguserName);
            if (mConversation == null) {
                mConversation = Conversation.createSingleConversation(jguserName);
            }
            Map customMap = new HashMap();
            customMap.put("taskId", 0 + "");
            customMap.put("bounty", title + "");
            customMap.put("dataString", "" + extra);
            customMap.put("time", "" + System.currentTimeMillis());

            mSendCustomMessage = mConversation.createSendCustomMessage(customMap, jguserName);
            JMessageClient.sendMessage(mSendCustomMessage);
//            Message msg;
//            TextContent content = new TextContent(extra);
//            msg = mConversation.createSendMessage(content,jguserName);
//            JMessageClient.sendMessage(msg);
        }

        Intent broadcastIntent = new Intent(context, CustomMessageReceiver.class);
//        Intent broadcastIntent = new Intent(context, MyMessageActivity.class);
        broadcastIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, "subscribe")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("" + title)
                .setContentText("" + alert)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_logo))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    //send msg to MyMessageActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MyMessageActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(FootPrintActivity.MESSAGE_RECEIVED_ACTION);
            Intent msgIntent = new Intent(MyMessageActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(FootPrintActivity.KEY_MESSAGE, message);
            msgIntent.putExtra(MyMessageActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MyMessageActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
//            if (message != null) {
//                String android = SCJsonUtils.parseString(message, "android");
//                String messageContent = SCJsonUtils.parseString(android, "extras");
//                String alert = SCJsonUtils.parseString(android, "alert");
//                String title = SCJsonUtils.parseString(android, "title");
//            }

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }
}
