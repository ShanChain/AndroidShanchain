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
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/11/25.
 */

public class MyStoryAdapter extends BaseQuickAdapter<StoryDetailInfo,BaseViewHolder> {

    private final CharacterInfo mCharacterInfo;
    private Drawable mDrawable;
    public MyStoryAdapter(@LayoutRes int layoutResId, @Nullable List<StoryDetailInfo> data) {
        super(layoutResId, data);
        String cacheCharacterInfo = SCCacheUtils.getCacheCharacterInfo();
        mCharacterInfo = JSONObject.parseObject(cacheCharacterInfo, CharacterInfo.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryDetailInfo item) {
        String headImg = mCharacterInfo.getHeadImg();
        String name = mCharacterInfo.getName();

        boolean fav = item.isFav();
        String content = "";
        List<String> imgs = new ArrayList<>();
        String intro = item.getIntro();
        if (intro.contains("content")){
            ReleaseContentInfo contentInfo = JSONObject.parseObject(intro,ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgs = contentInfo.getImgs();
        }else {
            content = intro;
        }

        TextView tvLike = helper.getView(R.id.tv_item_story_like);

        if (fav){
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
        }else {
            mDrawable = mContext.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
        }

        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        tvLike.setCompoundDrawables(mDrawable,null,null,null);
        tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mContext, 10));
        tvLike.setText(item.getSupportCount()+"");
        helper.setText(R.id.tv_item_story_comment,item.getCommentCount() + "");
        String time = DateUtils.formatFriendly(new Date(item.getCreateTime()));
        helper.setText(R.id.tv_item_story_time, time);
        ImageView ivHeadImg = helper.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,headImg,ivHeadImg,0);
        helper.setText(R.id.tv_item_story_name,name);

        NineGridImageView nineGridImageView = helper.getView(R.id.ngiv_item_story);

        if (imgs.size() == 0) {
            nineGridImageView.setVisibility(View.GONE);
        } else {
            nineGridImageView.setVisibility(View.VISIBLE);
            StoryItemNineAdapter adapter = new StoryItemNineAdapter();
            nineGridImageView.setAdapter(adapter);
            nineGridImageView.setImagesData(imgs);
        }

        helper.setText(R.id.tv_item_story_content, content);
        helper.setVisible(R.id.tv_item_story_forwarding, true);
        helper.setText(R.id.tv_item_story_forwarding, item.getTranspond() + "");
        helper.setVisible(R.id.lv_item_story,false);
        helper.setVisible(R.id.tv_item_story_floors,false);


        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);


    }
}
