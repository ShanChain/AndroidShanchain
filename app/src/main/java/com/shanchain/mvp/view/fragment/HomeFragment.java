package com.shanchain.mvp.view.fragment;

import android.view.View;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/16.
 * 主页页面
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.tv_con)
    TextView mTvCon;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_home, null);
    }

    @Override
    public void initData() {
        mTvCon.setText("小王");
    }


}
