package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChatRoomMsgAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.GroupBriefBean;
import com.shanchain.shandata.ui.model.MsgInfo;
import com.shanchain.shandata.ui.presenter.ChatPresenter;
import com.shanchain.shandata.ui.presenter.impl.ChatPresenterImpl;
import com.shanchain.shandata.ui.view.activity.chat.view.ChatView;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.widgets.rEdit.InsertModel;
import com.shanchain.shandata.widgets.rEdit.RichEditor;
import com.shanchain.shandata.widgets.switchview.SwitchView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


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
    RichEditor mEtChatMsg;
    @Bind(R.id.tv_chat_send)
    TextView mTvChatSend;
    @Bind(R.id.rv_chat_msg)
    RecyclerView mRvChatMsg;
    @Bind(R.id.srl_pull_history_msg)
    SwipeRefreshLayout mSrlPullHistoryMsg;

    public static ChatRoomActivity instance;
    private EMMessage.ChatType mChatType;
    private String toChatName;

    private ChatPresenter mChatPresenter;

    private ChatRoomMsgAdapter mChatRoomMsgAdapter;
    private LinearLayoutManager mLayoutManager;
    //群成员列表
    ArrayList<String> memberList = new ArrayList<>();
    //是否是群聊
    private boolean mIsGroup;
    private boolean move;
    //发消息的头像图片
    String myHeadImg = "";
    String nickName = "";
    private String groupHeadImg = "";
    private List<String> atMembers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViewsAndEvents() {
        LogUtils.i("当前环信账号 " + EMClient.getInstance().getCurrentUser());
        init();
        initToolBar();
        initListener();
        initGroup();
    }

    private void initGroup() {
        if (mChatType == EMMessage.ChatType.GroupChat) {
            //是群聊
            if (mChatPresenter != null) {
                mChatPresenter.initGroup(toChatName);
            }

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

        //消息输入栏监听
        mEtChatMsg.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void textChange(CharSequence s, int start, int before, int count) {

            }
        });

    }

    /**
     * 描述：初始化一些参数，比如 聊天类型，聊天对象等
     */
    private void init() {
        Intent intent = getIntent();
        mIsGroup = intent.getBooleanExtra("isGroup", false);
        String s = intent.getStringExtra("toChatName");
        String name = intent.getStringExtra("name");
        mTbChat.setTitleText(name);
        String cacheCharacterInfo = SCCacheUtils.getCacheCharacterInfo();
        CharacterInfo characterInfo = JSONObject.parseObject(cacheCharacterInfo, CharacterInfo.class);
        myHeadImg = characterInfo.getHeadImg();
        nickName = characterInfo.getName();
        if (mIsGroup) {
            mChatType = EMMessage.ChatType.GroupChat;
            mIvChatAgainstAt.setVisibility(View.VISIBLE);
            mIvChatIdleAt.setVisibility(View.VISIBLE);
        } else {
            mChatType = EMMessage.ChatType.Chat;
            mIvChatAgainstAt.setVisibility(View.GONE);
            mIvChatIdleAt.setVisibility(View.GONE);
        }
        this.toChatName = s;
        mChatPresenter = new ChatPresenterImpl(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mRvChatMsg.setLayoutManager(mLayoutManager);
    }

    private void initToolBar() {
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
        Intent intent = new Intent(this, ChatAtActivity.class);
        intent.putExtra("groupId", toChatName);
        intent.putStringArrayListExtra("members", memberList);
        startActivityForResult(intent, REQUEST_CODE_AT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            GroupBriefBean atBean = (GroupBriefBean) data.getSerializableExtra("at");
            String name = atBean.getName();
            String hxUserName = atBean.getHxUserName();
            InsertModel model = new InsertModel("@", name, "", 0);
            model.setExtra(hxUserName);
            mEtChatMsg.insertSpecialStr(model);
        }
    }

    private void insertFrame() {
        int selectionStart = mEtChatMsg.getSelectionStart();
        Editable editable = mEtChatMsg.getText();
        editable.insert(selectionStart, "【】");
        mEtChatMsg.setSelection(selectionStart + 1);
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
                if (TextUtils.isEmpty(sceneContent)) {
                    ToastUtils.showToast(mContext, "不能发布空的情境");
                    return;
                }
                int msgAttr = Constants.ATTR_SCENE;
                mChatPresenter.sendMsg(sceneContent, toChatName, msgAttr, mChatType, myHeadImg, nickName, mIsGroup, groupHeadImg, atMembers);
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
        boolean on = mShsChat.isOn();
        if (on) {
            //闲聊
            msgAttr = Constants.ATTR_DEFAULT;
        } else {
            //对戏
            msgAttr = Constants.ATTR_AGAINST;
        }

        List<InsertModel> models = mEtChatMsg.getRichInsertList();
        for (int i = 0; i < models.size(); i++) {
            String hxId = models.get(i).getExtra();
            atMembers.add(hxId);
        }

        mChatPresenter.sendMsg(msg, toChatName, msgAttr, mChatType, myHeadImg, nickName, mIsGroup, groupHeadImg, atMembers);
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
        if (mIsGroup) {
            intent.putExtra("", "");
        }
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

            }
        });

        mChatRoomMsgAdapter.setOnAvatarClickListener(new ChatRoomMsgAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(View v, int position) {
                MsgInfo msgInfo = mChatRoomMsgAdapter.getData().get(position);
                String character = msgInfo.getEMMessage().getStringAttribute(Constants.MSG_CHARACTER_ID, "");
                String userName = msgInfo.getEMMessage().getUserName();
                if (TextUtils.isEmpty(character)) {
                    obtainCharacterId(userName);
                } else {
                    goFriendHomePage(character);
                }
            }
        });

    }

    private void obtainCharacterId(String userName) {
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_QUERY)
                .addParams("userName", userName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取用户character信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到character信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                String characterId = SCJsonUtils.parseString(data, "characterId");
                                goFriendHomePage(characterId);
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
    }

    private void goFriendHomePage(String characterId) {
        Intent intent = new Intent(mContext, FriendHomeActivity.class);
        int character = Integer.parseInt(characterId);
        intent.putExtra("characterId",character);
        startActivity(intent);
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

    @Override
    public void initGroupSuccess(List<String> members) {
        if (members == null) {
            //ToastUtils.showToast(mContext,"获取群信息失败");
        } else {
            String groupName = members.get(0);
            members.remove(0);
            memberList = new ArrayList<>(members);
            mTbChat.setTitleText(groupName);
            for (int i = 0; i < memberList.size(); i++) {
                LogUtils.i("群成员有 = " + members.get(i));
            }
        }
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
