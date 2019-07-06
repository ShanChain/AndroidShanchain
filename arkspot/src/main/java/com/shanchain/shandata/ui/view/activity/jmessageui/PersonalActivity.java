package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.vod.common.utils.ThreadUtil;
import com.aliyun.vod.common.utils.ToastUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.impl.SelectAddressInterface;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.SelectAddressDialog;
import com.shanchain.shandata.utils.SharePreferenceManager;
import com.shanchain.shandata.widgets.photochoose.ChoosePhoto;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ${chenyn} on 2017/2/23.
 */

public class PersonalActivity extends BaseActivity implements SelectAddressInterface, ArthurToolBar.OnLeftClickListener, View.OnClickListener {

    public static final int SIGN = 1;
    public static final int FLAGS_SIGN = 2;
    public static final String SIGN_KEY = "sign_key";

    public static final int NICK_NAME = 4;
    public static final int FLAGS_NICK = 3;
    public static final String NICK_NAME_KEY = "nick_name_key";

    private RelativeLayout mRl_cityChoose;
    private TextView mTv_city;
    private SelectAddressDialog dialog;
    private RelativeLayout mRl_gender;
    private RelativeLayout mRl_birthday;

    private TextView mTv_birthday;
    private TextView mTv_gender;
    private RelativeLayout mSign;
    private TextView mTv_sign;
    private RelativeLayout mRl_nickName;


    Intent intent;
    private TextView mTv_nickName;
    private ImageView mIv_photo;
    private ChoosePhoto mChoosePhoto;
    private UserInfo mMyInfo;
    private TextView mTv_userName;
    private RelativeLayout mRl_zxing;
    private ArthurToolBar tbMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

//        Window window = this.getWindow();
//        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        window.setStatusBarColor(getResources().getColor(R.color.line_normal));

