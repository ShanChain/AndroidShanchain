package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SearchRoleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.SpaceCharacterBean;
import com.shanchain.shandata.ui.model.SpaceCharacterModelInfo;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


public class SearchRoleActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tb_search_role)
    ArthurToolBar mTbSearchRole;
    @Bind(R.id.et_search_role_search)
    EditText mEtSearchRoleSearch;
    @Bind(R.id.rv_search_role)
    RecyclerView mRvSearchRole;
    private List<SpaceCharacterBean> mDatas;
    private SearchRoleAdapter mSearchRoleAdapter;
    private int mSpaceId;
    private int page = 0;
    private int size = 10;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_role;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mSpaceId = intent.getIntExtra("spaceId", 0);
        initToolBar();
        initSpaceModel(page,size);
        initListener();
    }

    private void initListener() {
        /*mEtSearchRoleSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                show.clear();
                for (int i = 0; i < mDatas.size(); i ++) {
                    if (mDatas.get(i).getName().contains(input)||mDatas.get(i).getIntro().contains(input)){
                        show.add(mDatas.get(i));
                    }else {

                    }
                }

                mSearchRoleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }


    private void initSpaceModel(final int page , int size) {
        mDatas = new ArrayList<>();
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_MODEL_QUERY_SPACEID)
                .addParams("spaceId", mSpaceId + "")
                .addParams("page",""+page)
                .addParams("size",""+size)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取时空角色成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                SpaceCharacterModelInfo mModelInfo = JSONObject.parseObject(response).getObject("data", SpaceCharacterModelInfo.class);
                                Boolean last = mModelInfo.isLast();
                                List<SpaceCharacterBean> characterBeanList = mModelInfo.getContent();
                                mDatas.addAll(characterBeanList);
                                if (page == 0){
                                    initRecyclerView();
                                    mSearchRoleAdapter.disableLoadMoreIfNotFullPage(mRvSearchRole);
                                }else {
                                    mSearchRoleAdapter.addData(characterBeanList);
                                    mSearchRoleAdapter.notifyDataSetChanged();
                                    if (last){
                                        mSearchRoleAdapter.loadMoreEnd();
                                    }else {
                                        mSearchRoleAdapter.loadMoreComplete();
                                    }
                                }

                            }else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("获取时空角色列表失败");
                        }

                    }
                });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSearchRole.setLayoutManager(layoutManager);
        mRvSearchRole.addItemDecoration(new RecyclerViewDivider(this));
        mSearchRoleAdapter = new SearchRoleAdapter(R.layout.item_search_role,mDatas);
        mSearchRoleAdapter.setEnableLoadMore(true);
        mRvSearchRole.setAdapter(mSearchRoleAdapter);
        View emptyView = View.inflate(this,R.layout.empty_search_role,null);
        mSearchRoleAdapter.setEmptyView(emptyView);
        mSearchRoleAdapter.setOnLoadMoreListener(this,mRvSearchRole);
        mSearchRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();

                RNDetailExt detailExt = new RNDetailExt();
                RNGDataBean gDataBean = new RNGDataBean();
                String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);
                String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
                String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
                gDataBean.setCharacterId(characterId);
                gDataBean.setSpaceId(spaceId);
                gDataBean.setToken(token);
                gDataBean.setUserId(uId);
                detailExt.setgData(gDataBean);
                detailExt.setModelId(mSearchRoleAdapter.getData().get(position).getModelId() + "");

                String json =JSONObject.toJSONString(detailExt);
                bundle.putString(NavigatorModule.REACT_PROPS, json);
                NavigatorModule.startReactPage(mContext, RNPagesConstant.RoleDetailScreen,bundle);
            }
        });

    }

    private void initToolBar() {
        mTbSearchRole.setOnLeftClickListener(this);
        mTbSearchRole.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(mContext,AddRoleActivity.class);
        intent.putExtra("spaceId",mSpaceId);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        page ++;
        initSpaceModel(page,size);
    }
}
