package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
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
import com.shanchain.arkspot.ui.view.activity.story.SelectContactActivity;
import com.shanchain.arkspot.utils.KeyboardUtils;
import com.shanchain.arkspot.widgets.switchview.SwitchView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ChatRoomActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, ChatView, SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_CODE_AT = 100;
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
    @Bind(R.id.srl_pull_history_msg)
    SwipeRefreshLayout mSrlPullHistoryMsg;
    private EMMessage.ChatType mChatType;
    private String toChatName;

    private ChatPresenter mChatPresenter;

    private ChatRoomMsgAdapter mChatRoomMsgAdapter;
    private LinearLayoutManager mLayoutManager;
    //群成员列表
    List<String> memberList = new ArrayList<>();
    //是否是群聊
    private boolean mIsGroup;
    private boolean move;
    //发消息的头像图片
    String myHeadImg = "http://www.sioe.cn/z/uploadfile/201109/14/2152366361.jpg";
//    String myHeadImg = "http://www.sioe.cn/z/uploadfile/201109/13/1548377753.jpg";
    String nickName = "鸣人";
    private String groupHeadImg = "http://img1.2345.com/duoteimg/zixunImg/local/2016/11/16/1479289866985.jpg";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViewsAndEvents() {
        LogUtils.d("当前环信账号 " + EMClient.getInstance().getCurrentUser());
        init();
        initToolBar();
        initListener();
        initGroup();
    }

    private void initGroup() {
        if (mChatType == EMMessage.ChatType.GroupChat) {
            //是群聊
           /*
            从本地获取群成员列表
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatName);
            memberList = group.getMembers();
            List<String> adminList = group.getAdminList();
            for (int i = 0; i < memberList.size(); i++) {
                LogUtils.d("群成员:" + memberList.get(i));
            }

            for (int i = 0; i < adminList.size(); i++) {
                LogUtils.d("群管理员:" + adminList.get(i));
            }

            if (memberList.size() == 0) {
                LogUtils.d("未获取到群成员");
            }*/

            //添加全局管理员
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatName);
            memberList.add("admin");
            //添加群主
            memberList.add(group.getOwner());
            //添加管理员
            List<String> adminList = group.getAdminList();
            memberList.addAll(adminList);

            for (int i = 0; i < adminList.size(); i++) {
                LogUtils.d("群管理 : " + adminList.get(i));
            }

            //从环信服务器拉取群成员列表
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMCursorResult<String> result = null;
                        final int pageSize = 200;
                        do {
                            result = EMClient.getInstance().groupManager().fetchGroupMembers(toChatName,
                                    result != null ? result.getCursor() : "", pageSize);
                            memberList.addAll(result.getData());
                            LogUtils.d("fetchGroupMembers = " + result.getData().size());
                        }
                        while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
                    } catch (HyphenateException e) {
                        LogUtils.d("失败了");
                        e.printStackTrace();
                    }
                    for (int i = 0; i < memberList.size(); i++) {
                        LogUtils.d("群成员有" + memberList.get(i));
                    }
                    LogUtils.d("群成员列表初始化完成");
                }
            });

        }
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

        mSrlPullHistoryMsg.setOnRefreshListener(this);

    }

    /**
     * 描述：初始化一些参数，比如 聊天类型，聊天对象等
     */
    private void init() {
        Intent intent = getIntent();
        mIsGroup = intent.getBooleanExtra("isGroup", false);
        String s = intent.getStringExtra("toChatName");
        if (mIsGroup) {
            mChatType = EMMessage.ChatType.GroupChat;
        } else {
            mChatType = EMMessage.ChatType.Chat;
        }
        //注册观察者
        EventBus.getDefault().register(this);
        this.toChatName = s;
        mChatPresenter = new ChatPresenterImpl(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mRvChatMsg.setLayoutManager(mLayoutManager);
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

        LogUtils.d("消息来自 ： " + from);
        if (from.equals(toChatName)) {
            //是当前聊天对象
            mChatPresenter.updateData(toChatName);
        } else if (memberList.contains(from)) {
            mChatPresenter.updateData(toChatName);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.iv_chat_idle_at, R.id.iv_chat_idle_frame, R.id.iv_chat_against_drama, R.id.iv_chat_against_scene, R.id.iv_chat_against_at, R.id.iv_chat_against_frame, R.id.tv_chat_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_chat_idle_at:
                //闲聊模式下@群成员
                atMember();
                break;
            case R.id.iv_chat_idle_frame:
                //闲聊模式下编辑框框内容
                insertFrame();
                break;
            case R.id.iv_chat_against_drama:
                //对戏模式下大戏(只有管理员可以创建)

                Intent intent = new Intent(this, CreateDramaActivity.class);
                startActivity(intent);

                break;
            case R.id.iv_chat_against_scene:
                //对戏模式下创建情境
                createScene();
                break;
            case R.id.iv_chat_against_at:
                //对戏模式下@好友
                atMember();
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

    /**
     * 描述：@群成员
     */
    private void atMember() {
        Intent intent = new Intent(this, SelectContactActivity.class);
        intent.putExtra("isAt", true);
        startActivityForResult(intent, REQUEST_CODE_AT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<String> contacts = data.getStringArrayListExtra("contacts");
            String msg = mEtChatMsg.getText().toString().trim();
            String at = "";
            for (int i = 0; i < contacts.size(); i++) {
                at += "@" + contacts.get(i) + " ";
            }
            mEtChatMsg.setText(msg + at);
            mEtChatMsg.setSelection((msg + at).length());
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
                int msgAttr = Constants.ATTR_SCENE;
                mChatPresenter.sendMsg(sceneContent, toChatName, msgAttr, mChatType, myHeadImg, nickName,mIsGroup,groupHeadImg);
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();

    }

    private void sendMsg() {
        String msg = mEtChatMsg.getText().toString();
        //设置消息类型
        int msgAttr = Constants.ATTR_DEFAULT;
        //设置消息额外字符串参数（头像）
        //String myHeadImg = "http://www.sioe.cn/z/uploadfile/201109/13/1548377753.jpg";


        boolean on = mShsChat.isOn();
        if (on) {
            //闲聊
            msgAttr = Constants.ATTR_DEFAULT;

        } else {
            //对戏
            msgAttr = Constants.ATTR_AGAINST;
        }


        mChatPresenter.sendMsg(msg, toChatName, msgAttr, mChatType, myHeadImg, nickName,mIsGroup,groupHeadImg);
        mEtChatMsg.getText().clear();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //群信息或者好友信息
        Intent intent = new Intent(this, SceneDetailsActivity.class);
        intent.putExtra("isGroup", mIsGroup);
        intent.putExtra("toChatName", toChatName);
        startActivity(intent);
    }


    /**
     * 描述：接收到消息后更新ui
     */
    @Override
    public void onUpDate(List<MsgInfo> emMessageList) {
        mChatRoomMsgAdapter.notifyDataSetChanged();
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
        mChatRoomMsgAdapter = new ChatRoomMsgAdapter(msgInfoList);
        mRvChatMsg.setAdapter(mChatRoomMsgAdapter);
        if (msgInfoList.size() != 0) {
            mRvChatMsg.scrollToPosition(msgInfoList.size() - 1);
        }

        mChatRoomMsgAdapter.setOnBubbleClickListener(new ChatRoomMsgAdapter.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(View v, int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "点击了 " + msgInfoList.get(position).getEMMessage().getBody().toString());
            }
        });

        mChatRoomMsgAdapter.setOnItemClickListener(new ChatRoomMsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //点击空白地方时候隐藏软键盘
                KeyboardUtils.hideSoftInput(mActivity);
            }
        });

        mChatRoomMsgAdapter.setOnBubbleLongClickListener(new ChatRoomMsgAdapter.OnBubbleLongClickListener() {
            @Override
            public void onBubbleLongClick(View v, final int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "长按了 " + msgInfoList.get(position).getEMMessage().getBody().toString());
                if (msgInfoList.get(position).getEMMessage().direct() == EMMessage.Direct.SEND) {

                    try {
                        //消息撤回有问题
                        EMClient.getInstance().chatManager().recallMessage(msgInfoList.get(position).getEMMessage());
                        LogUtils.d("撤回消息成功~~~");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        LogUtils.d("撤回消息失败~~~");
                    }

                }
            }
        });

        mChatRoomMsgAdapter.setOnAvatarClickListener(new ChatRoomMsgAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(View v, int position) {
                ToastUtils.showToast(ChatRoomActivity.this, "点击了头像 他的消息是 " + msgInfoList.get(position).getEMMessage().getBody().toString());
            }
        });

    }

    /**
     * 描述： 返回历史记录，更新UI
     */
    @Override
    public void onPullHistory(List<EMMessage> emMessages) {
        mSrlPullHistoryMsg.setRefreshing(false);
        if (emMessages == null) {
            return;
        }
        final int index = emMessages.size() - 1;
        if (index < 0) {
            return;
        }
        mChatRoomMsgAdapter.notifyDataSetChanged();
        moveToPosition(index);
        mRvChatMsg.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在这里进行第二次滚动（最后的100米！）
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = index - mLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mRvChatMsg.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mRvChatMsg.getChildAt(n).getTop();
                        //最后的移动
                        mRvChatMsg.scrollBy(0, top);
                    }
                }

            }
        });
    }

    /**
     * 描述：配合recyclerview的滚动状态监听来设置recycleview第一个可见条目的位置
     */
    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRvChatMsg.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRvChatMsg.getChildAt(n - firstItem).getTop();
            mRvChatMsg.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRvChatMsg.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    /**
     * 描述：拉取本地聊天历史记录
     */
    @Override
    public void onRefresh() {
        mChatPresenter.pullHistoryMsg(toChatName);
    }
}
