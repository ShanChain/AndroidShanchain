package com.shanchain.arkspot.ui.view.activity.story;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.ReleaseContentInfo;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ForwardingActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_forward_dynamic)
    ArthurToolBar mTbForwardDynamic;
    @Bind(R.id.et_forward_dynamic_content)
    EditText mEtForwardDynamicContent;
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_forwarding;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        mBean = (StoryModelBean) getIntent().getSerializableExtra("forward");
        if (mBean == null){
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
        if (intro.contains("content")){
            ReleaseContentInfo contentInfo = JSONObject.parseObject(intro, ReleaseContentInfo.class);
            content = contentInfo.getContent();
            imgs = contentInfo.getImgs();
            if (imgs.size()>0){
                img = imgs.get(0);
            }else {
                img = mBean.getCharacterImg();
            }
        }else {
            content = intro;
            img = mBean.getCharacterImg();
        }

        GlideUtils.load(mContext,img,mIvForwardFrom,0);

        mTvForwardFromAt.setText("@" + mBean.getCharacterName());
        mTvForwardFromContent.setText(content);

    }

    @OnClick({R.id.et_forward_dynamic_content, R.id.iv_forward_icon_img, R.id.iv_forward_icon_at, R.id.iv_forward_icon_topic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_forward_dynamic_content:

                break;
            case R.id.iv_forward_icon_img:

                break;
            case R.id.iv_forward_icon_at:

                break;
            case R.id.iv_forward_icon_topic:

                break;
        }
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
