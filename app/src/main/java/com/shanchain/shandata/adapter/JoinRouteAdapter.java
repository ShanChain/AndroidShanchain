package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.StoryPagerInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class JoinRouteAdapter extends BaseCommonAdapter<StoryPagerInfo> {

    public JoinRouteAdapter(Context context, int layoutId, List<StoryPagerInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, StoryPagerInfo storyPagerInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_shenzhen)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_join_route_author_avatar));
    }
}
