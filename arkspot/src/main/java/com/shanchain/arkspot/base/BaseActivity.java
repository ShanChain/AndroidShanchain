package com.shanchain.arkspot.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.utils.SystemUtils;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;
import utils.LogUtils;

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
     *  描述：加载中。。。对话框
     *
     */
    private CustomDialog mCustomDialog;


    /**
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
        //禁止横竖屏切换
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 获取Intent数据
        initIntent();
        // 初始化属性
        initAttribute();
        // 初始化布局
        initLayout();


        // 初始化View和事件
        initViewsAndEvents();
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            SystemUtils.setImmersiveStatusBar_API21(this, getResources().getColor(R.color.colorWhite));
            SystemUtils.setStatusBarLightMode_API23(this);
        }
        SystemUtils.MIUISetStatusBarLightModeWithWhiteColor(this,getWindow(), true);
        SystemUtils.FlymeSetStatusBarLightModeWithWhiteColor(this,getWindow(), true);
    }

    /**
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
     * 描述: 重写onOptionsItemSelected 监听手机HOME键（硬件及虚拟）
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //finish();
            LogUtils.d("click->home");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * 描述: 接收EventBus通知
//     */
//    @Subscribe
//    public void onEventMainThread(Object object) {
//        // 获取到全部消息，暂不处理
//    }

    /**
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
     *  描述：友盟数据统计埋点
     */
    public void onResume() {
        super.onResume();
      //  MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
      //  MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
     //   MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
      //  MobclickAgent.onPause(this);
    }


    /**
     * 描述: 获取Bundle数据
     *
     * @param extras
     */
    public void getBundleExtras(Bundle extras) {

    }


    /* ===================================================================
     *                        父类抽象方法需实现
     * ===================================================================
     */


    /**
     * 描述: 绑定布局资源文件
     *
     * @return layout ID
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 描述: 初始化View和Event
     */
    protected abstract void initViewsAndEvents();


    /* ===================================================================
     *                        Activity跳转方法
     * ===================================================================
     */

    /**
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

    protected void showLoadingDialog() {
        mCustomDialog = new CustomDialog(this, 0.4, R.layout.dialog_progress, null);
        mCustomDialog.show();
    }

    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }
}
