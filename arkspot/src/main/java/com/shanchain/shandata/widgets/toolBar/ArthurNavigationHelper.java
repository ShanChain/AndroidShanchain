package com.shanchain.shandata.widgets.toolBar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 描述: 属性动画帮助类
 */
public class ArthurNavigationHelper {


    /**
     * 日期: 2017/1/9 15:39
     * 描述: 获取一个更改为纯颜色的的Drawable
     * @param drawable  改变前Drawable
     * @param color     改变颜色
     * @param forceTint 是否强制改变
     * @return 纯颜色的的Drawable
     */
    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color, boolean forceTint) {
        if (forceTint) {
            drawable.clearColorFilter();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawable.invalidateSelf();
            return drawable;
        }
        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    /**
     * 日期: 2017/1/9 15:39
     * 描述: 动画更新头部间距
     */
    public static void updateTopMargin(final View view, int fromMargin, int toMargin) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    p.setMargins(p.leftMargin, (int) animatedValue, p.rightMargin, p.bottomMargin);
                    view.requestLayout();
                }
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:39
     * 描述: 动画更新底部间距
     */
    public static void updateBottomMargin(final View view, int fromMargin, int toMargin, int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) animatedValue);
                    view.requestLayout();
                }
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:37
     * 描述: 动画更新View左间距
     */
    public static void updateLeftMargin(final View view, int fromMargin, int toMargin) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    p.setMargins((int) animatedValue, p.topMargin, p.rightMargin, p.bottomMargin);
                    view.requestLayout();
                }
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:44
     * 描述: 动画更新View的大小
     */
    public static void updateViewSize(final View view, int fromSize, int toSize){
        ValueAnimator animator = ValueAnimator.ofInt(fromSize, toSize);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.width = animatedValue;
                    params.height = animatedValue;
                    view.requestLayout();
                }
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:36
     * 描述: 动画更新字体大小
     */
    public static void updateTextSize(final TextView textView, float fromSize, float toSize) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromSize, toSize);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:36
     * 描述: 动画更新透明度
     */
    public static void updateAlpha(final View view, float fromValue, float toValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromValue, toValue);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setAlpha(animatedValue);
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:37
     * 描述: 动画更新字体颜色
     */
    public static void updateTextColor(final TextView textView, @ColorInt int fromColor, @ColorInt int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(150);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    /**
     * 日期: 2017/1/9 15:37
     * 描述: 动画更新View背景颜色
     */
    public static void updateViewBackgroundColor(final View view, @ColorInt int fromColor,
                                                 @ColorInt int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(150);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    /**
     * 日期: 2017/1/9 15:38
     * 描述: 动画更新image图片颜色
     */
    public static void updateDrawableColor(final Context context, final Drawable drawable,
                                           final ImageView imageView, @ColorInt int fromColor,
                                           @ColorInt int toColor, final boolean forceTint) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(150);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                imageView.setImageDrawable(ArthurNavigationHelper.getTintDrawable(drawable,
                        (Integer) animator.getAnimatedValue(), forceTint));
                imageView.requestLayout();
            }
        });
        colorAnimation.start();
    }

    /**
     * 日期: 2017/1/9 15:38
     * 描述: 动画更新宽度
     */
    public static void updateWidth(final View view, float fromWidth, float toWidth) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromWidth, toWidth);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = Math.round((float) animator.getAnimatedValue());
                view.setLayoutParams(params);
            }
        });
        animator.start();
    }

    /**
     * 日期: 2017/1/9 15:42
     * 描述: 检查状态栏是否是半透明的
     * @param context Context
     * @return
     */
    public static boolean isTranslucentStatusBar(Context context) {
        Window w = unwrap(context).getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        int flags = lp.flags;
        if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
            return true;
        }

        return false;
    }

    /**
     * 日期: 2017/1/9 15:42
     * 描述: 获取按钮栏的高度
     * @param context Context
     * @return
     */
    public static int getSoftButtonsBarSizePort(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            Window window = unwrap(context).getWindow();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            window.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    /**
     * 日期: 2017/1/9 15:42
     * 描述: 打开wactivity
     * @param context Context
     * @return Activity
     */
    public static Activity unwrap(Context context) {
        while (!(context instanceof Activity)) {
            ContextWrapper wrapper = (ContextWrapper) context;
            context = wrapper.getBaseContext();
        }
        return (Activity) context;
    }

    /**
     * 日期: 2017/1/16 11:55
     * 描述: Drawable 转 Bitmap 方法
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
