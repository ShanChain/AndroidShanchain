package com.shanchain.shandata.widgets.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.DensityUtils;

/**
 * Created by zhoujian on 2017/6/16.
 */

public class CustomSeekBar extends LinearLayout {

    private SeekBar mSeekBar;
    private TextView mTextView;
    private ViewGroup mViewGroup;
    private LayoutParams mLayoutParams;
    private int mProgress = 50;
    private int mMeasuredWidth;
    private int mTvMeasuredWidth;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.custom_seek_bar, this,false);
        addView(mViewGroup);
        measure(0,0);

        mSeekBar = (SeekBar) mViewGroup.findViewById(R.id.seek_bar);
        mSeekBar.setMax(100);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTextView = (TextView) mViewGroup.findViewById(R.id.tv_progress);
    /*    mViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mMeasuredWidth = mViewGroup.getMeasuredWidth();
                mTvMeasuredWidth = mTextView.getMeasuredWidth();



              //  setProgress(progress);
                setText(getProgress());
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });*/

        setProgress(mProgress);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setText(int progress) {
        mTextView.setText(progress + "%");
        //显示进度文本调整的偏移值
        int adjust = DensityUtils.dip2px(getContext(),12)*(100-progress)/100;
        mLayoutParams.leftMargin = (mMeasuredWidth - mTvMeasuredWidth) * progress / 100 +adjust ;
        mTextView.setLayoutParams(mLayoutParams);
    }


    public int getProgress(){
        return mSeekBar.getProgress();
    }

    public void setProgress( int progress) {
        mProgress = progress;
        mViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mMeasuredWidth = mViewGroup.getMeasuredWidth();
                mTvMeasuredWidth = mTextView.getMeasuredWidth();
                mSeekBar.setProgress(mProgress);
                setText(getProgress());
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    public void setTextColor(int color){
        mTextView.setTextColor(color);
    }

    public void setSeekBarEnable(boolean enable){
            mSeekBar.setEnabled(enable);
    }

}
