package com.shanchain.shandata.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 适应于scrollView 中嵌套的GridView
 * Created by wutong on 2014/7/1.
 */
public class CustomGridView extends GridView{

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
