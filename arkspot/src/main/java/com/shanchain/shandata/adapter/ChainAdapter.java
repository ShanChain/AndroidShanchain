package com.shanchain.shandata.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.StoryChainBean;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/11/23.
 */

public class ChainAdapter extends BaseQuickAdapter<StoryChainModel,BaseViewHolder> {
    private Drawable mDrawable;
    public ChainAdapter(@LayoutRes int layoutResId, @Nullable List<StoryChainModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryChainModel item) {

        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        StoryChainBean storyBean = item.getStoryBean();
        ContactBean characterBean = item.getCharacterBean();
        boolean beFav = item.isBeFav();
        long createTime = storyBean.getCreateTime();
        String time = DateUtils.formatFriendly(new Date(createTime));
        TextView tvLike = helper.getView(R.id.tv_item_story_like);
        helper.setText(R.id.tv_item_story_time, time);
        helper.setText(R.id.tv_item_story_comment, storyBean.getCommentCount() + "");
        helper.setText(R.id.tv_item_story_name,characterBean.getName());
        if (beFav){
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        }else {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        }
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        tvLike.setCompoundDrawables(mDrawable,null,null,null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));

        String intro = storyBean.getIntro();
        List<String> imgs = new ArrayList<>();
        String content = "";
        if (intro.contains("content")){
            ReleaseContentInfo contentInfo = JSONObject.parseObject(intro,ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgs = contentInfo.getImgs();
        }else {
            content = intro;
        }
        ImageView ivAvatar = helper.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,characterBean.getHeadImg(),ivAvatar,0);
        helper.setText(R.id.tv_item_story_chain_content,content);


        NineGridImageView nineGridImageView = helper.getView(R.id.ngiv_story_chain);

        if (imgs.size() == 0) {
            nineGridImageView.setVisibility(View.GONE);
        } else {
            nineGridImageView.setVisibility(View.VISIBLE);
            StoryItemNineAdapter adapter = new StoryItemNineAdapter();
            nineGridImageView.setAdapter(adapter);
            nineGridImageView.setImagesData(imgs);
        }


    }
}
