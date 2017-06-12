package com.shanchain.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.ChooseContactsInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class ChooseContactsAdapter extends BaseCommonAdapter<ChooseContactsInfo> {

    public ChooseContactsAdapter(Context context, int layoutId, List<ChooseContactsInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(final ViewHolder holder, final ChooseContactsInfo chooseContactsInfo, int position) {

        Glide.with(mContext).load(R.drawable.photo6)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_choose_contacts));

        holder.setText(R.id.tv_choose_name,chooseContactsInfo.getName());
        holder.setChecked(R.id.rb_item_choose_contacts,chooseContactsInfo.isChecked());
        holder.setOnClickListener(R.id.ll_item_choose_contacts, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseContactsInfo.isChecked()){
                    chooseContactsInfo.setChecked(false);
                    holder.setChecked(R.id.rb_item_choose_contacts,false);
                }else {
                    chooseContactsInfo.setChecked(true);
                    holder.setChecked(R.id.rb_item_choose_contacts,true);
                }
            }
        });

        holder.setOnClickListener(R.id.rb_item_choose_contacts, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseContactsInfo.isChecked()){
                    chooseContactsInfo.setChecked(false);
                    holder.setChecked(R.id.rb_item_choose_contacts,false);
                }else {
                    chooseContactsInfo.setChecked(true);
                    holder.setChecked(R.id.rb_item_choose_contacts,true);
                }
            }
        });
    }
}
