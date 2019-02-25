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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
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
import com.shanchain.shandata.ui.model.CharacterModel;
import com.shanchain.shandata.ui.model.StoryTagInfo;
import com.shanchain.shandata.ui.model.TagContentBean;
import com.shanchain.shandata.ui.model.TagInfo;
import com.shanchain.shandata.utils.EditTextUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_SPACE_ID;



public class AddRoleActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_add_role)
    ArthurToolBar mTbAddRole;
    @Bind(R.id.rv_add_role)
    RecyclerView mRvAddRole;
    @Bind(R.id.iv_add_role_tag)
    ImageView mIvAddRoleTag;
    @Bind(R.id.et_add_role_tag)
    EditText mEtAddRoleTag;
    @Bind(R.id.iv_add_role_img)
    ImageView mIvAddRoleImg;
    @Bind(R.id.et_add_role_nick)
    EditText mEtAddRoleNick;
    @Bind(R.id.et_add_role_introduce)
    EditText mEtAddRoleIntroduce;
    private List<StoryTagInfo> mDatas = new ArrayList<>();
    private AddRoleAdapter mAddRoleAdapter;

    private List<StoryTagInfo> selectedData = new ArrayList<>();
    private String mImgPath = "";
    private int mSpaceId;
    private String mNick;
    private String mIntro;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_role;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mSpaceId = intent.getIntExtra("spaceId", 0);
        if(mSpaceId == 0){
          String  userId = CommonCacheHelper.getInstance().getCache("0",CACHE_CUR_USER);
          String  spaceId = CommonCacheHelper.getInstance().getCache(userId,CACHE_SPACE_ID);
            if(!TextUtils.isEmpty(spaceId)){
                mSpaceId = Integer.parseInt(spaceId);
            }
        }
        initToolBar();
        initRecyclerView();
        initData();
        EditTextUtils.banEnterInput(mEtAddRoleTag);
        EditTextUtils.banEnterInput(mEtAddRoleNick);
    }

    private void initData() {
        SCHttpUtils.post()
                .url(HttpApi.TAG_QUERY)
                .addParams("type", "model")
                .addParams("page", "0")
                .addParams("size", "20")
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
                            ToastUtils.showToast(mContext, "获取模型标签失败！");
                        }


                    }
                });
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRvAddRole.setLayoutManager(layoutManager);
        mAddRoleAdapter = new AddRoleAdapter(R.layout.item_add_role, mDatas);
        mRvAddRole.setAdapter(mAddRoleAdapter);
        mAddRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                boolean selected = mDatas.get(position).isSelected();
                if (selected) {
                    selectedData.remove(mDatas.get(position));
                } else {
                    if (selectedData.size() >= 5) {
                        ToastUtils.showToast(AddRoleActivity.this, "最多可以添加五个标签哦~");
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
        mTbAddRole.setOnLeftClickListener(this);
        mTbAddRole.setOnRightClickListener(this);
    }

    @OnClick({R.id.iv_add_role_tag, R.id.iv_add_role_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_role_tag:
                addTag();
                break;
            case R.id.iv_add_role_img:
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
                Glide.with(this).load(mImgPath).into(mIvAddRoleImg);
            }
        }
    }

    private void addTag() {
        if (mDatas.size() >= 17) {
            ToastUtils.showToast(this, "最多只能自定义五个标签哦~");
            return;
        }

        if (selectedData.size() >= 5) {
            ToastUtils.showToast(AddRoleActivity.this, "最多可以添加五个标签哦~");
            return;
        }

        String tag = mEtAddRoleTag.getText().toString().trim();
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
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        addNewRole();
    }

    private void addNewRole() {

        mNick = mEtAddRoleNick.getText().toString().trim();
        mIntro = mEtAddRoleIntroduce.getText().toString().trim();
        if (TextUtils.isEmpty(mNick)) {
            ToastUtils.showToast(mContext, "人物昵称不能为空哦~");
            return;
        }

        if (TextUtils.isEmpty(mIntro)) {
            ToastUtils.showToast(mContext, "多少写点人物介绍呗~");
            return;
        }

        if (TextUtils.isEmpty(mImgPath)) {
            ToastUtils.showToast(mContext, "人物要有一个美美的头像哦~");
            return;
        }

        if (selectedData.size() == 0) {
            ToastUtils.showToast(mContext, "为了方便找到该人物，请给角色加几个标签吧~");
            return;
        }

        uploadImg();

    }

    private void uploadImg() {
        showLoadingDialog();
        List<String> imgSrc = new ArrayList<>();
        imgSrc.add(mImgPath);
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                LogUtils.i("上传成功");
                String url = urls.get(0);
                commitData(url);
            }

            @Override
            public void error() {
                closeLoadingDialog();
                ToastUtils.showToast(mContext, "上传头像失败");
                LogUtils.i("oss上传失败");
            }
        });

        helper.upLoadImg(mContext,imgSrc);

      /*  SCHttpUtils.post()
                .url(HttpApi.UP_LOAD_FILE)
                .addParams("num", 1 + "")
                .build()
                .execute(new SCHttpCallBack<UpLoadImgBean>(UpLoadImgBean.class) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        ToastUtils.showToast(mContext, "上传头像失败");
                        LogUtils.i("创建角色获取图片名失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(UpLoadImgBean response, int id) {
                        LogUtils.i("创建角色获取图片名成功 = " + response);

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
                                        ToastUtils.showToast(mContext, "上传头像失败");
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
                            ToastUtils.showToast(mContext, "上传头像失败");
                            LogUtils.i("=====code不对？=====");
                        }
                    }
                });*/
    }


    private void commitData(String imgUrl) {
        CharacterModel model = new CharacterModel();
        model.setName(mNick);
        model.setIntro(mIntro);
        model.setHeadImg(imgUrl);
        Gson gson = new Gson();
        String data = gson.toJson(model);

        List<String> jArray = new ArrayList<>();
        for (int i = 0; i < selectedData.size(); i++) {
            String tagName = selectedData.get(i).getTag();
            jArray.add(tagName);
        }
        String jArr = JSON.toJSONString(jArray);
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_MODEL_CREATE)
                .addParams("dataString", data)
                .addParams("jArray", jArr)
                .addParams("spaceId", mSpaceId + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                        ToastUtils.showToast(mContext, "创建人物模型失败");
                        LogUtils.i("创建人物模型失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        ToastUtils.showToast(mContext, "创建人物模型成功");
                        LogUtils.i("创建人物模型成功 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String resData = SCJsonUtils.parseData(response);
                            if (TextUtils.isEmpty(resData)){
                                ToastUtils.showToast(mContext,"服务端错误：创建人物模型失败");
                            }else {
                                SCBaseEvent event = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_MODEL_CREATE,null,null);
                                EventBus.getDefault().post(event);
                                ActivityStackManager.getInstance().finishActivity(SearchRoleActivity.class);
                                finish();
                            }
                        } else if (TextUtils.equals(code,NetErrCode.SPACE_CREATE_ERR_CODE)){
                            ToastUtils.showToast(mContext,"当前人物名称已存在");
                        }else {
                            ToastUtils.showToast(mContext,"创建人物模型失败");
                        }

                    }
                });
    }
}
