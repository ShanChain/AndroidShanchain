package com.shanchain.arkspot.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.ResponseCharacterBrief;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.arkspot.widgets.other.AutoHeightListView;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CurrentAdapter extends BaseMultiItemQuickAdapter<StoryBeanModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    private Gson gson;

    public CurrentAdapter(List<StoryBeanModel> data) {
        super(data);
        addItemType(StoryBeanModel.type1, R.layout.item_story_type3);
        addItemType(StoryBeanModel.type2, R.layout.item_story_type2);
        addItemType(StoryBeanModel.type3, R.layout.item_story_type4);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder holder, StoryBeanModel item) {
        StoryModel storyModel = item.getStoryModel();
        ResponseCharacterBrief characterBrief = storyModel.getModelInfo().getCharacterBrief();

        String headUrl = "";
        String name = "没有名字";
        if (characterBrief != null){
            headUrl = characterBrief.getHeadImg();
            name = characterBrief.getName();
        }


        holder.setText(R.id.tv_item_story_name, name);
        ImageView ivHeadImg = holder.getView(R.id.iv_item_story_avatar);

        GlideUtils.load(mContext, headUrl, ivHeadImg, R.mipmap.abs_addanewrole_def_photo_default);
        String time = DateUtils.formatFriendly(new Date(storyModel.getModelInfo().getBean().getCreateTime()));
        holder.setText(R.id.tv_item_story_time, time);
        holder.setText(R.id.tv_item_story_comment, storyModel.getModelInfo().getBean().getCommendCount() + "");
        holder.setText(R.id.tv_item_story_like, storyModel.getModelInfo().getBean().getSupportCount() + "");
        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                String content = "";
                List<String> imgs = new ArrayList<>();
                String intro = storyModel.getModelInfo().getBean().getIntro();
                LogUtils.d("内容信息 = " + intro);
                if (intro.contains("content")) {
                    ReleaseContentInfo contentInfo = gson.fromJson(intro, ReleaseContentInfo.class);
                    content = contentInfo.getContent();
                    imgs = contentInfo.getImgs();
                } else {
                    content = intro;
                }

                List<StoryModelInfo> storyChain = storyModel.getStoryChain();
                if (storyChain != null) {
                    LogUtils.d("故事连长度 = " + storyChain.size());
                }
                AutoHeightListView lv = holder.getView(R.id.lv_item_story);
                if (storyChain == null || storyChain.size() == 0) {
                    LogUtils.d("gone==========");
                    holder.setVisible(R.id.tv_item_story_floors, false);
                    holder.setVisible(R.id.lv_item_story, false);
                } else {
                    LogUtils.d("visible==========");
                    holder.setVisible(R.id.tv_item_story_floors, true);
                    holder.setVisible(R.id.lv_item_story, true);
                    StoryItemFloorsAdapter floorsAdapter = new StoryItemFloorsAdapter(storyChain);
                    lv.setAdapter(floorsAdapter);
                }


                holder.setVisible(R.id.tv_item_story_forwarding, true);
                NineGridImageView nineGridImageView = holder.getView(R.id.ngiv_item_story);

                if (imgs.size() == 0) {
                    nineGridImageView.setVisibility(View.GONE);
                } else {
                    nineGridImageView.setVisibility(View.VISIBLE);
                    StoryItemNineAdapter adapter = new StoryItemNineAdapter();
                    nineGridImageView.setAdapter(adapter);
                    nineGridImageView.setImagesData(imgs);
                }

                holder.setText(R.id.tv_item_story_content, content);
                holder.setText(R.id.tv_item_story_forwarding, storyModel.getModelInfo().getBean().getTranspond() + "");
                break;
            case StoryInfo.type2:
                holder.setText(R.id.tv_item_story_content, storyModel.getModelInfo().getBean().getIntro());
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                break;
            case StoryInfo.type3:
                String topicImg = storyModel.getModelInfo().getBean().getImg();
                if (TextUtils.isEmpty(topicImg)) {
                    holder.setVisible(R.id.iv_item_story_img, false);
                } else {
                    holder.setVisible(R.id.iv_item_story_img, false);
                    GlideUtils.load(mContext, storyModel.getModelInfo().getBean().getImg(), (ImageView) holder.getView(R.id.iv_item_story_img), R.mipmap.abs_addanewrole_def_photo_default);
                }
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                break;
        }
    }
}
