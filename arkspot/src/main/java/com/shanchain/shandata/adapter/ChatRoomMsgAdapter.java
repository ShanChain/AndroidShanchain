package com.shanchain.shandata.adapter;

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
import com.shanchain.shandata.R;
import com.shanchain.data.common.base.Constants;
import com.shanchain.shandata.ui.model.MsgInfo;
import com.shanchain.data.common.utils.GlideUtils;

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
        int msgAttr = emMessage.getIntAttribute(Constants.MSG_ATTR, 0);
        if (msgAttr == 3) {
            return 3;
        } else if (emMessage.direct() == EMMessage.Direct.RECEIVE) {
            return 0;
        } else if (emMessage.direct() == EMMessage.Direct.SEND) {
            return 1;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_text_receive, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_text_send, parent, false);
        } else if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_scene, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MsgInfo msgInfo = mMsgInfoList.get(position);
        holder.bindData(msgInfo, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });

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
                    avatarClickListener.onAvatarClick(v, position);
                }
            });
        }

        if (holder.ivSendAvatar != null) {
            holder.ivSendAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarClickListener.onAvatarClick(v, position);
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
        //场景或者公告的标题
        TextView tvItemMsgSceneTitle;
        //场景或者公告的内容
        TextView getTvItemMsgSceneContent;

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

            //公告item
            tvItemMsgSceneTitle = (TextView) itemView.findViewById(R.id.tv_item_msg_title);
            getTvItemMsgSceneContent = (TextView) itemView.findViewById(R.id.tv_item_msg_scene_content);
        }

        public void bindData(MsgInfo msgInfo, int position) {
            EMMessage emMessage = msgInfo.getEMMessage();
            EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
            String msg = body.getMessage();
            int msgAttribute = emMessage.getIntAttribute(Constants.MSG_ATTR, 0);
            String nick = emMessage.getStringAttribute(Constants.MSG_NICK_NAME, emMessage.getFrom());
            if (msgAttribute == Constants.ATTR_SCENE) {

                //场景
                if (tvItemMsgSceneTitle != null){
                    tvItemMsgSceneTitle.setText("场景");
                    getTvItemMsgSceneContent.setText(nick + " " + msg);
                }

            } else if (msgAttribute == Constants.ATTR_DEFAULT) {
                //闲聊(本人说话)
                if (tvReceiveRole != null) {
                    tvReceiveRole.setVisibility(View.VISIBLE);
                    tvReceiveContent.setBackgroundResource(R.drawable.selector_bg_msg_receive_normal);
                    tvReceiveContent.setTextColor(Color.parseColor("#ffffff"));
                }
                if (tvSendRole != null) {
                    tvSendRole.setVisibility(View.VISIBLE);
                    tvSendContent.setBackgroundResource(R.drawable.selector_bg_msg_send_normal);
                }
                setMsgContent(position, emMessage, msg);
            } else if (msgAttribute == Constants.ATTR_AGAINST) {
                //对戏(角色说话)
                if (tvReceiveRole != null) {
                    tvReceiveRole.setVisibility(View.GONE);
                    tvReceiveContent.setBackgroundResource(R.drawable.selector_bg_msg_receive_white);
                    tvReceiveContent.setTextColor(Color.parseColor("#666666"));
                }
                if (tvSendRole != null) {
                    tvSendRole.setVisibility(View.GONE);
                    tvSendContent.setBackgroundResource(R.drawable.selector_bg_msg_send_theme);
                }
                setMsgContent(position, emMessage, msg);
            }

        }

        /**
         *  描述：设置消息内容
         */
        private void setMsgContent(int position, EMMessage emMessage, String msg) {
            long msgTime = emMessage.getMsgTime();
            String timestampString = DateUtils.getTimestampString(new Date(msgTime));
            String headImg = emMessage.getStringAttribute(Constants.MSG_HEAD_IMG, "http://pic.xoyo.com/bbs/2011/05/05/11050521189a7010baf80224d6.jpg");
            String nickName = emMessage.getStringAttribute(Constants.MSG_NICK_NAME, emMessage.getFrom());
            if (emMessage.direct() == EMMessage.Direct.SEND) {
                //发送
                tvSendContent.setText(msg);
                tvSendTime.setText(timestampString);
                tvSendNick.setText(nickName);
                GlideUtils.load(ivSendAvatar.getContext(),headImg,ivSendAvatar,0);
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
                tvReceiverNick.setText(nickName);
                GlideUtils.load(ivReceiverAvatar.getContext(),headImg,ivReceiverAvatar,0);
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
        void onAvatarClick(View v, int position);
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
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
    public void setOnAvatarClickListener(OnAvatarClickListener avatarClickListener) {
        this.avatarClickListener = avatarClickListener;
    }

}
