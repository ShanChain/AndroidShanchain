package com.shanchain.data.common.ui.toolBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * 描述: 自定义TextView 实现 Drawable 居中显示
 */
public class DrawableCenterTextView extends android.support.v7.widget.AppCompatTextView {

    private Rect mRect;

    /** 描述：构造方法 */
    public DrawableCenterTextView(Context context) {
        super(context);
    }

    /** 描述：构造方法 */
    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** 描述：构造方法 */
    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 日期: 2017/1/10 14:03
     * 描述: 重写onDraw方法实现居中
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 获取TextView的Drawable对象，返回的数组长度应该是4，对应左上右下
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[0];
            if (drawable != null) {
                // 当左边Drawable的不为空时，测量要绘制文本的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawable.getIntrinsicWidth();
                // 计算总宽度（文本宽度 + drawablePadding + drawableWidth）
                float bodyWidth = textWidth + drawablePadding + drawableWidth;
                // 移动画布开始绘制的X轴
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            } else if ((drawable = drawables[1]) != null) {
                // 否则如果上边的Drawable不为空时，获取文本的高度
                if (mRect == null){
                    mRect = new Rect();
                }
                getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), mRect);
                float textHeight = mRect.height();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawable.getIntrinsicHeight();
                // 计算总高度（文本高度 + drawablePadding + drawableHeight）
                float bodyHeight = textHeight + drawablePadding + drawableHeight;
                // 移动画布开始绘制的Y轴
                canvas.translate(0, (getHeight() - bodyHeight) / 2);
            }
        }
        super.onDraw(canvas);
    }

}
