package com.shanchain.shandata.mvp.view.activity.mine;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.BackgroundAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.BackgroundInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class BackgroundActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private static final int RESULT_CODE_SPRING = 10;
    private static final int RESULT_CODE_SUMMER = 20;
    private static final int RESULT_CODE_FALL = 30;
    private static final int RESULT_CODE_WINTER = 40;
    ArthurToolBar mToolbarBackground;
    @Bind(R.id.xrv_background)
    XRecyclerView mXrvBackground;


    private int[] resBackgrounds = {R.drawable.mine_bg_spring_default,R.drawable.mine_bg_summer_default,R.drawable.mine_bg_fall_default,R.drawable.mine_bg_winter_default};
    private ArrayList<BackgroundInfo> mDatas;
    private boolean mIsBg;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_background;
    }

    @Override
    protected void initViewsAndEvents() {
        mIsBg = getIntent().getBooleanExtra("isBg", true);
        initToolBar();
        initData();
        initRecycler();
    }

    private void initData() {
        String[] desBackground = getResources().getStringArray(R.array.des_background);
        String[] typeBackground = getResources().getStringArray(R.array.type_background);
        mDatas = new ArrayList<>();
        for (int i = 0; i < resBackgrounds.length; i ++) {
            BackgroundInfo backgroundInfo = new BackgroundInfo();
            backgroundInfo.setDes(desBackground[i]);
            backgroundInfo.setRes(resBackgrounds[i]);
            backgroundInfo.setType(typeBackground[i]);
            mDatas.add(backgroundInfo);
        }
    }

    private void initRecycler() {
        mXrvBackground.setLayoutManager(new LinearLayoutManager(this));
        mXrvBackground.setLoadingMoreEnabled(false);
        mXrvBackground.setPullRefreshEnabled(false);
        BackgroundAdapter backgroundAdapter = new BackgroundAdapter(this,R.layout.item_background,mDatas);
        mXrvBackground.setAdapter(backgroundAdapter);
        mXrvBackground.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,10),
                getResources().getColor(R.color.colorWhite)));
        backgroundAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mIsBg){
                    Intent intent = new Intent();
                    PrefUtils.putInt(BackgroundActivity.this, "bgPath", resBackgrounds[position-1]);
                    intent.putExtra("bg", resBackgrounds[position-1]);
                    setResult(RESULT_CODE_SPRING, intent);
                    finish();
                }else {

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initToolBar() {
        mToolbarBackground = (ArthurToolBar) findViewById(R.id.toolbar_background);
        mToolbarBackground.setTitleText(mIsBg?"背景图":"卡片夹");
        mToolbarBackground.setBtnVisibility(true, false);
        mToolbarBackground.setBtnEnabled(true, false);
        mToolbarBackground.setOnLeftClickListener(this);
    }


    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
