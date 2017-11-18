package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class AnnouncementDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_announcement_details)
    ArthurToolBar mTbAnnouncementDetails;
    @Bind(R.id.tv_announcement_details_title)
    TextView mTvAnnouncementDetailsTitle;
    @Bind(R.id.tv_announcement_details_des)
    TextView mTvAnnouncementDetailsDes;
    @Bind(R.id.iv_announcement_details)
    ImageView mIvAnnouncementDetails;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_announcement_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {

        //只有是管理员身份时才可以修改公告，非管理员时设置右边编辑按钮不可用不可见
        // TODO:
        mTbAnnouncementDetails.setBtnEnabled(true,true);
        mTbAnnouncementDetails.setBtnVisibility(true,true);

        mTbAnnouncementDetails.setOnLeftClickListener(this);
        mTbAnnouncementDetails.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //修改公告
        Intent intent = new Intent(this,AddAnnouncementActivity.class);
        startActivity(intent);
    }
}
