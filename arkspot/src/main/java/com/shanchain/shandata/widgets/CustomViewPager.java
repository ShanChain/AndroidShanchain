package com.shanchain.shandata.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled = true;
    private boolean noScroll = false;
    public CustomViewPager(Context context) {
        super(context);
    }
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return this.isPagingEnabled && super.onTouchEvent(event);
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
