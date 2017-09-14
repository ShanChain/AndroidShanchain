package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.ui.model.AnnouncementInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class AnnouncementAdapter extends BaseQuickAdapter<AnnouncementInfo,BaseViewHolder> {

    public AnnouncementAdapter(@LayoutRes int layoutResId, @Nullable List<AnnouncementInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnnouncementInfo item) {

    }
}
