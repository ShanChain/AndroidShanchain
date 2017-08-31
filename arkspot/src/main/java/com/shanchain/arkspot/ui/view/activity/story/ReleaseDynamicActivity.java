package com.shanchain.arkspot.ui.view.activity.story;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.TopicInfo;
import com.shanchain.arkspot.widgets.switchview.SwitchView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import utils.LogUtils;
import utils.PrefUtils;
import utils.ToastUtils;

public class ReleaseDynamicActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int REQUEST_CODE_TOPIC = 10;
    private static final int REQUEST_CODE_AT = 20;
    private static final int REQUEST_CODE_BOX = 30;
    @Bind(R.id.tb_release_dynamic)
    ArthurToolBar mTbReleaseDynamic;
    @Bind(R.id.et_release_dynamic_title)
    EditText mEtReleaseDynamicTitle;
    @Bind(R.id.et_release_dynamic_content)
    EditText mEtReleaseDynamicContent;
    @Bind(R.id.tv_release_long_words)
    TextView mTvReleaseLongWords;
    @Bind(R.id.shs_release_dynamic)
    SwitchView mSvReleaseDynamic;
    @Bind(R.id.iv_release_icon_img)
    ImageView mIvReleaseIconImg;
    @Bind(R.id.iv_release_icon_at)
    ImageView mIvReleaseIconAt;
    @Bind(R.id.iv_release_icon_topic)
    ImageView mIvReleaseIconTopic;
    @Bind(R.id.iv_release_icon_frame)
    ImageView mIvReleaseIconFrame;
    @Bind(R.id.ll_release_function_common)
    LinearLayout mLlReleaseFunctionCommon;
    @Bind(R.id.tv_release_img)
    TextView mTvReleaseImg;
    @Bind(R.id.tv_release_at)
    TextView mTvReleaseAt;
    @Bind(R.id.tv_release_topic)
    TextView mTvReleaseTopic;
    @Bind(R.id.tv_release_tail)
    TextView mTvReleaseTail;
    @Bind(R.id.tv_release_read)
    TextView mTvReleaseRead;
    @Bind(R.id.ll_release_function_long)
    LinearLayout mLlReleaseFunctionLong;
    @Bind(R.id.iv_release_btn_switch)
    ImageView mIvReleaseBtnSwitch;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_release_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initListener();
    }

    private void initListener() {
        mSvReleaseDynamic.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if (isOn) {
                    //长文模式
                    mEtReleaseDynamicTitle.setVisibility(View.VISIBLE);
                    mEtReleaseDynamicContent.setHint("以角色的身份，今天的日记是什么内容？");
                    mLlReleaseFunctionCommon.setVisibility(View.GONE);
                    mIvReleaseBtnSwitch.setVisibility(View.VISIBLE);
                    imgCounts = 1;
                } else {
                    //普通模式
                    mEtReleaseDynamicTitle.setVisibility(View.GONE);
                    mEtReleaseDynamicContent.setHint("以角色的身份，想想你有什么故事想说......");
                    mLlReleaseFunctionCommon.setVisibility(View.VISIBLE);
                    mIvReleaseBtnSwitch.setVisibility(View.GONE);
                    mLlReleaseFunctionLong.setVisibility(View.GONE);
                    imgCounts = 3;
                }
            }
        });
    }

    private void initToolBar() {
        mTbReleaseDynamic.setOnLeftClickListener(this);
        mTbReleaseDynamic.setOnRightClickListener(this);
    }

    @OnClick({R.id.et_release_dynamic_content, R.id.iv_release_btn_switch, R.id.tv_release_img, R.id.tv_release_at, R.id.tv_release_topic, R.id.tv_release_tail, R.id.tv_release_read, R.id.iv_release_icon_img, R.id.iv_release_icon_at, R.id.iv_release_icon_topic, R.id.iv_release_icon_frame})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_release_icon_img:
                selectImage();
                break;
            case R.id.iv_release_icon_at:
                atContact();
                break;
            case R.id.iv_release_icon_topic:
                selectTopic();
                break;
            case R.id.iv_release_icon_frame:
                editBox();
                break;
            case R.id.tv_release_img:
                selectImage();
                break;
            case R.id.tv_release_at:
                atContact();
                break;
            case R.id.tv_release_topic:
                selectTopic();
                break;
            case R.id.tv_release_tail:
                //小尾巴
                tail();
                break;
            case R.id.tv_release_read:

                readModel();
                break;
            case R.id.iv_release_btn_switch:
                showKeyBoard = !showKeyBoard;
                switchLong();
                break;
            case R.id.et_release_dynamic_content:
                showKeyBoard = true;
                switchLong();
                break;
        }
    }

    /**
     *  描述：进入阅读模式
     */
    private void readModel() {
        Intent intent = new Intent(this,ReadModelActivity.class);
        startActivity(intent);
    }

    /**
     *  描述：小尾巴编辑框
     *
     */
    private void tail() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = View.inflate(this,R.layout.dialog_tail,null);
        builder.setView(view);
        ImageView ivCancel= (ImageView) view.findViewById(R.id.iv_dialog_tail_cancel);
        ImageView ivSure= (ImageView) view.findViewById(R.id.iv_dialog_tail_sure);
        final EditText etTail = (EditText) view.findViewById(R.id.et_dialog_tail);
        final TextView tvCount = (TextView) view.findViewById(R.id.tv_dialog_tail_count);
        String spTail = PrefUtils.getString(ReleaseDynamicActivity.this,"tail","");
        etTail.setText(spTail);
        etTail.setSelection(etTail.getText().length());
        etTail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvCount.setText(s.length() + "/25");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        ivSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tail = etTail.getText().toString().trim();
                if (TextUtils.isEmpty(tail)){

                }else {
                    PrefUtils.putString(ReleaseDynamicActivity.this,"tail",tail);
                }
                builder.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     *  描述：进入编辑框框
     */
    private void editBox() {
        Intent intent = new Intent(this,BoxActivity.class);
        startActivityForResult(intent,REQUEST_CODE_BOX);
    }

    /**
     * 描述：选择@联系人
     */
    private void atContact() {
        Intent atIntent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(atIntent, REQUEST_CODE_AT);
    }

    /**
     * 描述：选择话题
     */
    private void selectTopic() {
        Intent intent = new Intent(this, TopicActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TOPIC);
    }

    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0权限申请
            LogUtils.d("版本6.0");
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("未申请权限,正在申请");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                LogUtils.d("已经申请权限");
                pickImages();
            }
        } else {
            LogUtils.d("版本低于6.0");
            pickImages();
        }
    }

    private int imgCounts = 3;

    private void pickImages() {
        PhotoPicker.builder()
                .setPhotoCount(imgCounts)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权了
                pickImages();
            } else {
                ToastUtils.showToast(this, "未授权");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            //返回的插图
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

            }
        } else if (requestCode == REQUEST_CODE_TOPIC) {
            //话题页面返回的数据
            if (data != null) {
                TopicInfo topicInfo = (TopicInfo) data.getSerializableExtra("topic");
                LogUtils.d("返回的话题 : " + topicInfo.getTopic());
            }
        } else if (requestCode == REQUEST_CODE_AT) {
            //@页面返回的数据
            if (data != null) {
                ArrayList<String> contacts = data.getStringArrayListExtra("contacts");
                for (int i = 0; i < contacts.size(); i ++) {
                    LogUtils.d("@的人"+contacts.get(i));
                }
            }
        }else if (requestCode == REQUEST_CODE_BOX){
            //添加的框框数据
            if (data != null) {
                String box = data.getStringExtra("box");
                LogUtils.d("box编辑的内容" + box);
            }
        }
    }

    /**
     * 描述：是否显示键盘
     */
    private boolean showKeyBoard = true;

    private void switchLong() {
        if (showKeyBoard) {
            //显示键盘，隐藏功能按键
            LogUtils.d("显示键盘" + showKeyBoard);
            mIvReleaseBtnSwitch.setImageResource(R.mipmap.abs_release_btn_switch_long_default);
            mLlReleaseFunctionLong.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLlReleaseFunctionLong.getVisibility() == View.GONE) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEtReleaseDynamicContent, 0);
                    }
                }
            }, 200);
        } else {
            //显示功能按键，隐藏键盘
            LogUtils.d("隐藏键盘" + showKeyBoard);
            mIvReleaseBtnSwitch.setImageResource(R.mipmap.abs_release_btn_switchtwo_long_default);
            hideSoftInput();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLlReleaseFunctionLong.setVisibility(View.VISIBLE);
                }
            }, 200);
        }
    }

    @Override
    public void onLeftClick(View v) {
        //隐藏键盘
        hideSoftInput();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    private void hideSoftInput() {
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtReleaseDynamicContent.getWindowToken(), 0);
    }

    @Override
    public void onRightClick(View v) {

    }
}
