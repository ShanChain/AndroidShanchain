package com.shanchain.arkspot.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.global.Constants;
import com.shanchain.arkspot.ui.model.MsgInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/12.
 */

public class ChatRoomMsgAdapter extends RecyclerView.Adapter<ChatRoomMsgAdapter.ViewHolder> {

    private List<MsgInfo> mMsgInfoList;

    public ChatRoomMsgAdapter(List<MsgInfo> msgInfoList) {
        mMsgInfoList = msgInfoList;
    }


    @Override
    public int getItemCount() {
        return mMsgInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgInfo msgInfo = mMsgInfoList.get(position);
        EMMessage emMessage = msgInfo.getEMMessage();
        //int msgAttr = emMessage.getIntAttribute("msgAttr", 0);
        return emMessage.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_text_receive, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_text_send, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MsgInfo msgInfo = mMsgInfoList.get(position);
        holder.bindData(msgInfo, position);

        //添加一些控件的点击事件
        if (holder.tvSendContent != null) {
            holder.tvSendContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onBubbleClick(v, position);
                }
            });

            holder.tvSendContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onBubbleLongClick(v, position);
                    return true;
                }
            });
        }

        if (holder.tvReceiveContent != null) {
            holder.tvReceiveContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onBubbleClick(v, position);
                }
            });

            holder.tvReceiveContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onBubbleLongClick(v, position);
                    return true;
                }
            });
        }

        if (holder.ivReceiverAvatar != null) {
            holder.ivReceiverAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarClickListener.onAvatarClick(v,position);
                }
            });
        }

        if (holder.ivSendAvatar != null) {
            holder.ivSendAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarClickListener.onAvatarClick(v,position);
                }
            });
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSendTime;
        TextView tvReceiveTime;
        TextView tvSendContent;
        TextView tvReceiveContent;
        TextView tvSendRole;
        TextView tvReceiveRole;
        ImageView ivSendAvatar;
        ImageView ivReceiverAvatar;
        TextView tvSendNick;
        TextView tvReceiverNick;
        //群权限
        TextView tvReceiverPer;

        public ViewHolder(View itemView) {
            super(itemView);

            tvSendTime = (TextView) itemView.findViewById(R.id.tv_item_msg_send_time);
            tvReceiveTime = (TextView) itemView.findViewById(R.id.tv_item_msg_receive_time);
            tvSendContent = (TextView) itemView.findViewById(R.id.tv_item_msg_send_content);
            tvReceiveContent = (TextView) itemView.findViewById(R.id.tv_item_msg_receive_content);
            tvSendRole = (TextView) itemView.findViewById(R.id.tv_item_msg_send_role);
            tvReceiveRole = (TextView) itemView.findViewById(R.id.tv_item_msg_receive_role);
            ivSendAvatar = (ImageView) itemView.findViewById(R.id.iv_item_msg_send_avatar);
            ivReceiverAvatar = (ImageView) itemView.findViewById(R.id.iv_item_msg_receive_avatar);
            tvSendNick = (TextView) itemView.findViewById(R.id.tv_item_msg_send_name);
            tvReceiverNick = (TextView) itemView.findViewById(R.id.tv_item_msg_receive_name);
            tvReceiverPer = (TextView) itemView.findViewById(R.id.tv_item_msg_receive_permissions);

        }

        public void bindData(MsgInfo msgInfo, int position) {
            EMMessage emMessage = msgInfo.getEMMessage();
            int msgAttribute = emMessage.getIntAttribute("msgAttr", 0);

            if (msgAttribute == Constants.attrDefault ) {
                //闲聊(本人说话)
                if (tvReceiveRole!=null){
                    tvReceiveRole.setVisibility(View.VISIBLE);
                    tvReceiveContent.setBackgroundResource(R.drawable.selector_bg_msg_receive_normal);
                    tvReceiveContent.setTextColor(Color.parseColor("#ffffff"));
                }
                if (tvSendRole != null) {
                    tvSendRole.setVisibility(View.VISIBLE);
                    tvSendContent.setBackgroundResource(R.drawable.selector_bg_msg_send_normal);
                }
            }else if (msgAttribute == Constants.attrAgainst){
                //对戏(角色说话)
                if (tvReceiveRole!=null){
                    tvReceiveRole.setVisibility(View.GONE);
                    tvReceiveContent.setBackgroundResource(R.drawable.selector_bg_msg_receive_white);
                    tvReceiveContent.setTextColor(Color.parseColor("#666666"));
                }
                if (tvSendRole != null) {
                    tvSendRole.setVisibility(View.GONE);
                    tvSendContent.setBackgroundResource(R.drawable.selector_bg_msg_send_theme);
                }
            }


            long msgTime = emMessage.getMsgTime();
            String timestampString = DateUtils.getTimestampString(new Date(msgTime));
            EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
            String msg = body.getMessage();
            if (emMessage.direct() == EMMessage.Direct.SEND) {
                //发送
                tvSendContent.setText(msg);
                tvSendTime.setText(timestampString);

                if (position == 0) {
                    tvSendTime.setVisibility(View.VISIBLE);
                } else {
                    long msgSendTimePre = mMsgInfoList.get(position - 1).getEMMessage().getMsgTime();
                    if (DateUtils.isCloseEnough(msgSendTimePre, msgTime)) {
                        tvSendTime.setVisibility(View.GONE);
                    } else {
                        tvSendTime.setVisibility(View.VISIBLE);
                    }
                }


            } else {
                //接收
                tvReceiveContent.setText(msg);
                tvReceiveTime.setText(timestampString);

                if (position == 0) {
                    tvReceiveTime.setVisibility(View.VISIBLE);
                } else {
                    long msgSendTimePre = mMsgInfoList.get(position - 1).getEMMessage().getMsgTime();
                    if (DateUtils.isCloseEnough(msgSendTimePre, msgTime)) {
                        tvReceiveTime.setVisibility(View.GONE);
                    } else {
                        tvReceiveTime.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
    }

    private OnBubbleClickListener clickListener;

    public interface OnBubbleClickListener {
        void onBubbleClick(View v, int position);
    }

    private OnBubbleLongClickListener longClickListener;

    public interface OnBubbleLongClickListener {
        void onBubbleLongClick(View v, int position);
    }

    private OnAvatarClickListener avatarClickListener;
    public interface OnAvatarClickListener {
        void onAvatarClick(View v,int position);
    }

    /**
     * 描述：消息气泡的点击事件
     */
    public void setOnBubbleClickListener(OnBubbleClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 描述：消息气泡的长按点击事件
     */
    public void setOnBubbleLongClickListener(OnBubbleLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    /**
     * 描述：头像的点击事件
     */
    public void setOnAvatarClickListener(OnAvatarClickListener avatarClickListener){
        this.avatarClickListener = avatarClickListener;
    }

}
