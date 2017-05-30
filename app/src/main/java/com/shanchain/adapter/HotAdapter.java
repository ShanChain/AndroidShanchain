package com.shanchain.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.mvp.view.activity.ReportActivity;
import com.shanchain.widgets.bottomPop.BottomReportPop;
import com.shanchain.widgets.dialog.BottomDialog;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/23.
 */

public class HotAdapter extends BaseCommonAdapter<PublisherInfo> {


    private BottomReportPop mPop;

    public HotAdapter(Context context, int layoutId, List<PublisherInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindDatas(final ViewHolder holder, final PublisherInfo publisherInfo, int position) {

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
        holder.setText(R.id.tv_des, publisherInfo.getDes());
        switch (type) {
            case 1:
               /* //普通条目
                holder.setVisible(R.id.rv_images, true);
                holder.setVisible(R.id.iv_story, false);
                holder.setVisible(R.id.ll_challenge, false);
                //只有一张图片的时候
                if (images.size() == 1) {
                    holder.setVisible(R.id.rv_images, false);
                    holder.setVisible(R.id.iv_story, true);
                    Glide.with(mContext).load(R.drawable.photo2).into((ImageView) holder.getView(R.id.iv_story));
                }

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
*/
                break;
            case 2:
                //挑战条目
    //            holder.setVisible(R.id.rv_images, false);
                holder.setVisible(R.id.iv_story, false);
                holder.setVisible(R.id.ll_challenge, true);

                holder.setText(R.id.tv_challenge, publisherInfo.getTitle());
                if (TextUtils.isEmpty(publisherInfo.getAddr())) {
                    holder.setVisible(R.id.tv_time_addr, false);
                } else {
                    holder.setVisible(R.id.tv_time_addr, true);
                    holder.setText(R.id.tv_time_addr, publisherInfo.getChallegeTime() + "," + publisherInfo.getAddr());
                }
                Glide.with(mContext).load(R.mipmap.popular_image_challenge_default).into((ImageView) holder.getView(R.id.iv_icon));
                holder.setText(R.id.tv_challenge_des, publisherInfo.getActiveDes());
                holder.setText(R.id.tv_challenge_call, publisherInfo.getOtherDes());


                break;
            case 3:
                //故事条目
        //        holder.setVisible(R.id.rv_images, false);

                holder.setVisible(R.id.ll_challenge, true);
                if (TextUtils.isEmpty(publisherInfo.getStroyImgUrl())) {
                    holder.setVisible(R.id.iv_story, false);
                } else {
                    holder.setVisible(R.id.iv_story, true);
                    Glide.with(mContext).load(R.drawable.photo).into((ImageView) holder.getView(R.id.iv_story));
                }

                Glide.with(mContext).load(R.mipmap.popular_image_story_default).into((ImageView) holder.getView(R.id.iv_icon));

                holder.setText(R.id.tv_challenge, publisherInfo.getTitle());
                holder.setText(R.id.tv_challenge_des, publisherInfo.getActiveDes());
                holder.setText(R.id.tv_challenge_call, publisherInfo.getOtherDes());
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
               /* mPop = new BottomReportPop(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.btn_pop_report){
                            Intent intent = new Intent(mContext, ReportActivity.class);
                            intent.putExtra("publishInfo",publisherInfo);
                            mContext.startActivity(intent);
                            mPop.dismiss();
                        }
                    }
                });
                LinearLayout rootView= holder.getView(R.id.ll_fragment_hot);
                mPop.showAtLocation(rootView, Gravity.BOTTOM,0,0);*/

                BottomDialog dialog = new BottomDialog(mContext,R.layout.pop_report,new int[]{R.id.btn_pop_report,R.id.btn_pop_cancle});
                dialog.setOnBottomItemClickListener(new BottomDialog.OnBottomItemClickListener() {
                    @Override
                    public void OnBottomItemClick(BottomDialog dialog, View view) {
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

    }

}
