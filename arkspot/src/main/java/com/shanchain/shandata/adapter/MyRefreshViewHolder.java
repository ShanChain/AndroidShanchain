package com.shanchain.shandata.adapter;

import android.content.Context;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;

/**
 * Created by WealChen
 * Date : 2019/5/21
 * Describe :
 */
public class MyRefreshViewHolder extends BGANormalRefreshViewHolder {
    private boolean isLoadingMore;

    /**
     * @param context
     * @param isLoadingMoreEnabled 上拉加载更多是否可用
     */
    public MyRefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
    }

    private void initAnimator() {

    }

}
