package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SceneDetailsAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.ui.model.ComUserInfo;
import com.shanchain.arkspot.ui.model.GroupMemberBean;
import com.shanchain.arkspot.ui.model.SceneImgInfo;
import com.shanchain.arkspot.ui.model.UserDetailInfo;
import com.shanchain.arkspot.ui.view.activity.story.ReportActivity;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.MarqueeText;
import com.shanchain.arkspot.widgets.switchview.SwitchView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

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
    @Bind(R.id.iv_scene_details_modify)
    ImageView mIvSceneDetailsModify;
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
    @Bind(R.id.iv_scene_details_all)
    ImageView mIvSceneDetailsAll;
    @Bind(R.id.rl_scene_detail_check)
    RelativeLayout mRlSceneDetailCheck;
    private boolean mIsGroup;
    private String mToChatName;

    private String mCurrentUser;
    private List<GroupMemberBean> mMembers;
    private ComUserInfo mGroupOwner;
    /**
     * 描述：头像显示的数据集合
     */
    private List<SceneImgInfo> mSceneImgInfos = new ArrayList<>();
    /**
     * 描述：管理员数据集合
     */
    private List<ComUserInfo> mAdmin = new ArrayList<>();
    private SceneDetailsAdapter mAdapter;

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
        mAdapter = new SceneDetailsAdapter(R.layout.item_scene_numbers, mSceneImgInfos);
        mRvSceneDetailsNumbers.setAdapter(mAdapter);
    }

    private void onInitView() {

        if (mMembers == null) {
            return;
        }
        //默认隐藏加群验证设置
        mRlSceneDetailCheck.setVisibility(View.GONE);
        //当前用户是群主时，显示加群验证设置
        if (mCurrentUser.equals(mGroupOwner.getUserName())) {
            mRlSceneDetailCheck.setVisibility(View.VISIBLE);
        }
        //当前用户是群管理时，显示加群验证设置
        for (int i = 0; i < mAdmin.size(); i++) {
            if (mCurrentUser.equals(mAdmin.get(i).getUserName())) {
                mRlSceneDetailCheck.setVisibility(View.VISIBLE);
            }
        }

       // mTvSceneDetailsName.setText(mSceneTotalInfo.getGroupName());



    }

    private void initData() {
        showLoadingDialog();
        Intent intent = getIntent();
        mIsGroup = intent.getBooleanExtra("isGroup", false);
        mToChatName = intent.getStringExtra("toChatName");
        mCurrentUser = EMClient.getInstance().getCurrentUser();
        LogUtils.d("是否是群组 " + mIsGroup);
        LogUtils.d("聊天对象 " + mToChatName);
        if (mIsGroup) {
            //群
            mIvSceneDetailsAll.setVisibility(View.VISIBLE);
            mBtnSceneDetailsLeave.setVisibility(View.VISIBLE);
            mTvSceneDetailsAnnouncement.setVisibility(View.VISIBLE);

           //自己服务器极客接口获取群信息


        } else {
            //单聊
            mIvSceneDetailsAll.setVisibility(View.GONE);
            mRlSceneDetailCheck.setVisibility(View.GONE);
            mBtnSceneDetailsLeave.setVisibility(View.GONE);
            mTvSceneDetailsAnnouncement.setVisibility(View.GONE);


            //获取对聊人信息
            SCHttpUtils.post()
                    .url(HttpApi.HX_USER_DETAIL)
                    .addParams("userName", "sc-738727063")
                    .build()
                    .execute(new SCHttpCallBack<UserDetailInfo>(UserDetailInfo.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.e("获取用户信息失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(UserDetailInfo response, int id) {
                            closeLoadingDialog();
                            if (response == null) {
                                LogUtils.d("..获取...sc-738727191....null.........");
                                return;
                            }


                            LogUtils.d("????获取到用户信息" + response.getUserInfo().getUserName());
                            SceneImgInfo sceneImgInfo = new SceneImgInfo();
                            sceneImgInfo.setCharacterId(response.getCharacterId());
                            sceneImgInfo.setUserName(response.getName());
                            sceneImgInfo.setImg(response.getHeadImg());
                            mSceneImgInfos.add(sceneImgInfo);
                            mAdapter.notifyDataSetChanged();
                            mTvSceneDetailsNumbers.setText("角色  " + "(" + mSceneImgInfos.size() + ")");
                        }

                    });

            //获取当前用户信息
            SCHttpUtils.post()
                    .url(HttpApi.HX_USER_DETAIL)
                    .addParams("userName", mCurrentUser)
                    .build()
                    .execute(new SCHttpCallBack<UserDetailInfo>(UserDetailInfo.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.e("获取用户信息失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(UserDetailInfo response, int id) {
                            closeLoadingDialog();
                            if (response == null) {
                                LogUtils.d("..获取...mCurrentUser....null.........");
                                return;
                            }
                            LogUtils.d("获取到用户信息" + response.getUserInfo().getUserName());
                            SceneImgInfo sceneImgInfo = new SceneImgInfo();
                            sceneImgInfo.setCharacterId(response.getCharacterId());
                            sceneImgInfo.setUserName(response.getName());
                            sceneImgInfo.setImg(response.getHeadImg());
                            mSceneImgInfos.add(sceneImgInfo);
                            mAdapter.notifyDataSetChanged();
                            mTvSceneDetailsNumbers.setText("角色  " + "(" + mSceneImgInfos.size() + ")");
                        }

                    });
            onInitView();
        }

    }

    private void initToolBar() {
        mTbSceneDetails.setOnLeftClickListener(this);
        mTbSceneDetails.setOnRightClickListener(this);
    }

    @OnClick({R.id.tv_scene_details_announcement, R.id.iv_scene_details_img, R.id.iv_scene_details_modify, R.id.ll_scene_details_numbers, R.id.tv_scene_details_all, R.id.tv_scene_details_history, R.id.btn_scene_details_leave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_scene_details_announcement:
                //公告
                lookAnnouncement();
                break;
            case R.id.iv_scene_details_img:
                //场景图片
                Intent intent = new Intent(this, EditSceneActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_scene_details_modify:
                //修改群信息

                break;
            case R.id.ll_scene_details_numbers:
                //所有角色
                if (mIsGroup) {
                    allNumbers();
                } else {
                    return;
                }
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
        showLoadingDialog();
        ToastUtils.showToast(this, "退群成功！");
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(mToChatName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeLoadingDialog();
                            ToastUtils.showToast(SceneDetailsActivity.this, "退群成功");
                            ActivityManager.getInstance().finishActivity(ChatRoomActivity.class);
                            ActivityManager.getInstance().finishActivity(ContactActivity.class);
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeLoadingDialog();
                            ToastUtils.showToast(SceneDetailsActivity.this, "退群失败");
                        }
                    });
                }
            }
        });


    }

    private void allNumbers() {
        Intent intent = new Intent(this, SceneNumbersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("members", (Serializable) mMembers);
        intent.putExtras(bundle);
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
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel, R.id.tv_report_dialog_code});
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
                    case R.id.tv_report_dialog_code:
                        //二维码

                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }

}
