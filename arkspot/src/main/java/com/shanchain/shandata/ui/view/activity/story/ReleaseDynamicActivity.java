package com.shanchain.shandata.ui.view.activity.story;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicImagesAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.ReleaseSucEvent;
import com.shanchain.shandata.ui.model.AtBean;
import com.shanchain.shandata.ui.model.DynamicImageInfo;
import com.shanchain.shandata.ui.model.RichTextModel;
import com.shanchain.shandata.ui.model.TopicInfo;
import com.shanchain.shandata.ui.presenter.ReleaseDynamicPresenter;
import com.shanchain.shandata.ui.presenter.impl.ReleaseDynamicPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.ReleaseDynamicView;
import com.shanchain.shandata.widgets.rEdit.InsertModel;
import com.shanchain.shandata.widgets.rEdit.RichEditor;
import com.shanchain.shandata.widgets.richEditor.RichTextEditor;
import com.shanchain.shandata.widgets.switchview.SwitchView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;


public class ReleaseDynamicActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, ReleaseDynamicView {

    private static final int REQUEST_CODE_TOPIC = 10;
    private static final int REQUEST_CODE_AT = 20;
    @Bind(R.id.tb_release_dynamic)
    ArthurToolBar mTbReleaseDynamic;
    @Bind(R.id.et_release_dynamic_title)
    EditText mEtReleaseDynamicTitle;
    @Bind(R.id.et_release_dynamic_content)
    RichEditor mEtReleaseDynamicContent;
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
    @Bind(R.id.ll_release_function_common)
    LinearLayout mLlReleaseFunctionCommon;
    @Bind(R.id.rv_release_dynamic)
    RecyclerView mRvReleaseDynamic;
    @Bind(R.id.et_release_dynamic_long)
    RichTextEditor mEtReleaseDynamicLong;
    @Bind(R.id.tv_release_common)
    TextView mTvReleaseCommon;
    @Bind(R.id.iv_release_icon_read)
    ImageView mIvReleaseIconRead;
    private List<DynamicImageInfo> imgData = new ArrayList<>();
    private DynamicImagesAdapter mImagesAdapter;
    /**
     * 描述：标记是否是编辑长文状态
     */
    private boolean isEditLong;
    private ReleaseDynamicPresenter mPresenter;
    private List<Integer> topicIds = new ArrayList<>();
    private List<Integer> atIds = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_release_dynamic;
    }

    @Override
    protected void initViewsAndEvents() {
        init();
        initToolBar();
        initListener();
        initRecyclerView();
        String draft = PrefUtils.getString(mContext, Constants.SP_KEY_DRAFT, "");
        if (TextUtils.isEmpty(draft)) {

        } else {
            LogUtils.i("草稿箱内容 = " + draft);
            getDraftContent(draft);
        }
    }


    private void init() {
        mPresenter = new ReleaseDynamicPresenterImpl(this);
        //mEtReleaseDynamicLong.
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvReleaseDynamic.setLayoutManager(linearLayoutManager);
        mImagesAdapter = new DynamicImagesAdapter(R.layout.item_dynamic_image, imgData);
        mRvReleaseDynamic.setAdapter(mImagesAdapter);
        mImagesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                imgData.remove(position);
                imgCounts = 3 - imgData.size();
                mImagesAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initListener() {
        mSvReleaseDynamic.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                mEtReleaseDynamicTitle.setText("");
                mEtReleaseDynamicContent.setText("");
                imgData.clear();
                if (mImagesAdapter != null) {
                    mImagesAdapter.notifyDataSetChanged();
                }
                if (isOn) {
                    //长文模式
                    mTbReleaseDynamic.setTitleText("撰写小说");
                    mEtReleaseDynamicTitle.setVisibility(View.VISIBLE);
                    mEtReleaseDynamicLong.setVisibility(View.VISIBLE);
                    mEtReleaseDynamicContent.setVisibility(View.GONE);
                    mIvReleaseIconRead.setVisibility(View.VISIBLE);
                    mRvReleaseDynamic.setVisibility(View.GONE);
                    mLlReleaseFunctionCommon.setVisibility(View.GONE);
                    mIvReleaseIconAt.setVisibility(View.GONE);
                    mIvReleaseIconTopic.setVisibility(View.GONE);
                    imgCounts = 1;
                    isEditLong = true;
                } else {
                    //普通模式
                    mTbReleaseDynamic.setTitleText("发布动态");
                    mEtReleaseDynamicTitle.setVisibility(View.GONE);
                    mEtReleaseDynamicContent.setVisibility(View.VISIBLE);
                    mEtReleaseDynamicLong.setVisibility(View.GONE);
                    mIvReleaseIconRead.setVisibility(View.INVISIBLE);
                    mRvReleaseDynamic.setVisibility(View.INVISIBLE);
                    mLlReleaseFunctionCommon.setVisibility(View.VISIBLE);
                    mIvReleaseIconTopic.setVisibility(View.VISIBLE);
                    mIvReleaseIconAt.setVisibility(View.VISIBLE);
                    imgCounts = 3;
                    isEditLong = false;
                }
            }
        });

        mEtReleaseDynamicTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLlReleaseFunctionCommon.setVisibility(View.GONE);
                } else {
                    mLlReleaseFunctionCommon.setVisibility(View.VISIBLE);
                }
            }
        });

        mEtReleaseDynamicContent.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void textChange(CharSequence s, int start, int before, int count) {

                int selectionStart = mEtReleaseDynamicContent.getSelectionStart();
                String string = s.toString();
                String sub = string.substring(0, selectionStart);

                if (sub.endsWith("@")){
                    atContact();
                }else if (sub.endsWith("#")){
                    selectTopic();
                }
            }
        });

    }

    private void initToolBar() {
        mTbReleaseDynamic.setOnLeftClickListener(this);
        mTbReleaseDynamic.setOnRightClickListener(this);
    }

    @OnClick({R.id.et_release_dynamic_content, R.id.iv_release_icon_img, R.id.iv_release_icon_at, R.id.iv_release_icon_topic, R.id.iv_release_icon_read})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_release_icon_img:
                if (imgCounts == 0) {
                    ToastUtils.showToast(this, "最多可以选三张图片哦~");
                    return;
                }
                selectImage();
                break;
            case R.id.iv_release_icon_at:
                atContact();
                break;
            case R.id.iv_release_icon_topic:
                selectTopic();
                break;
            case R.id.iv_release_icon_read:
                if (isEditLong) {
                    readModel();
                }
                break;

        }
    }

    /**
     * 描述：进入阅读模式
     */
    private void readModel() {
        List<RichTextModel> editData = getEditData();
        Intent intent = new Intent(this, ReadModelActivity.class);
        String jsonString = JSONObject.toJSONString(editData);
        intent.putExtra("native", true);
        intent.putExtra("editData", jsonString);
        startActivity(intent);
    }

    /**
     * 描述：选择@联系人
     */
    private void atContact() {
        Intent atIntent = new Intent(this, SelectContactActivity.class);
        atIntent.putExtra("isAt", true);
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

    /**
     * 描述：选择图片的个数
     */
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
                if (isEditLong) {
                    //编辑长文状态
                    String imagePath = photos.get(0);
                    insertImg(imagePath);
                } else {
                    //普通编辑状态
                    for (int i = 0; i < photos.size(); i++) {
                        DynamicImageInfo imageInfo = new DynamicImageInfo();
                        imageInfo.setImg(photos.get(i));
                        imgData.add(imageInfo);
                    }

                    imgCounts = 3 - imgData.size();
                    mImagesAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == REQUEST_CODE_TOPIC) {
            //话题页面返回的数据
            if (data != null) {
                TopicInfo topicInfo = (TopicInfo) data.getSerializableExtra("topic");
                String topic = topicInfo.getTopic();
                int topicId = topicInfo.getTopicId();

                int start = mEtReleaseDynamicContent.getSelectionStart();
                Editable text = mEtReleaseDynamicContent.getText();
                LogUtils.i("txt = " + text.toString());
                String sub = text.toString().substring(0, start);
                if (sub.endsWith("#")){
                    text.delete(start-1,start);
                }
                InsertModel model = new InsertModel("#",topic,"#3bbac8",topicId);
                mEtReleaseDynamicContent.insertSpecialStr(model);
            }
        } else if (requestCode == REQUEST_CODE_AT) {
            //@页面返回的数据
            if (data != null) {
                ArrayList<String> contacts = data.getStringArrayListExtra("contacts");

                ArrayList<AtBean> list = (ArrayList<AtBean>) data.getSerializableExtra("atBeans");

                for (int i = 0; i < list.size(); i ++) {

                    int start = mEtReleaseDynamicContent.getSelectionStart();
                    Editable text = mEtReleaseDynamicContent.getText();
                    LogUtils.i("txt = " + text.toString());
                    String sub = text.toString().substring(0, start);
                    if (sub.endsWith("@")){
                        text.delete(start-1,start);
                    }

                    InsertModel model = new InsertModel("@",list.get(i).getName(),"#3bbac8",list.get(i).getAtId());
                    mEtReleaseDynamicContent.insertSpecialStr(model);
                }
            }
        }
    }

    private void insertImg(final String imagePath) {
        mEtReleaseDynamicLong.insertImage(imagePath);
    }


    @Override
    public void onLeftClick(View v) {
        //隐藏键盘
        hideSoftInput();
        if (isEditLong) {

            String title = mEtReleaseDynamicTitle.getText().toString().trim();

            List<RichTextModel> editData = getEditData();
            RichTextModel model = editData.get(0);
            boolean isImg = model.isImg();
            if (isImg) {
                popDialog();
            } else {
                String text = model.getText();
                if (TextUtils.isEmpty(text) && TextUtils.isEmpty(title)) {
                    finish();
                } else {
                    popDialog();
                }
            }
        } else {
            String dynamicContent = mEtReleaseDynamicContent.getText().toString().trim();
            if (!TextUtils.isEmpty(dynamicContent) || imgData.size() > 0) {
                popDialog();
            } else {
                finish();
            }
        }
    }

    private void popDialog() {
        final StandardDialog dialog = new StandardDialog(mContext);
        dialog.setStandardMsg("当前动态未发布，是否保存草稿以便下次继续编辑？");
        dialog.setCancelText("取消");
        dialog.setSureText("保存");
        dialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                //确定按钮
                saveDraft();
                dialog.dismiss();
                finish();
            }
        }, new Callback() {
            @Override
            public void invoke() {
                //取消按钮
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    //保存草稿
    private void saveDraft() {


        JSONObject object = new JSONObject();
        if (isEditLong) {
            //保存小说草稿
            String title = mEtReleaseDynamicTitle.getText().toString().trim();
            List<RichTextModel> editData = getEditData();
            String content = JSONObject.toJSONString(editData);
            object.put("long", isEditLong);
            object.put("content", content);
            object.put("title", title);

        } else {
            //保存普通动态草稿
            String dynamicContent = mEtReleaseDynamicContent.getText().toString().trim();
            String dynamicImgs = JSONObject.toJSONString(imgData);
            JSONObject dynamicJson = new JSONObject();
            dynamicJson.put("dynamicContent", dynamicContent);
            dynamicJson.put("dynamicImgs", dynamicImgs);
            String content = JSONObject.toJSONString(dynamicJson);
            object.put("long", isEditLong);
            object.put("content", content);
        }
        String jsonContent = JSONObject.toJSONString(object);
        PrefUtils.putString(mContext, Constants.SP_KEY_DRAFT, jsonContent);
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

        final List<String> imgPaths = new ArrayList<>();
        if (imgData != null && imgData.size() != 0) {
            for (int i = 0; i < imgData.size(); i++) {
                imgPaths.add(imgData.get(i).getImg());
            }
        }

        String tailId = "";
        //发布动态
        String word = mEtReleaseDynamicContent.getText().toString().trim();

        if (isEditLong) {
            //编辑小说状态
            String title = mEtReleaseDynamicTitle.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.showToast(mContext, "给你的小说添加个标题吧~");
                return;
            }
            List<RichTextModel> editData = getEditData();
            showLoadingDialog(false);
            mPresenter.ReleaseLongText(this, title, editData);
        } else {
            if (TextUtils.isEmpty(word)) {
                ToastUtils.showToast(mContext, "请输入内容");
                return;
            }

            //普通编辑
            showLoadingDialog(false);
            List<InsertModel> richInsertList = mEtReleaseDynamicContent.getRichInsertList();

            if (imgData.size() == 0) {
                //无图片
                mPresenter.releaseDynamic(word, imgPaths, tailId, richInsertList);
            } else {
                //有图片
                mPresenter.upLoadImgs(mContext, word, imgPaths, tailId, richInsertList);
            }
        }

    }

    private List<RichTextModel> getEditData() {
        List<RichTextEditor.EditData> editDataList = mEtReleaseDynamicLong.buildEditData();

        List<RichTextModel> list = new ArrayList<>();

        for (int i = 0; i < editDataList.size(); i++) {
            RichTextModel model = new RichTextModel();
            RichTextEditor.EditData editData = editDataList.get(i);
            model.setIndex(i);
            if (!TextUtils.isEmpty(editData.inputStr)) {
                LogUtils.i("str " + i + " = " + editData.inputStr);
                model.setImg(false);
                model.setText(editData.inputStr);
            } else if (!TextUtils.isEmpty(editData.imagePath)) {
                LogUtils.i("img " + i + " = " + editData.imagePath);
                model.setImg(true);
                model.setImgPath(editData.imagePath);
            }
            list.add(model);
        }
        return list;
    }

    @Override
    public void releaseSuccess() {
        //发布成功
        ToastUtils.showToast(mContext, "发布成功");
        closeLoadingDialog();

        clearDraft();

        finish();
        ReleaseSucEvent releaseSucEvent = new ReleaseSucEvent(true);
        EventBus.getDefault().post(new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT, EventConstant.EVENT_KEY_RELEASE, releaseSucEvent, null));

    }

    /**
     * 描述：清除草稿
     */
    private void clearDraft() {
        String draft = PrefUtils.getString(mContext, Constants.SP_KEY_DRAFT, "");
        if (!TextUtils.isEmpty(draft)) {
            LogUtils.i("草稿箱内容 = " + draft);
            Boolean isLong = JSONObject.parseObject(draft).getBoolean("long");
            if (isEditLong) {    //小说编辑状态
                if (isLong) {    //存的草稿也是小说的草稿
                    PrefUtils.putString(mContext, Constants.SP_KEY_DRAFT, "");
                }
            } else {
                if (!isLong) {
                    PrefUtils.putString(mContext, Constants.SP_KEY_DRAFT, "");
                }
            }
        }
    }

    @Override
    public void releaseFailed(String msg, Exception e) {
        //发布失败
        closeLoadingDialog();
        ToastUtils.showToast(mContext, "发布失败" + msg);
    }

    public void getDraftContent(String draft) {
        Boolean isLong = JSONObject.parseObject(draft).getBoolean("long");
        String content = JSONObject.parseObject(draft).getString("content");
        if (isLong) {    //长文草稿
            String title = JSONObject.parseObject(draft).getString("title");
            List<RichTextModel> richTextList = JSONObject.parseArray(content, RichTextModel.class);
            LogUtils.i("草稿中的标题 = " + title);
            LogUtils.i("草稿中的内容 = " + content);
            mEtReleaseDynamicTitle.setText(title);
            mEtReleaseDynamicLong.clearAllLayout();
            for (int i = 0; i < richTextList.size(); i++) {
                RichTextModel model = richTextList.get(i);
                boolean img = model.isImg();
                if (img) {
                    LogUtils.i("位置 = " + mEtReleaseDynamicLong.getLastIndex() + " ; 图片地址 = " + model.getImgPath());
                    int lastIndex = mEtReleaseDynamicLong.getLastIndex();
                    mEtReleaseDynamicLong.addImageViewAtIndex(lastIndex, model.getImgPath());
                } else {
                    LogUtils.i("位置 = " + mEtReleaseDynamicLong.getLastIndex() + "; 文本 = " + model.getText());
                    int lastIndex = mEtReleaseDynamicLong.getLastIndex();
                    mEtReleaseDynamicLong.addEditTextAtIndex(lastIndex, model.getText());
                }
            }

        } else {     //普通动态
            String dynamicContent = JSONObject.parseObject(content).getString("dynamicContent");
            String dynamicImgs = JSONObject.parseObject(content).getString("dynamicImgs");
            List<DynamicImageInfo> imgs = JSONObject.parseArray(dynamicImgs, DynamicImageInfo.class);
            LogUtils.i("动态内容 = " + dynamicContent);
            LogUtils.i("动态图片 = " + dynamicImgs);
            imgData.addAll(imgs);
            mImagesAdapter.notifyDataSetChanged();
            mEtReleaseDynamicContent.setText(dynamicContent);
        }
    }
}
