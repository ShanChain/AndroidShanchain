package com.shanchain.adapter;

import android.content.Context;

import com.shanchain.base.BaseCommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/31.
 */

public class CommentsAdapter extends BaseCommonAdapter<String> {


    public CommentsAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, String s, int position) {

    }
}
