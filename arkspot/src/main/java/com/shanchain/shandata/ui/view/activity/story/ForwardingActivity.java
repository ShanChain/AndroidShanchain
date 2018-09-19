package com.shanchain.shandata.ui.view.activity.story;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.push.PushFilterBuilder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.DynamicImagesAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.AtBean;
import com.shanchain.shandata.ui.model.DynamicImageInfo;
import com.shanchain.shandata.ui.model.ReleaseContentInfo;
import com.shanchain.shandata.ui.model.ReleaseStoryContentInfo;
import com.shanchain.shandata.ui.model.SpanBean;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.TopicInfo;
import com.shanchain.shandata.widgets.rEdit.InsertModel;
import com.shanchain.shandata.widgets.rEdit.RichEditor;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;


public class ForwardingActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {
    private static final int REQUEST_CODE_TOPIC = 10;
    private static final int REQUEST_CODE_AT = 20;
    @Bind(R.id.tb_forward_dynamic)
    ArthurToolBar mTbForwardDynamic;
    @Bind(R.id.et_forward_dynamic_content)
    RichEditor mEtForwardDynamicContent;
    @Bind(R.id.iv_forward_from)
    ImageView mIvForwardFrom;
    @Bind(R.id.tv_forward_from_at)
    TextView mTvForwardFromAt;
    @Bind(R.id.tv_forward_from_content)
    TextView mTvForwardFromContent;
    @Bind(R.id.rv_forward_img)
    RecyclerView mRvForwardImg;
    @Bind(R.id.iv_forward_icon_img)
    ImageView mIvForwardIconImg;
    @Bind(R.id.iv_forward_icon_at)
    ImageView mIvForwardIconAt;
    @Bind(R.id.iv_forward_icon_topic)
    ImageView mIvForwardIconTopic;
    @Bind(R.id.ll_forward_function)
    LinearLayout mLlForwardFunction;
    private StoryModelBean mBean;
    private List<DynamicImageInfo> imgData = new ArrayList<>();
    private DynamicImagesAdapter mImagesAdapter;
    private ArrayList<String> replaceAt = new ArrayList<>();
    private ArrayList<String> replaceTopic = new ArrayList<>();
    private List<Integer> topicIds = new ArrayList<>();
    private List<Integer> atIds = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_forwarding;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        mBean = (StoryModelBean) getIntent().getSerializableExtra("forward");
        if (mBean == null) {
            return;
        }
        initView();
    }

    private void initToolBar() {
        mTbForwardDynamic.setOnLeftClickListener(this);
        mTbForwardDynamic.setOnRightClickListener(this);
    }

    private void initView() {
        String intro = mBean.getIntro();
        String content = "";
        String img = "";
        List<String> imgs = new ArrayList<>();
        if (intro.contains("content")) {
            ReleaseContentInfo contentInfo = JSONObject.parseObject(intro, ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgs = contentInfo.getImgs();
            if (imgs.size() > 0) {
                img = imgs.get(0);
            } else {
                img = mBean.getCharacterImg();
            }
        } else {
            content = intro;
            img = mBean.getCharacterImg();
        }

        GlideUtils.load(mContext, img, mIvForwardFrom, 0);

        mTvForwardFromAt.setText("@" + mBean.getCharacterName());
        mTvForwardFromContent.setText(content);

        mEtForwardDynamicContent.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void textChange(CharSequence s, int start, int before, int count) {

                int selectionStart = mEtForwardDynamicContent.getSelectionStart();
                String string = s.toString();
                String sub = string.substring(0, selectionStart);

                if (sub.endsWith("@")){
                    clickAt();
                }else if (sub.endsWith("#")){
                    clickTopic();
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvForwardImg.setLayoutManager(layoutManager);
        mImagesAdapter = new DynamicImagesAdapter(R.layout.item_dynamic_image, imgData);
        mRvForwardImg.setAdapter(mImagesAdapter);
        mImagesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                imgData.remove(position);
                imgCounts = 3 - imgData.size();
                mImagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.et_forward_dynamic_content, R.id.iv_forward_icon_img, R.id.iv_forward_icon_at, R.id.iv_forward_icon_topic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_forward_dynamic_content:

                break;
            case R.id.iv_forward_icon_img:
                clickImg();
                break;
            case R.id.iv_forward_icon_at:
                clickAt();
                break;
            case R.id.iv_forward_icon_topic:
                clickTopic();
                break;
        }
    }

    private void clickTopic() {
        Intent intent = new Intent(this, TopicActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TOPIC);
    }

    private void clickAt() {
        Intent atIntent = new Intent(this, SelectContactActivity.class);
        atIntent.putExtra("isAt", true);
        startActivityForResult(atIntent, REQUEST_CODE_AT);
    }

    private void clickImg() {
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
                for (int i = 0; i < photos.size(); i++) {
                    DynamicImageInfo imageInfo = new DynamicImageInfo();
                    imageInfo.setImg(photos.get(i));
                    imgData.add(imageInfo);

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

                int start = mEtForwardDynamicContent.getSelectionStart();
                Editable text = mEtForwardDynamicContent.getText();
                LogUtils.i("txt = " + text.toString());
                String sub = text.toString().substring(0, start);
                if (sub.endsWith("#")){
                    text.delete(start-1,start);
                }
                InsertModel model = new InsertModel("#",topic,"#3bbac8",topicId);
                mEtForwardDynamicContent.insertSpecialStr(model);
            }
        } else if (requestCode == REQUEST_CODE_AT) {
            //@页面返回的数据
            if (data != null) {

                ArrayList<AtBean> list = (ArrayList<AtBean>) data.getSerializableExtra("atBeans");

                for (int i = 0; i < list.size(); i ++) {

                    int start = mEtForwardDynamicContent.getSelectionStart();
                    Editable text = mEtForwardDynamicContent.getText();
                    LogUtils.i("txt = " + text.toString());
                    String sub = text.toString().substring(0, start);
                    if (sub.endsWith("@")){
                        text.delete(start-1,start);
                    }

                    InsertModel model = new InsertModel("@",list.get(i).getName(),"#3bbac8",list.get(i).getAtId());
                    mEtForwardDynamicContent.insertSpecialStr(model);
                }
            }
        }
    }

    @Override
    public void onRightClick(View v) {
        String content = mEtForwardDynamicContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            ToastUtils.showToast(mContext, "转发填点东西呗");
            return;
        }

        showLoadingDialog(true);
        forwardStory(content);

    }

    private void forwardStory(String content) {
        if (imgData.size() == 0) {
            forward(content, new ArrayList<String>());
        } else {
            List<String> imgPaths = new ArrayList<>();
            for (int i = 0; i < imgData.size(); i++) {
                imgPaths.add(imgData.get(i).getImg());
            }
            upLoadImg(content, imgPaths);
        }


    }

    private void upLoadImg(final String content, List<String> imgPaths) {
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                forward(content, urls);
            }

            @Override
            public void error() {
                closeLoadingDialog();
                ToastUtils.showToast(mContext, "上传图片失败");
            }
        });

        helper.upLoadImg(mContext, imgPaths);
    }

    private void forward(String content, List<String> urls) {
        List<InsertModel> insertList = mEtForwardDynamicContent.getRichInsertList();
        List<Integer> topicIds = new ArrayList<>();
        List<Integer> atList = new ArrayList<>();
        List<SpanBean> spanBeanList = new ArrayList<>();
        for (InsertModel model : insertList) {
            SpanBean spanBean = new SpanBean();
            String insertContent = model.getInsertContent();
            String insertRule = model.getInsertRule();
            int insertId = model.getInsertId();
            spanBean.setStr(insertContent);
            spanBean.setSpaceId(SCCacheUtils.getCacheSpaceId());
            if (TextUtils.equals(insertRule, "@")) {
                atList.add(insertId);
                spanBean.setType(Constants.SPAN_TYPE_AT);
            } else if (TextUtils.equals(insertRule, "#")) {
                topicIds.add(insertId);
                spanBean.setType(Constants.SPAN_TYPE_TOPIC);
            }
            spanBean.setBeanId(insertId);
            spanBeanList.add(spanBean);
        }


        ReleaseContentInfo releaseContentInfo = new ReleaseContentInfo();
        releaseContentInfo.setContent(content);
        releaseContentInfo.setImgs(urls);
        releaseContentInfo.setSpanBeanList(spanBeanList);
        String data = JSONObject.toJSONString(releaseContentInfo);

        ReleaseStoryContentInfo contentInfo = new ReleaseStoryContentInfo();
        contentInfo.setIntro(data);
        String dataString = JSONObject.toJSONString(contentInfo);
        String topicArr = JSONObject.toJSONString(topicIds);
        JSONArray jsonArray = new JSONArray();
        for (Integer val:atList) {
            JSONObject tagJson = new JSONObject();
            tagJson.put("tag","MODEL_"+ val);
            jsonArray.add(tagJson);
        }
        PushFilterBuilder builder = new PushFilterBuilder();
        if(jsonArray.size() > 0){
            builder.addOrFilter(jsonArray);
        }

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_TRANSPOND)
                .addParams("dataString", dataString)
                .addParams("topicIds", topicArr)
                .addParams("filter", builder.getFilter())
                .addParams("storyId", mBean.getDetailId().substring(1))
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("转发失败");
                        e.printStackTrace();
                        closeLoadingDialog();
                        ToastUtils.showToast(mContext,"网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("转发成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                String resData = JSONObject.parseObject(response).getString("data");
                                if (TextUtils.isEmpty(resData)){
                                    //转发失败
                                    closeLoadingDialog();
                                    ToastUtils.showToast(mContext,"转发失败");
                                }else { //转发成功
                                    closeLoadingDialog();
                                    ToastUtils.showToast(mContext,"转发成功");
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeLoadingDialog();
                        }

                    }
                });


    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
