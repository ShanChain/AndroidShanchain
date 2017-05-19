package com.shanchain.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.shanchain.manager.ActivityManager;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;

/**
 * Created by 周建 on 2017/5/13.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 描述：Log日志 Tag
     */
    protected static String TAG = null;

    /**
     * 描述：上下文对象
     */
    protected Context mContext = null;

    /**
     * 描述：Activity对象
     */
    protected Activity mActivity = null;

    // 屏幕信息 -------------------------

    /**
     * 描述：屏幕宽度
     */
    protected int mScreenWidth = 0;

    /**
     * 描述：屏幕高度
     */
    protected int mScreenHeight = 0;

    /**
     * 描述：屏幕密度
     */
    protected float mScreenDensity = 0.0f;


    /**
     * 日期: 2017/1/5 11:14
     * 描述: onCreate 初始化
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 调用父类onCreate
        super.onCreate(savedInstanceState);
        // 注册EventBus
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        // 添加Activity入栈
        ActivityManager.getInstance().addActivity(this);
        // 注册监听网络状态
        // 获取Intent数据
        initIntent();
        // 初始化属性
        initAttribute();
        // 初始化布局
        initLayout();

        // 初始化View和事件
        initViewsAndEvents();
    }

    /**
     * 日期: 2017/1/5 11:14
     * 描述: 初始化Intent传值
     */
    private void initIntent() {
        // 获取传值
        Bundle extras = getIntent().getExtras();
        // 校验是否为空，为空则不初始化
        if (null != extras) {
            getBundleExtras(extras);
        }
    }

    /**
     * 日期: 2017/1/5 11:22
     * 描述: 初始化属性
     */
    private void initAttribute() {
        // 获取当前类名至为TAG
        TAG = this.getClass().getSimpleName();
        // 初始化上下文对象
        mContext = this;
        // 初始化Activity对象
        mActivity = this;
        // 获取屏幕宽高及密度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 初始化屏幕属性
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    /**
     * 日期: 2017/1/5 11:26
     * 描述: 初始化布局
     */
    private void initLayout() {
        // 根据返回LayoutID设置布局
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("你必须返回一个正确的contentview布局的资源ID");
        }
    }

    /**
     * 日期: 2017/1/5 11:36
     * 描述: 重写setContentView 添加注解
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // 调用父类方法
        super.setContentView(layoutResID);
        // 绑定注解
        ButterKnife.bind(this);
    }

    /**
     * 日期: 2017/1/5 12:01
     * 描述: 重写onOptionsItemSelected 监听手机HOME键（硬件及虚拟）
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * 日期: 2017/1/5 14:10
//     * 描述: 接收EventBus通知
//     */
//    @Subscribe
//    public void onEventMainThread(Object object) {
//        // 获取到全部消息，暂不处理
//    }

    /**
     * 日期: 2017/1/5 11:38
     * 描述: 重写finish使Activity出栈
     */
    @Override
    public void finish() {
        // 调用父类方法
        super.finish();
        // Activity出栈
        ActivityManager.getInstance().removeActivity(this);
    }

    /**
     * 日期: 2017/1/5 11:41
     * 描述: 重写onDestroy实现反注册EventBus
     */
    @Override
    protected void onDestroy() {
        // 调用父类方法
        super.onDestroy();
        // 解除注解绑定
        ButterKnife.unbind(this);
        // 反注册EventBus
      //  EventBus.getDefault().unregister(this);
        // 解除网络状态监听器
        OkHttpUtils.getInstance().cancelTag(this);
    }


    /**
     * 日期: 2017/1/5 11:16
     * 描述: 获取Bundle数据
     *
     * @param extras
     */
    public void getBundleExtras(Bundle extras){

    }


    /* ===================================================================
     *                        父类抽象方法需实现
     * ===================================================================
     */



    /**
     * 日期: 2017/1/5 11:28
     * 描述: 绑定布局资源文件
     *
     * @return layout ID
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 日期: 2017/1/5 11:57
     * 描述: 初始化View和Event
     */
    protected abstract void initViewsAndEvents();


    /* ===================================================================
     *                        Activity跳转方法
     * ===================================================================
     */

    /**
     * 日期: 2017/1/5 14:52
     * 描述: Activity跳转
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 描述: Activity跳转 传参
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 日期: 2017/1/5 14:52
     * 描述: Activity跳转 关闭当前Activity
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 日期: 2017/1/5 14:52
     * 描述: Activity跳转 传参且关闭当前Activity
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 日期: 2017/1/5 14:52
     * 描述: Activity跳转 回调
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 日期: 2017/1/5 14:52
     * 描述: Activity跳转 传参且回调
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
