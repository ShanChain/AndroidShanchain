package com.shanchain.arkspot.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryModel;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.utils.DateUtils;
import com.shanchain.arkspot.widgets.other.AutoHeightListView;
import com.shanchain.data.common.utils.DensityUtils;
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
    private Drawable mDrawable;

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
        StoryModelBean bean = storyModel.getModelInfo().getBean();
        String characterImg = bean.getCharacterImg();
        String characterName = bean.getCharacterName();
        boolean beFav = bean.isBeFav();

        holder.setText(R.id.tv_item_story_name, characterName);
        ImageView ivHeadImg = holder.getView(R.id.iv_item_story_avatar);

        GlideUtils.load(mContext, characterImg, ivHeadImg, R.mipmap.abs_addanewrole_def_photo_default);
        String time = DateUtils.formatFriendly(new Date(bean.getCreateTime()));
        holder.setText(R.id.tv_item_story_time, time);
        holder.setText(R.id.tv_item_story_comment, bean.getCommendCount() + "");
        holder.setText(R.id.tv_item_story_like, bean.getSupportCount() + "");

        TextView tvLike = holder.getView(R.id.tv_item_story_like);

        if (beFav){
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        }else {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        }



        /*
        Drawable likeSelected = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        likeSelected.setBounds(0, 0, likeSelected.getMinimumWidth(), likeSelected.getMinimumHeight());
        tvLike.setCompoundDrawables(likeSelected,null, null,  null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));
        */
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        tvLike.setCompoundDrawables(mDrawable,null,null,null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));


        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);


        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                String content = "";
                List<String> imgs = new ArrayList<>();
                String intro = bean.getIntro();
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
                holder.setText(R.id.tv_item_story_forwarding, bean.getTranspond() + "");
                break;
            case StoryInfo.type2:
                holder.setText(R.id.tv_item_story_content, bean.getIntro());
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                break;
            case StoryInfo.type3:
                String topicImg = bean.getBackground();
                String title = bean.getTitle();
                holder.setText(R.id.tv_story_topic_title,"#"+title+"#");
                if (TextUtils.isEmpty(topicImg)) {
                    holder.setVisible(R.id.iv_item_story_img, false);
                } else {
                    holder.setVisible(R.id.iv_item_story_img, true);
                    GlideUtils.load(mContext, topicImg, (ImageView) holder.getView(R.id.iv_item_story_img), R.mipmap.abs_addanewrole_def_photo_default);
                }
                holder.setVisible(R.id.tv_item_story_forwarding, false);
                holder.setText(R.id.tv_item_story_intro, bean.getIntro());
                holder.setVisible(R.id.ll_topic_function,false);
                break;
        }
    }
}
