package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ChatRoomMsgAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.global.Constants;
import com.shanchain.arkspot.ui.model.MsgInfo;
import com.shanchain.arkspot.ui.presenter.ChatPresenter;
import com.shanchain.arkspot.ui.presenter.impl.ChatPresenterImpl;
import com.shanchain.arkspot.ui.view.activity.chat.view.ChatView;
import com.shanchain.arkspot.widgets.switchview.SwitchView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import utils.LogUtils;
import utils.ToastUtils;


public class ChatRoomActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, ChatView {

    @Bind(R.id.tb_chat)
    ArthurToolBar mTbChat;
    @Bind(R.id.tv_chat_idle)
    TextView mTvChatIdle;
    @Bind(R.id.shs_chat)
    SwitchView mShsChat;
    @Bind(R.id.tv_chat_against)
    TextView mTvChatAgainst;
    @Bind(R.id.iv_chat_idle_at)
    ImageView mIvChatIdleAt;
    @Bind(R.id.iv_chat_idle_frame)
    ImageView mIvChatIdleFrame;
    @Bind(R.id.ll_chat_idle)
    LinearLayout mLlChatIdle;
    @Bind(R.id.iv_chat_against_drama)
    ImageView mIvChatAgainstDrama;
    @Bind(R.id.iv_chat_against_scene)
    ImageView mIvChatAgainstScene;
    @Bind(R.id.iv_chat_against_at)
    ImageView mIvChatAgainstAt;
    @Bind(R.id.iv_chat_against_frame)
    ImageView mIvChatAgainstFrame;
    @Bind(R.id.ll_chat_against)
    LinearLayout mLlChatAgainst;
    @Bind(R.id.et_chat_msg)
    EditText mEtChatMsg;
    @Bind(R.id.tv_chat_send)
    TextView mTvChatSend;
    @Bind(R.id.rv_chat_msg)
    RecyclerView mRvChatMsg;
    private String mUserName;
    private EMMessage.ChatType mChatType;
    //private List<MsgInfo> msgList;
    private String toChatName;

    private ChatPresenter mChatPresenter;
    //private ChatMsgAdapter mMsgAdapter;

    private ChatRoomMsgAdapter mChatRoomMsgAdaptere;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViewsAndEvents() {
        init();
        initToolBar();
        login();
        initListener();

    }

