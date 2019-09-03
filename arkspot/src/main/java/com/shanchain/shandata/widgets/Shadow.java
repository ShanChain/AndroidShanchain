package com.shanchain.shandata.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by seeker on 2017/5/23.
 */

public class Shadow extends View {

    private int screenHeitht;
    private int screenWidth;

    public Shadow(Context context) {
        this(context,null);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*canvas.clipRect(0, 0, screenWidth, screenHeitht);
//        canvas.clipRect(getShadowRegionRect1(), Region.Op.DIFFERENCE);
        Path mPath = new Path();
        mPath.addCircle(screenWidth/2, screenHeitht/2, 150, Path.Direction.CCW);
        canvas.clipPath(mPath,Region.Op.DIFFERENCE);
        canvas.drawColor(0x60000000);
        canvas.save();
        canvas.restore();*/

        canvas.clipRect(0, 0, screenWidth, screenHeitht);
        Path mPath = new Path();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#36748CA6"));
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(screenWidth/2,screenHeitht/2,150,paint);
        mPath.addCircle(screenWidth/2, screenHeitht/2, 150, Path.Direction.CCW);

        canvas.clipPath(mPath,Region.Op.DIFFERENCE);
        canvas.drawColor(0x60000000);
        canvas.save();
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenHeitht = getMeasuredHeight();
        screenWidth = getMeasuredWidth();
    }

    /**
     * 获取身份证取景框的矩形
     * @return
     */
   private Rect getShadowRegionRect(){
       int height = (int) (screenWidth * 0.8);//拍照的阴影框的高度为屏幕宽度的80%  0.8
       int width = (int) (height * 1.6);//身份证宽高比例为1.6
//        int height= (int) (screenWidth/1.6);
       int x_center=screenWidth/2;
       int y_center=screenHeitht/2;
//       return new Rect(0, y_center - (height / 2), screenWidth, height/2 + y_center);
       return new Rect(x_center-(height/2), y_center - (width / 2), x_center+(height/2), (width/2)+ y_center);

    }
    private Rect getShadowRegionRect1(){
        int height = (int) (screenWidth * 0.4);//拍照的阴影框的高度为屏幕宽度的80%  0.8
//        int width = (int) (height * 1.6);//身份证宽高比例为1.6
        int width = height;
//        int height= (int) (screenWidth/1.6);
        int x_center=screenWidth/2;
        int y_center=screenHeitht/2;
//       return new Rect(0, y_center - (height / 2), screenWidth, height/2 + y_center);
        return new Rect(x_center-(height/2), y_center - (width / 2), x_center+(height/2), (width/2)+ y_center);

    }
}