package com.shanchain.arkspot.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;


public class CurrentAdapter extends BaseMultiItemQuickAdapter<StoryBeanModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CurrentAdapter(List<StoryBeanModel> data) {
        super(data);
        addItemType(StoryInfo.type1, R.layout.item_story_type3);
        addItemType(StoryInfo.type2, R.layout.item_story_type2);
        addItemType(StoryInfo.type3, R.layout.item_story_type4);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoryBeanModel item) {
        StoryModel storyModel = item.getStoryModel();
        holder.setText(R.id.tv_item_story_name,storyModel.getModelInfo().getBean().getCharacterId()+"");
        ImageView ivHeadImg = holder.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,storyModel.getModelInfo().getBean().getImg(),ivHeadImg,R.drawable.photo_yue);
        String time = DateUtils.getStandardDate(storyModel.getModelInfo().getBean().getCreateTime() + "");
        long currentTimeMillis = System.currentTimeMillis();
        long ctime = currentTimeMillis - 1000000;
        String ttime = DateUtils.getStandardDate(ctime + "");
        holder.setText(R.id.tv_item_story_time,ttime);
        holder.setText(R.id.tv_item_story_comment,storyModel.getModelInfo().getBean().getCommendCount()+"");
        holder.setText(R.id.tv_item_story_like,storyModel.getModelInfo().getBean().getSupportCount()+"");
        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                holder.setVisible(R.id.tv_item_story_forwarding,true);
                NineGridImageView nineGridImageView = holder.getView(R.id.ngiv_item_story);
                nineGridImageView.setVisibility(View.GONE);
                holder.setText(R.id.tv_item_story_content,storyModel.getModelInfo().getBean().getIntro());
                break;
            case StoryInfo.type2:
                holder.setText(R.id.tv_item_story_content,storyModel.getModelInfo().getBean().getIntro());
                holder.setVisible(R.id.tv_item_story_forwarding,false);
                break;
            case StoryInfo.type3:
                String topicImg = storyModel.getModelInfo().getBean().getImg();
                if (TextUtils.isEmpty(topicImg)){
                    holder.setVisible(R.id.iv_item_story_img,false);
                }else {
                    holder.setVisible(R.id.iv_item_story_img,false);
                    GlideUtils.load(mContext,storyModel.getModelInfo().getBean().getImg(),(ImageView) holder.getView(R.id.iv_item_story_img),R.drawable.photo_public);
                }
                holder.setVisible(R.id.tv_item_story_forwarding,false);
                break;
        }
    }
}
