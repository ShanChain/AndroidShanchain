package com.shanchain.shandata.mvp.view.activity.mine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MessagesAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.DynamicMessageInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;

public class DynamicActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarMessages;
    @Bind(R.id.smlv_messages)
    SwipeMenuListView mSmlvMessages;

    private ArrayList<DynamicMessageInfo> mdatas;
    private MessagesAdapter mMessagesAdapter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initDatas();
        initListView();
    }

    private void initToolBar() {
        mToolbarMessages = (ArthurToolBar) findViewById(R.id.toolbar_messages);
        mToolbarMessages.setBtnEnabled(true);
        mToolbarMessages.setBtnVisibility(true);
        mToolbarMessages.setOnLeftClickListener(this);
        mToolbarMessages.setOnRightClickListener(this);
    }

    private void initDatas() {
        mdatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DynamicMessageInfo dynamicMessageInfo = new DynamicMessageInfo();
            dynamicMessageInfo.setTitle("有人加入挑战");
            dynamicMessageInfo.setDes("陈梦萍加入你参与的挑战");
            dynamicMessageInfo.setRead(false);
            dynamicMessageInfo.setTime("6月30日16:30");
            mdatas.add(dynamicMessageInfo);
        }
    }

    private void initListView() {

        mMessagesAdapter = new MessagesAdapter(mdatas);
        mSmlvMessages.setAdapter(mMessagesAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建菜单条目
                SwipeMenuItem readItem = new SwipeMenuItem(
                        getApplicationContext());
                // 设置条目背景颜色
                readItem.setBackground(new ColorDrawable(Color.rgb(0xb3, 0xb3,
                        0xb3)));
                // 设置条目宽度
                readItem.setWidth(DensityUtils.dip2px(DynamicActivity.this, 68));
                // 设置条目文本
                readItem.setTitle("已读");
                // 设置文本字体大小
                readItem.setTitleSize(14);
                // 设置字体颜色
                readItem.setTitleColor(Color.WHITE);
                // 添加条目到菜单
                menu.addMenuItem(readItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // 设置背景
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xf6, 0x29,
                        0x1b)));
                // 设置宽度
                deleteItem.setWidth(DensityUtils.dip2px(DynamicActivity.this, 68));
                // 设置条目文本
                deleteItem.setTitle("删除");
                // 设置文本字体大小
                deleteItem.setTitleSize(14);
                // 设置字体颜色
                deleteItem.setTitleColor(Color.WHITE);
                // 添加条目到菜单
                menu.addMenuItem(deleteItem);
            }
        };

        //
        mSmlvMessages.setMenuCreator(creator);


        mSmlvMessages.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        mdatas.get(position).setRead(true);
                        mMessagesAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        // delete
                        mdatas.remove(position);
                        mMessagesAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        mSmlvMessages.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        CustomDialog dialog = new CustomDialog(this,true,true,0.95,R.layout.dialog_dynamic,new int[]{R.id.tv_dialog_delete,R.id.tv_dialog_cancel});

        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_dialog_delete:
                        for (int i = 0; i < mdatas.size(); i ++) {
                            mdatas.clear();
                        }
                        mMessagesAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tv_dialog_cancel:

                        break;
                }
            }
        });
        dialog.show();

    }
}