        initView();
        initListener();
        initData();
    }

    private void initToolBar() {
        tbMain = findViewById(R.id.tb_main);
        tbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        tbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbMain.getTitleView().setLayoutParams(layoutParams);
        tbMain.setTitleText(getString(R.string.personal_information));
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(this);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    private void initData() {
        final Dialog dialog = DialogCreator.createLoadingDialog(PersonalActivity.this,
                PersonalActivity.this.getString(R.string.jmui_loading));
        dialog.show();
        mMyInfo = JMessageClient.getMyInfo();
        if (mMyInfo != null) {
            mTv_nickName.setText(mMyInfo.getNickname());
            SharePreferenceManager.setRegisterUsername(mMyInfo.getNickname());
            mTv_userName.setText(getString(R.string.user_name)+":" + mMyInfo.getUserName());
            mTv_sign.setText(mMyInfo.getSignature());
            UserInfo.Gender gender = mMyInfo.getGender();
            if (gender != null) {
                if (gender.equals(UserInfo.Gender.male)) {
                    mTv_gender.setText(R.string.man);
                } else if (gender.equals(UserInfo.Gender.female)) {
                    mTv_gender.setText(getString(R.string.woman));
                } else {
                    mTv_gender.setText(getString(R.string.unknown));
                }
            }
            long birthday = mMyInfo.getBirthday();
            if (birthday == 0) {
                mTv_birthday.setText("");
            } else {
                Date date = new Date(mMyInfo.getBirthday());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mTv_birthday.setText(format.format(date));
            }
            mTv_city.setText(mMyInfo.getAddress());
            mMyInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int responseCode, String responseMessage, Bitmap avatarBitmap) {
                    if (responseCode == 0) {
                        mIv_photo.setImageBitmap(avatarBitmap);
                    } else {
                        mIv_photo.setImageResource(R.drawable.rc_default_portrait);
                    }
                }
            });
            dialog.dismiss();
        }
    }

    private void initListener() {
        mRl_cityChoose.setOnClickListener(this);
        mRl_birthday.setOnClickListener(this);
        mRl_gender.setOnClickListener(this);
        mSign.setOnClickListener(this);
        mRl_nickName.setOnClickListener(this);
        mIv_photo.setOnClickListener(this);
        mRl_zxing.setOnClickListener(this);
    }

    private void initView() {
        initToolBar();
        mRl_cityChoose = (RelativeLayout) findViewById(R.id.rl_cityChoose);
        mTv_city = (TextView) findViewById(R.id.tv_city);
        mRl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        mRl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        mTv_birthday = (TextView) findViewById(R.id.tv_birthday);
        mTv_gender = (TextView) findViewById(R.id.tv_gender);
        mSign = (RelativeLayout) findViewById(R.id.sign);
        mTv_sign = (TextView) findViewById(R.id.tv_sign);
        mRl_nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        mTv_nickName = (TextView) findViewById(R.id.tv_nickName);
        mIv_photo = (ImageView) findViewById(R.id.iv_photo);
        mTv_userName = (TextView) findViewById(R.id.tv_userName);
        mRl_zxing = (RelativeLayout) findViewById(R.id.rl_zxing);

        mChoosePhoto = new ChoosePhoto();
        mChoosePhoto.setPortraitChangeListener(PersonalActivity.this, mIv_photo, 2);

    }

    @Override
    public void setAreaString(String area) {
        mTv_city.setText(area);
    }

    @Override
    public void setTime(String time) {
    }

    @Override
    public void setGender(String gender) {
        mTv_gender.setText(gender);
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(PersonalActivity.this, ModifyUserInfoActivity.class);
        switch (v.getId()) {
            case R.id.iv_photo:
                //头像
                if ((ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(PersonalActivity.this, R.string.permission_check, Toast.LENGTH_SHORT).show();
                }
                mChoosePhoto.setInfo(PersonalActivity.this, true);
                mChoosePhoto.showPhotoDialog(PersonalActivity.this);
                break;
            case R.id.rl_nickName:
                //昵称
//                intent.setFlags(FLAGS_NICK);
//                intent.putExtra("old_nick", mMyInfo.getNickname());
//                startActivityForResult(intent, NICK_NAME);
                startActivity(intent);
                break;
            case R.id.sign:
                //签名
//                intent.setFlags(FLAGS_SIGN);
//                intent.putExtra("old_sign", mMyInfo.getSignature());
//                startActivityForResult(intent, SIGN);
                startActivity(intent);
                break;
            case R.id.rl_gender:
                //弹出性别选择器
                dialog = new SelectAddressDialog(PersonalActivity.this);
                dialog.showGenderDialog(PersonalActivity.this, mMyInfo);
                break;
            case R.id.rl_birthday:
                //弹出时间选择器选择生日
                TimePickerView timePickerView = new TimePickerView.Builder(PersonalActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {
                        mMyInfo.setBirthday(date.getTime());
                        JMessageClient.updateMyInfo(UserInfo.Field.birthday, mMyInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage) {
                                if (responseCode == 0) {
                                    mTv_birthday.setText(getDataTime(date));
                                    Toast.makeText(PersonalActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PersonalActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                        .setType(new boolean[]{true, true, true,false, false, false})
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.str_sure))
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setOutSideCancelable(true)
                        .isCyclic(true)
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setSubmitColor(Color.GRAY)//确定按钮文字颜色
                        .setCancelColor(Color.GRAY)//取消按钮文字颜色
                        .isCenterLabel(false)
                        .build();
                timePickerView.show();
//                dialog = new SelectAddressDialog(PersonalActivity.this);
//                dialog.showDateDialog(PersonalActivity.this, mMyInfo);
                break;
            case R.id.rl_cityChoose:
                //点击选择省市
                dialog = new SelectAddressDialog(PersonalActivity.this,
                        PersonalActivity.this, SelectAddressDialog.STYLE_THREE, null, mMyInfo);
                dialog.showDialog();
                break;
            case R.id.rl_zxing:
                //二维码
//                Intent intent = new Intent(PersonalActivity.this, Person2CodeActivity.class);
//                intent.putExtra("appkey", mMyInfo.getAppKey());
//                intent.putExtra("username", mMyInfo.getUserName());
//                if (mMyInfo.getAvatarFile() != null) {
//                    intent.putExtra("avatar", mMyInfo.getAvatarFile().getAbsolutePath());
//                }
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch (resultCode) {
                case SIGN:
                    final String sign = bundle.getString(SIGN_KEY);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyInfo.setSignature(sign);
                            JMessageClient.updateMyInfo(UserInfo.Field.signature, mMyInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String responseMessage) {
                                    if (responseCode == 0) {
                                        mTv_sign.setText(sign);
                                        ToastUtils.showToast(PersonalActivity.this, getString(R.string.update_success));
                                    } else {
                                        ToastUtils.showToast(PersonalActivity.this, getString(R.string.update_failed));
                                    }
                                }
                            });
                        }
                    });
                    break;
                case NICK_NAME:
                    final String nick = bundle.getString(NICK_NAME_KEY);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyInfo.setNickname(nick);
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, mMyInfo, new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String responseMessage) {
                                    if (responseCode == 0) {
                                        mTv_nickName.setText(nick);
                                        ToastUtils.showToast(PersonalActivity.this, getString(R.string.update_success));
                                    } else {
                                        ToastUtils.showToast(PersonalActivity.this, getString(R.string.update_failed));
                                    }
                                }
                            });
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                mChoosePhoto.photoUtils.onActivityResult(PersonalActivity.this, requestCode, resultCode, data);
                break;
        }
    }

    public String getDataTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
