package com.shanchain.mvp.view.fragment;

import android.view.View;

import com.shanchain.R;
import com.shanchain.base.LazyFragment;

/**
 * Created by zhoujian on 2017/5/19.
 */

public class FoundFragment extends LazyFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_found,null);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }
}
