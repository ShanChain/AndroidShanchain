package com.shanchain.shandata.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :
 */
public class PhoneFrontAdapter extends BaseQuickAdapter<PhoneFrontBean,BaseViewHolder> {
    private Context context;
    private List<PhoneFrontBean> mList;

    public PhoneFrontAdapter(int layoutResId, @Nullable List<PhoneFrontBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneFrontBean item) {
        helper.setText(R.id.tv_contry,item.getContry());
        helper.setText(R.id.tv_phone_front,item.getPhoneFront());
    }
}
