package com.shanchain.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanchain.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by 周建 on 2017/5/14.
 * 懒加载fragment
 */

public abstract class LazyFragment extends Fragment {
    /** 描述：fragment是否可见*/
    protected boolean isVisible;

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

    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;

    }*/

    /**
     *  2017/5/16
     *  描述：为fragment填充布局
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();

        return view;
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
        LogUtils.d("这一句总执行了吧");
        onVisible();
    }

    /**
     * 初始化布局
     *
     * @return
     */
    public abstract View initView();



    /**
     * fragment可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 懒加载数据
     */
    protected abstract void lazyLoad();


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
