package com.shanchain.arkspot.ui.view.activity.chat;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;
import utils.ToastUtils;


public class CreateDramaActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_create_drama)
    ArthurToolBar mTbCreateDrama;
    @Bind(R.id.et_create_drama_name)
    EditText mEtCreateDramaName;
    @Bind(R.id.et_create_drama_des)
    EditText mEtCreateDramaDes;
    @Bind(R.id.ll_create_drama_time)
    LinearLayout mLlCreateDramaTime;
    @Bind(R.id.btn_create_drama_start)
    Button mBtnCreateDramaStart;
    @Bind(R.id.btn_create_drama_cancel)
    Button mBtnCreateDramaCancel;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_create_drama;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mTbCreateDrama.setBtnEnabled(true,false);
        mTbCreateDrama.setOnLeftClickListener(this);
    }


    @OnClick({R.id.ll_create_drama_time, R.id.btn_create_drama_start, R.id.btn_create_drama_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_create_drama_time:
                //设定时间
                break;
            case R.id.btn_create_drama_start:
                //

                startDrama();

                break;
            case R.id.btn_create_drama_cancel:
                finish();
                break;
        }
    }

    private void startDrama() {
        String name = mEtCreateDramaName.getText().toString().trim();
        String des = mEtCreateDramaDes.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            ToastUtils.showToast(this,"戏名不能为空");
            return;
        }

        if (TextUtils.isEmpty(des)) {
            ToastUtils.showToast(this,"对戏描述不能为空");
            return;
        }

        //提交数据
        showLoadingDialog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadingDialog();
                finish();
            }
        }, 2000);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
