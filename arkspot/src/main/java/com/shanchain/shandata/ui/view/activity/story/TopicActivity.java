package com.shanchain.shandata.ui.view.activity.story;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TopicAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.ResponseTopicContentBean;
import com.shanchain.shandata.ui.model.ResponseTopicData;
import com.shanchain.shandata.ui.model.ResponseTopicInfo;
import com.shanchain.shandata.ui.model.TopicInfo;
import com.shanchain.shandata.ui.view.activity.square.AddTopicActivity;
import com.shanchain.shandata.utils.EditTextUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


public class TopicActivity extends BaseActivity {

    private static final int RESULT_CODE_TOPIC = 10;
    @Bind(R.id.et_topic)
    EditText mEtTopic;
    @Bind(R.id.tv_topic_cancel)
    TextView mTvTopicCancel;
    @Bind(R.id.rv_topic)
    RecyclerView mRvTopic;
    private List<TopicInfo> datas = new ArrayList<>();
    private List<TopicInfo> show = new ArrayList<>();
    private TopicAdapter mTopicAdapter;
    private boolean hasNew;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_topic;
    }

    @Override
    protected void initViewsAndEvents() {
        initRecyclerView();
        initData();
        initListener();
    }

    private void initListener() {
        EditTextUtils.banEnterInput(mEtTopic);
        mEtTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                show.clear();
                for (int i = 0; i < datas.size(); i ++) {
                    if (datas.get(i).getTopic().contains(input)){
                        show.add(datas.get(i));
                    }else {

                    }
                }

                if (show.size() == 0) {
                    TopicInfo topicInfo = new TopicInfo();
                    topicInfo.setTopic(input);
                    topicInfo.setNew(true);
                    show.add(0,topicInfo);
                    hasNew = true;
                }else {
                    if (hasNew){
                        show.remove(0);
                        hasNew=false;
                    }
                }

                mTopicAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTopic.setLayoutManager(layoutManager);
        mTopicAdapter = new TopicAdapter(R.layout.item_topic, show);
        mRvTopic.addItemDecoration(new RecyclerViewDivider(this));
        mRvTopic.setAdapter(mTopicAdapter);
        mTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean isNew = show.get(position).isNew();
                if (isNew){
                    Intent intent = new Intent(mContext, AddTopicActivity.class);
                    String topic = show.get(position).getTopic();
                    intent.putExtra("name",topic);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("topic",show.get(position));
                    setResult(RESULT_CODE_TOPIC,intent);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtTopic.getWindowToken(),0);
                    finish();
                }

            }
        });
    }

    private void initData() {
        SCHttpUtils.postWithSpaceId()
                .url(HttpApi.TOPIC_QUERY_SPACEID)
                .addParams("page","0")
                .addParams("sort","desc")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取话题失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取话题成功" + response);
                        if (response == null){
                            return;
                        }
                        ResponseTopicInfo responseTopicInfo = JSONObject.parseObject(response, ResponseTopicInfo.class);
                        if (responseTopicInfo ==null){
                            return;
                        }

                        ResponseTopicData data = responseTopicInfo.getData();
                        if (data == null){
                            return;
                        }

                        List<ResponseTopicContentBean> topicContentBeanList = data.getContent();

                        if (topicContentBeanList == null) {
                            return;
                        }

                        for (int i = 0; i < topicContentBeanList.size(); i ++) {
                            String topicName = topicContentBeanList.get(i).getTitle();
                            int topicId = topicContentBeanList.get(i).getTopicId();
                            TopicInfo topicInfo = new TopicInfo();
                            topicInfo.setTopic(topicName);
                            topicInfo.setTopicId(topicId);
                            datas.add(topicInfo);
                        }
                        show.addAll(datas);

                        mTopicAdapter.notifyDataSetChanged();
                    }
                });

    }

    @OnClick(R.id.tv_topic_cancel)
    public void onClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtTopic.getWindowToken(),0);
        finish();
    }
}
