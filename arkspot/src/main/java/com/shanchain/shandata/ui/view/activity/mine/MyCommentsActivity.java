package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.encryption.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyCommentsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdMyCommentBean;
import com.shanchain.shandata.ui.model.CommentBean;
import com.shanchain.shandata.ui.model.CommentListMineInfo;
import com.shanchain.shandata.ui.model.CommentMineData;
import com.shanchain.shandata.ui.model.CommentStoryInfo;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.ResponseStoryList;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.other.SCEmptyView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


public class MyCommentsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {


    @Bind(R.id.tb_my_comments)
    ArthurToolBar mTbMyComments;
    @Bind(R.id.rv_comments)
    RecyclerView mRvComments;
    @Bind(R.id.srl_my_comments)
    SwipeRefreshLayout mSrlMyComments;
    private List<BdMyCommentBean> datas;
    private List<BdMyCommentBean> tempDatas;
    private MyCommentsAdapter mAdapter;
    private boolean mLast;
    private int page = 0;
    private int pageSise = 10;
    private StoryModelBean mBean;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_comments;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData(false, true);
        //initRecyclerView();

    }

    private void initData(final boolean isLoadMore, final boolean isFirst) {
        if (isFirst){
            datas = new ArrayList<>();
            tempDatas = new ArrayList<>();
        }
        SCHttpUtils.postWithUserId()
                .url(HttpApi.COMMENT_LIST_MINE)
                .addParams("page", page + "")
                .addParams("size", pageSise + "")
                .build()
                .execute(new SCHttpStringCallBack() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        initError();
                        LogUtils.i("获取我的评论列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            initError();
                            return;
                        }

                        CommentListMineInfo commentListMineInfo = JSONObject.parseObject(response, CommentListMineInfo.class);
                        if (commentListMineInfo == null) {
                            initError();
                            return;
                        }

                        String code = commentListMineInfo.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initError();
                            return;
                        }

                        CommentMineData data = commentListMineInfo.getData();
                        if (data == null) {
                            initError();
                            return;
                        }

                        List<CommentBean> content = data.getContent();
                        mLast = data.isLast();

                        tempDatas.clear();
                        for (int i = 0; i < content.size(); i++) {
                            int storyId = content.get(i).getStoryId();
                            BdMyCommentBean bdMyCommentBean = new BdMyCommentBean();
                            bdMyCommentBean.setCommentBean(content.get(i));
                            bdMyCommentBean.setStoryId(storyId);
                            tempDatas.add(bdMyCommentBean);
                        }

                        obtainStoryInfo(isLoadMore, isFirst);

                    }
                });

    }

    private void obtainStoryInfo(final boolean isloadMore, final boolean isFirst) {
        final List<Integer> storyIds = new ArrayList<>();
        for (int i = 0; i < tempDatas.size(); i++) {
            storyIds.add(tempDatas.get(i).getStoryId());
        }

        String dataArr = JSON.toJSONString(storyIds);

        SCHttpUtils.post()
                .url(HttpApi.STORY_GET)
                .addParams("dataArray", dataArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        initError();
                        LogUtils.i("获取故事列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取故事列表成功 = " + response);
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }

                        ResponseStoryList storyList = JSONObject.parseObject(response, ResponseStoryList.class);
                        if (storyList == null) {
                            initError();
                            return;
                        }

                        String code = storyList.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initError();
                            return;
                        }
                        List<CommentStoryInfo> infoList = storyList.getData();
                        if (infoList == null) {
                            initError();
                            return;
                        }

                        for (int i = 0; i < tempDatas.size(); i++) {
                            int storyId = tempDatas.get(i).getStoryId();
                            for (CommentStoryInfo info : infoList) {
                                if (storyId == info.getStoryId()) {
                                    tempDatas.get(i).setStoryInfo(info);
                                }
                            }
                        }

                        obtainCharacterInfo(isloadMore,isFirst);

                    }
                });

    }

    private void obtainCharacterInfo(final boolean isloadMore,final boolean isFirst) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < tempDatas.size(); i ++) {
            int characterId = tempDatas.get(i).getCommentBean().getCharacterId();
            ids.add(characterId);
        }

        String jArr = JSONObject.toJSONString(ids);

        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取角色简要信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取到角色简要信息 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            String data = SCJsonUtils.parseData(response);
                            List<ContactBean> beanList = JSONObject.parseArray(data, ContactBean.class);
                            for (int i = 0; i < tempDatas.size(); i ++) {
                                int characterId = tempDatas.get(i).getCommentBean().getCharacterId();
                                for (ContactBean bean : beanList){
                                    if (characterId == bean.getCharacterId()){
                                        tempDatas.get(i).setContactBean(bean);
                                    }
                                }
                            }

                            datas.addAll(tempDatas);
                            if (isFirst) {
                                initRecyclerView();
                            } else {
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            if (isloadMore) {
                                mAdapter.loadMoreComplete();
                            }
                            if (mLast) {
                                //没有数据了
                                mAdapter.loadMoreEnd();
                            } else {
                                //不是最后一页，有数据
                                page++;
                            }
                            if (mSrlMyComments != null) {
                                mSrlMyComments.setRefreshing(false);
                            }


                        }else{
                        
                        }
                    }
                });
    }

    private void initError() {
        if (mSrlMyComments!=null){
            mSrlMyComments.setRefreshing(false);
        }

        if (mAdapter != null) {
            mAdapter.loadMoreFail();
        }

    }

    private void initRecyclerView() {
        mSrlMyComments.setOnRefreshListener(this);
        mSrlMyComments.setEnabled(false);
        View emptyView = new SCEmptyView(this, R.string.str_empty_my_comments, R.mipmap.abs_comment_icon_thumbsup_default_1);
        mRvComments.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyCommentsAdapter(R.layout.item_my_comments, datas);
        mAdapter.setEnableLoadMore(true);
        mRvComments.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 1), getResources().getColor(R.color.colorDivider)));
        mAdapter.setOnLoadMoreListener(this, mRvComments);
        mRvComments.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnItemClickListener(this);
    }

    private void initToolBar() {
        mTbMyComments.setBtnEnabled(true, false);
        mTbMyComments.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        mSrlMyComments.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSrlMyComments != null) {
                    mSrlMyComments.setRefreshing(false);
                }
            }
        }, 1000);
    }

    @Override
    public void onLoadMoreRequested() {
        initData(true,false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
       /* BdMyCommentBean bdMyCommentBean = mAdapter.getData().get(position);
        String cacheSpaceId = SCCacheUtils.getCacheSpaceId();
        int spaceId = bdMyCommentBean.getStoryInfo().getSpaceId();
        if (TextUtils.equals(cacheSpaceId,spaceId + "")){
            mBean = new StoryModelBean();
            getStoryCharacterInfo(bdMyCommentBean);
        }else {
             ToastUtils.showToast(mContext,"当前角色不在该动态世界");
        }*/
    }

    private boolean characterSuc;
    private boolean favSuc;

    private void getStoryCharacterInfo(final BdMyCommentBean bdMyCommentBean) {
        int characterId = bdMyCommentBean.getStoryInfo().getCharacterId();
        final int storyId = bdMyCommentBean.getStoryId();
        List<Integer> ids = new ArrayList<>();
        ids.add(characterId);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray",JSONObject.toJSONString(ids))
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取角色简要信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取到角色简要信息 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            String data = SCJsonUtils.parseData(response);
                            List<ContactBean> beanList = JSONObject.parseArray(data, ContactBean.class);
                            ContactBean contactBean = beanList.get(0);

                            CommentStoryInfo storyInfo = bdMyCommentBean.getStoryInfo();

                            mBean.setCharacterId(storyInfo.getCharacterId());
                            mBean.setSupportCount(storyInfo.getSupportCount());
                            mBean.setType(storyInfo.getType());
                            mBean.setCharacterImg(contactBean.getHeadImg());
                            mBean.setCharacterName(contactBean.getName());
                            mBean.setCommendCount(storyInfo.getCommentCount());
                            mBean.setCreateTime(storyInfo.getCreateTime());
                            mBean.setDetailId("s"+storyInfo.getStoryId());
                            mBean.setGenNum(storyInfo.getGenNum());
                            mBean.setIntro(storyInfo.getIntro());
                            mBean.setLineNum(storyInfo.getLineNum());
                            mBean.setRootId(storyInfo.getRootId());
                            mBean.setSpaceId(storyInfo.getSpaceId());
                            mBean.setStatus(storyInfo.getStatus());
                            mBean.setTitle(storyInfo.getTitle());
                            mBean.setTranspond(storyInfo.getTranspond());
                            characterSuc = true;
                            if (favSuc){
                                toDetail();
                            }
                        }else{

                        }
                    }
                });

        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_IS_FAV)
                .addParams("checkId",""+storyId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("获取故事点赞结果失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取到故事点赞结果 = " + response);
                        String code = SCJsonUtils.parseCode(response);
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            String data = SCJsonUtils.parseData(response);
                            boolean fav = SCJsonUtils.parseBoolean(data, "data");
                            mBean.setBeFav(fav);
                            favSuc = true;
                            if (characterSuc){
                                toDetail();
                            }
                        }else{

                        }
                    }
                });


    }

    private void toDetail() {
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        intent.putExtra("story", mBean);
        startActivity(intent);
    }


}
