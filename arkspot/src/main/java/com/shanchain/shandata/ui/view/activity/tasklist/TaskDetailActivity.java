package com.shanchain.shandata.ui.view.activity.tasklist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicCommentAdapter;
import com.shanchain.shandata.adapter.StoryItemNineAdapter;
import com.shanchain.shandata.adapter.TaskCommentAdapter;
import com.shanchain.shandata.base.BaseActivity;
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
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.model.ChatEventMessage;
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
    private TextView mTvHeadComment;
    private TextView mTvHeadLike;
    private ChatEventMessage chatEventMessage;
    private TaskPresenter taskPresenter;
    private CharacterInfo characterInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        chatEventMessage = (ChatEventMessage) getIntent().getSerializableExtra("chatEventMessage");

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initHeadView() {
//        BdCommentBean bdCommentBean = commentList.get(0);
//        chatEventMessage = bdCommentBean.getChatEventMessage();
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(),CharacterInfo.class);

        mHeadView = View.inflate(this, R.layout.item_task_details_head, null);
        ImageView ivAvatar = (ImageView) mHeadView.findViewById(R.id.iv_item_story_avatar);
        TextView tvName = (TextView) mHeadView.findViewById(R.id.tv_item_story_name);
        TextView tvTime = (TextView) mHeadView.findViewById(R.id.tv_item_story_time);
        TextView bounty = (TextView) mHeadView.findViewById(R.id.even_message_bounty);
        TextView tvContent = (TextView) mHeadView.findViewById(R.id.even_message_content);

//        NineGridImageView nineGridImageView = (NineGridImageView) mHeadView.findViewById(R.id.ngiv_item_story);
        final Button btnEvenTask = mHeadView.findViewById(R.id.btn_event_task);
        mTvHeadLike = (TextView) mHeadView.findViewById(R.id.even_message_like_num);
        mTvHeadComment = (TextView) mHeadView.findViewById(R.id.even_message_comment_num);

        //获取角色信息
        characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(),CharacterInfo.class);
        String characterImg = characterInfo.getHeadImg();
        String characterName = characterInfo.getName();

        if(chatEventMessage==null){
            return;
        }
        if (chatEventMessage.getHeadImg()!=null&&chatEventMessage.getName()!=null){
            GlideUtils.load(mContext, chatEventMessage.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(chatEventMessage.getName()==null?"无昵称":chatEventMessage.getName());
        }else {
            GlideUtils.load(mContext, characterInfo.getHeadImg(), ivAvatar, 0);//加载头像
            tvName.setText(characterInfo.getName()==null?"无昵称":characterInfo.getName());
        }
        bounty.setText("赏金："+chatEventMessage.getBounty() + "SEAT");
        tvContent.setText(chatEventMessage.getIntro() + "");
        mTvHeadLike.setText(chatEventMessage.getSupportCount() + "");
        mTvHeadComment.setText(chatEventMessage.getCommentCount() + "");
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        String expiryTime = sdf.format(chatEventMessage.getExpiryTime());

        tvTime.setText(expiryTime+"");


//        tvTime.setText(DateUtils.formatFriendly(new Date(mDynamicModel.getCreateTime())));
//        boolean isFav = isBeFav;
//        Drawable like_def = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
//        Drawable like_selected = getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);

//        like_def.setBounds(0, 0, like_def.getMinimumWidth(), like_def.getMinimumHeight());
//        like_selected.setBounds(0, 0, like_selected.getMinimumWidth(), like_selected.getMinimumHeight());

//        mTvHeadLike.setCompoundDrawables(isFav ? like_selected : like_def, null, null, null);
//        mTvHeadLike.setCompoundDrawablePadding(DensityUtils.dip2px(this, 10));


        //领取任务
        if (chatEventMessage.getStatus()==10){
            btnEvenTask.setText("已被领取")
            ;btnEvenTask.setFocusable(false);
        }
        btnEvenTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] idItems = new int[]{R.id.btn_dialog_task_detail_sure, R.id.iv_dialog_close};
                final CustomDialog dialog = new CustomDialog(TaskDetailActivity.this, false, 1.0, R.layout.common_dialog_task_receive, idItems);
                dialog.show();
                TextView messageContent = (TextView) dialog.getByIdView(R.id.even_message_content);
                TextView lastTime = (TextView) dialog.getByIdView(R.id.tv_task_detail_last_time);
                messageContent.setText(chatEventMessage.getIntro()+"");
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
                String expiryTime = sdf.format(chatEventMessage.getExpiryTime());
                lastTime.setText(expiryTime);
                lastTime.setText(expiryTime+"");
                dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(final CustomDialog dialog, View view) {


                        switch (view.getId()) {
                            case R.id.btn_dialog_input_sure:
                                Intent intent = new Intent(TaskDetailActivity.this, SingleChatActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.iv_dialog_close:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
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
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                    String data = JSONObject.parseObject(response).getString("data");
//                                    if (message.equals("任务不存在")){
//
//                                    }
                                    btnEvenTask.setText("已被领取");
                                    dialog.dismiss();
                                }

                            }
                        });
            }

        });


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

    private void initRecyclerView() {
        initHeadView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rvTaskComment.setLayoutManager(linearLayoutManager);
        taskCommentAdapter = new TaskCommentAdapter(R.layout.item_task_details_comment, commentList, this);
        rvTaskComment.addItemDecoration(new RecyclerViewDivider(this));
        taskCommentAdapter.setEnableLoadMore(true);
        rvTaskComment.setAdapter(taskCommentAdapter);
        taskCommentAdapter.setHeaderView(mHeadView);

    }

    private void initData() {
        taskPresenter = new TaskPresenterImpl(this);
        if (chatEventMessage==null){
            return;
        }
        String taskId = chatEventMessage.getTaskId()==null?"不限时":chatEventMessage.getTaskId()+"";
        SCHttpUtils.postWithUserId()
                .url(HttpApi.TASK_DETAIL)
                .addParams("taskId",taskId+"")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        String data = JSONObject.parseObject(response).getString("data");
                        chatEventMessage = JSONObject.parseObject(data,ChatEventMessage.class);
                        initHeadView();
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
        tbTaskComment.getTitleView().setLayoutParams(layoutParams);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorHomeBtn));
        tbTaskComment.setOnLeftClickListener(this);
//        tbTaskComment.setOnRightClickListener(this);

    }


    @OnClick(R.id.tv_task_details_comment)
    public void onClick() {
        showPop();
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
                String taskId = String.valueOf(chatEventMessage.getTaskId());
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
    public void addSuccess(boolean success) {

    }

    @Override
    public void releaseTaskView(List<ChatEventMessage> list, boolean isSuccess) {

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
