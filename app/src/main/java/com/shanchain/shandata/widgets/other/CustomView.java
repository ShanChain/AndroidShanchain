package com.shanchain.shandata.widgets.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.shanchain.shandata.R;

public class CustomView extends View {

    private Paint mPaint;
    private Path mPath;

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        typedArray.getInt(R.styleable.RoundCornerImageView_corner_size,0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint == null){
            mPaint = new Paint();
        }
        mPaint.setColor(getResources().getColor(R.color.colorCreationText));
        if (mPath == null){
            mPath = new Path();
        }
        mPath.lineTo(100,300);
        mPath.lineTo(50,160);
        mPath.lineTo(150,230);
        mPath.lineTo(200,200);
        mPath.lineTo(130,500);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }
}
