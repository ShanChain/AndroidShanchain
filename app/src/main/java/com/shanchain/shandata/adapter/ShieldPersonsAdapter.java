package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ShieldPersonInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/29.
 */

public class ShieldPersonsAdapter extends BaseCommonAdapter<ShieldPersonInfo> {
    public ShieldPersonsAdapter(Context context, int layoutId, List<ShieldPersonInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(final ViewHolder holder, final ShieldPersonInfo shieldPersonInfo, int position) {
        Glide.with(mContext)
                .load(R.mipmap.bing_link_wechat_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_shield_avatar));
        holder.setText(R.id.tv_item_shield_name,shieldPersonInfo.getName());
        if (shieldPersonInfo.isShielded()){
            holder.setText(R.id.tv_item_shield_btn,"已屏蔽");
        }else {
            holder.setText(R.id.tv_item_shield_btn,"取消屏蔽");
        }
        holder.setOnClickListener(R.id.tv_item_shield_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shieldPersonInfo.setShielded(!shieldPersonInfo.isShielded());
                if (shieldPersonInfo.isShielded()){
                    holder.setText(R.id.tv_item_shield_btn,"已屏蔽");
                }else {
                    holder.setText(R.id.tv_item_shield_btn,"取消屏蔽");
                }
            }
        });
    }
}
