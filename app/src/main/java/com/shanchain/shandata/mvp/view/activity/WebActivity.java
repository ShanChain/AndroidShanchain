package com.shanchain.shandata.mvp.view.activity;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.http.HttpApi;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.other.CustomView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import okhttp3.Call;

public class WebActivity extends BaseActivity {

    @Bind(R.id.tv_web)
    TextView mTvWeb;
    @Bind(R.id.cv_web)
    CustomView mCvWeb;
    @Bind(R.id.activity_web)
    LinearLayout mActivityWeb;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initViewsAndEvents() {
        OkHttpUtils.get()
                .url(HttpApi.GET_USER_INFO)
                .addParams("userId", "23")
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("" + id);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("成功" + response);
            }
        });
    }

}
