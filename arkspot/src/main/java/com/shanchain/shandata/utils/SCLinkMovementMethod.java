package com.shanchain.shandata.utils;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

/**
 * Created by zhoujian on 2017/12/4.
 */

public class SCLinkMovementMethod extends LinkMovementMethod {

    private ClickableSpanNoUnderline mSpan;
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        boolean b = super.onTouchEvent(widget, buffer, event);

        if (!b && event.getAction() == MotionEvent.ACTION_UP) {
            ViewParent parent = widget.getParent();
            if (parent instanceof ViewGroup) {
                return ((ViewGroup) parent).performClick();
            }

        }

        return b;
    }

    public static SCLinkMovementMethod getInstance() {
        if (mInstance == null) {
            mInstance = new SCLinkMovementMethod();
        }
        return mInstance;
    }

    private static SCLinkMovementMethod mInstance;


    private ClickableSpanNoUnderline getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        ClickableSpanNoUnderline[] link = spannable.getSpans(off, off, ClickableSpanNoUnderline.class);
        ClickableSpanNoUnderline touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

}
