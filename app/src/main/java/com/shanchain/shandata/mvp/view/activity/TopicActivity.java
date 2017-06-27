package com.shanchain.shandata.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TopicAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.TopicInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class TopicActivity extends BaseActivity {


    private static final int RESULT_CODE = 10;
    @Bind(R.id.et_topic_search)
    EditText mEtTopicSearch;
    @Bind(R.id.tv_topic_cancle)
    TextView mTvTopicCancle;
    @Bind(R.id.rv_topic_list)
    RecyclerView mRvTopicList;
    @Bind(R.id.activity_topic)
    LinearLayout mActivityTopic;
    private List<TopicInfo> datas;
    private List<TopicInfo> show;
    private TopicAdapter mAdapter;

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
        mEtTopicSearch.addTextChangedListener(new TextWatcher() {
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

                if (show.size() == 0){
                    TopicInfo topicInfo = new TopicInfo();
                    topicInfo.setTopic("#"+input+"#");
                    topicInfo.setNew(true);
                    show.add(topicInfo);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTopicList.setLayoutManager(linearLayoutManager);
        mRvTopicList.addItemDecoration(new RecyclerViewDivider(TopicActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(TopicActivity.this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        mAdapter = new TopicAdapter(this, R.layout.item_topic, show);
        mRvTopicList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                clickItem(position);
            }
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            TopicInfo topicInfo = new TopicInfo();
            topicInfo.setTopic("#高考加油#" + i);
            datas.add(topicInfo);
        }
        show.addAll(datas);
    }

    private void clickItem(int position) {
        TopicInfo topicInfo = show.get(position);
        Intent intent = new Intent();
        intent.putExtra("topicReturn",topicInfo);
        setResult(RESULT_CODE,intent);
        finish();
    }
    @OnClick(R.id.tv_topic_cancle)
    public void onClick() {
        finish();
    }
}
