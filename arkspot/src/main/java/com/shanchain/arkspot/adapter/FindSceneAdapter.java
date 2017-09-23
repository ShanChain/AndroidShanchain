package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.ui.model.FindSceneInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/18.
 */

public class FindSceneAdapter extends BaseQuickAdapter<FindSceneInfo,BaseViewHolder> {

    public FindSceneAdapter(@LayoutRes int layoutResId, @Nullable List<FindSceneInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindSceneInfo item) {

    }
}
