package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.PublisherInfo;
import com.shanchain.shandata.mvp.view.activity.DetailsActivity;
import com.shanchain.shandata.mvp.view.activity.PersonalHomePagerActivity;
import com.shanchain.shandata.mvp.view.activity.ReportActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class HotAdapter extends BaseCommonAdapter<PublisherInfo> {



    public HotAdapter(Context context, int layoutId, List<PublisherInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindData(final ViewHolder holder, final PublisherInfo publisherInfo, final int position) {

        String name = publisherInfo.getName();
        String time = publisherInfo.getTime();
        final int likes = publisherInfo.getLikes();
        List<String> images = publisherInfo.getImages();
        final int comments = publisherInfo.getComments();
        int type = publisherInfo.getType();
        Glide.with(mContext).load(R.mipmap.logo)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_publisher_avatar));
        holder.setText(R.id.tv_publisher_name, name);
        holder.setText(R.id.tv_publisher_time, time);
        holder.setText(R.id.tv_like, likes + "");
        holder.setText(R.id.tv_comments, comments + "");
        holder.setText(R.id.tv_des, publisherInfo.getDes());

        NineGridImageView nineGridImageView = holder.getView(R.id.nine_grid_layout);
        NineGridImageViewAdapter nineGridAdapter = new NineGridAdapter();
        nineGridImageView.setAdapter(nineGridAdapter);
        nineGridImageView.setImagesData(images);

        switch (type) {
            case 1:
                holder.setVisible(R.id.iv_item_hot_vote,false);
                holder.setVisible(R.id.tv_item_hot_vote,false);
        //        holder.setVisible(R.id.nine_grid_layout,true);
        //        holder.setVisible(R.id.iv_story, false);
                holder.setVisible(R.id.ll_challenge, false);

                break;
            case 2:
                //挑战条目
           //     holder.setVisible(R.id.iv_story, false);
                holder.setVisible(R.id.ll_challenge, true);
            //    holder.setVisible(R.id.nine_grid_layout,false);
                holder.setVisible(R.id.iv_item_hot_vote,false);
                holder.setVisible(R.id.tv_item_hot_vote,false);
                holder.setText(R.id.tv_challenge, publisherInfo.getTitle());
                if (TextUtils.isEmpty(publisherInfo.getAddr())) {
                    holder.setVisible(R.id.tv_time_addr, false);
                } else {
                    holder.setVisible(R.id.tv_time_addr, true);
                    holder.setText(R.id.tv_time_addr, publisherInfo.getChallegeTime() + "," + publisherInfo.getAddr());
                }
                Glide.with(mContext)
                        .load(R.mipmap.popular_image_challenge_default)
                        .transform(new GlideCircleTransform(mContext))
                        .into((ImageView) holder.getView(R.id.iv_icon));
                holder.setText(R.id.tv_challenge_des, publisherInfo.getActiveDes());
                holder.setText(R.id.tv_challenge_call, publisherInfo.getOtherDes());

                break;
            case 3:
                //故事条目
                holder.setVisible(R.id.ll_challenge, true);
                holder.setVisible(R.id.iv_item_hot_vote,true);
                holder.setVisible(R.id.tv_item_hot_vote,true);
                holder.setText(R.id.tv_item_hot_vote,publisherInfo.getVote()+"");
                if (publisherInfo.isVoted()){
                    ToastUtils.showToast(mContext,"你已经投过票了");
                    holder.setImageResource(R.id.iv_item_hot_vote,R.mipmap.icon_btn_vote_selected);
                }else {
                    holder.setImageResource(R.id.iv_item_hot_vote,R.mipmap.icon_btn_vote_default);

                    holder.setOnClickListener(R.id.iv_item_hot_vote, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final CustomDialog customDialog = new CustomDialog(mContext,false,0.8,R.layout.dialog_vote,new int[]{R.id.tv_dialog_vote_cancle,R.id.tv_dialog_vote_sure});
                            customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                @Override
                                public void OnItemClick(CustomDialog dialog, View view) {
                                    if (view.getId() == R.id.tv_dialog_vote_sure){
                                        holder.setText(R.id.tv_item_hot_vote,publisherInfo.getVote() + 1 + "");
                                        publisherInfo.setVote(publisherInfo.getVote() + 1);

                                        holder.setImageResource(R.id.iv_item_hot_vote,R.mipmap.icon_btn_vote_selected);

                                        publisherInfo.setVoted(true);
                                        notifyItemChanged(position);
                                        //   holder.getView(R.id.iv_item_hot_vote).setEnabled(false);
                                    }else if (view.getId() == R.id.tv_dialog_vote_cancle){

                                    }
                                }
                            });
                            customDialog.show();
                        }
                    });


                }

                Glide.with(mContext)
                        .load(R.mipmap.popular_image_story_default)
                        .transform(new GlideCircleTransform(mContext))
                        .into((ImageView) holder.getView(R.id.iv_icon));

                holder.setText(R.id.tv_challenge, publisherInfo.getTitle());
                holder.setText(R.id.tv_challenge_des, publisherInfo.getActiveDes());
                holder.setText(R.id.tv_challenge_call, publisherInfo.getOtherDes());
                break;
            default:

                break;

        }
        //喜欢图标的点击事件
        holder.setOnClickListener(R.id.iv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tvLike = holder.getView(R.id.tv_like);
                if (Integer.parseInt(tvLike.getText().toString().trim()) == likes) {
                    //点赞
                    holder.setImageResource(R.id.iv_like, R.mipmap.icon_btn_like_selected);
                    holder.setText(R.id.tv_like, likes + 1 + "");
                } else {
                    //取消赞
                    holder.setImageResource(R.id.iv_like, R.mipmap.icon_btn_like_default);
                    holder.setText(R.id.tv_like, likes + "");
                }
            }
        });
        //点赞量文字的点击事件
        holder.setOnClickListener(R.id.tv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvLike = holder.getView(R.id.tv_like);
                if (Integer.parseInt(tvLike.getText().toString().trim()) == likes) {
                    holder.setImageResource(R.id.iv_like, R.mipmap.icon_btn_like_selected);
                    holder.setText(R.id.tv_like, likes + 1 + "");
                } else {
                    holder.setImageResource(R.id.iv_like, R.mipmap.icon_btn_like_default);
                    holder.setText(R.id.tv_like, likes + "");
                }

            }
        });

        holder.setImageResource(R.id.iv_like, R.mipmap.icon_btn_like_default);

        //评论图标的点击事件
        holder.setOnClickListener(R.id.iv_comments, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        //评论文字的点击事件
        holder.setOnClickListener(R.id.tv_comments, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        //更多图标的点击事件
        holder.setOnClickListener(R.id.iv_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(mContext,true,true,1,R.layout.pop_report,new int[]{R.id.btn_pop_report,R.id.btn_pop_cancle});
                dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()){
                            case R.id.btn_pop_report:
                                Intent intent = new Intent(mContext, ReportActivity.class);
                                intent.putExtra("publishInfo",publisherInfo);
                                mContext.startActivity(intent);
                                break;
                            case R.id.btn_pop_cancle:
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        holder.setOnClickListener(R.id.iv_publisher_avatar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPersonalHomePager(publisherInfo);
            }
        });

        holder.setOnClickListener(R.id.tv_publisher_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPersonalHomePager(publisherInfo);
            }
        });


    }

    private void toPersonalHomePager(PublisherInfo publisherInfo){
        Intent intent = new Intent(mContext, PersonalHomePagerActivity.class);
        intent.putExtra("publisherInfo",publisherInfo);
        mContext.startActivity(intent);
    }

}
