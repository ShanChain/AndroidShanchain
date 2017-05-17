package com.shanchain.mvp.view.fragment;

import android.view.View;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.LazyFragment;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/16.
 */

public class HomeFragment extends LazyFragment {
    @Bind(R.id.tv_con)
    TextView mTvCon;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home, null);
        return view;
    }


    @Override
    protected void lazyLoad() {
        mTvCon.setText("小王");
    }

}
