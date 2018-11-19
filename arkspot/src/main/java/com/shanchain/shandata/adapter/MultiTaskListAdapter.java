package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.utils.DateUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


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

    public MultiTaskListAdapter(Context context, List list, int[] itemLayoutId) {
        super(context, list, itemLayoutId);
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public void setData(BaseViewHolder holder, ChatEventMessage item, int viewType,int position) {
        this.chatEventMessage = item;
        this.holder = holder;
        if (holder.itemView.getTag() != null) {
            this.position = (Integer) holder.itemView.getTag();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expiryTime = simpleDateFormat.format(new Date(item.getExpiryTime()));//截止时间
        String createTime = DateUtils.formatFriendly(new Date(item.getCreateTime()));//创建时间

        holder.setTextView(R.id.even_message_last_time, "完成时限：" + expiryTime + "");
        holder.setTextView(R.id.even_message_content, item.getIntro() + "");
        holder.setTextView(R.id.even_message_bounty, "赏金： " + item.getBounty() + " SEAT");

        holder.isRecyclable();
        int characterId = item.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        if (viewType==0){
            holder.setTextView(R.id.tv_item_story_time,createTime+"");
            CharacterInfo characterInfo = com.alibaba.fastjson.JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(),CharacterInfo.class);
            holder.setImageURL(R.id.iv_item_story_avatar,item.getHeadImg()!=null?item.getHeadImg():characterInfo.getHeadImg());
            holder.setTextView(R.id.tv_item_story_name,item.getName()==null?"无昵称":item.getName());

        }else if(viewType==1){
            holder.setTextView(R.id.tv_item_story_time,createTime+"");

            holder.setTextView(R.id.tv_item_story_name,item.getName()+"");
            CharacterInfo characterInfo = com.alibaba.fastjson.JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(),CharacterInfo.class);
            holder.setImageURL(R.id.iv_item_story_avatar,item.getHeadImg()!=null?item.getHeadImg():characterInfo.getHeadImg());
        }else if(viewType==2){
            holder.setTextView(R.id.even_message_location,""+item.getRoomName());
        }
        setViewOnClick(holder,item,viewType);
        holder.itemView.setTag(position);
    }

    private void setViewOnClick(BaseViewHolder holder, final ChatEventMessage itemData, int viewType) {
        position = holder.getLayoutPosition();
        int characterId = itemData.getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        holder.getViewId(R.id.btn_event_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(context,"点击了查看按钮"+itemData.getIntro());
                Intent intent = new Intent(context,TaskDetailActivity.class);
                intent.putExtra("chatEventMessage",itemData);
                context.startActivity(intent);
            }
        });

        switch (viewType) {
            case 0:
                holder.setIsRecyclable(false);
                holder.setLayoutViewOnClick(itemLayoutId, viewType, itemData, this);
            case 1:
                holder.setIsRecyclable(false);
                holder.setLayoutViewOnClick(itemLayoutId, viewType, itemData, this);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        int status = list.get(position).getStatus();
        int characterId = list.get(position).getCharacterId();
        String character = SCCacheUtils.getCacheCharacterId();
        if (status == 5) {
            if (character.equals(String.valueOf(characterId))){
                return 2;
            }
            return 1;
        }else {
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
            onClickListener.OnClick(chatEventMessage, view, holder,position);
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder,int position);

    }


    public interface OnClickListener {

        void OnClick(ChatEventMessage item, View view, BaseViewHolder holder,int position);

    }
}
