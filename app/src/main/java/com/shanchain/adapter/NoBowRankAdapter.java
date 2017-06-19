package com.shanchain.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.NoBowRankInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/16.
 */

public class NoBowRankAdapter extends BaseCommonAdapter<NoBowRankInfo> {

    public NoBowRankAdapter(Context context, int layoutId, List<NoBowRankInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, NoBowRankInfo noBowRankInfo, int position) {

        holder.setText(R.id.tv_item_happier_name,noBowRankInfo.getNickName());
        holder.setText(R.id.tv_item_happier_prise_counts,noBowRankInfo.getTime());

        Glide.with(mContext)
                .load(R.mipmap.home_icon_focuson_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_happier_img));


        int rank = noBowRankInfo.getRank();
        TextView tvRank= holder.getView(R.id.tv_item_happier_rank);
        if (rank == 1){
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_champion_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else if (rank == 2) {
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_runnerup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else if (rank == 3) {
            tvRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_thirdrunner_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvRank.setCompoundDrawables(drawable,null,null,null);
        }else {
            tvRank.setCompoundDrawables(null,null,null,null);
            tvRank.setText(rank+"");
        }
    }
}
