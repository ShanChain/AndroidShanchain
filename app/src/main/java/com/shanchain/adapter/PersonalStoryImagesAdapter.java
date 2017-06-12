package com.shanchain.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.StoryInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/8.
 */

public class PersonalStoryImagesAdapter extends BaseCommonAdapter<StoryInfo> {
    public PersonalStoryImagesAdapter(Context context, int layoutId, List<StoryInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, StoryInfo storyInfo, int position) {

        Glide.with(mContext)
                .load(R.drawable.photo)
                .into((ImageView) holder.getView(R.id.iv_item_story_image));

        holder.setText(R.id.tv_item_story_des,storyInfo.getVoteCounts()+"   人投票");
    }
}
