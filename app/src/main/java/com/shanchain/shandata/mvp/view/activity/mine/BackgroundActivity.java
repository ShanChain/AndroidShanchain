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
    /*@Bind(R.id.rl_background_spring)
    RelativeLayout mRlBackgroundSpring;
    @Bind(R.id.rl_background_summer)
    RelativeLayout mRlBackgroundSummer;
    @Bind(R.id.rl_background_fall)
    RelativeLayout mRlBackgroundFall;
    @Bind(R.id.rl_background_winter)
    RelativeLayout mRlBackgroundWinter;
    @Bind(R.id.activity_background)
    LinearLayout mActivityBackground;*/

    private int[] resBackgrounds = {R.drawable.mine_bg_spring_default,R.drawable.mine_bg_summer_default,R.drawable.mine_bg_fall_default,R.drawable.mine_bg_winter_default};
    private ArrayList<BackgroundInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_background;
    }

    @Override
    protected void initViewsAndEvents() {
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
                getResources().getColor(R.color.colorTransparent)));
        backgroundAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                PrefUtils.putInt(BackgroundActivity.this, "bgPath", resBackgrounds[position-1]);
                intent.putExtra("bg", resBackgrounds[position-1]);
                setResult(RESULT_CODE_SPRING, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initToolBar() {
        mToolbarBackground = (ArthurToolBar) findViewById(R.id.toolbar_background);
        mToolbarBackground.setBtnVisibility(true, false);
        mToolbarBackground.setBtnEnabled(true, false);
        mToolbarBackground.setOnLeftClickListener(this);
    }


   /* @OnClick({R.id.rl_background_spring, R.id.rl_background_summer, R.id.rl_background_fall, R.id.rl_background_winter})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.rl_background_spring:
                PrefUtils.putInt(this, "bgPath", R.drawable.mine_bg_spring_default);
                intent.putExtra("bg", R.drawable.mine_bg_spring_default);
                setResult(RESULT_CODE_SPRING, intent);
                finish();
                break;
            case R.id.rl_background_summer:
                PrefUtils.putInt(this, "bgPath", R.drawable.mine_bg_summer_default);
                intent.putExtra("bg", R.drawable.mine_bg_summer_default);
                setResult(RESULT_CODE_SUMMER, intent);
                finish();
                break;
            case R.id.rl_background_fall:
                PrefUtils.putInt(this, "bgPath", R.drawable.mine_bg_fall_default);
                intent.putExtra("bg", R.drawable.mine_bg_fall_default);
                setResult(RESULT_CODE_FALL, intent);
                finish();
                break;
            case R.id.rl_background_winter:
                PrefUtils.putInt(this, "bgPath", R.drawable.mine_bg_winter_default);
                intent.putExtra("bg", R.drawable.mine_bg_winter_default);
                setResult(RESULT_CODE_WINTER, intent);
                finish();
                break;
        }
    }*/

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
