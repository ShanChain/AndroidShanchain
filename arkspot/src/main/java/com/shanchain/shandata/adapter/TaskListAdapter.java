package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModel;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.utils.ClickableSpanNoUnderline;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.SCLinkMovementMethod;
import com.shanchain.shandata.widgets.other.AutoHeightListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


public class TaskListAdapter extends BaseMultiItemQuickAdapter<ChatEventMessage, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    private Context context;
    private List<ChatEventMessage> list;
    private ChatEventMessage chatEventMessage;


    public TaskListAdapter(Context context, List<ChatEventMessage> data) {
        super(data);
        this.context = context;
        this.list = data;
        addItemType(ChatEventMessage.type1, R.layout.item_task_type_two);//未领取
        addItemType(ChatEventMessage.type2, R.layout.item_task_type_recevice);//已领取
        addItemType(ChatEventMessage.type3, R.layout.item_task_type_four);//已完成
        addItemType(ChatEventMessage.type4, R.layout.item_task_type_three);//已过期
        addItemType(ChatEventMessage.type5, R.layout.item_task_type_six);//查看任务

    }

    @Override
    protected void convert(BaseViewHolder holder, ChatEventMessage item) {
        this.chatEventMessage = item;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expiryTime = simpleDateFormat.format(item.getExpiryTime());
        holder.setText(R.id.even_message_last_time, "完成时限：" + expiryTime + "");
//        holder.setText(R.id.even_message_location, "来自 东经21°北纬35°");
        holder.setText(R.id.even_message_content, item.getIntro() + "");
        holder.setText(R.id.even_message_bounty, "赏金： " + item.getBounty() + " SEAT");

        switch (holder.getItemViewType()) {
            case ChatEventMessage.type5:
                holder.setText(R.id.btn_event_task, "悯农");
                break;
            case ChatEventMessage.type1:
                holder.setText(R.id.btn_event_task, "领取任务")
                        .setText(R.id.btn_event_task, "领取任务");
                break;
            case ChatEventMessage.type2:
                holder.setText(R.id.btn_event_task, "领取任务");

                break;
        }
        holder.addOnClickListener(R.id.btn_event_task)
                .addOnClickListener(R.id.item_task_undone)
                .addOnClickListener(R.id.item_task_done);


//        holder.setText()
        String characterId = SCCacheUtils.getCacheCharacterId();
        //区分显示任务列表中在自己发布的任务
//        if (TextUtils.equals(String.valueOf(item.getCharacterId()), characterId)) {
//            holder.setText(R.id.btn_event_task, "已发布");
//        } else {
//            switch (item.getStatus()) {
//                case 5:
//                    holder.setText(R.id.btn_event_task, "查看任务");
//                    break;
//            }
//        }



    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getStatus();
    }

}
