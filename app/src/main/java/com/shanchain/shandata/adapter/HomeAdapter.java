package com.shanchain.shandata.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.DynamicInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class HomeAdapter extends BaseCommonAdapter<DynamicInfo> {

    private List<DynamicInfo> datas;

    public HomeAdapter(Context context, int layoutId, List<DynamicInfo> datas) {
        super(context, layoutId, datas);
        this.datas = datas;
    }

    @Override
    public void bindData(ViewHolder holder, DynamicInfo dynamicInfo, int position) {
        holder.setText(R.id.tv_item_home_left1,dynamicInfo.getLeft1());
        if (position == datas.size() +1 ){
            LogUtils.d("position = " + position);
            TextView tvBottom = holder.getView(R.id.tv_item_home_bottom);
            tvBottom.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = DensityUtils.dip2px(mContext,20);
            tvBottom.setLayoutParams(layoutParams);

            holder.setBackgroundRes(R.id.tv_item_home_icon,R.mipmap.home_icon_start_default);
            holder.setText(R.id.tv_item_home_right1,"2017年5月27号");
            holder.setText(R.id.tv_item_home_right2,"开启了善数者的旅程");
            holder.setText(R.id.tv_item_home_left1,"");
            holder.setText(R.id.tv_item_home_left2,"");
        }else {
            TextView tvBottom = holder.getView(R.id.tv_item_home_bottom);
            tvBottom.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = DensityUtils.dip2px(mContext,6);
            layoutParams.width = DensityUtils.dip2px(mContext,1);
            tvBottom.setBackgroundColor(Color.parseColor("#3bbac8"));
            holder.setBackgroundRes(R.id.tv_item_home_icon,R.mipmap.home_icon_happy_default);
            holder.setText(R.id.tv_item_home_right1,"");
            holder.setText(R.id.tv_item_home_right2,"");
            holder.setText(R.id.tv_item_home_left1,"开心点挑战 1小时");
            holder.setText(R.id.tv_item_home_left2,"1小时10分钟 挑战成功");
            tvBottom.setLayoutParams(layoutParams);
        }
    }
}
