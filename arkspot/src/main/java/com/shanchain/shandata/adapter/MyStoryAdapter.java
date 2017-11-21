package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.StoryContentBean;

import java.util.List;


/**
 * Created by zhoujian on 2017/11/21.
 */

public class MyStoryAdapter extends BaseQuickAdapter<StoryContentBean,BaseViewHolder> {

    public MyStoryAdapter(@LayoutRes int layoutResId, @Nullable List<StoryContentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryContentBean item) {
        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.tv_item_story_floors);

        int supportCount = item.getSupportCount();
        int commentCount = item.getCommentCount();
        String intro = item.getIntro();
        String content = "";
        if (intro.contains("content")){
            content = JSONObject.parseObject(intro).getString("content");
        }else {
            content = intro;
        }

       // item.get

    }
}
