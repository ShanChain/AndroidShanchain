package com.shanchain.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.utils.ToastUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/23.
 */

public class HotAdapter extends BaseCommonAdapter<PublisherInfo> {



    public HotAdapter(Context context, int layoutId, List<PublisherInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindDatas(final ViewHolder holder, PublisherInfo publisherInfo, int position) {

        String name = publisherInfo.getName();
        String time = publisherInfo.getTime();
        final int likes = publisherInfo.getLikes();
        List<String> images = publisherInfo.getImages();
        int comments = publisherInfo.getComments();
        int type = publisherInfo.getType();

        holder.setText(R.id.tv_publisher_name, name);
        holder.setText(R.id.tv_publisher_time, time);
        holder.setText(R.id.tv_like, likes + "");
        holder.setText(R.id.tv_comments, comments + "");

        switch (type){
            case 1:
                //普通条目

                RecyclerView mRvImages = holder.getView(R.id.rv_images);
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                mRvImages.setLayoutManager(layoutManager);
                ImageAdapter adapter = new ImageAdapter(mContext, R.layout.item_images, images);
                mRvImages.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        ToastUtils.showToast(mContext, "点击了第" + position + "张图片");
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });

                break;
            case 2:
                //挑战条目

                break;
            case 3:
                //故事条目


                break;

        }
        //喜欢图标的点击事件
        holder.setOnClickListener(R.id.iv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.setImageResource(R.id.iv_like,R.mipmap.icon_btn_like_selected);
                holder.setText(R.id.tv_like,likes+1+"");
            }
        });
        //点赞量文字的点击事件
        holder.setOnClickListener(R.id.tv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setImageResource(R.id.iv_like,R.mipmap.icon_btn_like_selected);
                holder.setText(R.id.tv_like,likes+1+"");
            }
        });
        //评论图标的点击事件
        holder.setOnClickListener(R.id.iv_comments, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //评论文字的点击事件
        holder.setOnClickListener(R.id.tv_comments, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //更多图标的点击事件
        holder.setOnClickListener(R.id.iv_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

}
