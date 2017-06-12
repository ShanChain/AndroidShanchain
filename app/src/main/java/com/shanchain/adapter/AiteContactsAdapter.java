package com.shanchain.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.ContactInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class AiteContactsAdapter extends BaseCommonAdapter<ContactInfo> {

    public AiteContactsAdapter(Context context, int layoutId, List<ContactInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, ContactInfo contactInfo, int position) {

        Glide.with(mContext).load(R.drawable.photo6)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_aite_avatar));

        holder.setText(R.id.tv_item_aite_name,contactInfo.getName());
    }
}
