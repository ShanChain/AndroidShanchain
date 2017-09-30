package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.ui.model.MyCommentsInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/29.
 */

public class MyCommentsAdapter extends BaseQuickAdapter<MyCommentsInfo,BaseViewHolder> {

    public MyCommentsAdapter(@LayoutRes int layoutResId, @Nullable List<MyCommentsInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCommentsInfo item) {

    }
}
