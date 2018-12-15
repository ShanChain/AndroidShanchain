package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicCommentAdapter;
import com.shanchain.shandata.adapter.StoryItemNineAdapter;
import com.shanchain.shandata.adapter.TaskCommentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.CommentBean;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.TaskCommentContent;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingerChatInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.ClickableSpanNoUnderline;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.utils.SCLinkMovementMethod;
import com.shanchain.shandata.widgets.dialog.CommentDialog;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class TaskDetailActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener, TaskView {

    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.rv_task_comment)
    RecyclerView rvTaskComment;
    @Bind(R.id.tv_task_details_comment)
    TextView tvTaskDetailsComment;

    private List<BdCommentBean> commentList = new ArrayList();
    private BaseQuickAdapter taskCommentAdapter;
    private View mHeadView;
    private TaskPresenter taskPresenter;
    private TextView mTvHeadComment;
    private TextView mTvHeadLike;
    private ChatEventMessage detailsChatEventMessage;
    private CharacterInfo characterInfo;
    private DefaultUser defaultUser;
    private LinearLayout liAddTask;
    private Handler handler, dialogHandler;
    private double seatRmbRate;
    private TextView tvSeatRate;
    private TextView limitedTime;
    private Thread addTaskThread;
    private long timeStamp;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private ChatEventMessage chatEventMessage1;
    private String formatDate,roomID;
    private Conversation chatRoomConversation;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        detailsChatEventMessage = (ChatEventMessage) getIntent().getSerializableExtra("chatEventMessage");
        roomID =  getIntent().getStringExtra("roomId")!=null?getIntent().getStringExtra("roomId"):"0";
        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        if (null == chatRoomConversation) {
            chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
        }
        initToolBar();
        initView();
        initData(detailsChatEventMessage);

    }

    private void initView() {
        dialogHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ToastUtils.showToastLong(TaskDetailActivity.this, "任务发送失败，您的余额不足");
                } else if (msg.what == 2) {
                    ToastUtils.showToastLong(TaskDetailActivity.this, "任务发送失败，网络连接错误");
                }

            }
        };

        liAddTask= findViewById(R.id.linear_add_task);
        liAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTaskDialog();
            }
        });
    }

    private void initHeadView(final ChatEventMessage chatEventMessage) {
//        BdCommentBean bdCommentBean = commentList.get(0);
//        chatEventMessage = bdCommentBean.getChatEventMessage();
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
        mHeadView = View.inflate(this, R.layout.item_task_details_head, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView tvLastTime = (TextView) mHeadView.findViewById(R.id.even_message_last_time);
        TextView bounty = (TextView) mHeadView.findViewById(R.id.even_message_bounty);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.even_message_content);

//        NineGridImageView nineGridImageView = (NineGridImageView) mHeadView.findViewById(R.id.ngiv_item_story);
        final Button btnEvenTask = mHeadView.findViewById(R.id.btn_event_task);
        mTvHeadLike = (TextView) mHeadView.findViewById(R.id.even_message_like_num);
        mTvHeadComment = (TextView) mHeadView.findViewById(R.id.even_message_comment_num);


        //获取角色信息
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
        String characterImg = characterInfo.getHeadImg();
        String characterName = characterInfo.getName();

        if (chatEventMessage == null) {
            return;
        }
        if (chatEventMessage.getHeadImg() != null && chatEventMessage.getName() != null) {
            GlideUtils.load(mContext, chatEventMessage.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(chatEventMessage.getName() == null ? "无昵称" : chatEventMessage.getName());
        } else {
            GlideUtils.load(mContext, characterInfo.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(characterInfo.getName() == null ? "无昵称" : characterInfo.getName());
        }
        bounty.setText("赏金：￥ " + chatEventMessage.getBounty());
        tvContent.setText(chatEventMessage.getIntro() + "");
        mTvHeadLike.setText(chatEventMessage.getSupportCount() + "");
        mTvHeadComment.setText(chatEventMessage.getCommentCount() + "");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm MM-dd");
        String createTime = sdf1.format(chatEventMessage.getPublishTime());
        String expiryTime = sdf.format(chatEventMessage.getExpiryTime());
//        String expiryTime = DateUtils.formatFriendly(new Date(chatEventMessage.getExpiryTime()));

        tvTime.setText(createTime + "");
        tvLastTime.setText("完成时限："+expiryTime + "");
//        ivAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("userInfo", defaultUser);
//                bundle.putString("hxUserId", defaultUser.getHxUserId());
//                readyGo(SingerChatInfoActivity.class, bundle);
//            }
//        });


//        tvTime.setText(DateUtils.formatFriendly(new Date(mDynamicModel.getCreateTime())));
//        boolean isFav = isBeFav;
//        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
//        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

//        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
//        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());

//        mTvHeadLike.setCompoundDrawables(isFav ? like_selected : like_def, null, null, null);
//        mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));


        //领取任务
        if (chatEventMessage.getStatus() >=10) {
            btnEvenTask.setText("已被领取");
            btnEvenTask.setFocusable(false);
            btnEvenTask.setOnClickListener(null);
            btnEvenTask.setTextColor(getResources().getColor(R.color.aurora_bg_edittext_default));
        } else {
            btnEvenTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //领取任务请求
                    SCHttpUtils.postWithUserId()
                            .url(HttpApi.TASK_DETAIL_RECEIVE)
                            .addParams("roomId", SCCacheUtils.getCacheRoomId() + "")
                            .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                            .addParams("taskId", chatEventMessage.getTaskId() + "")
                            .build()
                            .execute(new SCHttpStringCallBack() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    ToastUtils.showToast(TaskDetailActivity.this, "任务已被领取");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = JSONObject.parseObject(response).getString("code");
                                    if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                        String data = JSONObject.parseObject(response).getString("data");
                                        final String HxUserName = JSONObject.parseObject(data).getString("HxUserName");
                                        btnEvenTask.setText("已被领取");
                                        btnEvenTask.setFocusable(false);
                                        btnEvenTask.setOnClickListener(null);
                                        btnEvenTask.setTextColor(getResources().getColor(R.color.aurora_bg_edittext_default));
                                        if (chatEventMessage.getFromUser() != null) {
                                            IUser user = chatEventMessage.getFromUser();
                                            String displayName = chatEventMessage.getFromUser().getDisplayName();
                                            defaultUser = new DefaultUser(user.getId(), displayName, user.getAvatarFilePath());
                                            defaultUser.setHxUserId(HxUserName);
                                        }
                                    }
                                }
                            });
                    final int[] idItems = new int[]{R.id.btn_dialog_task_detail_sure, R.id.iv_dialog_close};
                    final CustomDialog dialog = new CustomDialog(TaskDetailActivity.this, false, 1.0, R.layout.common_dialog_task_receive, idItems);
                    dialog.show();
                    TextView messageContent = (TextView) dialog.getByIdView(R.id.even_message_content);
                    TextView lastTime = (TextView) dialog.getByIdView(R.id.tv_task_detail_last_time);
                    messageContent.setText(chatEventMessage.getIntro() + "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
                    String expiryTime = sdf.format(chatEventMessage.getExpiryTime());
                    lastTime.setText(expiryTime + "");
                    EventBus.getDefault().postSticky(new EventMessage<ChatEventMessage>(1));
                    dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(final CustomDialog dialog, View view) {

                            switch (view.getId()) {
                                case R.id.btn_dialog_task_detail_sure:
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("userInfo", defaultUser);
                                    bundle.putString("hxUserId", defaultUser.getHxUserId());
                                    readyGo(SingerChatInfoActivity.class, bundle);
                                    dialog.dismiss();
                                    break;
                                case R.id.iv_dialog_close:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });

                }

            });

        }

       /* String intro = bdCommentBean.getChatEventMessage().getIntro();
        String content = "";
        List<String> imgList = new ArrayList<>();
        List<SpanBean> spanBeanList = null;
        if (intro.contains("content")) {
            ReleaseContentInfo contentInfo = new Gson().fromJson(intro, ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgList = contentInfo.getImgs();
            spanBeanList = contentInfo.getSpanBeanList();
        } else {
            content = intro;
        }
*/
        /*SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

        if (spanBeanList != null) {
            for (int i = 0; i < spanBeanList.size(); i++) {
                ClickableSpanNoUnderline clickSpan = new ClickableSpanNoUnderline(Color.parseColor("#3bbac8"), new ClickableSpanNoUnderline.OnClickListener() {
                    @Override
                    public void onClick(View widget, ClickableSpanNoUnderline span) {
                        //ToastUtils.showToast(mContext, span.getClickData().getStr());
                        SpanBean clickData = span.getClickData();
                        if (clickData.getType() == Constants.SPAN_TYPE_AT) {
                            Bundle bundle = new Bundle();
                            RNDetailExt detailExt = new RNDetailExt();
                            RNGDataBean gDataBean = new RNGDataBean();
                            String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                            String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);
                            String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
                            String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
                            gDataBean.setCharacterId(characterId);
                            gDataBean.setSpaceId(spaceId);
                            gDataBean.setToken(token);
                            gDataBean.setUserId(uId);
                            detailExt.setgData(gDataBean);
                            detailExt.setModelId(clickData.getBeanId() + "");
                            String json = JSONObject.toJSONString(detailExt);
                            bundle.putString(NavigatorModule.REACT_PROPS, json);
                            NavigatorModule.startReactPage(mContext, RNPagesConstant.RoleDetailScreen, bundle);
                        } else if (clickData.getType() == Constants.SPAN_TYPE_TOPIC) {
                            Intent intent = new Intent(mContext, TopicDetailsActivity.class);
                            intent.putExtra("from", 1);
                            intent.putExtra("topicId", clickData.getBeanId() + "");
                            startActivity(intent);
                        }
                    }
                });
                String str = spanBeanList.get(i).getStr();
                if (spanBeanList.get(i).getType() == Constants.SPAN_TYPE_AT) {
                    String temp = "@" + str;
                    int indexAt = content.indexOf(temp);
                    if (indexAt == -1) {
                        return;
                    }
                    spannableStringBuilder.setSpan(clickSpan, indexAt, indexAt + temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } else if (spanBeanList.get(i).getType() == Constants.SPAN_TYPE_TOPIC) {
                    String temp = "#" + str + "#";
                    int indexTopic = content.indexOf(temp);
                    if (indexTopic == -1) {
                        return;
                    }
                    spannableStringBuilder.setSpan(clickSpan, indexTopic, indexTopic + temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                clickSpan.setClickData(spanBeanList.get(i));
            }

        }
        tvContent.setMovementMethod(SCLinkMovementMethod.getInstance());
        tvContent.setText(spannableStringBuilder);
*/

