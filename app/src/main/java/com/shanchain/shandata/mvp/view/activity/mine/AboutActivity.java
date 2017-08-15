package com.shanchain.shandata.mvp.view.activity.mine;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.ChatRoomActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarShieldedPersons;
    @Bind(R.id.tv_about)
    TextView mTvAbout;
    @Bind(R.id.tv_about_weibo)
    TextView mTvAboutWeibo;
    @Bind(R.id.tv_about_wechat)
    TextView mTvAboutWechat;
    @Bind(R.id.tv_about_web)
    TextView mTvAboutWeb;
    @Bind(R.id.tv_about_introduce)
    TextView mTvAboutIntroduce;
    @Bind(R.id.tv_about_version)
    TextView mTvAboutVersion;
    @Bind(R.id.tv_about_user_agreement)
    TextView mTvAboutUserAgreement;
    @Bind(R.id.activity_about)
    LinearLayout mActivityAbout;
    @Bind(R.id.ll_about_version)
    LinearLayout mLlAboutVersion;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        login();
    }


    private void login() {
        String mUserName = "test1";
        EMClient.getInstance().login(mUserName, "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ToastUtils.showToast(AboutActivity.this, "登录成功！");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showToast(AboutActivity.this, "登录失败！");
                LogUtils.d("登录失败原因：" +s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private void initToolBar() {
        mToolbarShieldedPersons = (ArthurToolBar) findViewById(R.id.toolbar_shielded_persons);
        mToolbarShieldedPersons.setBtnEnabled(true,false);
        mToolbarShieldedPersons.setBtnVisibility(true,false);
        mToolbarShieldedPersons.setOnLeftClickListener(this);
    }

    @OnClick({R.id.tv_about_weibo, R.id.tv_about_wechat, R.id.tv_about_web, R.id.tv_about_introduce, R.id.tv_about_user_agreement,R.id.ll_about_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_about_weibo:
                readyGo(ChatRoomActivity.class);
                break;
            case R.id.tv_about_wechat:

                break;
            case R.id.tv_about_web:

                break;
            case R.id.tv_about_introduce:

                break;
            case R.id.tv_about_user_agreement:

                break;
            case R.id.ll_about_version:
                showLoadingDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeLoadingDialog();
                        ToastUtils.showToast(AboutActivity.this,"已是最新版本");
                    }
                }, 1500);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
