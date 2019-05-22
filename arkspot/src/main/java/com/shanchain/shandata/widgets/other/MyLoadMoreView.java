package com.shanchain.shandata.widgets.other;

import com.shanchain.shandata.R;

/**
 * Created by WealChen
 * Date : 2019/5/21
 * Describe :
 */
public class MyLoadMoreView extends com.chad.library.adapter.base.loadmore.LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.progress_bar_load_more;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.tv_normal_refresh_footer_status;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.tv_normal_refresh_footer_status;
    }
}
