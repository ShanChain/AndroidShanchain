package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.StoryPagerListInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/7/20.
 */

public class ThingsRecordAdapter extends BaseCommonAdapter<StoryPagerListInfo> {

    public ThingsRecordAdapter(Context context, int layoutId, List<StoryPagerListInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, StoryPagerListInfo storyPagerListInfo, int position) {

    }
}
