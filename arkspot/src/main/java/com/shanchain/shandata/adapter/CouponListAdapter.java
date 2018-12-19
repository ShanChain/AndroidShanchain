package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


public class CouponListAdapter extends CommonAdapter<ChatEventMessage> implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnLayoutViewClickListener, View.OnClickListener {
    private List<ChatEventMessage> list;
    private int[] itemLayoutId;
    private Context context;
    private ChatEventMessage chatEventMessage;
    private OnItemClickListener onItemClickListener;
    private OnClickListener onClickListener;
    private BaseViewHolder holder;

    public CouponListAdapter(Context context, List list, int[] itemLayoutId) {
        super(context, list, itemLayoutId);
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public void setData(BaseViewHolder holder, ChatEventMessage item, int viewType,int position) {
        this.chatEventMessage = item;
        this.holder = holder;
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

    }

    @Override
    public void OnLayoutViewClick(View view) {
        if (onClickListener != null) {
            onClickListener.OnClick(chatEventMessage, view, holder);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);
    }

    public interface OnClickListener {
        void OnClick(ChatEventMessage item, View view, BaseViewHolder holder);

    }
}
