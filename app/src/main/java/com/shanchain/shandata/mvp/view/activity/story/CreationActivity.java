package com.shanchain.shandata.mvp.view.activity.story;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


public class CreationActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarCreation;
    @Bind(R.id.iv_creation_refresh)
    ImageView mIvCreationRefresh;
    @Bind(R.id.tv_creation_tag1)
    TextView mTvCreationTag1;
    @Bind(R.id.rl_creation)
    RelativeLayout mRlCreation;
    @Bind(R.id.iv_creation_title)
    ImageView mIvCreationTitle;
    @Bind(R.id.activity_creation)
    RelativeLayout mActivityCreation;
    @Bind(R.id.tv_creation_tag2)
    TextView mTvCreationTag2;
    @Bind(R.id.tv_creation_tag3)
    TextView mTvCreationTag3;
    @Bind(R.id.tv_creation_tag4)
    TextView mTvCreationTag4;
    @Bind(R.id.tv_creation_tag5)
    TextView mTvCreationTag5;
    @Bind(R.id.tv_creation_tag6)
    TextView mTvCreationTag6;
    @Bind(R.id.tv_creation_tag7)
    TextView mTvCreationTag7;
    @Bind(R.id.tv_creation_tag8)
    TextView mTvCreationTag8;
    @Bind(R.id.tv_creation_tag9)
    TextView mTvCreationTag9;
    @Bind(R.id.et_creation_tag)
    EditText mEtCreationTag;
    @Bind(R.id.iv_creation_add)
    ImageView mIvCreationAdd;
    @Bind(R.id.et_creation_story_name)
    EditText mEtCreationStoryName;
    @Bind(R.id.et_creation_story_introduce)
    EditText mEtCreationStoryIntroduce;
    private ObjectAnimator mOa;
    private boolean[] isSelected = {false, false, false, false, false, false, false, false, false};
    private String[] addTagNames = {"", ""};
    private ArrayList<Integer> addedText;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_creation;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAnimation();
            }
        }, 500);
    }


    private void initAnimation() {

        startTranslateAnimation(mTvCreationTag1, 1000);
        startTranslateAnimation(mTvCreationTag2, 650);
        startTranslateAnimation(mTvCreationTag3, 820);
        startTranslateAnimation(mTvCreationTag4, 930);
        startTranslateAnimation(mTvCreationTag5, 880);
        startTranslateAnimation(mTvCreationTag6, 700);
        startTranslateAnimation(mTvCreationTag7, 960);

    }

    /**
     * 描述：执行上下位移动画
     */
    private void startTranslateAnimation(TextView tv, int time) {
        mOa = ObjectAnimator.ofFloat(tv, "translationY", 0f, 5f, 12f, 25f);
        mOa.setDuration(time);
        mOa.setRepeatCount(ObjectAnimator.INFINITE);
        mOa.setRepeatMode(ObjectAnimator.REVERSE);
        mOa.start();
        tv.setTag(mOa);
    }

    /**
     * 描述：执行缩放动画
     */
    private void startScaleAnimation(final TextView tv, final int time) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 0.1f, 0.3f, 0.6f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 0.1f, 0.3f, 0.6f, 1f);
        ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(tv, scaleX, scaleY).setDuration(500);

        oa.setInterpolator(new AnticipateOvershootInterpolator(3f));
        oa.start();
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTranslateAnimation(tv, time);
                    }
                }, 500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initData() {
        addedText = new ArrayList<>();
        Glide.with(this)
                .load(R.drawable.photo_heart)
                .transform(new GlideCircleTransform(this))
                .into(mIvCreationTitle);
    }

    private void initToolBar() {
        mToolbarCreation = (ArthurToolBar) findViewById(R.id.toolbar_creation);
        mToolbarCreation.setBtnEnabled(true);
        mToolbarCreation.setOnLeftClickListener(this);
        mToolbarCreation.setOnRightClickListener(this);
    }

    /**
     * 描述：取消view的动画
     */
    private void cancelAnimator(TextView tv) {
        ObjectAnimator oa = (ObjectAnimator) tv.getTag();

        if (oa != null) {
            oa.end();
        } else {
            LogUtils.d(tv + "的动画不存在！");
        }

    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(ParticipantsInfoActivity.class);
    }


    @OnClick({R.id.tv_creation_tag1, R.id.tv_creation_tag2, R.id.tv_creation_tag3, R.id.tv_creation_tag4, R.id.tv_creation_tag5, R.id.tv_creation_tag6, R.id.tv_creation_tag7, R.id.tv_creation_tag8, R.id.tv_creation_tag9, R.id.iv_creation_add, R.id.iv_creation_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_creation_tag1:
                ToastUtils.showToast(this, mTvCreationTag1.getText().toString());
                setTextColor(0);
                break;
            case R.id.tv_creation_tag2:
                setTextColor(1);
                break;
            case R.id.tv_creation_tag3:
                setTextColor(2);
                break;
            case R.id.tv_creation_tag4:
                setTextColor(3);
                break;
            case R.id.tv_creation_tag5:
                setTextColor(4);
                break;
            case R.id.tv_creation_tag6:
                setTextColor(5);
                break;
            case R.id.tv_creation_tag7:
                setTextColor(6);
                break;
            case R.id.tv_creation_tag8:
                setTextColor(7);
                break;
            case R.id.tv_creation_tag9:
                setTextColor(8);
                break;
            case R.id.iv_creation_add:
                addTag();
                break;
            case R.id.iv_creation_title:

                break;
        }
    }

    private void setTextColor(int i) {

        isSelected[i] = !isSelected[i];

        if (isSelected[i] && !addedText.contains(i)) {
            if (addedText.size() == 3) {
                isSelected[addedText.get(0)] = false;
                addedText.remove(0);
                addedText.add(i);

            } else {
                addedText.add(i);
            }
        }

        if (!isSelected[i] && addedText.contains(i)) {
            addedText.remove(new Integer(i));
        }

        mTvCreationTag1.setTextColor(isSelected[0] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag2.setTextColor(isSelected[1] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag3.setTextColor(isSelected[2] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag4.setTextColor(isSelected[3] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag5.setTextColor(isSelected[4] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag6.setTextColor(isSelected[5] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag7.setTextColor(isSelected[6] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag8.setTextColor(isSelected[7] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));
        mTvCreationTag9.setTextColor(isSelected[8] ?
                getResources().getColor(R.color.colorCreationText) : getResources().getColor(R.color.colorWhite));

    }

    private void addTag() {
        String tagName = mEtCreationTag.getText().toString().trim();
        if (TextUtils.isEmpty(tagName)) {
            ToastUtils.showToast(this, "自定义的标签名不能为空");
            return;
        }

        if (addTagNames[0].equals("")) {
            addTagNames[0] = tagName;
            mTvCreationTag8.setVisibility(View.VISIBLE);
            mTvCreationTag8.setText(tagName);
            setTextColor(7);
            startScaleAnimation(mTvCreationTag8, 788);
        } else {
            if (addTagNames[1].equals("")) {
                addTagNames[1] = tagName;
                mTvCreationTag9.setVisibility(View.VISIBLE);
                mTvCreationTag9.setText(tagName);
                setTextColor(8);
                startScaleAnimation(mTvCreationTag9, 745);
            } else {
                ToastUtils.showToast(this, "最多可以添加两个自定义的标签哦~");
            }
        }
    }
}
