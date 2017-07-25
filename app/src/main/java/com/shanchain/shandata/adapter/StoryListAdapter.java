package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.StoryListInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/7/17.
 */

public class StoryListAdapter extends BaseCommonAdapter<StoryListInfo> {

    public StoryListAdapter(Context context, int layoutId, List<StoryListInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, StoryListInfo storyListInfo, int position) {

    }
}
