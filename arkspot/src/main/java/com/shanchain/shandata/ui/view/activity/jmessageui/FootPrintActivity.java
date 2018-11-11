package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

public class FootPrintActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{

    private ArthurToolBar arthurToolBar;
    private LinearLayout linearFootPrint;
    private RecyclerView reviewFoodPrint;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_food_print;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();


    }

    private void initView() {
        linearFootPrint = findViewById(R.id.linear_foot_print);
        reviewFoodPrint = findViewById(R.id.review_food_print);
        reviewFoodPrint.setVisibility(View.GONE);
    }

    private void initToolBar() {
        arthurToolBar = findViewById(R.id.toolbar_nav);
        arthurToolBar.isShowChatRoom(false);//不显示群信息
        arthurToolBar.setTitleText("足迹");
        arthurToolBar.setTitleTextColor(Color.WHITE);
        arthurToolBar.setLeftImage(R.mipmap.back);//设置左侧导航栏
        arthurToolBar.setOnLeftClickListener(this);

    }

    @Override
    public void onLeftClick(View v) {
        finish();

    }
}
