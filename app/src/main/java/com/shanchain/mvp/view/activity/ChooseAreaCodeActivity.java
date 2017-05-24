package com.shanchain.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.adapter.AreaAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;

public class ChooseAreaCodeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private static final int RESULTCODE = 10;
    /** 描述：顶部工具栏*/
    @Bind(R.id.toolbar_area)
    ArthurToolBar mToolbarArea;
    /** 描述：区域列表*/
    @Bind(R.id.rv_area)
    RecyclerView mRvArea;
    /** 描述：总布局*/
    @Bind(R.id.activity_choose_area_code)
    LinearLayout mActivityChooseAreaCode;

    static ArrayList<String> areaList = new ArrayList<>();
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_area_code;
    }

    @Override
    protected void initViewsAndEvents() {
        //初始化数据
        initData();
        //初始化工具栏
        initToolBar();
        initRecycleView();
    }

    /**
     *  2017/5/17
     *  描述：初始化数据
     *
     */
    private void initData() {
        for (int i = 0; i < 80; i ++) {
            if (i%3==0){
                areaList.add("中国大陆 +" +i);
            }if (i%3==1){
                areaList.add("中国台湾  +8" + i );
            }if (i%3==2){
                areaList.add("中国香港 +2" + i);
            }
        }
    }

    /**
     *  2017/5/17
     *  描述：初始化recycleView
     *
     */
    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvArea.setLayoutManager(layoutManager);
        AreaAdapter<String> adapter = new AreaAdapter<>(ChooseAreaCodeActivity.this,areaList);
        mRvArea.setAdapter(adapter);
        adapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                String area = areaList.get(position);
                String[] result = area.split("\\+");
                if (result.length>=2){
                    String code = result[1].trim();
                    Intent intent = new Intent();
                    intent.putExtra("areacode","+" + code);
                    setResult(RESULTCODE,intent);
                    finish();
                }

            }
        });
    }

    /**
     *  2017/5/17
     *  描述：初始化工具栏,沉浸式和按钮事件
     *
     */
    private void initToolBar() {
        //没图标时,右侧设置为不可用
        mToolbarArea.setBtnEnabled(true,false);

        //设置沉浸式
        mToolbarArea.setImmersive(this, true);
        mToolbarArea.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        //顶部工具栏左侧按钮点击事件
        mToolbarArea.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
