package com.shanchain.shandata.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 适应于scrollView 中嵌套的listview
 * Created by wutong on 2014/7/1.
 */
public class CustomListView extends ListView {


    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