    private void initListener() {

        mShsChat.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if (isOn) {
                    mLlChatAgainst.setVisibility(View.GONE);
                    mLlChatIdle.setVisibility(View.VISIBLE);
                    mEtChatMsg.setHint("以本人的身份随便聊聊吧");
                } else {
                    mLlChatAgainst.setVisibility(View.VISIBLE);
                    mLlChatIdle.setVisibility(View.GONE);
                    mEtChatMsg.setHint("以角色的身份说话");
                }
            }
        });

        mEtChatMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    mTvChatSend.setEnabled(false);
                } else {
                    mTvChatSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * 显示最多最近的20条聊天记录，然后定位RecyclerView到最后一行
         */
        mChatPresenter.initChat(toChatName);

    }

    /**
     * 描述：初始化一些参数，比如 聊天类型，聊天对象等
     */
    private void init() {
        //注册观察者
        EventBus.getDefault().register(this);
        mUserName = "test666";
        toChatName = "test111";
        mChatType = EMMessage.ChatType.Chat;
        mChatPresenter = new ChatPresenterImpl(this);

        ArrayList<String> contacts = getIntent().getStringArrayListExtra("contacts");
        for (int i = 0; i < contacts.size(); i++) {
            LogUtils.d(contacts.get(i));
        }
    }

    private void initToolBar() {
        mTbChat.setTitleText(toChatName);
        mTbChat.setOnLeftClickListener(this);
        mTbChat.setOnRightClickListener(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message) {
        //接收到消息
        LogUtils.d("收到消息" + message.getBody().toString());

        String from = message.getFrom();
        if (from.equals(toChatName)) {
            //是当前聊天对象
            mChatPresenter.updateData(toChatName);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void login() {

        EMClient.getInstance().login(mUserName, "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(ChatRoomActivity.this, mUserName + "登录成功！");
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(ChatRoomActivity.this, mUserName + "登录失败！");
                        LogUtils.d("登录失败日志" + s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    @OnClick({R.id.iv_chat_idle_at, R.id.iv_chat_idle_frame, R.id.iv_chat_against_drama, R.id.iv_chat_against_scene, R.id.iv_chat_against_at, R.id.iv_chat_against_frame, R.id.tv_chat_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_chat_idle_at:
                //闲聊模式下@群成员

                break;
            case R.id.iv_chat_idle_frame:
                //闲聊模式下编辑框框内容
                insertFrame();
                break;
            case R.id.iv_chat_against_drama:
                //对戏模式下大戏(只有管理员可以创建)

                Intent intent = new Intent(this,CreateDramaActivity.class);
                startActivity(intent);

                break;
            case R.id.iv_chat_against_scene:
                //对戏模式下创建情境
                createScene();
                break;
            case R.id.iv_chat_against_at:
                //对戏模式下@好友

                break;
            case R.id.iv_chat_against_frame:
                //对戏模式下编辑框框内容
                insertFrame();
                break;
            case R.id.tv_chat_send:
                //发送文本消息
                sendMsg();
                break;
        }
    }

    private void insertFrame() {
        String content = mEtChatMsg.getText().toString();
        content += "【】";
        mEtChatMsg.setText(content);
        mEtChatMsg.setSelection(content.length() - 1);
    }

    /**
     * 描述：聊天时创建情景
     */
    private void createScene() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_add_scene, null);
        Button btnCancel = (Button) view.findViewById(R.id.btn_dialog_add_scene_cancel);
        Button btnSure = (Button) view.findViewById(R.id.btn_dialog_add_scene_sure);
        final EditText etSceneContent = (EditText) view.findViewById(R.id.et_dialog_add_scene);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sceneContent = etSceneContent.getText().toString().trim();
                ToastUtils.showToast(ChatRoomActivity.this, sceneContent);
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();

    }

    private void sendMsg() {
        String msg = mEtChatMsg.getText().toString();
        ToastUtils.showToast(this, msg);
        //设置消息类型
        int msgAttr = Constants.attrDefault;
        boolean on = mShsChat.isOn();
        if (on) {
            //闲聊
            msgAttr = Constants.attrDefault;
        } else {
            //对戏
            msgAttr = Constants.attrAgainst;
        }

        mChatPresenter.sendMsg(msg, toChatName, msgAttr, mChatType);
        mEtChatMsg.getText().clear();
    }

    @Override
    public void onLeftClick(View v) {
        finish();

    }

    @Override
    public void onRightClick(View v) {
        //群信息或者好友信息
        readyGo(SceneDetailsActivity.class);
    }


    /**
     * 描述：接收到消息后更新ui
     */
    @Override
    public void onUpDate(List<MsgInfo> emMessageList) {
        mChatRoomMsgAdaptere.notifyDataSetChanged();
        if (emMessageList.size() != 0) {
            mRvChatMsg.scrollToPosition(emMessageList.size() - 1);
        }
    }

    /**
     * 描述：初始化消息列表，展示最近聊天的最多20条数据
     */
    @Override
    public void onInit(final List<MsgInfo> msgInfoList) {
        /**
         *  描述：初始化recyclerview
         */

        mRvChatMsg.setLayoutManager(new LinearLayoutManager(this));
        mChatRoomMsgAdaptere = new ChatRoomMsgAdapter(msgInfoList);
        //mMsgAdapter = new ChatMsgAdapter(msgInfoList);
        mRvChatMsg.setAdapter(mChatRoomMsgAdaptere);
        if (msgInfoList.size() != 0) {
            mRvChatMsg.scrollToPosition(msgInfoList.size() - 1);
        }

        mChatRoomMsgAdaptere.setOnBubbleClickListener(new ChatRoomMsgAdapter.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(View v, int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "点击了 " + msgInfoList.get(position).getEMMessage().getBody().toString());
            }
        });

        mChatRoomMsgAdaptere.setOnBubbleLongClickListener(new ChatRoomMsgAdapter.OnBubbleLongClickListener() {
            @Override
            public void onBubbleLongClick(View v, final int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "长按了 " + msgInfoList.get(position).getEMMessage().getBody().toString());
                if (msgInfoList.get(position).getEMMessage().direct() == EMMessage.Direct.SEND) {

                    try {
                        EMClient.getInstance().chatManager().recallMessage(msgInfoList.get(position).getEMMessage());
                        LogUtils.d("撤回消息成功~~~");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        LogUtils.d("撤回消息失败~~~");
                    }

                }
            }
        });

        mChatRoomMsgAdaptere.setOnAvatarClickListener(new ChatRoomMsgAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(View v, int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "点击了头像 他的消息是 " + msgInfoList.get(position).getEMMessage().getBody().toString());
            }
        });

    }


}
