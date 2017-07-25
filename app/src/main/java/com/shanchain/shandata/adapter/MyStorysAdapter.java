package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.MyStoryInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MyStorysAdapter extends BaseCommonAdapter<MyStoryInfo> {
    public MyStorysAdapter(Context context, int layoutId, List<MyStoryInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, MyStoryInfo myStoryInfo, int position) {


        Glide.with(mContext)
                .load(R.mipmap.bing_link_qq_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_my_story_avatar));

        holder.setText(R.id.tv_item_my_story,myStoryInfo.getStory());
        holder.setText(R.id.tv_item_my_story_time,myStoryInfo.getTime());
        holder.setText(R.id.tv_item_my_story_des,myStoryInfo.getDes());
    }
}
