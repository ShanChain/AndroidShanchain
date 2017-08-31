package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.ContactInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/30.
 */

public class SelectContactAdapter extends BaseQuickAdapter<ContactInfo, BaseViewHolder> {

    private List<ContactInfo> datas;

    public SelectContactAdapter(@LayoutRes int layoutResId, @Nullable List<ContactInfo> data) {
        super(layoutResId, data);
        datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactInfo item) {
        int layoutPosition = helper.getLayoutPosition();

        if (layoutPosition != 0) {

            if (datas.get(layoutPosition-1).getLetter().equals(datas.get(layoutPosition).getLetter())){
                helper.setVisible(R.id.ll_item_contact_letter,false);
            }else {
                helper.setVisible(R.id.ll_item_contact_letter,true);
            }

        }

        helper.setText(R.id.tv_item_contact_letter,item.getLetter())
                .setText(R.id.tv_item_contact_name,item.getName())
                .setChecked(R.id.rb_item_contact,item.isSelected());

        helper.addOnClickListener(R.id.rb_item_contact);

        Glide.with(mContext)
                .load(R.drawable.photo_yue)
                .into((ImageView) helper.getView(R.id.iv_item_contact_avatar));
    }
}
