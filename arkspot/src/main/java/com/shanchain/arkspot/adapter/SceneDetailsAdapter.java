package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.ui.model.SceneDetailsInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class SceneDetailsAdapter extends BaseQuickAdapter<SceneDetailsInfo,BaseViewHolder> {

    public SceneDetailsAdapter(@LayoutRes int layoutResId, @Nullable List<SceneDetailsInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneDetailsInfo item) {

    }
}
