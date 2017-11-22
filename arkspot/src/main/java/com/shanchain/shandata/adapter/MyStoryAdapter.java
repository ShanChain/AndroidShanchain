package com.shanchain.shandata.adapter;


import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by zhoujian on 2017/11/21.
 */

public class MyStoryAdapter extends BaseMultiItemQuickAdapter<StoryContentBean, BaseViewHolder> {

    private final CharacterInfo mCharacterInfo;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyStoryAdapter(List<StoryContentBean> data) {
        super(data);
        String characterInfoStr = SCCacheUtils.getCacheCharacterInfo();
        mCharacterInfo = JSONObject.parseObject(characterInfoStr, CharacterInfo.class);
        addItemType(StoryInfo.type1, R.layout.item_story_type3);
        addItemType(StoryInfo.type2, R.layout.item_story_type2);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryContentBean item) {
        int itemViewType = helper.getItemViewType();
        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.tv_item_story_floors);
        int supportCount = item.getSupportCount();
        int commentCount = item.getCommentCount();
        long createTime = item.getCreateTime();
        String time = DateUtils.formatFriendly(new Date(createTime));
        helper.setText(R.id.tv_item_story_like, supportCount + "");
        helper.setText(R.id.tv_item_story_comment, commentCount + "");
        helper.setText(R.id.tv_item_story_time, time);
        helper.setText(R.id.tv_item_story_name, mCharacterInfo.getName());
        ImageView ivHeadImg = helper.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext, mCharacterInfo.getHeadImg(), ivHeadImg, 0);

        switch (itemViewType) { //普通动态
            case StoryInfo.type1:
                int transpond = item.getTranspond();
                helper.setText(R.id.tv_item_story_forwarding, transpond + "");
                helper.setVisible(R.id.tv_item_story_floors, false);
                helper.setVisible(R.id.lv_item_story, false);
                helper.setVisible(R.id.tv_item_story_forwarding, true);

                String intro = item.getIntro();
                String content = "";
                List<String> imgArr = new ArrayList<>();
                if (intro.contains("content")) {
                    content = JSONObject.parseObject(intro).getString("content");
                    String imgs = JSONObject.parseObject(intro).getString("imgs");
                    imgArr = JSONObject.parseArray(imgs, String.class);
                } else {
                    content = intro;
                }

                helper.setVisible(R.id.tv_item_story_forwarding, true);
                NineGridImageView nineGridImageView = helper.getView(R.id.ngiv_item_story);
                if (imgArr.size() == 0) {
                    nineGridImageView.setVisibility(View.GONE);
                } else {
                    nineGridImageView.setVisibility(View.VISIBLE);
                    StoryItemNineAdapter adapter = new StoryItemNineAdapter();
                    nineGridImageView.setAdapter(adapter);
                    nineGridImageView.setImagesData(imgArr);
                }
                helper.setText(R.id.tv_item_story_content, content);
                break;
            case StoryInfo.type2:   //小说
                helper.setVisible(R.id.tv_item_story_forwarding, false);
                helper.setText(R.id.tv_item_story_title, item.getTitle());
                String introLong = item.getIntro();
                String replace = introLong.replace(item.getTitle() + "\n", "");
                helper.setText(R.id.tv_item_story_content, replace);
                helper.setVisible(R.id.tv_item_story_forwarding, false);
                break;
        }
    }
}
