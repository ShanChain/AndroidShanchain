package com.shanchain.shandata.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.mvp.model.HappierRankInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;

import java.util.List;

import static com.shanchain.shandata.base.MyApplication.mContext;

public class HasPartInSleepListAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<HappierRankInfo> mDatas;

    public HasPartInSleepListAdapter(BaseActivity activity, List<HappierRankInfo> datas) {
        mDatas = datas;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_happier_rank, (ViewGroup)mActivity.findViewById(android.R.id.content),false);
            holder.mTvItemHappierRank = (TextView) convertView.findViewById(R.id.tv_item_happier_rank);
            holder.mIvItemHappierImg = (ImageView) convertView.findViewById(R.id.iv_item_happier_img);
            holder.mTvItemHappierName = (TextView) convertView.findViewById(R.id.tv_item_happier_name);
            holder.mTvItemHappierPriseCounts = (TextView) convertView.findViewById(R.id.tv_item_happier_prise_counts);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HappierRankInfo happierRankInfo = mDatas.get(position);
        Glide.with(mContext)
                .load(R.mipmap.home_icon_story_default)
                .transform(new GlideCircleTransform(mContext))
                .into(holder.mIvItemHappierImg);

        holder.mTvItemHappierName.setText(happierRankInfo.getNickName());
        holder.mTvItemHappierPriseCounts.setText(happierRankInfo.getPriseCount() + "赞");

        if (position == 0) {
            holder.mTvItemHappierRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_champion_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mTvItemHappierRank.setCompoundDrawables(drawable, null, null, null);
        } else if (position == 1) {
            holder.mTvItemHappierRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_runnerup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mTvItemHappierRank.setCompoundDrawables(drawable, null, null, null);
        } else if (position == 2) {
            holder.mTvItemHappierRank.setText("");
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.cheerup_def_thirdrunner_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.mTvItemHappierRank.setCompoundDrawables(drawable, null, null, null);
        } else {
            holder.mTvItemHappierRank.setCompoundDrawables(null, null, null, null);
            holder.mTvItemHappierRank.setText(position + 1 + "");
        }

        return convertView;
    }


    static class ViewHolder {
        TextView mTvItemHappierRank;
        ImageView mIvItemHappierImg;
        TextView mTvItemHappierName;
        TextView mTvItemHappierPriseCounts;
    }
}
