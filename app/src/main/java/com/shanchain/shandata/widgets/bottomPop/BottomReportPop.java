package com.shanchain.shandata.widgets.bottomPop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.LogUtils;

public class BottomReportPop extends BaseBottomPop {

    private ViewGroup mView;

    public BottomReportPop(Context mContext, View.OnClickListener clickListener) {
        super(mContext,clickListener);

    }

    @Override
    public View initViewAndEvent() {
        mView = (ViewGroup) ViewGroup.inflate(mContext, R.layout.pop_report,null);
        View firstView = mView.getChildAt(0);
        int childCount = mView.getChildCount();
        int id = firstView.getId();
        LogUtils.d(id+"=====" + childCount);


        TextView btnPopReport= (TextView) mView.findViewById(R.id.btn_pop_report);
        TextView btnPopCancle= (TextView) mView.findViewById(R.id.btn_pop_cancle);
        btnPopCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 设置按钮监听
        btnPopReport.setOnClickListener(mOnClickListener);

        return mView;
    }

    @Override
    public void initPopStyle() {
        TextView topView = (TextView) mView.findViewById(R.id.btn_pop_report);
        setPopStyle(mView,topView);
    }
}
