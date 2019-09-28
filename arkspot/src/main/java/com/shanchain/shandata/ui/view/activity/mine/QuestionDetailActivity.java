package com.shanchain.shandata.ui.view.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.QuestionBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/9/23
 * Describe :问题详情
 */
public class QuestionDetailActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_about)
    ArthurToolBar tbAbout;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private QuestionBean mQuestionBean;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        mQuestionBean = (QuestionBean) getIntent().getSerializableExtra("info");
        initToolBar();
    }

    private void initToolBar() {
        tbAbout.setTitleText(getString(R.string.answer_questions));
        tbAbout.setTitleTextColor(Color.BLACK);
        tbAbout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbAbout.setOnLeftClickListener(this);
        tvTitle.setText(mQuestionBean.getTitle());
        switch (mQuestionBean.getId()){
            case 2:
                tvDetail.setText(getString(R.string.question_detail_1));
                break;
            case 3:
                tvDetail.setText(getString(R.string.question_detail_2));
                break;
            case 4:
                tvDetail.setText(getString(R.string.question_detail_3));
                break;
            case 5:
                tvDetail.setText(getString(R.string.question_detail_4));
                break;
            case 6:
                tvDetail.setText(getString(R.string.question_detail_5));
                break;
            case 7:
                tvDetail.setText(getString(R.string.question_detail_6));
                break;
            case 8:
                tvDetail.setText(getString(R.string.question_detail_7));
                break;

        }


    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
