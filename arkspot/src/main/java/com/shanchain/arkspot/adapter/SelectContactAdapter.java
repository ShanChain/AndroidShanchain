package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.BdAtContactInfo;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/30.
 */

public class SelectContactAdapter extends BaseQuickAdapter<BdAtContactInfo, BaseViewHolder> {

    private List<BdAtContactInfo> datas;

    public SelectContactAdapter(@LayoutRes int layoutResId, @Nullable List<BdAtContactInfo> data) {
        super(layoutResId, data);
        datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, BdAtContactInfo item) {
        int layoutPosition = helper.getLayoutPosition();
        boolean isAt = item.isAt();
        if (layoutPosition != 0) {

            if (datas.get(layoutPosition-1).getContactInfo().getLetter().equals(datas.get(layoutPosition).getContactInfo().getLetter())){
                helper.setVisible(R.id.ll_item_contact_letter,false);
            }else {
                helper.setVisible(R.id.ll_item_contact_letter,true);
            }

        }else {
            helper.setVisible(R.id.ll_item_contact_letter,true);
        }

        helper.setText(R.id.tv_item_contact_letter,item.getContactInfo().getLetter())
                .setText(R.id.tv_item_contact_name,item.getContactInfo().getName())
                .setChecked(R.id.rb_item_contact,item.isSelected());

        helper.addOnClickListener(R.id.rb_item_contact);

        GlideUtils.load(mContext,item.getContactInfo().getImg(),(ImageView) helper.getView(R.id.iv_item_contact_avatar),0);
    }
}
