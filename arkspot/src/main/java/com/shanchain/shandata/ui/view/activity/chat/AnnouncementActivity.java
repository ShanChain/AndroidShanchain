package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AnnouncementAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.ui.model.AnnouncementInfo;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.net.SCHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(AnnouncementActivity.this,AnnouncementDetailsActivity.class);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                deleteAnnouncement(position);
                return true;
            }
        });
    }

    /**
     * 描述：删除一条公告
     * 只有管理员身份才可以删除
     */
    private void deleteAnnouncement(final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_delete_announcement, null);
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
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");

        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_GET_NOTICE)
                .addParams("groupId",groupId)
                .addParams("page","0")
                .addParams("size","20")
                .addParams("sort","desc")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取群公告失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("获取群公告:" + response);
                    }
                });

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
        final CustomDialog customDialog = new CustomDialog(this, true, 1, R.layout.dialog_add_new_announcement, new int[]{R.id.tv_dialog_add_announcement, R.id.tv_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_add_announcement:
                        Intent intent = new Intent(AnnouncementActivity.this, AddAnnouncementActivity.class);
                        startActivity(intent);
                        customDialog.dismiss();
                        break;
                    case R.id.tv_dialog_cancel:
                        customDialog.dismiss();
                        break;
                }
            }
        });

        customDialog.show();
    }
}
