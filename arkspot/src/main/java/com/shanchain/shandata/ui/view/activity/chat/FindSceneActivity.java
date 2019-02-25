package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FindSceneAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.FindSceneInfo;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class FindSceneActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_edit_scene)
    ArthurToolBar mTbEditScene;
    @Bind(R.id.iv_find_scene_img)
    CircleImageView mIvFindSceneImg;
    @Bind(R.id.tv_find_scene_name)
    TextView mTvFindSceneName;
    @Bind(R.id.tv_find_scene_des)
    TextView mTvFindSceneDes;
    @Bind(R.id.tv_scene_details_numbers)
    TextView mTvSceneDetailsNumbers;
    @Bind(R.id.ll_find_scene_numbers)
    LinearLayout mLlFindSceneNumbers;
    @Bind(R.id.rv_find_scene_numbers)
    RecyclerView mRvFindSceneNumbers;
    @Bind(R.id.tv_find_scene_title)
    TextView mTvFindSceneTitle;
    @Bind(R.id.tv_find_scene_dynamic)
    TextView mTvFindSceneDynamic;
    @Bind(R.id.tv_find_scene_notify)
    TextView mTvFindSceneNotify;
    @Bind(R.id.btn_find_scene_in)
    Button mBtnFindSceneIn;
    @Bind(R.id.tv_find_scene_result)
    TextView mTvFindSceneResult;
    private List<FindSceneInfo> data;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_find_scene;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvFindSceneNumbers.setLayoutManager(layoutManager);
        FindSceneAdapter findSceneAdapter = new FindSceneAdapter(R.layout.item_scene_numbers, data);
        mRvFindSceneNumbers.setAdapter(findSceneAdapter);
        findSceneAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            FindSceneInfo info = new FindSceneInfo();
            data.add(info);
        }
    }

    private void initToolBar() {
        mTbEditScene.setOnLeftClickListener(this);
        mTbEditScene.setBtnEnabled(true, false);
    }

    @OnClick({R.id.iv_find_scene_img, R.id.ll_find_scene_numbers, R.id.btn_find_scene_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_find_scene_img:
                //群头像

                break;
            case R.id.ll_find_scene_numbers:
                //全部成员
                Intent numberIntent = new Intent(this,SceneNumbersActivity.class);
                startActivity(numberIntent);
                break;
            case R.id.btn_find_scene_in:
                //加入群
                partInGroup();
                break;
        }
    }

    /**
     * 描述：加入某群组
     */
    private void partInGroup() {
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBtnFindSceneIn.setVisibility(View.GONE);
                mTvFindSceneResult.setVisibility(View.VISIBLE);
                closeLoadingDialog();
            }
        }, 1000);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
