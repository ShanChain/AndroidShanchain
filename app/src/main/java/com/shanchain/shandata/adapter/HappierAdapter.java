package com.shanchain.shandata.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.HappierRankInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/13.
 */

public class HappierAdapter extends BaseCommonAdapter<HappierRankInfo> {
    public HappierAdapter(Context context, int layoutId, List<HappierRankInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, HappierRankInfo happierRankInfo, int position) {
        holder.setText(R.id.tv_item_happier_name,happierRankInfo.getNickName());
        holder.setText(R.id.tv_item_happier_prise_counts,happierRankInfo.getPriseCount()+"赞");
        TextView tvRank = holder.getView(R.id.tv_item_happier_rank);

        Glide.with(mContext)
                .load(R.mipmap.home_icon_story_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_happier_img));

        if (position == 2){
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_champion_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else if (position == 3){
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_runnerup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else if (position == 4){
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_thirdrunner_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else {
            tvRank.setCompoundDrawables(null,null,null,null);
            tvRank.setText(position-1 + "");
        }
    }
}
