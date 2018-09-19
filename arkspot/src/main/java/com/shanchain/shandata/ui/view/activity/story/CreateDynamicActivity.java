package com.shanchain.shandata.ui.view.activity.story;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.MyInterpolator;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateDynamicActivity extends BaseActivity {

    @Bind(R.id.iv_create_dynamic_close)
    ImageView mIvCreateDynamicClose;
    @Bind(R.id.ll_btn_dynamic)
    LinearLayout mLlBtnDynamic;
    @Bind(R.id.ll_btn_video)
    LinearLayout mLlBtnVideo;
    private GestureDetector mGestureDetector;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_create_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (velocityY < 200){
                    LogUtils.i("无效滑动，速度太慢");
                }else {
                    float x1 = e1.getX();
                    float y1 = e1.getY();
                    float x2 = e2.getX();
                    float y2 = e2.getY();

                    if (Math.abs(x1-x2)>Math.abs(y1-y2)){
                        LogUtils.i("横向滑动较大，无效操作");
                    }else {
                        if ((y2-y1)> DensityUtils.dip2px(mContext,80)){
                            finishThis();
                        }else {
                            LogUtils.i("竖直方向距离太短，无效操作");
                        }
                    }

                }

                return true;
            }
        });
    }


    @OnClick({R.id.iv_create_dynamic_close, R.id.ll_btn_dynamic, R.id.ll_btn_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_create_dynamic_close:
//                readyGo(VideoDynamicDetailActivity.class);
                finishThis();
                break;
            case R.id.ll_btn_dynamic:
                readyGo(ReleaseDynamicActivity.class);
                finish();
                break;
            case R.id.ll_btn_video:
                readyGo(ReleaseVideoActivity.class);
                finish();
                break;
        }
    }

    private void finishThis() {
        finish();
        overridePendingTransition(R.anim.activity_anim_normal,R.anim.activity_create_dynamic_exit);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void translateAnim(View view){
        Animation translate = new TranslateAnimation(0,0,DensityUtils.dip2px(mContext,600),0);
        translate.setInterpolator(new MyInterpolator());
        translate.setDuration(300);
        view.startAnimation(translate);
    }


}
