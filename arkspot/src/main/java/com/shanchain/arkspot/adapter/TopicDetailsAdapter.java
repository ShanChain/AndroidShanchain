package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/1.
 */

public class TopicDetailsAdapter extends BaseQuickAdapter<StoryInfo,BaseViewHolder> {

    public TopicDetailsAdapter(@LayoutRes int layoutResId, @Nullable List<StoryInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryInfo item) {

        helper.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        helper.setText(R.id.tv_item_story_time,item.getTime());

        NineGridImageView ngiv = helper.getView(R.id.ngiv_item_story);
        StoryItemNineAdapter itemNineAdapter = new StoryItemNineAdapter();
        ngiv.setAdapter(itemNineAdapter);
        List<String> imgs = new ArrayList();
        imgs.add("https://s-media-cache-ak0.pinimg.com/736x/41/47/ff/4147ff1464ce4fd64d88e7f3f345d6d4.jpg");
        imgs.add("https://s-media-cache-ak0.pinimg.com/236x/0b/fd/9a/0bfd9af8525ec09263e0ffda06f85c84.jpg");
        ngiv.setImagesData(imgs);

    }
}
