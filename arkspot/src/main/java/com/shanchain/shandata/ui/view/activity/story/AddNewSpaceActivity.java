package com.shanchain.shandata.ui.view.activity.story;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AddRoleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.SpaceModel;
import com.shanchain.shandata.ui.model.StoryTagInfo;
import com.shanchain.shandata.ui.model.TagContentBean;
import com.shanchain.shandata.ui.model.TagInfo;
import com.shanchain.shandata.utils.EditTextUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

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

    private List<StoryTagInfo> mDatas = new ArrayList<>();
    private AddRoleAdapter mAddRoleAdapter;

    private List<StoryTagInfo> selectedData = new ArrayList<>();
    private String mImgPath;
    private String mSpaceName;
    private String mSlogan;
    private String mIntro;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_new_space;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
        initData();
        initListener();
    }

    private void initListener() {
        EditTextUtils.banEnterInput(mEtAddSpaceTag);
        EditTextUtils.banEnterInput(mEtAddSpaceNick);
        EditTextUtils.banEnterInput(mEtAddSpaceSlogan);
    }

    private void initData() {
        SCHttpUtils.post()
                .url(HttpApi.TAG_QUERY)
                .addParams("type", "space")
                .addParams("size","20")
                .addParams("page","0")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取模型标签失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取模型标签成功 = " + response);
                        TagInfo tagInfo = new Gson().fromJson(response, TagInfo.class);
                        String code = tagInfo.getCode();
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            List<TagContentBean> tagContentBeanList = tagInfo.getData().getContent();
                            int size = tagContentBeanList.size() >= 12 ? 12 : tagContentBeanList.size();
                            for (int i = 0; i < size; i++) {
                                StoryTagInfo storyTagInfo = new StoryTagInfo();
                                storyTagInfo.setTag(tagContentBeanList.get(i).getTagName());
                                storyTagInfo.setTagBean(tagContentBeanList.get(i));
                                mDatas.add(storyTagInfo);
                            }
                            mAddRoleAdapter.notifyDataSetChanged();
                        } else {
                            //ToastUtils.showToast(mContext, "");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRvAddNewSpace.setLayoutManager(layoutManager);
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

                mAddRoleAdapter.notifyItemChanged(position);
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
            default:
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
        mSpaceName = mEtAddSpaceNick.getText().toString().trim();
        mSlogan = mEtAddSpaceSlogan.getText().toString().trim();
        mIntro = mEtAddSpaceIntroduce.getText().toString().trim();

        if (TextUtils.isEmpty(mSpaceName)){
            ToastUtils.showToast(mContext,"世界名不能为空哦~");
            return;
        }

        if (TextUtils.isEmpty(mSlogan)){
            ToastUtils.showToast(mContext,"给你创造的世界加个口号吧~");
            return;
        }

        if (TextUtils.isEmpty(mIntro)){
            ToastUtils.showToast(mContext,"简单介绍下你创造的世界吧~");
            return;
        }

        if (TextUtils.isEmpty(mImgPath)){
            ToastUtils.showToast(mContext,"快去给你创造的世界添加一张封面图吧~");
            return;
        }

        if (selectedData.size() == 0){
            ToastUtils.showToast(mContext,"给你创造的世界加个标签吧~");
            return;
        }

        upLoad();
    }

    private void upLoad() {
        showLoadingDialog();
        List<String> imgSrc = new ArrayList<>();
        imgSrc.add(mImgPath);
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                String imgUrl = urls.get(0);
                commitData(imgUrl);
            }

            @Override
            public void error() {
                closeLoadingDialog();
                ToastUtils.showToast(mContext,"上传时空背景失败");
                LogUtils.i("oss上传失败");
            }
        });

        helper.upLoadImg(mContext,imgSrc);

     /*   SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num", 1 + "")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        ToastUtils.showToast(mContext,"上传头像失败");
                        LogUtils.i("创建时空获取图片名失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(UpLoadImgBean response, int id) {
                        LogUtils.i("创建时空获取图片名成功 = " + response);

                        if (response != null) {
                            final String fileName = response.getUuidList().get(0);
                            String accessKeyId = response.getAccessKeyId();
                            String accessKeySecret = response.getAccessKeySecret();
                            String securityToken = response.getSecurityToken();

                            final OssHelper ossHelper = new OssHelper(mContext, accessKeyId, accessKeySecret, securityToken);
                            ossHelper.setOnUploadListener(new OssHelper.OnUploadListener() {
                                @Override
                                public void upLoadSuccess(boolean isSuccess) {
                                    if (isSuccess) {
                                        LogUtils.i("oss上传成功");
                                        String imgUrl = ossHelper.getImgUrl(fileName);
                                        commitData(imgUrl);

                                    } else {
                                        closeLoadingDialog();
                                        ToastUtils.showToast(mContext,"上传时空背景失败");
                                        LogUtils.i("oss上传失败");
                                    }
                                }
                            });

                            List<String> objKeys = new ArrayList<>();
                            objKeys.add(fileName);
                            List<String> paths = new ArrayList<>();
                            paths.add(mImgPath);
                            List<String> comPaths = SCImageUtils.compressImages(mContext, paths);
                            ossHelper.ossUpload(comPaths, objKeys);

                        } else {
                            closeLoadingDialog();
                            ToastUtils.showToast(mContext,"上传时空背景失败");
                            LogUtils.i("=====code不对？=====");
                        }
                    }
                });*/

    }


    private void commitData(String imgUrl) {

        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SpaceModel spaceModel = new SpaceModel();
        spaceModel.setUserId(userId);
        spaceModel.setIntro(mIntro);
        spaceModel.setBackground(imgUrl);
        spaceModel.setSlogan(mSlogan);
        spaceModel.setName(mSpaceName);
        String data = JSON.toJSONString(spaceModel);

        if (selectedData != null && selectedData.size() != 0) {

            List<String> jArray = new ArrayList<>();
            for (int i = 0; i < selectedData.size(); i++) {
                String tagName = selectedData.get(i).getTag();
                jArray.add(tagName);
            }
            String jArr = JSON.toJSONString(jArray);
            SCHttpUtils.post()
                    .url(HttpApi.SPACE_CREAT)
                    .addParams("dataString", data)
                    .addParams("jArray", jArr)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            closeLoadingDialog();
                            ToastUtils.showToast(mContext,"添加时空失败");
                            LogUtils.i("创建时空模型失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                closeLoadingDialog();
                                LogUtils.i("创建时空模型成功 = " + response);
                                String code = SCJsonUtils.parseCode(response);
                                if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                    String resData = SCJsonUtils.parseData(response);
                                    if (TextUtils.isEmpty(resData)){
                                        ToastUtils.showToast(mContext,"创建世界失败");
                                    }else {
                                        int spaceId = JSONObject.parseObject(resData).getIntValue("spaceId");
                                        collectSpace(spaceId);
                                        finish();
                                    }
                                }else if (TextUtils.equals(code,NetErrCode.SPACE_CREATE_ERR_CODE)){
                                    ToastUtils.showToast(mContext,"当前世界名称已存在");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.showToast(mContext,"创建世界失败");
                            }


                        }
                    });

        } else {
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.SPACE_CREAT)
                    .addParams("dataString", data)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            closeLoadingDialog();
                            ToastUtils.showToast(mContext,"创建世界失败");
                            LogUtils.i("创建世界失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                closeLoadingDialog();
                                LogUtils.i("创建时空模型成功 = " + response);
                                String code = SCJsonUtils.parseCode(response);
                                if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                    String resData = SCJsonUtils.parseData(response);
                                    if (TextUtils.isEmpty(resData)){
                                        ToastUtils.showToast(mContext,"创建世界失败");
                                    }else {
                                        int spaceId = JSONObject.parseObject(resData).getIntValue("spaceId");
                                        ToastUtils.showToast(mContext,"哇！现在你有了一个新世界");
                                        collectSpace(spaceId);
                                        finish();
                                    }
                                }else if (TextUtils.equals(code,NetErrCode.SPACE_CREATE_ERR_CODE)){
                                    ToastUtils.showToast(mContext,"当前世界名称已存在");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.showToast(mContext,"创建世界失败");
                            }
                        }
                    });
        }

    }

    /**
     *  描述：收藏时空
     *
     */
    private void collectSpace(int spaceId) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.SPACE_FAVORITE)
                .addParams("spaceId","" + spaceId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("收藏时空失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("收藏时空成功 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            LogUtils.i("收藏时空成功");
                        }else{
                            LogUtils.i("收藏时空失败");
                        }

                    }
                });


    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
