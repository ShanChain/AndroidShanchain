package com.shanchain.arkspot.ui.view.activity.story;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.TopicAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.TopicInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class TopicActivity extends BaseActivity {

    private static final int RESULT_CODE_TOPIC = 10;
    @Bind(R.id.et_topic)
    EditText mEtTopic;
    @Bind(R.id.tv_topic_cancel)
    TextView mTvTopicCancel;
    @Bind(R.id.rv_topic)
    RecyclerView mRvTopic;
    private List<TopicInfo> datas;
    private List<TopicInfo> show;
    private TopicAdapter mTopicAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_topic;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
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
                    show.add(topicInfo);
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
                Intent intent = new Intent();
                intent.putExtra("topic",show.get(position));
                setResult(RESULT_CODE_TOPIC,intent);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtTopic.getWindowToken(),0);
                finish();
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TopicInfo topicInfo = new TopicInfo();
            topicInfo.setTopic("这是什么话题哦" + i);
            datas.add(topicInfo);
        }

        show.addAll(datas);
    }

    @OnClick(R.id.tv_topic_cancel)
    public void onClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtTopic.getWindowToken(),0);
        finish();
    }
}
