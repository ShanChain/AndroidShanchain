package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ContactInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;



public class AiteContactsAdapter extends BaseCommonAdapter<ContactInfo> {

    public AiteContactsAdapter(Context context, int layoutId, List<ContactInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, ContactInfo contactInfo, int position) {

        Glide.with(mContext).load(R.drawable.photo6)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_aite_avatar));

        holder.setText(R.id.tv_item_aite_name,contactInfo.getName());
    }
}
