package com.shanchain.data.common.ui.widgets;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.common.R;
import com.shanchain.data.common.base.Callback;

/**
 * Created by hmg on 2019/10/19
 * 登陆打卡
 */

public class SCCheckInDialog {

    private String mTitle;
    private String mPlaceHolder;
    private AlertDialog mAlertDialog;
    private Context mContext;

    public SCCheckInDialog(@NonNull Context context, String title, String placeHolder) {
        this.mTitle = title;
        this.mPlaceHolder = placeHolder;
        mAlertDialog = new AlertDialog.Builder(context).create();
        mContext = context;
    }

    public void show() {
        View view = View.inflate(mContext, R.layout.common_dialog_checkin, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvNums = (TextView) view.findViewById(R.id.tv_nums);
        TextView tvTip = view.findViewById(R.id.tv_tip);
        ImageView imageView = view.findViewById(R.id.iv_closed);

        tvTitle.setText(mTitle);
        String arr[] = mPlaceHolder.split(",");
        tvTip.setText(arr[0]);
        tvNums.setText(arr[1]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
            }
        });
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(true);
        mAlertDialog.show();
    }

    public void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }


}
