package com.shanchain.shandata.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.FriendInfoActivity;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;


public class MultiTaskListAdapter extends CommonAdapter<ChatEventMessage> implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnLayoutViewClickListener, View.OnClickListener {
    private List<ChatEventMessage> list;
    private int[] itemLayoutId;
    private Context context;
    private ChatEventMessage chatEventMessage;
    private OnItemClickListener onItemClickListener;
    private OnClickListener onClickListener;
    private int position;
    private ChatEventMessage itemData;
    private BaseViewHolder holder;
    private String roomID;

    public MultiTaskListAdapter(Context context, List list, int[] itemLayoutId) {
        super(context, list, itemLayoutId);
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }

    public void setList(List<ChatEventMessage> list){
        this.list = list;
    }

    @Override
    public void setData(BaseViewHolder holder, ChatEventMessage item, int viewType, int position) {
        this.chatEventMessage = item;
        this.holder = holder;
        if (holder.itemView.getTag() != null) {
            this.position = (Integer) holder.itemView.getTag();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expiryTime = simpleDateFormat.format(new Date(item.getExpiryTime()));//截止时间
        String createTime = DateUtils.formatFriendly(new Date(item.getCreateTime()));//创建时间

        holder.setTextView(R.id.even_message_last_time, context.getResources().getString(R.string.time_llimit) + expiryTime + "");
        holder.setTextView(R.id.even_message_content, item.getIntro() + "");
        holder.setTextView(R.id.even_message_bounty, "" + item.getBounty());
//        holder.setTextView(R.id.even_message_bounty, "" + item.getPrice());

        holder.isRecyclable();
        int characterId = item.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        if (viewType == 0) {
            holder.setTextView(R.id.tv_item_story_time, createTime + "");
            CharacterInfo characterInfo = com.alibaba.fastjson.JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg() != null ? item.getHeadImg() : characterInfo.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name, item.getName() == null ? context.getResources().getString(R.string.no_nickname) : item.getName());

        } else if (viewType == 1) {
            holder.setTextView(R.id.tv_item_story_time, createTime + "");

            holder.setTextView(R.id.tv_item_story_name, item.getName() + "");
            CharacterInfo characterInfo = com.alibaba.fastjson.JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
            holder.setImageURL(R.id.iv_item_story_avatar, item.getHeadImg() != null ? item.getHeadImg() : characterInfo.getHeadImg());
        } else if (viewType == 2) {
            holder.setTextView(R.id.even_message_location, "" + item.getRoomName());
        }
        setViewOnClick(holder, item, viewType);
        holder.itemView.setTag(position);
    }

    private void setViewOnClick(BaseViewHolder holder, final ChatEventMessage itemData, int viewType) {
        position = holder.getLayoutPosition();
        final int characterId = itemData.getCharacterId();
        final int userId = itemData.getUserId();
        String character = SCCacheUtils.getCacheCharacterId();
        final TextView btnEvenTask = holder.getViewId(R.id.btn_event_task);
        //头像点击事件
        holder.getViewId(R.id.iv_item_story_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultUser userInfo = new DefaultUser(0, itemData.getName(), itemData.getHeadImg());
                userInfo.setHxUserId(itemData.getHxUserName());
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", userInfo);
                LogUtils.d("CacheUserId", SCCacheUtils.getCacheUserId() + "");
                LogUtils.d("CacheCharacter", SCCacheUtils.getCacheCharacterId() + "");
                if (String.valueOf(userId).equals(SCCacheUtils.getCacheUserId()) &&
                        String.valueOf(characterId).equals(SCCacheUtils.getCacheCharacterId())) {
                    return;
                } else {
                    Intent intent = new Intent(context, FriendInfoActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        btnEvenTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog showPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(context, true, 1.0,
                        R.layout.dialog_bottom_wallet_password,
                        new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                Activity activity = (Activity) context;
                roomID = activity.getIntent().getStringExtra("roomId") != null ? activity.getIntent().getStringExtra("roomId") : SCCacheUtils.getCacheRoomId();
                SCHttpUtils.postWithUserId()
                        .url(HttpApi.TASK_DETAIL_RECEIVE)
                        .addParams("roomId", roomID + "")
                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                        .addParams("taskId", itemData.getTaskId() + "")
                        .build()
                        .execute(new SCHttpStringCallBack(context, showPasswordDialog) {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showToast(context, R.string.renwu_lingqub);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                                final String message = com.alibaba.fastjson.JSONObject.parseObject(response).getString("message");
                                if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                    String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
                                    final String HxUserName = com.alibaba.fastjson.JSONObject.parseObject(data).getString("HxUserName");
                                    btnEvenTask.setText(context.getResources().getString(R.string.my_task_receive));
                                    btnEvenTask.setFocusable(false);
                                    btnEvenTask.setOnClickListener(null);
                                    btnEvenTask.setTextColor(context.getResources().getColor(R.color.aurora_bg_edittext_default));
//                                    if (chatEventMessage.getFromUser() != null) {
//                                        IUser user = chatEventMessage.getFromUser();
//                                        String displayName = chatEventMessage.getFromUser().getDisplayName();
//                                        defaultUser = new DefaultUser(user.getId(), displayName, user.getAvatarFilePath());
//                                        defaultUser.setHxUserId(HxUserName);
//                                    }
                                }
                            }
                        });
            }
        });

        switch (viewType) {
            case 0:
                holder.setIsRecyclable(false);
                holder.setLayoutViewOnClick(itemLayoutId, viewType, itemData, this);
            case 1:
                holder.setIsRecyclable(false);
//                holder.setLayoutViewOnClick(itemLayoutId, viewType, itemData, this);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int status = list.get(position).getStatus();
        int characterId = list.get(position).getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        if (status == 5) {
            if (character.equals(String.valueOf(characterId))) {
                return 2;
            }
            return 1;
        } else {
            return 0;
        }
    }


    public BaseViewHolder getHolder() {
        return holder;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
//        onClickListener.OnClick();
    }


    @Override
    public void OnItemClick(View view) {
        /*switch (view.getId()) {
            case R.id.btn_event_task:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_urge:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_cancel:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.btn_event_confirm:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_done:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
            case R.id.item_task_undone:
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(chatEventMessage, view, holder, position);
                }
                break;
        }*/

    }

    @Override
    public void OnLayoutViewClick(View view) {
        if (onClickListener != null) {
            onClickListener.OnClick(chatEventMessage, view, holder, position);
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);

    }


    public interface OnClickListener {

        void OnClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);

    }
}
