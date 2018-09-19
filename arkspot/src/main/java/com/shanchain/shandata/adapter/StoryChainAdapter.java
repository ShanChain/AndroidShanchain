package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.ui.model.StoryChainBean;
import com.shanchain.shandata.ui.model.StoryChainModel;

import java.util.List;


/**
 * Created by zhoujian on 2017/9/1.
 */

public class StoryChainAdapter extends BaseQuickAdapter<StoryChainModel, BaseViewHolder> {

    public StoryChainAdapter(@LayoutRes int layoutResId, @Nullable List<StoryChainModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryChainModel item) {
        StoryChainBean storyBean = item.getStoryBean();
        int supportCount = storyBean.getSupportCount();
        int commentCount = storyBean.getCommentCount();
        String intro = storyBean.getIntro();

       /* helper.addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more);
        helper.setVisible(R.id.tv_item_story_floors,false);
        helper.setVisible(R.id.lv_item_story,false);
        StoryChainBean storyBean = item.getStoryBean();
        ContactBean characterBean = item.getCharacterBean();
        boolean beFav = item.isBeFav();
        ImageView ivHeadImg = helper.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,characterBean.getHeadImg(),ivHeadImg,0);
        //helper.setText(R.id.tv_item_story_forwarding,)
        String time = DateUtils.formatFriendly(new Date(storyBean.getCreateTime()));
        helper.setText(R.id.tv_item_story_time, time);
        helper.setText(R.id.tv_item_story_chain_content,storyBean.getIntro());*/
    }
}
