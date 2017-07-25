package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.CommentsInfo;
import com.shanchain.shandata.mvp.view.activity.PersonalHomePagerActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class CommentsAdapter extends BaseCommonAdapter<CommentsInfo> {


    public CommentsAdapter(Context context, int layoutId, List<CommentsInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(final ViewHolder holder, CommentsInfo commentsInfo, int position) {
        final int likes = commentsInfo.getLike();
        holder.setText(R.id.tv_item_comments_counts,commentsInfo.getLike()+"");
        holder.setText(R.id.tv_item_comments_time,commentsInfo.getTime());
        Glide.with(mContext)
                .load(R.mipmap.popular_image_story_default)
                .transform(new GlideCircleTransform(mContext)).
                into((ImageView) holder.getView(R.id.iv_item_comments_avatar));

        holder.setOnClickListener(R.id.iv_item_comments_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvLike = holder.getView(R.id.tv_item_comments_counts);
                if (Integer.parseInt(tvLike.getText().toString().trim()) == likes) {
                    //点赞
                    holder.setImageResource(R.id.iv_item_comments_like, R.mipmap.text_btn_thumbsup_selected);
                    holder.setText(R.id.tv_item_comments_counts, likes + 1 + "");
                } else {
                    //取消赞
                    holder.setImageResource(R.id.iv_item_comments_like, R.mipmap.text_btn_thumbsup_default);
                    holder.setText(R.id.tv_item_comments_counts, likes + "");
                }
            }
        });


        holder.setOnClickListener(R.id.iv_item_comments_avatar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalHomePagerActivity.class);
                mContext.startActivity(intent);
            }
        });

    }
}
