package com.shanchain.shandata.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by 周建 on 2017/5/13.
 */

public class ActivityManager {
    /** 描述：管理器对象 */
    private static ActivityManager mActivityManager;

    /** 描述：Activity堆栈 */
    private Stack<Activity> mActivityStack;

    /** 描述：构造方法不含参数，初始化堆栈 */
    private ActivityManager() {
        mActivityStack = new Stack<>();
    }

    /** 描述：获取管理器实例 */
    public static ActivityManager getInstance() {
        if (mActivityManager == null) {
            mActivityManager = new ActivityManager();
        }
        return mActivityManager;
    }

    /** 描述：获取Activity堆栈 */
    public Stack<Activity> getStack() {
        return mActivityStack;
    }

    /**
     * 日期: 2017/1/4 17:23
     * 描述: 入栈
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 日期: 2017/1/4 17:23
     * 描述: 出栈
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 日期: 2017/1/4 17:24
     * 描述: 清空栈堆，彻底退出
     */
    public void finishAllActivity() {
        Activity activity;
        while (!mActivityStack.empty()) {
            activity = mActivityStack.pop();
            if (activity != null) activity.finish();
        }
    }

    /**
     * 日期: 2017/1/4 17:24
     * 描述: 结束指定的activity
     * @param actCls  Activity
     */
    public boolean finishActivity(Class<? extends Activity> actCls) {
        Activity activity = findActivityByClass(actCls);
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            return true;
        }
        return false;
    }

    /**
     * 日期: 2017/1/4 17:24
     * 描述: 结束指定的activity
     * @param actCls Activity.class
     */
    public Activity findActivityByClass(Class<? extends Activity> actCls) {
        Activity activity = null;
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            activity = iterator.next();
            if (activity != null && activity.getClass().getName().equals(actCls.getName()) && !activity.isFinishing()) {
                break;
            }
            activity = null;
        }
        return activity;
    }

    /**
     * 日期: 2017/1/4 17:26
     * 描述: finish指定的activity之上的所有activity
     * @param actCls
     * @param isIncludeSelf
     */
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }
}
