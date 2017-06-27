package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.PersonalInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/8.
 */

public class PersonalHomePagerAdapter extends BaseCommonAdapter<PersonalInfo> {
    public PersonalHomePagerAdapter(Context context, int layoutId, List<PersonalInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, PersonalInfo personalInfo, int position) {

    }
}
