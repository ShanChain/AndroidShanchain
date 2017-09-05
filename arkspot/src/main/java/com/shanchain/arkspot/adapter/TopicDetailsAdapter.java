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
        List<Integer> imgs = new ArrayList();
        imgs.add(R.drawable.photo_bear);
        imgs.add(R.drawable.photo_yue);
        ngiv.setImagesData(imgs);

    }
}
