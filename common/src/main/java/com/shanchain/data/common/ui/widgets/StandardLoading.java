package com.shanchain.data.common.ui.widgets;

import android.content.Context;
import android.os.Bundle;

import com.shanchain.common.R;

/**
 * Created by flyye on 2017/9/28.
 */

public class StandardLoading extends CustomDialog{

    public StandardLoading(Context context){
        super(context, 0.4, R.layout.common_dialog_progress, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }
}
