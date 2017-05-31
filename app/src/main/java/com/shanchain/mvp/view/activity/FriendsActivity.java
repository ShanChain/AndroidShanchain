package com.shanchain.mvp.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.ContactInfo;
import com.shanchain.utils.ContactInfoUtils;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.PrefUtils;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.dialog.CustomDialog;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FriendsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarFriends;
    @Bind(R.id.textView)
    TextView mTextView;
    @Bind(R.id.btn_friends_start)
    Button mBtnFriendsStart;
    @Bind(R.id.activity_friends)
    LinearLayout mActivityFriends;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friends;
    }

    @Override
    protected void initViewsAndEvents() {
        PrefUtils.putBoolean(this, "isOpen", true);
        boolean isOpen = PrefUtils.getBoolean(this, "isOpen", true);
            initToolBar(isOpen);


    }

    private void initToolBar(boolean isOpen) {
        mToolbarFriends= (ArthurToolBar) findViewById(R.id.toolbar_friends);
        mToolbarFriends.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        if (isOpen){
            //开启通讯录好友
            mToolbarFriends.setBtnEnabled(true);
            mToolbarFriends.setBtnVisibility(true,true);
            mToolbarFriends.setOnLeftClickListener(this);
            mToolbarFriends.setOnRightClickListener(this);
            mToolbarFriends.setImmersive(this, true);
        }else {
            //未开启通讯录好友
            mToolbarFriends.setBtnVisibility(true,false);
            mToolbarFriends.setBtnEnabled(true,false);
            mToolbarFriends.setOnLeftClickListener(this);
            mToolbarFriends.setImmersive(this, true);
        }
    }


    @OnClick(R.id.btn_friends_start)
    public void onClick() {
        System.out.println("=============");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},100);
            }else {
                readContacts();
            }
        }else {
            readContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //授权了
                    readContacts();
                }else {
                    ToastUtils.showToast(FriendsActivity.this,"未授权");
                }
                break;

        }
    }

    private void readContacts() {
        List<ContactInfo> allContactInfos = ContactInfoUtils.getAllContactInfos(this);
        for (int i = 0; i < allContactInfos.size(); i ++) {
            String phone = allContactInfos.get(i).getPhone();
            LogUtils.d(phone);
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
        CustomDialog dialog = new CustomDialog(this,false,0.8,R.layout.dialog_tip_friends,new int[]{R.id.btn_dialog_friends_cancle,R.id.btn_dialog_friends_sure});
        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.btn_dialog_friends_cancle:
                        break;
                    case R.id.btn_dialog_friends_sure:
                        PrefUtils.putBoolean(FriendsActivity.this,"isOpen",false);
                        initViewsAndEvents();
                        break;
                }
            }
        });
        dialog.show();
    }
}
