package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SceneDetailsAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.SceneDetailsInfo;
import com.shanchain.arkspot.ui.view.activity.AnnouncementActivity;
import com.shanchain.arkspot.ui.view.activity.story.ReportActivity;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.MarqueeText;
import com.shanchain.arkspot.widgets.switchview.SwitchView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import utils.ToastUtils;

public class SceneDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    @Bind(R.id.tb_scene_details)
    ArthurToolBar mTbSceneDetails;
    @Bind(R.id.tv_scene_details_announcement)
    MarqueeText mTvSceneDetailsAnnouncement;
    @Bind(R.id.iv_scene_details_img)
    ImageView mIvSceneDetailsImg;
    @Bind(R.id.cv_scene_details)
    CardView mCvSceneDetails;
    @Bind(R.id.tv_scene_details_name)
    TextView mTvSceneDetailsName;
    @Bind(R.id.iv_scene_details_code)
    ImageView mIvSceneDetailsCode;
    @Bind(R.id.tv_scene_details_des)
    TextView mTvSceneDetailsDes;
    @Bind(R.id.tv_scene_details_numbers)
    TextView mTvSceneDetailsNumbers;
    @Bind(R.id.ll_scene_details_numbers)
    LinearLayout mLlSceneDetailsNumbers;
    @Bind(R.id.tv_scene_details_all)
    TextView mTvSceneDetailsAll;
    @Bind(R.id.tv_scene_details_history)
    TextView mTvSceneDetailsHistory;
    @Bind(R.id.sv_scene_details_msg)
    SwitchView mSvSceneDetailsMsg;
    @Bind(R.id.sv_scene_details_top)
    SwitchView mSvSceneDetailsTop;
    @Bind(R.id.sv_scene_details_validation)
    SwitchView mSvSceneDetailsValidation;
    @Bind(R.id.btn_scene_details_leave)
    Button mBtnSceneDetailsLeave;
    @Bind(R.id.rv_scene_details_numbers)
    RecyclerView mRvSceneDetailsNumbers;
    private List<SceneDetailsInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_scene_details;
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
        mRvSceneDetailsNumbers.setLayoutManager(layoutManager);
        SceneDetailsAdapter adapter = new SceneDetailsAdapter(R.layout.item_scene_numbers, datas);
        mRvSceneDetailsNumbers.setAdapter(adapter);
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            SceneDetailsInfo info = new SceneDetailsInfo();
            datas.add(info);
        }
    }

    private void initToolBar() {
        mTbSceneDetails.setOnLeftClickListener(this);
        mTbSceneDetails.setOnRightClickListener(this);
    }


    @OnClick({R.id.tv_scene_details_announcement,R.id.iv_scene_details_img, R.id.iv_scene_details_code, R.id.ll_scene_details_numbers, R.id.tv_scene_details_all, R.id.tv_scene_details_history, R.id.btn_scene_details_leave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_scene_details_announcement:
                //公告
                lookAnnouncement();
                break;
            case R.id.iv_scene_details_img:
                //场景图片

                break;
            case R.id.iv_scene_details_code:
                //二维码图片

                break;
            case R.id.ll_scene_details_numbers:
                //所有角色
                allNumbers();
                break;
            case R.id.tv_scene_details_all:
                //全部对戏

                break;
            case R.id.tv_scene_details_history:
                //历史大戏

                break;
            case R.id.btn_scene_details_leave:
                //离开(退群)
                leave();
                break;
        }
    }

    /**
     *  描述：查看公告历史列表
     */
    private void lookAnnouncement() {
        Intent intent = new Intent(this, AnnouncementActivity.class);
        startActivity(intent);
    }

    /**
     *  描述：离开当前群
     */
    private void leave() {
        final CustomDialog customDialog = new CustomDialog(this,false,1.0,R.layout.dialog_leave_scene,new int[]{R.id.tv_dialog_leave_cancel,R.id.tv_dialog_leave_sure});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_leave_cancel:
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_leave_sure:
                        leaveScene();
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }

    /**
     *  描述：
     */
    private void leaveScene() {
        //退群操作
        ToastUtils.showToast(this,"退群成功！");
    }

    private void allNumbers() {
        Intent intent = new Intent(this,SceneNumbersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //举报
        report();
    }

    private void report() {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        startActivity(reportIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }

}
