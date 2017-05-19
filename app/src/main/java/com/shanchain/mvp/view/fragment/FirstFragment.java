package com.shanchain.mvp.view.fragment;

import android.view.View;

import com.shanchain.R;
import com.shanchain.base.LazyFragment;

/**
 * Created by zhoujian on 2017/5/19.
 */

public class FirstFragment extends LazyFragment {
    @Override
    public View initView() {

        return View.inflate(mActivity, R.layout.fragment_first,null);
    }

    @Override
    protected void lazyLoad() {

    }
}
