package com.shanchain.shandata.ui.view.activity.login;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GuidePagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {

    @Bind(R.id.vp_guide)
    ViewPager mVpGuide;
    @Bind(R.id.point_guide_1)
    View mPointGuide1;
    @Bind(R.id.point_guide_2)
    View mPointGuide2;
    @Bind(R.id.point_guide_3)
    View mPointGuide3;
    @Bind(R.id.point_guide_4)
    View mPointGuide4;
    @Bind(R.id.ll_guide_points)
    LinearLayout mLlGuidePoints;
    @Bind(R.id.btn_guide_join)
    TextView mBtnGuideJoin;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViewsAndEvents() {
        GuidePagerAdapter adapter = new GuidePagerAdapter(getSupportFragmentManager());
        mVpGuide.setAdapter(adapter);
        selectPoint(0);
        mVpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPoint(position);
                if (position == 3){
                    mLlGuidePoints.setVisibility(View.GONE);
                    mBtnGuideJoin.setVisibility(View.VISIBLE);
                }else {
                    mLlGuidePoints.setVisibility(View.VISIBLE);
                    mBtnGuideJoin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btn_guide_join)
    public void onClick() {
        PrefUtils.putBoolean(mContext, Constants.SP_KEY_GUIDE,true);
        readyGo(FootPrintNewActivity.class);
        finish();
    }

    private void selectPoint(int position) {
        switch (position) {
            case 0:
                mPointGuide1.setSelected(true);
                mPointGuide2.setSelected(false);
                mPointGuide3.setSelected(false);
                mPointGuide4.setSelected(false);
                break;
            case 1:
                mPointGuide1.setSelected(false);
                mPointGuide2.setSelected(true);
                mPointGuide3.setSelected(false);
                mPointGuide4.setSelected(false);
                break;
            case 2:
                mPointGuide1.setSelected(false);
                mPointGuide2.setSelected(false);
                mPointGuide3.setSelected(true);
                mPointGuide4.setSelected(false);
                break;
            case 3:
                mPointGuide1.setSelected(false);
                mPointGuide2.setSelected(false);
                mPointGuide3.setSelected(false);
                mPointGuide4.setSelected(true);
                break;
        }
    }

}
