package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FocusInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/7/3.
 */

public class FocusAdapter extends BaseCommonAdapter<FocusInfo> {

    public FocusAdapter(Context context, int layoutId, List<FocusInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, final FocusInfo focusInfo, int position) {

        Glide.with(mContext)
                .load(R.drawable.photo_bear)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_foucs_avatar));

        holder.setText(R.id.tv_item_focus_name,focusInfo.getName());

        if (focusInfo.isFocused()){
            holder.setBackgroundRes(R.id.tv_item_focus_btn,R.drawable.shap_btn_regist_hint);
            holder.setText(R.id.tv_item_focus_btn,"已关注");
        }else {
            holder.setBackgroundRes(R.id.tv_item_focus_btn,R.drawable.shap_btn_regist);
            holder.setText(R.id.tv_item_focus_btn,"关注");
        }

        holder.setOnClickListener(R.id.tv_item_focus_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusInfo.setFocused(!focusInfo.isFocused());
                notifyDataSetChanged();
            }
        });

    }
}
