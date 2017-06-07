package com.shanchain.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.R;
import com.shanchain.adapter.PositionAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.PositionInfo;
import com.shanchain.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class PositionActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    public static final int RESULT_CODE = 20;
    @Bind(R.id.et_position_search)
    EditText mEtPositionSearch;
    @Bind(R.id.rv_position_list)
    RecyclerView mRvPositionList;
    @Bind(R.id.activity_position)
    LinearLayout mActivityPosition;
    private ArthurToolBar mPositionToolBar;
    private List<PositionInfo> datas;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_position;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPositionList.setLayoutManager(linearLayoutManager);
        PositionAdapter adapter = new PositionAdapter(this,R.layout.item_position,datas);
        mRvPositionList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //返回位置信息
                returnPosition(position);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void returnPosition(int position) {
        Intent intent = new Intent();
        intent.putExtra("positionInfo",datas.get(position));
        setResult(RESULT_CODE,intent);
        finish();
    }

    private void initDatas() {
        datas = new ArrayList<>();
        for (int i = 0; i < 30; i ++) {
            PositionInfo positionInfo = new PositionInfo();
            positionInfo.setAddress("软件产业基地" + i);
            positionInfo.setDetails("中国,广东省,深圳市,南山区");
            datas.add(positionInfo);
        }
    }

    private void initToolBar() {
        mPositionToolBar = (ArthurToolBar) findViewById(R.id.toolbar_position);
        mPositionToolBar.setBtnEnabled(true,false);
        mPositionToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
