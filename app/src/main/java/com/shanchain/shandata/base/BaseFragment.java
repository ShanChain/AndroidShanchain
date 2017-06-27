package com.shanchain.shandata.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by 周建 on 2017/5/14.
 * 懒加载fragment
 */

public abstract class BaseFragment extends Fragment {
    public String TAG = null;

    /** 描述：fragment所依赖的activity*/
    public BaseActivity mActivity;

    /**
     *  2017/5/16
     *  描述：fragment创建时调用的方法,在此获取fragment所依赖的activitty对象
     *  
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        TAG = this.getClass().getSimpleName();
    }


    /**
     *  2017/5/16
     *  描述：为fragment填充布局
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return initView();
    }


    /**
     *  2017/5/16
     *  描述：fragment创建完成后
     *
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // 注册ButterKnife
        ButterKnife.bind(this, view);
        initData();
    }

    /*========================================
     * 子类需重写的抽象方法
     =========================================*/

    /**
     * 初始化布局
     *
     * @return 布局视图
     */
    public abstract View initView();

    /**
     *  2017/5/22
     *  描述：给视图填充数据
     *
     */
    public abstract void initData();





    /**
     * 日期: 2017/1/8 10:13
     * 描述: 重写onDestroy 取消ButterKnife
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消ButterKnife
        ButterKnife.unbind(getActivity());
    }
}
