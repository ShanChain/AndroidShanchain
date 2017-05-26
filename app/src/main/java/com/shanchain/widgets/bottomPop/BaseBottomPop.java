package com.shanchain.widgets.bottomPop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.shanchain.R;

/**
 * Created by zhoujian on 2017/5/26.
 */

public abstract class BaseBottomPop extends PopupWindow {
     public Context mContext;

    public View.OnClickListener mOnClickListener;



    public BaseBottomPop(Context mContext, View.OnClickListener clickListener) {
        this.mContext = mContext;
        this.mOnClickListener = clickListener;
        initViewAndEvent();
        initPopStyle();
    }



    /** 设置弹出窗口特征 */
    public void setPopStyle(final View view, final View topView) {

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = topView.getTop();



                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        // 设置视图
        this.setContentView(view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.bottom_pop_anim);
    }

    public abstract View initViewAndEvent() ;
    public abstract void initPopStyle();
}
