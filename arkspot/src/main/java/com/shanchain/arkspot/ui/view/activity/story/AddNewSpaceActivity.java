package com.shanchain.arkspot.ui.view.activity.story;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.AddRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.StoryTagInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import utils.LogUtils;
import utils.ToastUtils;

public class AddNewSpaceActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_add_new_space)
    ArthurToolBar mTbAddNewSpace;
    @Bind(R.id.rv_add_new_space)
    RecyclerView mRvAddNewSpace;
    @Bind(R.id.iv_add_space_tag)
    ImageView mIvAddSpaceTag;
    @Bind(R.id.et_add_space_tag)
    EditText mEtAddSpaceTag;
    @Bind(R.id.iv_add_space_img)
    ImageView mIvAddSpaceImg;
    @Bind(R.id.et_add_space_nick)
    EditText mEtAddSpaceNick;
    @Bind(R.id.et_add_space_slogan)
    EditText mEtAddSpaceSlogan;
    @Bind(R.id.et_add_space_introduce)
    EditText mEtAddSpaceIntroduce;

    private List<StoryTagInfo> mDatas;
    private AddRoleAdapter mAddRoleAdapter;

    private List<StoryTagInfo> selectedData;
    private String mImgPath;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_new_space;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        selectedData = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRvAddNewSpace.setLayoutManager(layoutManager);
        mDatas = new ArrayList<>();
        String[] tags = {"原创", "历史", "动漫", "游戏", "武侠", "科幻", "玄幻", "悬疑", "小说", "影视", "体育", "校园"};
        for (int i = 0; i < tags.length; i++) {
            StoryTagInfo tagInfo = new StoryTagInfo();
            tagInfo.setTag(tags[i]);
            tagInfo.setSelected(false);
            mDatas.add(tagInfo);
        }
        mAddRoleAdapter = new AddRoleAdapter(R.layout.item_add_role, mDatas);
        mRvAddNewSpace.setAdapter(mAddRoleAdapter);
        mAddRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                boolean selected = mDatas.get(position).isSelected();
                if (selected) {
                    selectedData.remove(mDatas.get(position));
                } else {
                    if (selectedData.size() >= 5) {
                        ToastUtils.showToast(AddNewSpaceActivity.this, "最多可以添加五个标签哦~");
                        return;
                    }
                    selectedData.add(mDatas.get(position));
                }
                mDatas.get(position).setSelected(!selected);

                mAddRoleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initToolBar() {
        mTbAddNewSpace.setOnLeftClickListener(this);
        mTbAddNewSpace.setOnRightClickListener(this);
    }

    @OnClick({R.id.iv_add_space_tag, R.id.iv_add_space_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_space_tag:
                addTag();
                break;
            case R.id.iv_add_space_img:
                selectImg();
                break;
        }
    }

    private void selectImg() {
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

    private void pickImages() {
        PhotoPicker.builder()
                .setPhotoCount(1)
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
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mImgPath = photos.get(0);
                if (TextUtils.isEmpty(mImgPath)) {
                    return;
                }
                Glide.with(this).load(mImgPath).into(mIvAddSpaceImg);
            }
        }
    }

    private void addTag() {
        if (mDatas.size() >= 17) {
            ToastUtils.showToast(this, "最多只能自定义五个标签哦~");
            return;
        }

        if (selectedData.size() >= 5) {
            ToastUtils.showToast(AddNewSpaceActivity.this, "最多可以添加五个标签哦~");
            return;
        }

        String tag = mEtAddSpaceTag.getText().toString().trim();
        if (TextUtils.isEmpty(tag)) {
            ToastUtils.showToast(this, "不能定义空白标签哦~");
            return;
        }

        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getTag().equals(tag)) {
                ToastUtils.showToast(this, "您定义的标签已存在");
                return;
            }
        }

        StoryTagInfo storyTagInfo = new StoryTagInfo();
        storyTagInfo.setTag(tag);
        storyTagInfo.setSelected(true);
        selectedData.add(storyTagInfo);
        mDatas.add(storyTagInfo);
        mAddRoleAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(this,AddResultActivity.class);
        intent.putExtra("isRole",false);
        startActivity(intent);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
