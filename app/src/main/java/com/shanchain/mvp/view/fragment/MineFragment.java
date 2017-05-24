package com.shanchain.mvp.view.fragment;

import android.view.View;

import com.shanchain.R;
import com.shanchain.base.BaseFragment;

/**
 * Created by zhoujian on 2017/5/19.
 * 我的页面
 */

public class MineFragment extends BaseFragment {


    @Override
    public View initView() {

        return View.inflate(mActivity, R.layout.fragment_mine,null);
    }

    @Override
    public void initData() {

    }

}
