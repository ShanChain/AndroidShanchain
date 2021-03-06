package com.shanchain.data.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 单例吐司工具类
 */

public class ToastUtils {
    /**
     * 描述：Toast的对象
     */
    private static Toast mToast = null;

    /**
     * 描述：构造器Throw
     */
    private ToastUtils() {
        throw new AssertionError();
    }

    /**
     *  2017/5/16
     *  描述：短时间的吐司显示
     *  @param context 上下文对象
     *  @param msg 要显示的提示信息
     */
    public static void showToast(Context context, String msg) {
        if (null == mToast) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    /**
     *  描述：长时间吐司显示
     *  @param context 上下文对象
     *  @param msg 要显示的提示信息
     */
    public static void showToastLong(Context context, String msg) {
        if (null == mToast) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 描述: 提示文字
     *
     * @param context 上下文对象
     * @param msg     提示内容（资源文件ID）
     */
    public static void showToast(Context context, int msg) {
        if (null == mToast) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
