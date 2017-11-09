package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.RichTextModel;
import com.shanchain.arkspot.widgets.richEditor.RichTextView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

public class ReadModelActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_read_model)
    ArthurToolBar mTbReadModel;
    @Bind(R.id.tv_read_content)
    RichTextView mTvReadContent;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_read_model;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        Intent intent = getIntent();
        boolean isNative = intent.getBooleanExtra("native", true);
        if (isNative){
            String content = intent.getStringExtra("editData");
            LogUtils.i("富文本信息 = " + content);
            List<RichTextModel> modelList = JSONObject.parseArray(content, RichTextModel.class);
            showData(modelList);
        }else {
            showLoadingDialog(true);
            String storyId = intent.getStringExtra("storyId");
            SCHttpUtils.post()
                    .url(HttpApi.STORY_GET_BY_ID)
                    .addParams("storyId",storyId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("获取小说内容失败");
                            e.printStackTrace();
                            closeLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                LogUtils.i("获取小说内容成功 = " + response);
                                String code = JSONObject.parseObject(response).getString("code");
                                if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String content = JSONObject.parseObject(data).getString("content");
                                    List<RichTextModel> richTextModels = JSONObject.parseArray(content, RichTextModel.class);
                                    String title = JSONObject.parseObject(data).getString("title");
                                    closeLoadingDialog();
                                    mTvReadContent.addTextViewAtIndex(0,title);
                                    showData(richTextModels);
                                }else {
                                    closeLoadingDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                closeLoadingDialog();

                            }
                        }
                    });
        }

    }

    private void showData(final List<RichTextModel> modelList) {

        for (int i = 0; i < modelList.size(); i++) {
            RichTextModel model = modelList.get(i);
            boolean img = model.isImg();
            if (img) {
                String imgPath = model.getImgPath();
                mTvReadContent.addImageViewAtIndex(model.getIndex(), imgPath);
            } else {
                String text = model.getText();
                mTvReadContent.addTextViewAtIndex(model.getIndex(), text);
            }
        }

    }

    private void initToolBar() {
        mTbReadModel.setOnLeftClickListener(this);
        mTbReadModel.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

}
