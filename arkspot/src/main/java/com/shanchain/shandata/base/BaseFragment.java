package com.shanchain.shandata.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * 懒加载fragment
 */

public abstract class BaseFragment extends Fragment {
    public String TAG = null;

    /**
     * 描述：fragment所依赖的activity
     */
    public MainActivity mActivity;
    public TaskListActivity mTaskListActivity;
    private CustomDialog mCustomDialog;

    /**
     * 描述：fragment创建时调用的方法,在此获取fragment所依赖的activitty对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTaskListActivity = (TaskListActivity) getActivity();
        TAG = this.getClass().getSimpleName();

    }


    /**
     * 描述：为fragment填充布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }


    /**
     * 描述：fragment创建完成后
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // 注册ButterKnife
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object obj) {

    }

    /**
     * 描述：友盟统计
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    protected void showLoadingDialog() {
        mCustomDialog = new CustomDialog(getContext(), 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(false);
    }

    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
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
     * 2017/5/22
     * 描述：给视图填充数据
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
        EventBus.getDefault().unregister(this);
    }

//    public
}
