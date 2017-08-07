package com.shanchain.shandata.mvp.view.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.login.ResetPasswordActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class BindResultActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int BIND_TYPE_WEIBO = 10;
    private static final int BIND_TYPE_WECHAT = 20;
    private static final int BIND_TYPE_QQ = 30;
    private static final int BIND_TYPE_PHONE = 40;
    private static final int BIND_TYPE_EMAIL = 50;


    private static final int OPERATION_PHONE_RESET_PWD = 200;
    private static final int OPERATION_EMAIL_RESET_PWD = 400;

    ArthurToolBar mToolbarBindResult;
    @Bind(R.id.iv_bind_result_icon)
    ImageView mIvBindResultIcon;
    @Bind(R.id.tv_bind_result_type)
    TextView mTvBindResultType;
    @Bind(R.id.tv_bind_result_des)
    TextView mTvBindResultDes;
    @Bind(R.id.activity_bind_result)
    LinearLayout mActivityBindResult;
    @Bind(R.id.btn_bind_result_reset)
    Button mBtnBindResultReset;
    private int bindType = BIND_TYPE_PHONE;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bind_result;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initToolBar();

    }

    private void initData() {
        Intent intent = getIntent();
        bindType = intent.getIntExtra("bindType", 10);
    }

    private void initToolBar() {
        mToolbarBindResult = (ArthurToolBar) findViewById(R.id.toolbar_bind_result);

        switch (bindType) {
            case BIND_TYPE_WEIBO:
                mToolbarBindResult.setTitleText("绑定微博");
                mIvBindResultIcon.setImageResource(R.mipmap.bing_link_weibo_default);
                mTvBindResultType.setText("微博");
                mTvBindResultDes.setText("已成功绑定，您可以通过微博账号登录善数者");
                mBtnBindResultReset.setVisibility(View.GONE);
                break;
            case BIND_TYPE_WECHAT:
                mToolbarBindResult.setTitleText("绑定微信");
                mIvBindResultIcon.setImageResource(R.mipmap.bing_link_wechat_default);
                mTvBindResultType.setText("微信");
                mTvBindResultDes.setText("已成功绑定，您可以通过微信账号登录善数者");
                mBtnBindResultReset.setVisibility(View.GONE);
                break;
            case BIND_TYPE_QQ:
                mToolbarBindResult.setTitleText("绑定QQ");
                mIvBindResultIcon.setImageResource(R.mipmap.bing_link_wechat_default);
                mTvBindResultType.setText("QQ");
                mTvBindResultDes.setText("已成功绑定，您可以通过QQ账号登录善数者");
                mBtnBindResultReset.setVisibility(View.GONE);
                break;
            case BIND_TYPE_PHONE:
                mToolbarBindResult.setTitleText("绑定手机");
                mIvBindResultIcon.setImageResource(R.mipmap.bing_link_phone_default);
                mTvBindResultType.setText("手机号码:15374326832");
                mTvBindResultDes.setText("已成功绑定，您可以通过手机账号登录善数者");
                mBtnBindResultReset.setVisibility(View.VISIBLE);
                break;
            case BIND_TYPE_EMAIL:
                mToolbarBindResult.setTitleText("绑定邮箱");
                mIvBindResultIcon.setImageResource(R.mipmap.bing_link_mailbox_default);
                mTvBindResultType.setText("邮箱");
                mTvBindResultDes.setText("已成功绑定，您可以通过邮箱账号登录善数者");
                mBtnBindResultReset.setVisibility(View.VISIBLE);
                break;

        }

        mToolbarBindResult.setBtnEnabled(true);
        mToolbarBindResult.setBtnVisibility(true);
        mToolbarBindResult.setOnLeftClickListener(this);
        mToolbarBindResult.setOnRightClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    CustomDialog customDialog;

    @Override
    public void onRightClick(View v) {

        switch (bindType) {
            case BIND_TYPE_WEIBO:
                customDialog = new CustomDialog(this, false, 0.8, R.layout.dialog_bind_account_weibo, new int[]{R.id.tv_dialog_account_cancel, R.id.tv_dialog_account_sure});
                break;
            case BIND_TYPE_WECHAT:
                customDialog = new CustomDialog(this, false, 0.8, R.layout.dialog_bind_account_wechat, new int[]{R.id.tv_dialog_account_cancel, R.id.tv_dialog_account_sure});
                break;
            case BIND_TYPE_QQ:
                customDialog = new CustomDialog(this, false, 0.8, R.layout.dialog_bind_account_qq, new int[]{R.id.tv_dialog_account_cancel, R.id.tv_dialog_account_sure});
                break;
            case BIND_TYPE_PHONE:
                customDialog = new CustomDialog(this, true, true, 0.95, R.layout.dialog_bind_account_phone, new int[]{R.id.tv_dialog_account_cancel, R.id.tv_dialog_account_sure});
                break;
            case BIND_TYPE_EMAIL:
                customDialog = new CustomDialog(this, true, true, 0.95, R.layout.dialog_bind_account_phone, new int[]{R.id.tv_dialog_account_cancel, R.id.tv_dialog_account_sure});
                break;
        }
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_account_cancel:

                        break;
                    case R.id.tv_dialog_account_sure:
                        //返回上一页
                        back();
                        break;
                }
            }
        });
        customDialog.show();

    }

    private void back() {
        //请求解绑接口

        finish();
    }

    @OnClick(R.id.btn_bind_result_reset)
    public void onClick() {
        switch (bindType) {
            case BIND_TYPE_PHONE:
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("operationType", OPERATION_PHONE_RESET_PWD);
                startActivity(intent);
                break;
            case BIND_TYPE_EMAIL:
                Intent intent2 = new Intent(this, ResetPasswordActivity.class);
                intent2.putExtra("operationType", OPERATION_EMAIL_RESET_PWD);
                startActivity(intent2);
                break;
        }
    }
}
