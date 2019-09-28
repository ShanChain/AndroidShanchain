package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IAttentionCallback;
import com.shanchain.shandata.interfaces.ICommentDeleteCallback;
import com.shanchain.shandata.interfaces.ICommentPraiseCallback;
import com.shanchain.shandata.ui.model.CommentEntity;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/30
 * Describe :
 */
public class CommetListAdapter extends BaseAdapter {
    private List<CommentEntity> mList;
    private Context mContext;
    private ICommentPraiseCallback mICommentPraiseCallback;
    private ICommentDeleteCallback mICommentDeleteCallback;
    public CommetListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }
    public void setICommentPraiseCallback(ICommentPraiseCallback callback){
        this.mICommentPraiseCallback = callback;
    }
    public void setICommentDeleteCallback(ICommentDeleteCallback commentDeleteCallback){
        this.mICommentDeleteCallback = commentDeleteCallback;
    }
    public void setList(List<CommentEntity> mList){
        if(mList!=null && mList.size()>0){
            this.mList = mList;
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView = view;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.comment_item_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CommentEntity commentEntity = mList.get(position);
        if(null != commentEntity){
            Glide.with(mContext).load(commentEntity.getSendHeadIcon())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(viewHolder.ivUserHead1);
            viewHolder.tvNickname.setText(commentEntity.getSendNickName());
            viewHolder.tvTime.setText(TimeUtils.friendlyTime1(mContext,new Date(commentEntity.getCreateTime())));
            viewHolder.tvComment.setText(commentEntity.getContent());
            if(commentEntity.getReviceCount() == 0){
                viewHolder.tvRenums.setText(mContext.getString(R.string.reply));
            }else {
                viewHolder.tvRenums.setText(mContext.getString(R.string.reply_nums,commentEntity.getReviceCount()+""));
            }
            if("0".equals(commentEntity.getIsAttention())){
                //未关注
                viewHolder.tvAttention.setBackgroundResource(R.drawable.squra_attention_n_shape);
                viewHolder.tvAttention.setTextColor(mContext.getResources().getColor(R.color.login_marjar_color));
                viewHolder.tvAttention.setText(mContext.getResources().getString(R.string.attention));
            }else {
                viewHolder.tvAttention.setBackgroundResource(R.drawable.squra_attent_shape);
                viewHolder.tvAttention.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.tvAttention.setText(mContext.getResources().getString(R.string.Concerned));
            }
            if(Integer.parseInt(SCCacheUtils.getCacheUserId()) == commentEntity.getSendUserId()){
                viewHolder.tvAttention.setVisibility(View.GONE);
                viewHolder.llDelete.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvAttention.setVisibility(View.VISIBLE);
                viewHolder.llDelete.setVisibility(View.GONE);
            }

            //关注
            viewHolder.tvAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mICommentPraiseCallback!=null){
                        mICommentPraiseCallback.praiseToUser(commentEntity);
                    }
                }
            });

            //删除评论
            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mICommentDeleteCallback!=null){
                        mICommentDeleteCallback.deleteComment(commentEntity);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.iv_user_head1)
        CircleImageView ivUserHead1;
        @Bind(R.id.tv_nickname)
        TextView tvNickname;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_attention)
        TextView tvAttention;
        @Bind(R.id.tv_comment)
        TextView tvComment;
        @Bind(R.id.tv_delete)
        TextView tvDelete;
        @Bind(R.id.tv_renums)
        TextView tvRenums;
        @Bind(R.id.ll_delete)
        LinearLayout llDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
