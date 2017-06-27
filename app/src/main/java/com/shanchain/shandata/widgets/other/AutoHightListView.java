package com.shanchain.shandata.widgets.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zhoujian on 2017/6/26.
 */

public class AutoHightListView extends ListView {

    public AutoHightListView(Context context) {
        super(context);
    }

    public AutoHightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}
