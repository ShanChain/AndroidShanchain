package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SearchRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.RNDetailExt;
import com.shanchain.arkspot.ui.model.RNGDataBean;
import com.shanchain.arkspot.ui.model.SpaceCharacterBean;
import com.shanchain.arkspot.ui.model.SpaceCharacterModelInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class SearchRoleActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_search_role)
    ArthurToolBar mTbSearchRole;
    @Bind(R.id.et_search_role_search)
    EditText mEtSearchRoleSearch;
    @Bind(R.id.rv_search_role)
    RecyclerView mRvSearchRole;
    private List<SpaceCharacterBean> mDatas = new ArrayList<>();
    private List<SpaceCharacterBean> show = new ArrayList<>();
    private SearchRoleAdapter mSearchRoleAdapter;
    private SpaceCharacterModelInfo mSpaceInfo;
    private int mSpaceId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_role;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mSpaceInfo = (SpaceCharacterModelInfo) intent.getSerializableExtra("spaceInfo");
        mSpaceId = intent.getIntExtra("spaceId", 0);
        mDatas = mSpaceInfo.getContent();
        show.addAll(mDatas);
        initToolBar();
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mEtSearchRoleSearch.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void initData() {

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSearchRole.setLayoutManager(layoutManager);
        mRvSearchRole.addItemDecoration(new RecyclerViewDivider(this));
        mSearchRoleAdapter = new SearchRoleAdapter(R.layout.item_search_role,show);
        mRvSearchRole.setAdapter(mSearchRoleAdapter);

        View emptyView = View.inflate(this,R.layout.empty_search_role,null);
        mSearchRoleAdapter.setEmptyView(emptyView);
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
                detailExt.setModelId(show.get(position).getModelId() + "");

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
}
