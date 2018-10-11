package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SceneDetailsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdGroupMemberInfo;
import com.shanchain.shandata.ui.model.NoticeBean;
import com.shanchain.shandata.ui.model.SceneDetailData;
import com.shanchain.shandata.ui.presenter.SceneDetailsPresenter;
import com.shanchain.shandata.ui.presenter.impl.SceneDetailsPresenterImpl;
import com.shanchain.shandata.ui.view.activity.chat.view.SceneDetailsView;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.MarqueeText;
import com.shanchain.shandata.widgets.switchview.SwitchView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SceneDetailsActivity extends BaseActivity implements SceneDetailsView, ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


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
    @Bind(R.id.iv_scene_details_modify)
    ImageView mIvSceneDetailsModify;
    @Bind(R.id.tv_scene_details_des)
    TextView mTvSceneDetailsDes;
    @Bind(R.id.tv_scene_details_numbers)
    TextView mTvSceneDetailsNumbers;
    @Bind(R.id.ll_scene_details_numbers)
    LinearLayout mLlSceneDetailsNumbers;
    @Bind(R.id.sv_scene_details_msg)
    SwitchView mSvSceneDetailsMsg;
    @Bind(R.id.sv_scene_details_top)
    SwitchView mSvSceneDetailsTop;
    @Bind(R.id.btn_scene_details_leave)
    Button mBtnSceneDetailsLeave;
    @Bind(R.id.rv_scene_details_numbers)
    RecyclerView mRvSceneDetailsNumbers;
    @Bind(R.id.iv_scene_details_all)
    ImageView mIvSceneDetailsAll;
    @Bind(R.id.rl_group_info)
    RelativeLayout mRlGroupInfo;
    private boolean mIsGroup;
    private String mToChatName;

    private String mCurrentUser;
    /**
     * 描述：头像显示的数据集合
     */
    private List<BdGroupMemberInfo> mSceneImgInfos = new ArrayList<>();
    /**
     * 描述：管理员数据集合
     */
    private SceneDetailsAdapter mAdapter;
    private SceneDetailsPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_scene_details;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new SceneDetailsPresenterImpl(this);
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSceneDetailsNumbers.setLayoutManager(layoutManager);
        mAdapter = new SceneDetailsAdapter(R.layout.item_scene_numbers, mSceneImgInfos);
        mRvSceneDetailsNumbers.setAdapter(mAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        mIsGroup = intent.getBooleanExtra("isGroup", false);
        mToChatName = intent.getStringExtra("toChatName");
        mCurrentUser = EMClient.getInstance().getCurrentUser();
        LogUtils.d("是否是群组 " + mIsGroup);
        LogUtils.d("聊天对象 " + mToChatName);
        if (mIsGroup) {
            showLoadingDialog(true);
            //自己服务器极客接口获取群信息
            mPresenter.getGroupInfo(mToChatName);
            mIvSceneDetailsAll.setVisibility(View.VISIBLE);
            mBtnSceneDetailsLeave.setVisibility(View.VISIBLE);
            mTvSceneDetailsAnnouncement.setVisibility(View.GONE);
            mRlGroupInfo.setVisibility(View.VISIBLE);
        } else {
            mIvSceneDetailsAll.setVisibility(View.GONE);
            mBtnSceneDetailsLeave.setVisibility(View.GONE);
            mTvSceneDetailsAnnouncement.setVisibility(View.GONE);
            mRlGroupInfo.setVisibility(View.GONE);
            mPresenter.getUserInfo(mToChatName);
        }

    }

    private void initToolBar() {
        mTbSceneDetails.setOnLeftClickListener(this);
        mTbSceneDetails.setBtnEnabled(true,false);
        mTbSceneDetails.setBtnVisibility(true,false);
       // mTbSceneDetails.setOnRightClickListener(this);
    }

    @OnClick({R.id.tv_scene_details_announcement, R.id.iv_scene_details_img, R.id.iv_scene_details_modify, R.id.ll_scene_details_numbers, R.id.btn_scene_details_leave,R.id.rl_group_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_scene_details_announcement:
                //公告
                lookAnnouncement();
                break;
            case R.id.iv_scene_details_img:
                //场景图片

                break;
            case R.id.iv_scene_details_modify:
                //修改群信息

                break;
            case R.id.ll_scene_details_numbers:
                //所有角色
                if (mIsGroup) {
                    //allNumbers();
                } else {
                    return;
                }
                break;

            case R.id.btn_scene_details_leave:
                //离开(退群)
                leave();
                break;
            case R.id.rl_group_info:
                Intent intent = new Intent(this, EditSceneActivity.class);
                intent.putExtra("groupId",mToChatName);
                startActivity(intent);
                break;
        }
    }

    /**
     * 描述：查看公告历史列表
     */
    private void lookAnnouncement() {
        Intent intent = new Intent(this, AnnouncementActivity.class);
        intent.putExtra("groupId", mToChatName);
        startActivity(intent);
    }

    /**
     * 描述：离开当前群
     */
    private void leave() {
        final CustomDialog customDialog = new CustomDialog(this, false, 1.0, R.layout.common_dialog_leave_scene, new int[]{R.id.tv_dialog_leave_cancel, R.id.tv_dialog_leave_sure});
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
     * 描述：退群
     */
    private void leaveScene() {
        //退群操作
        mPresenter.leaveGroup(mToChatName);
    }

    private void allNumbers() {
        Intent intent = new Intent(this, SceneNumbersActivity.class);
        List<BdGroupMemberInfo> data = mAdapter.getData();
        if (data != null) {
            String memberList = JSONObject.toJSONString(data);
            intent.putExtra("member", memberList);
        }
        startActivity(intent);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //举报
        //report();
    }

    private void report() {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel, R.id.tv_report_dialog_code});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("groupReport",true);
                        startActivity(reportIntent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_code:
                        //二维码

                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }

    @Override
    public void initGroupInfoSuc(SceneDetailData sceneDetailInfo, List<BdGroupMemberInfo> bdInfos) {
        //群

        closeLoadingDialog();
        if (sceneDetailInfo != null) {
            GlideUtils.load(mContext, sceneDetailInfo.getIconUrl(), mIvSceneDetailsImg, 0);
            mTvSceneDetailsDes.setText(sceneDetailInfo.getGroupDesc());
            mTvSceneDetailsName.setText(sceneDetailInfo.getGroupName());
        }

        if (bdInfos != null) {
            mAdapter.setNewData(bdInfos);
            mAdapter.notifyDataSetChanged();
            mTvSceneDetailsNumbers.setText("角色  ("+bdInfos.size()+")");
        }
    }

    @Override
    public void initUserInfo(List<BdGroupMemberInfo> bdInfos) {
        //单聊

        if (bdInfos != null) {
            mAdapter.setNewData(bdInfos);
            mAdapter.notifyDataSetChanged();
            mTvSceneDetailsNumbers.setText("角色  ("+bdInfos.size()+")");
        }

    }

    @Override
    public void initNoticeSuc(NoticeBean noticeBean) {
        if (noticeBean != null){
            String notice = noticeBean.getNotice();
            mTvSceneDetailsAnnouncement.setText(notice);
        }

    }

    @Override
    public void leaveGroupSuc(boolean result) {
        if (result){
            ToastUtils.showToast(mContext,"退群成功");
            if (ChatRoomActivity.instance!=null){
                ChatRoomActivity.instance.finish();
            }
//            ActivityStackManager.getInstance().finishActivity(ChatRoomActivity.class);
            finish();
        }else {
            ToastUtils.showToast(mContext,"退群失败");
        }
    }

}
