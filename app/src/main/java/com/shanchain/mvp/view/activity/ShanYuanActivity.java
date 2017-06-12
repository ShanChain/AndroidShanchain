package com.shanchain.mvp.view.activity;

import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class ShanYuanActivity extends BaseActivity {

    @Bind(R.id.sl_shan_yuan)
    SuitLines mSlShanYuan;
    @Bind(R.id.activity_shan_yuan)
    LinearLayout mActivityShanYuan;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_shan_yuan;
    }

    @Override
    protected void initViewsAndEvents() {
        mSlShanYuan.setLineStyle(SuitLines.SOLID);
        //折线颜色
        mSlShanYuan.setDefaultOneLineColor(getResources().getColor(R.color.colorBlack));
        //背景颜色
        mSlShanYuan.setBackgroundColor(getResources().getColor(R.color.colorActive));

        List<Unit> lines = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Unit unit = new Unit(new Random().nextInt(32), i + "");
            lines.add(unit);
        }
        mSlShanYuan.feedWithAnim(lines);

    }

}
