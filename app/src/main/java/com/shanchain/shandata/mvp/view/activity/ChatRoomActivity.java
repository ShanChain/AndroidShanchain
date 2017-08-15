package com.shanchain.shandata.mvp.view.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowText;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ThreadUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.customEaseUI.CustomInputMenu;

import java.util.List;

import butterknife.Bind;

public class ChatRoomActivity extends BaseActivity {

    @Bind(R.id.etb_chat_room)
    EaseTitleBar mEtbChatRoom;
    @Bind(R.id.message_list)
    EaseChatMessageList mMessageList;
    @Bind(R.id.input_menu)
    EaseChatInputMenu mInputMenu;
    private String mToChatUserName;
    private String mUserName = "test1";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected void initViewsAndEvents() {

        initInputMenu();
        initMessageList();
       // login();
        initListener();

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createGroup();
            }
        }, 3000);*/

        /*

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount("test3", "123456");
                    EMClient.getInstance().createAccount("test4", "123456");
                    LogUtils.e("注册成功");
                } catch (HyphenateException e) {
                    LogUtils.e("注册失败");
                    e.printStackTrace();
                }
            }
        });
        */
    }

    private void createGroup() {


        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                EMGroupOptions options = new EMGroupOptions();
                options.maxUsers = 100;
                options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                String groupName = "善圆群";
                String desc = "新建的第一个群";
                // String[] allNums = new String[]{"test4"};
                String reason = "邀请您的加入";
                try {
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, new String[]{}, reason, options);
                    LogUtils.d("创建群成功！" + group);
                } catch (HyphenateException e) {
                    LogUtils.d("创建群失败！");
                    e.printStackTrace();
                }
            }
        });


    }

    private void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    private void login() {
        mUserName = "test3";
        EMClient.getInstance().login(mUserName, "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ToastUtils.showToast(ChatRoomActivity.this, "登录成功！");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showToast(ChatRoomActivity.this, "登录失败！");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void initMessageList() {
        mToChatUserName = "24437254586369";
        int chatType = EaseConstant.CHATTYPE_GROUP;
        mMessageList.init(mToChatUserName, chatType, new CustomChatRowProvider());
        mMessageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            @Override
            public void onResendClick(EMMessage message) {
                //重发消息点击事件
                LogUtils.d("重发");
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                //气泡框点击事件，EaseUI有默认实现这个事件，如果需要覆盖，return值要返回true
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                //气泡框长按事件

            }

            @Override
            public void onUserAvatarClick(String username) {
                //头像点击事件
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                //头像长按事件
                LogUtils.d("点击头像");
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = mMessageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {




                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });
    }

    private void initInputMenu() {
        mInputMenu.setCustomPrimaryMenu(new CustomInputMenu(this));
        mInputMenu.init();
        mInputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                LogUtils.d(content);
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(content, mToChatUserName);
                //设置为群消息
                message.setChatType(EMMessage.ChatType.GroupChat);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                mMessageList.refresh();
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                LogUtils.d(emojicon.getName());
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        private static final int MESSAGE_TYPE_SENDSHAREPIC = 1;//发送
        private static final int MESSAGE_TYPE_RECEIVEDSHAREPIC = 2;//接收

        @Override
        public int getCustomChatRowTypeCount() {
            //音、视频通话发送、接收共4种
            return 2;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //这里做个判断    如果能取到扩展字段    就返回消息类型
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECEIVEDSHAREPIC : MESSAGE_TYPE_SENDSHAREPIC;
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            //同上  如果能取到扩展字段    就返回消息类型
            if (message.getType() == EMMessage.Type.TXT) {
                return new EaseChatRowText(mContext, message, position, adapter);
            }
            return null;
        }
    }

    private EMMessageListener mMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //接受到消息
         for (EMMessage msg : list) {
                String msgBody = msg.getBody().toString();
                LogUtils.d("body :  " + msgBody);
            }
            /*   mMessageList.refresh();*/
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            //消息已送达
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            //下次状态改变
        }
    };
}


