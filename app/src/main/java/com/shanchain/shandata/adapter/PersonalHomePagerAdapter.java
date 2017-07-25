package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.PersonalInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class PersonalHomePagerAdapter extends BaseCommonAdapter<PersonalInfo> {
    public PersonalHomePagerAdapter(Context context, int layoutId, List<PersonalInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, PersonalInfo personalInfo, int position) {

    }
}
