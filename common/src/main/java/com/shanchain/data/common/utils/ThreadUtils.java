package com.shanchain.data.common.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhoujian on 2017/5/23.
 * 用于主线程和子线程切换的工具类
 */

public class ThreadUtils {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    //当前是单一线程池,如需多个线程池改变接口的实现类即可
    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    /**
     *  2017/5/23
     *  描述：运行在子线程
     *
     */
    public static void runOnSubThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    /**
     *  2017/5/23
     *  描述：运行在主线程
     *
     */
    public static void runOnMainThread(Runnable runnable) {
        sHandler.post(runnable);

    }
}
