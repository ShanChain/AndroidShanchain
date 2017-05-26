package com.shanchain.mvp.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.SystemUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchNickActivity extends BaseActivity {

    @Bind(R.id.ll_add_search)
    LinearLayout mLlAddSearch;
    @Bind(R.id.tv_search_cancle)
    TextView mTvSearchCancle;
    @Bind(R.id.rv_search_nicks)
    RecyclerView mRvSearchNicks;
    @Bind(R.id.activity_search_nick)
    LinearLayout mActivitySearchNick;
    @Bind(R.id.et_search_nick)
    EditText mEtSearchNick;
    @Bind(R.id.v_statusbar)
    View mImmersiveView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_nick;
    }

    @Override
    protected void initViewsAndEvents() {

        init();

        mEtSearchNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //请求后台数据

                LogUtils.d(s + "");
            }
        });
    }
    //初始化沉浸式
    private void init() {

        int statusBarHeight = SystemUtils.getStatusBarHeight(mContext);
        LogUtils.d(statusBarHeight + "statusBarHeight");
        mImmersiveView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight));
        mImmersiveView.setBackgroundColor(Color.GRAY);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @OnClick(R.id.tv_search_cancle)
    public void onClick() {
        finish();
    }



}
