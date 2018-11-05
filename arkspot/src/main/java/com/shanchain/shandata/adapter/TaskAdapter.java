package com.shanchain.shandata.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


public class TaskAdapter extends BaseMultiItemQuickAdapter<ChatEventMessage, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    private Drawable mDrawable;
    private final String mCacheSpaceId;

    public TaskAdapter(List<ChatEventMessage> data) {
        super(data);
        addItemType(ChatEventMessage.type1, R.layout.item_task_type1);
        addItemType(ChatEventMessage.type2, R.layout.item_task_type2);
        addItemType(ChatEventMessage.type2, R.layout.item_task_type3);
        mCacheSpaceId = SCCacheUtils.getCacheSpaceId();
    }

    @Override
    protected void convert(BaseViewHolder holder, ChatEventMessage item) {

//        holder.setText(R.id.iv_item_story_avatar,item.getiUser().getAvatarFilePath());//加载头像
        holder.setText(R.id.tv_item_story_name,item.getiUser().getDisplayName()+"  发布的：");//加载名称
        holder.setText(R.id.even_message_bounty,"赏金："+item.getBounty()+" SEAT");//加载赏金
        holder.setText(R.id.even_message_content,item.getIntro()); //加载任务内容
        holder.setText(R.id.even_message_last_time,"完成时限："+item.getLimitedTime());//加载完成时限

        switch (holder.getItemViewType()){
            case ChatEventMessage.type1:
                holder.addOnClickListener(R.id.btn_event_task);//设置领取任务按钮监听
                break;
        }


    }

}
