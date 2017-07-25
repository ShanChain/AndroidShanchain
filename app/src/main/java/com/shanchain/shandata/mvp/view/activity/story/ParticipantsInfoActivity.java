package com.shanchain.shandata.mvp.view.activity.story;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

import static android.animation.ObjectAnimator.ofFloat;

public class ParticipantsInfoActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarParticipantsInfo;
    @Bind(R.id.rb_participants_info_time)
    RadioButton mRbParticipantsInfoTime;
    @Bind(R.id.rl_participants_info_time)
    RelativeLayout mRlParticipantsInfoTime;
    @Bind(R.id.rb_participants_info_address)
    RadioButton mRbParticipantsInfoAddress;
    @Bind(R.id.rl_participants_info_address)
    RelativeLayout mRlParticipantsInfoAddress;
    @Bind(R.id.rb_participants_info_text)
    RadioButton mRbParticipantsInfoText;
    @Bind(R.id.rl_participants_info_text)
    RelativeLayout mRlParticipantsInfoText;
    @Bind(R.id.rb_participants_info_photo)
    RadioButton mRbParticipantsInfoPhoto;
    @Bind(R.id.rl_participants_info_photo)
    RelativeLayout mRlParticipantsInfoPhoto;
    @Bind(R.id.et_participants_info_param1)
    EditText mEtParticipantsInfoParam1;
    @Bind(R.id.iv_participants_info_delete1)
    ImageView mIvParticipantsInfoDelete1;
    @Bind(R.id.ll_participants_info_param1)
    RelativeLayout mLlParticipantsInfoParam1;
    @Bind(R.id.et_participants_info_param2)
    EditText mEtParticipantsInfoParam2;
    @Bind(R.id.iv_participants_info_delete2)
    ImageView mIvParticipantsInfoDelete2;
    @Bind(R.id.ll_participants_info_param2)
    RelativeLayout mLlParticipantsInfoParam2;
    @Bind(R.id.et_participants_info_param3)
    EditText mEtParticipantsInfoParam3;
    @Bind(R.id.iv_participants_info_delete3)
    ImageView mIvParticipantsInfoDelete3;
    @Bind(R.id.ll_participants_info_param3)
    RelativeLayout mLlParticipantsInfoParam3;
    @Bind(R.id.tv_participants_info_param_add)
    TextView mTvParticipantsInfoParamAdd;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_participants_info;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarParticipantsInfo = (ArthurToolBar) findViewById(R.id.toolbar_participants_info);
        mToolbarParticipantsInfo.setBtnEnabled(true);
        mToolbarParticipantsInfo.setOnLeftClickListener(this);
        mToolbarParticipantsInfo.setOnRightClickListener(this);

    }


    @OnClick({R.id.rl_participants_info_time, R.id.rb_participants_info_time, R.id.rb_participants_info_address, R.id.rb_participants_info_text, R.id.rb_participants_info_photo, R.id.rl_participants_info_address, R.id.rl_participants_info_text, R.id.rl_participants_info_photo, R.id.iv_participants_info_delete1, R.id.iv_participants_info_delete2, R.id.iv_participants_info_delete3, R.id.tv_participants_info_param_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_participants_info_time:
                mRbParticipantsInfoTime.setChecked(mRbParticipantsInfoTime.isChecked());
                break;
            case R.id.rl_participants_info_time:
                mRbParticipantsInfoTime.setChecked(!mRbParticipantsInfoTime.isChecked());
                break;
            case R.id.rb_participants_info_address:
                mRbParticipantsInfoAddress.setChecked(mRbParticipantsInfoAddress.isChecked());
                break;
            case R.id.rl_participants_info_address:
                mRbParticipantsInfoAddress.setChecked(!mRbParticipantsInfoAddress.isChecked());
                break;
            case R.id.rb_participants_info_text:
                mRbParticipantsInfoText.setChecked(!mRbParticipantsInfoText.isChecked());
                break;
            case R.id.rl_participants_info_text:
                mRbParticipantsInfoText.setChecked(!mRbParticipantsInfoText.isChecked());
                break;
            case R.id.rb_participants_info_photo:
                mRbParticipantsInfoPhoto.setChecked(!mRbParticipantsInfoPhoto.isChecked());
                break;
            case R.id.rl_participants_info_photo:
                mRbParticipantsInfoPhoto.setChecked(!mRbParticipantsInfoPhoto.isChecked());
                break;
            case R.id.iv_participants_info_delete1:
                performAnimation(mLlParticipantsInfoParam1);
                paramShow[0] = false;
                break;
            case R.id.iv_participants_info_delete2:
                performAnimation(mLlParticipantsInfoParam2);
                paramShow[1] = false;
                break;
            case R.id.iv_participants_info_delete3:
                performAnimation(mLlParticipantsInfoParam3);
                paramShow[2] = false;
                break;
            case R.id.tv_participants_info_param_add:
                if (paramShow[0] && paramShow[1] && paramShow[2]) {
                    ToastUtils.showToast(this, "最多可添加3条自定义参数哦~");
                } else {
                    addParam();
                }
                break;


        }
    }

    private boolean[] paramShow = {false, false, false};

    private void performAnimation(final RelativeLayout view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotationX", 0f, 90f);
        oa.setDuration(500);
        oa.start();
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                ObjectAnimator oa2 = ofFloat(view, "rotationX", 90f, 0f);
                oa2.setDuration(10);
                oa2.setRepeatCount(0);
                oa2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void addParam() {

        for (int i = 0; i < paramShow.length; i++) {
            if (paramShow[i] == false) {
                paramShow[i] = true;
                break;
            }
        }

        mLlParticipantsInfoParam1.setVisibility(paramShow[0] ? View.VISIBLE : View.GONE);
        mLlParticipantsInfoParam2.setVisibility(paramShow[1] ? View.VISIBLE : View.GONE);
        mLlParticipantsInfoParam3.setVisibility(paramShow[2] ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(CreateSuccessActivity.class);
    }

}
