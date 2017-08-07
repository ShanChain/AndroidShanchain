package com.shanchain.shandata.mvp.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FriendsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ContactInfo;
import com.shanchain.shandata.mvp.model.FriendsInfo;
import com.shanchain.shandata.utils.ContactsUtils;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.PrefUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FriendsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarFriends;
    @Bind(R.id.textView)
    TextView mTextView;
    @Bind(R.id.activity_friends)
    LinearLayout mActivityFriends;
    @Bind(R.id.btn_friends_start)
    Button mBtnFriendsStart;
    @Bind(R.id.ll_friends_open)
    LinearLayout mLlFriendsOpen;
    @Bind(R.id.xrv_friends)
    XRecyclerView mXrvFriends;
    private List<FriendsInfo> datas;

    private List<FriendsInfo> isShanchainer;
    private List<FriendsInfo> notShanchainer;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friends;
    }

    @Override
    protected void initViewsAndEvents() {

        boolean isOpen = PrefUtils.getBoolean(this, "isOpen", false);
        initToolBar(isOpen);
        initDatas(isOpen);
        initRecyclerView(isOpen);

    }

    private void initDatas(boolean isOpen) {
        if (isOpen) {

            if (datas == null) {
                datas = new ArrayList<>();
                isShanchainer = new ArrayList<>();
                notShanchainer = new ArrayList<>();
                //读取联系人


                for (int i = 0; i < 16; i++) {
                    FriendsInfo friendsInfo = new FriendsInfo();
                    friendsInfo.setAvatarUrl("");
                    friendsInfo.setName("杨志" + i);
                    friendsInfo.setNickName("杨大志");
                    friendsInfo.setFocus(i % 2 == 0 ? true : false);
                    friendsInfo.setShanChainer(i % 3 == 0 ? true : false);
                    if (i % 3 == 0) {
                        isShanchainer.add(friendsInfo);
                    } else {
                        notShanchainer.add(friendsInfo);
                    }
                }

                datas.addAll(isShanchainer);
                datas.addAll(notShanchainer);

            }

        } else {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadingDialog();
            }
        }, 1000);
    }

    private void initRecyclerView(boolean isOpen) {
        if (isOpen) {
            mLlFriendsOpen.setVisibility(View.GONE);
            mXrvFriends.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mXrvFriends.setLayoutManager(linearLayoutManager);

            mXrvFriends.addItemDecoration(new RecyclerViewDivider(FriendsActivity.this,
                    LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(FriendsActivity.this, 1),
                    getResources().getColor(R.color.colorAddFriendDivider)));

            mXrvFriends.setPullRefreshEnabled(false);
            mXrvFriends.setLoadingMoreEnabled(false);
            FriendsAdapter friendsAdapter = new FriendsAdapter(this, R.layout.item_friends, datas);
            mXrvFriends.setAdapter(friendsAdapter);


        } else {
            mLlFriendsOpen.setVisibility(View.VISIBLE);
            mXrvFriends.setVisibility(View.GONE);
        }
    }

    private void initToolBar(boolean isOpen) {
        mToolbarFriends = (ArthurToolBar) findViewById(R.id.toolbar_friends);
        if (isOpen) {
            //开启通讯录好友
            mToolbarFriends.setBtnEnabled(true);
            mToolbarFriends.setBtnVisibility(true, true);
            mToolbarFriends.setOnLeftClickListener(this);
            mToolbarFriends.setOnRightClickListener(this);
        } else {
            //未开启通讯录好友
            mToolbarFriends.setBtnVisibility(true, false);
            mToolbarFriends.setBtnEnabled(true, false);
            mToolbarFriends.setOnLeftClickListener(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权了
                    readContacts();
                } else {
                    ToastUtils.showToast(FriendsActivity.this, "未授权");
                }
                break;

        }
    }

    private void readContacts() {

        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);

        LogUtils.d("读取联系人信息");
       /* List<ContactInfo> allContactInfos = ContactInfoUtils.getAllContactInfos(this);
        for (int i = 0; i < allContactInfos.size(); i++) {
            String phone = allContactInfos.get(i).getPhone();
            String name = allContactInfos.get(i).getName();
            LogUtils.d(name + " : " + phone);
        }*/
        ArrayList<ContactInfo> allCallRecords = ContactsUtils.getAllCallRecords(this);
        for (int i = 0; i < allCallRecords.size(); i++) {
            ContactInfo info = allCallRecords.get(i);
            LogUtils.d(info.getName() + ":" + info.getPhone());

        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        dialog();
    }


    private void dialog() {
        CustomDialog dialog = new CustomDialog(this, false, 0.8, R.layout.dialog_tip_friends, new int[]{R.id.btn_dialog_friends_cancle, R.id.btn_dialog_friends_sure});
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_dialog_friends_cancle:
                        break;
                    case R.id.btn_dialog_friends_sure:
                        PrefUtils.putBoolean(FriendsActivity.this, "isOpen", false);
                        initViewsAndEvents();
                        break;
                }
            }
        });
        dialog.show();
    }


    @OnClick(R.id.btn_friends_start)
    public void onClick() {

        System.out.println("=======启用======");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogUtils.d("版本6.0");
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("未申请权限,正在申请");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
            } else {
                LogUtils.d("已经申请权限");
                readContacts();

            }
        } else {
            LogUtils.d("版本低于6.0");
            readContacts();
        }

        PrefUtils.putBoolean(this, "isOpen", true);
        initViewsAndEvents();
    }

}
