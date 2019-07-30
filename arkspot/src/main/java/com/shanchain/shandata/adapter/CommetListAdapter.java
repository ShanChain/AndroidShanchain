package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CommentEntity;
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

    public CommetListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
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

        CommentEntity commentEntity = mList.get(position);
        if(null != commentEntity){
            Glide.with(mContext).load(commentEntity.getSendHeadIcon())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(viewHolder.ivUserHead1);
            viewHolder.tvNickname.setText(commentEntity.getSendNickName());
            viewHolder.tvTime.setText(TimeUtils.friendlyTime1(new Date(commentEntity.getCreateTime())));
            viewHolder.tvComment.setText(commentEntity.getContent());
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
