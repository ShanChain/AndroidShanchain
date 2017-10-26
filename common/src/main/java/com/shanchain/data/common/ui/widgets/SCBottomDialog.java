package com.shanchain.data.common.ui.widgets;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.common.R;
import com.shanchain.data.common.utils.LogUtils;

import java.util.List;


public class SCBottomDialog extends CustomDialog {
    private List<String> items;

    public SCBottomDialog(Context context) {
        super(context, true, 1.0, R.layout.commen_rn_dialog_bottom, new int[]{R.id.tv_bottom_dialog_top, R.id.tv_bottom_dialog_cancel,R.id.tv_bottom_dialog_3_top
        ,R.id.tv_bottom_dialog_3_mid,R.id.tv_bottom_dialog_3_bottom});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvCancel = (TextView) findViewById(R.id.tv_bottom_dialog_cancel);
        TextView tvTop = (TextView) findViewById(R.id.tv_bottom_dialog_top);
        LinearLayout ll_3 = (LinearLayout) findViewById(R.id.ll_bottom_dialog_3);
        TextView tvTop_3 = (TextView) findViewById(R.id.tv_bottom_dialog_3_top);
        TextView tvMid_3 = (TextView) findViewById(R.id.tv_bottom_dialog_3_mid);
        TextView tvBtm_3 = (TextView) findViewById(R.id.tv_bottom_dialog_3_bottom);
        int size = items.size();
        LogUtils.i("====items==== " + size);
        switch (size){

            case 2:
                ll_3.setVisibility(View.GONE);
                tvTop.setVisibility(View.VISIBLE);
                tvTop.setText(items.get(0));
                tvCancel.setText(items.get(1));
                break;
            case 3:
                ll_3.setVisibility(View.VISIBLE);
                tvTop.setVisibility(View.GONE);
                tvMid_3.setVisibility(View.GONE);
                tvTop_3.setText(items.get(0));
                tvBtm_3.setText(items.get(1));
                tvCancel.setText(items.get(2));
                break;
            case 4:
                ll_3.setVisibility(View.VISIBLE);
                tvTop.setVisibility(View.GONE);
                tvTop_3.setText(items.get(0));
                tvMid_3.setText(items.get(1));
                tvBtm_3.setText(items.get(2));
                tvCancel.setText(items.get(3));
                break;
            default:


                break;
        }


        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                TextView tv = (TextView) view;
                String btnValue = tv.getText().toString();
                mBottomCallBack.btnClick(btnValue);
                dismiss();
            }
        });

    }


    public void setItems(List<String> items){
        this.items = items;
    }

    public void setCallback(BottomCallBack bottomCallBack) {
       this.mBottomCallBack = bottomCallBack;
    }

    private BottomCallBack mBottomCallBack;

    public interface BottomCallBack{
        void btnClick(String btnValue);
    }

}
