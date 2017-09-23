package com.shanchain.arkspot.ui.view.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.AttentionAdapter;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.arkspot.ui.view.activity.story.ReportActivity;
import com.shanchain.arkspot.ui.view.activity.story.StoryChainActivity;
import com.shanchain.arkspot.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.ToastUtils;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class AttentionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.xrv_attention)
    RecyclerView mXrvAttention;
    @Bind(R.id.srl_fragment_attention)
    SwipeRefreshLayout mSrlFragmentAttention;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_attention, null);
    }

    @Override
    public void initData() {
        mXrvAttention.setLayoutManager(new LinearLayoutManager(mActivity));
        final List<StoryInfo> datas = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            StoryInfo info = new StoryInfo();
            if (i % 4 == 0) {
                info.setItemType(StoryInfo.type1);
            } else if (i % 4 == 1) {
                info.setItemType(StoryInfo.type2);
            } else if (i % 4 == 2) {
                info.setItemType(StoryInfo.type3);
            } else if (i % 4 == 3) {
                info.setItemType(StoryInfo.type4);
            }
            datas.add(info);
        }
        mSrlFragmentAttention.setColorSchemeColors(getResources().getColor(R.color.colorDialogBtn));
        mSrlFragmentAttention.setOnRefreshListener(this);


        AttentionAdapter adapter = new AttentionAdapter(datas);
        mXrvAttention.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mXrvAttention.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_item_story_avatar:
                        clickAvatar(position);
                        break;
                    case R.id.iv_item_story_more:
                        report();
                        break;
                    case R.id.tv_item_story_forwarding:
                        clickForwarding(position);
                        break;
                    case R.id.tv_item_story_comment:
                        clickComment(position);
                        break;
                    case R.id.tv_item_story_like:
                        clickLike(position);
                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int viewType = adapter.getItemViewType(position);
                switch (viewType) {
                    case StoryInfo.type1:
                        //类型1的条目点击事件
                        Intent intentType1 = new Intent(mActivity, StoryChainActivity.class);
                        startActivity(intentType1);
                        break;
                    case StoryInfo.type2:
                        //类型2的条目点击事件
                        Intent intentType2 = new Intent(mActivity, DynamicDetailsActivity.class);
                        startActivity(intentType2);
                        break;
                    case StoryInfo.type3:
                        //类型3的条目点击事件
                        Intent intentType3 = new Intent(mActivity, DynamicDetailsActivity.class);
                        startActivity(intentType3);
                        break;
                    case StoryInfo.type4:
                        //类型4的条目点击事件
                        Intent intentType4 = new Intent(mActivity, TopicDetailsActivity.class);
                        startActivity(intentType4);
                        break;
                }
            }
        });

    }

    /**
     *  描述：头像的点击事件
     *
     */
    private void clickAvatar(int position) {
        ToastUtils.showToast(mActivity, "头像");
    }

    /**
     *  描述：转发的点击事件
     *
     */
    private void clickForwarding(int position) {
        ToastUtils.showToast(mActivity, "转发");
    }

    /**
     *  描述：评论的点击事件
     *
     */
    private void clickComment(int position) {
        Intent intentComment = new Intent(mActivity,DynamicDetailsActivity.class);
        startActivity(intentComment);
    }

    /**
     *  描述：喜欢的点击事件
     */
    private void clickLike(int position) {

    }

    private void report() {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_shielding, R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_shielding:
                        //屏蔽
                        showShieldingDialog();
                        customDialog.dismiss();
                        break;
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

    private void showShieldingDialog() {
        final CustomDialog shieldingDialog = new CustomDialog(mActivity, false, 1, R.layout.dialog_shielding, new int[]{R.id.tv_shielding_dialog_cancel, R.id.tv_shielding_dialog_sure});
        shieldingDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_shielding_dialog_cancel:
                        shieldingDialog.dismiss();
                        break;
                    case R.id.tv_shielding_dialog_sure:
                        //确定屏蔽，请求接口

                        shieldingDialog.dismiss();
                        break;
                }
            }
        });
        shieldingDialog.show();
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSrlFragmentAttention.setRefreshing(false);
            }
        }, 3000);
    }

}
