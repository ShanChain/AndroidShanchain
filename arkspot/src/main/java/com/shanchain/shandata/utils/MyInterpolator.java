package com.shanchain.shandata.utils;

import android.view.animation.Interpolator;

/**
 * Created by zhoujian on 2017/12/19.
 */

public class MyInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        float factor = 0.4f;
        float v = (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);

        return v;
    }
}
