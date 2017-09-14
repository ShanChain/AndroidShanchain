package com.shanchain.arkspot.ui.view.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.AnnouncementAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.AnnouncementInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class AnnouncementActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_announcement)
    ArthurToolBar mTbAnnouncement;
    @Bind(R.id.rv_announcement)
    RecyclerView mRvAnnouncement;
    private List<AnnouncementInfo> data;
    private AnnouncementAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_announcement;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvAnnouncement.setLayoutManager(layoutManager);
        mRvAnnouncement.addItemDecoration(new RecyclerViewDivider(this));

        mAdapter = new AnnouncementAdapter(R.layout.item_announcement, data);

        mRvAnnouncement.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                deleteAnnouncement(position);
                return true;
            }
        });
    }

    /**
     *  描述：删除一条公告
     *      只有管理员身份才可以删除
     */
    private void deleteAnnouncement(final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this,R.layout.dialog_delete_announcement,null);
        TextView tvDelete = (TextView) view.findViewById(R.id.tv_dialog_delete_announcement);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AnnouncementInfo announcementInfo = new AnnouncementInfo();
            data.add(announcementInfo);
        }
    }

    private void initToolBar() {
        mTbAnnouncement.setOnLeftClickListener(this);
        mTbAnnouncement.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