//        ivAvatar.setOnClickListener(this);
//        tvForwarding.setOnClickListener(this);
//        mTvHeadComment.setOnClickListener(this);
//        mTvHeadLike.setOnClickListener(this);
//        ivMore.setVisibility(View.GONE);

    }

    private void initRecyclerView(ChatEventMessage chatEventMessage) {
        initHeadView(chatEventMessage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rvTaskComment.setLayoutManager(linearLayoutManager);
        taskCommentAdapter = new TaskCommentAdapter(R.layout.item_task_details_comment, commentList, this);
        rvTaskComment.addItemDecoration(new RecyclerViewDivider(this));
        taskCommentAdapter.setEnableLoadMore(true);
        rvTaskComment.setAdapter(taskCommentAdapter);
        taskCommentAdapter.setHeaderView(mHeadView);

    }

    private void initData(ChatEventMessage chatEventMessage) {
        taskPresenter = new TaskPresenterImpl(this);
        if (chatEventMessage == null) {
            return;
        }
        String taskId = chatEventMessage.getTaskId() == null ? "不限时" : chatEventMessage.getTaskId() + "";
        SCHttpUtils.postWithUserId()
                .url(HttpApi.TASK_DETAIL)
                .addParams("taskId", taskId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        String data = JSONObject.parseObject(response).getString("data");
                        ChatEventMessage chatEventMessage1 = JSONObject.parseObject(data, ChatEventMessage.class);
                        initRecyclerView(chatEventMessage1);
                    }
                });
        /*for (int i = 0; i < 10; i++) {
            BdCommentBean bdCommentBean = new BdCommentBean();
            CommentBean commentBean = new CommentBean();
            commentBean.setCommentId(13);
            commentBean.setStoryId(8);
            commentBean.setContent("蝴蝶如我，我如蝴蝶");
            commentBean.setCreateTime(Long.valueOf("1507639682000"));
            commentBean.setIsAnon(0);
            commentBean.setMySupport(false);
            commentBean.setUserId(8);

            ContactBean contactBean = new ContactBean();
            contactBean.setIntro("长久以来，大陆一直流传着关于灭世魔神王的传说。他象征着绝对的黑暗");
            contactBean.setName("项羽");
            contactBean.setType(1);
            contactBean.setCharacterId(69);
            contactBean.setUserId(25);

            bdCommentBean.setCharacterId(126);
            bdCommentBean.setCommentBean(commentBean);
            bdCommentBean.setContactBean(contactBean);
            bdCommentBean.setChatEventMessage(chatEventMessage);
            commentList.add(bdCommentBean);

        }*/

       /* SCHttpUtils.postWithUserId()
                .url(HttpApi.TASK_COMMENT_QUERY)
                .addParams("characterId",SCCacheUtils.getCacheCharacterId()+"")
                .addParams("taskId",chatEventMessage.getTaskId()+"")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");
                            TaskCommentContent commentContent = JSONObject.parseObject(content,TaskCommentContent.class);

                        }
                    }
                });*/

    }

    private void initToolBar() {
        tbTaskComment.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbTaskComment.setTitleText("社区帮");
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.getTitleView().setLayoutParams(layoutParams);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setOnLeftClickListener(this);
        tbTaskComment.setRightText("我的");
        tbTaskComment.setOnRightClickListener(this);

    }

    /*
     * 初始化弹窗
     *
     * */
    private void initTaskDialog() {
        SCHttpUtils.get()
                .url(HttpApi.GET_SEAT_CURRENCY)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String rate = JSONObject.parseObject(data).getString("rate");//汇率，获取当前汇率
                            String currency = JSONObject.parseObject(data).getString("currency");

                            seatRmbRate = Double.valueOf(rate);
                        }
                    }
                });

        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.et_input_dialog_bounty, R.id.dialog_select_task_time, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        final CustomDialog dialog = new CustomDialog(TaskDetailActivity.this, false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        View layout = View.inflate(TaskDetailActivity.this, R.layout.common_dialog_chat_room_task, null);
        dialog.setView(layout);
        handler = new Handler() {
            @Override
            public void handleMessage(final android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                            @Override
                            public void OnItemClick(final CustomDialog dialog, View view) {
                                final EditText describeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                                final EditText bountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
                                bountyEditText.setFocusable(true);
                                bountyEditText.setFocusableInTouchMode(true);
                                bountyEditText.requestFocus();
                                limitedTime = (TextView) dialog.getByIdView(R.id.dialog_select_task_time);
                                tvSeatRate = (TextView) dialog.getByIdView(R.id.seatRate);
                                tvSeatRate.setText("1 SEAT");
                                //输入框监听
                                bountyEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (s.toString().length() > 0) {
                                            float bounty = Float.parseFloat(s.toString());
                                            tvSeatRate.setText(("" + bounty / seatRmbRate) + " SEAT");
                                            bountyEditText.setText("￥" + s.toString());
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                switch (view.getId()) {
                                    case R.id.et_input_dialog_describe:
                                        ToastUtils.showToast(TaskDetailActivity.this, "输入任务描述");
                                        break;
                                    case R.id.et_input_dialog_bounty:
                                        ToastUtils.showToast(TaskDetailActivity.this, "输入赏金");
                                        break;
                                    case R.id.btn_dialog_input_sure:
                                        showLoadingDialog();
                                        releaseTask(dialog, describeEditText, bountyEditText, limitedTime);
                                        break;
                                    case R.id.iv_dialog_close:
                                        dialog.dismiss();
                                        break;
                                    //选择时间
                                    case R.id.dialog_select_task_time:
                                        initPickerView();
                                        scTimePickerView.setOnTimeSelectListener(onTimeSelectListener);
                                        scTimePickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
                                            @Override
                                            public void onCancelClick(View v) {

                                            }
                                        });
                                        scTimePickerView.show(limitedTime);
                                        break;
                                }
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        };
    }

    //初始化时间选择弹窗
    private void initPickerView() {
        SCTimePickerView.Builder builder = new SCTimePickerView.Builder(TaskDetailActivity.this);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 10, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置结束年份
        builder.setType(new boolean[]{true, true, true, true, false, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("清除")
                .setCancelColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setRangDate(startDate, endDate)
                .setSubCalSize(14)
                .setTitleBgColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                .setSubmitColor(TaskDetailActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .isDialog(true)
                .build();

        scTimePickerView = new SCTimePickerView(builder);
        scTimePickerView.setDate(Calendar.getInstance());

        onTimeSelectListener = new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatDate = simpleDateFormat.format(date);
                timeStamp = date.getTime();
                LogUtils.d("SCTimePickerView", "点击了SCTimePickerView" + formatDate);
                TextView clickView = (TextView) v;
                clickView.setText(formatDate);
                scTimePickerView.show(limitedTime);
            }
        };
    }

    /*
     * 发布任务
     *
     * */
    private void releaseTask(final CustomDialog dialog, EditText describeEditText, EditText bountyEditText, TextView textViewTime) {
        if (TextUtils.isEmpty(describeEditText.getText().toString()) && TextUtils.isEmpty(bountyEditText.getText().toString()) && TextUtils.isEmpty(textViewTime.getText().toString())) {
            ToastUtils.showToast(TaskDetailActivity.this, "请输入完整信息");
        } else {
            final String spaceId = SCCacheUtils.getCacheSpaceId();//获取当前的空间ID
            final String bounty = bountyEditText.getText().toString();
            final String dataString = describeEditText.getText().toString();
            final String LimitedTtime = textViewTime.getText().toString();

            final String characterId = SCCacheUtils.getCacheCharacterId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            final String createTime = simpleDateFormat.format(LimitedTtime);
            //向服务器请求添加任务
//          taskPresenter.releaseTask(characterId, String.valueOf(roomID), bounty, dataString, timeStamp);

            SCHttpUtils.postWithUserId()
                    .url(HttpApi.CHAT_TASK_ADD)
                    .addParams("characterId", characterId + "")
                    .addParams("price", bounty)
                    .addParams("roomId", roomID + "")
                    .addParams("dataString", dataString + "") //任务内容
                    .addParams("time", timeStamp + "")
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("TaskPresenterImpl", "添加任务失败");
                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            MyMessage myMessage = new MyMessage(IMessage.MessageType.EVENT.ordinal());
                            myMessage.setChatEventMessage(chatEventMessage1);
//                            mAdapter.addToStart(myMessage, true);
                            closeLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            dialog.dismiss();
                            closeLoadingDialog();
                            String code = JSONObject.parseObject(response).getString("code");
                            final String message = JSONObject.parseObject(response).getString("message");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String publishTime = JSONObject.parseObject(data).getString("PublishTime");
                                String task = JSONObject.parseObject(data).getString("Task");
                                chatEventMessage1 = JSONObject.parseObject(task, ChatEventMessage.class);
                                Map customMap = new HashMap();
                                customMap.put("taskId", chatEventMessage1.getTaskId() + "");
                                customMap.put("bounty", chatEventMessage1.getBounty() + "");
                                customMap.put("dataString", chatEventMessage1.getIntro());
                                customMap.put("time", timeStamp + "");

                                Message sendCustomMessage = chatRoomConversation.createSendCustomMessage(customMap);
                                sendCustomMessage.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        String s1 = s;
                                        MyMessage myMessage = new MyMessage();
                                        if (0 == i) {
                                            Toast.makeText(TaskDetailActivity.this, "发送任务消息成功", Toast.LENGTH_SHORT);
                                            LogUtils.d("发送任务消息", "code: " + i + " 回调信息：" + s);
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                            myMessage.setChatEventMessage(chatEventMessage1);
//                                            mAdapter.addToStart(myMessage, true);
                                        } else {
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                            myMessage.setChatEventMessage(chatEventMessage1);
//                                            mAdapter.addToStart(myMessage, true);
                                        }
                                    }
                                });
                                JMessageClient.sendMessage(sendCustomMessage);

//                                chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                                mAdapter.addToStart(chatEventMessage1, true);
                                dialog.dismiss();
                            } else if (code.equals("10001")) {
                                dialogHandler.sendEmptyMessage(1);
                                dialog.dismiss();
                                //余额不足
                                dialogHandler.sendEmptyMessage(1);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TaskDetailActivity.this, "您的钱包余额不足", Toast.LENGTH_SHORT);

                                    }
                                });
                            } else {
                                dialogHandler.sendEmptyMessage(2);
                                dialog.dismiss();
                                dialogHandler.sendEmptyMessage(2);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TaskDetailActivity.this, "错误消息" + message, Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        }
                    });
        }
    }


    @OnClick(R.id.tv_task_details_comment)
    public void onClick() {
//        showPop();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    private void showPop() {
        FragmentManager manager = getSupportFragmentManager();
        CommentDialog dialog = new CommentDialog();
        dialog.show(manager, "tag");
        dialog.setOnSendClickListener(new CommentDialog.OnSendClickListener() {
            @Override
            public void onSendClick(View v, String msg) {
                String taskId = String.valueOf(detailsChatEventMessage.getTaskId());
                String characterId = SCCacheUtils.getCacheCharacterId();

                taskPresenter.addTaskComment(characterId, taskId, msg);
                taskCommentAdapter.notifyDataSetChanged();
            }
        });
        KeyboardUtils.showSoftInput(this);
    }

    @Override
    public void initTask(List<ChatEventMessage> list, boolean isSuccess) {

    }

    @Override
    public void initUserTaskList(List<ChatEventMessage> list, boolean isSuccess) {

    }

    @Override
    public void addSuccess(boolean success) {

    }

    @Override
    public void releaseTaskView(boolean isSuccess) {

    }


    @Override
    public void supportSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void supportCancelSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void deleteTaskView(boolean isSuccess, int position) {

    }
}
