package com.shanchain.data.common.rn.modules;

import android.app.Activity;
import android.view.View;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.shanchain.common.R;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhoujian on 2017/10/31.
 */

public class BirthdayPickerModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "BirthdayPicker";

    public BirthdayPickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void show(final Callback callback) {
        final Activity topActivity = ActivityStackManager.getInstance().getTopActivity();
        SCTimePickerView pickerView = new SCTimePickerView.Builder(topActivity, new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String format = simpleDateFormat.format(date);
                callback.invoke(format);
            }

        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false)
                .setCancelText("清除")
                .setCancelColor(topActivity.getResources().getColor(R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setSubCalSize(14)
                .setTitleBgColor(topActivity.getResources().getColor(R.color.colorWhite))
                .setSubmitColor(topActivity.getResources().getColor(R.color.colorDialogBtn))
                .build();
        pickerView.setDate(Calendar.getInstance());
        pickerView.show();

      pickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
          @Override
          public void onCancelClick(View v) {
              callback.invoke("clear");
          }
      });
    }


}
