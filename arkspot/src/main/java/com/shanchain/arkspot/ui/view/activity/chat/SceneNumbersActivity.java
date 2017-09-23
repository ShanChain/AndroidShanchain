package com.shanchain.arkspot.ui.view.activity.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SceneNumbersAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.SceneNumberInfo;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.shanchain.data.common.utils.ToastUtils;


public class SceneNumbersActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_scene_numbers)
    ArthurToolBar mTbSceneNumbers;
    @Bind(R.id.et_search_scene_numbers)
    EditText mEtSearchSceneNumbers;
    @Bind(R.id.rv_scene_details_numbers)
    RecyclerView mRvSceneDetailsNumbers;
    private List<SceneNumberInfo> datas;
    private SceneNumbersAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_scene_numbers;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSceneDetailsNumbers.setLayoutManager(layoutManager);
        mRvSceneDetailsNumbers.addItemDecoration(new RecyclerViewDivider(this));
        mAdapter = new SceneNumbersAdapter(R.layout.item_scene_all, datas);

        mRvSceneDetailsNumbers.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_all_leave){

                    leaveNumber(position);


                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showToast(SceneNumbersActivity.this,"条目" + position);
            }
        });

    }

    /**
     *  描述：请某人离开当前场景
     */
    private void leaveNumber(final int position) {
        CustomDialog dialog = new CustomDialog(this,false,1.0,R.layout.dialog_leave_number,new int[]{R.id.tv_dialog_leave_sure,R.id.tv_dialog_leave_cancel});
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_leave_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.tv_dialog_leave_sure:
                        ToastUtils.showToast(SceneNumbersActivity.this,"请" + position +"离开");
                        datas.remove(position);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        break;
                }
            }
        });

        dialog.show();

    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            SceneNumberInfo info = new SceneNumberInfo();
            info.setName("没什么啊");
            info.setNum(i + "");
            datas.add(info);
        }
    }

    private void initToolBar() {
        mTbSceneNumbers.setOnLeftClickListener(this);
        mTbSceneNumbers.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
