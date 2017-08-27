package com.shanchain.arkspot.ui.view.activity.story;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SearchRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.RoleInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

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
    private List<RoleInfo> datas;
    private List<RoleInfo> show;
    private SearchRoleAdapter mSearchRoleAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_role;
    }

    @Override
    protected void initViewsAndEvents() {
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
                for (int i = 0; i < datas.size(); i ++) {
                    if (datas.get(i).getName().contains(input)){
                        show.add(datas.get(i));
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
        datas = new ArrayList<>();
        show = new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            RoleInfo roleInfo = new RoleInfo();
            if (i % 5== 0) {
                roleInfo.setImg(R.drawable.photo_city);
                roleInfo.setName("特朗普" + i);
                roleInfo.setDes("庄颜，中国科幻小说《三体|| 黑暗森林》中的人 物。“她”是逻辑构思的小说的主人公，是.....");
            }else if (i%5 == 1) {
                roleInfo.setImg(R.drawable.photo_bear);
                roleInfo.setName("庄周" + i);
                roleInfo.setDes("一壶浊酒喜相逢，古今多少事，都付笑谈中。");
            }else if (i%5 == 2) {
                roleInfo.setImg(R.drawable.photo_heart);
                roleInfo.setName("逗比" + i);
                roleInfo.setDes("不是每个逗比斗很逗。。。");
            }else if (i%5 == 3) {
                roleInfo.setImg(R.drawable.photo_public);
                roleInfo.setName("曾小贤" + i);
                roleInfo.setDes("就这些吧，没啥介绍的了");
            }else if (i%5 == 4) {
                roleInfo.setImg(R.drawable.photo_yue);
                roleInfo.setName("曹操" + i);
                roleInfo.setDes("一壶浊酒喜相逢，古今多少事，都付笑谈中。");
            }
            datas.add(roleInfo);
        }
        show.addAll(datas);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSearchRole.setLayoutManager(layoutManager);
        mRvSearchRole.addItemDecoration(new RecyclerViewDivider(this));
        mSearchRoleAdapter = new SearchRoleAdapter(R.layout.item_search_role,show);
        mRvSearchRole.setAdapter(mSearchRoleAdapter);

        mSearchRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

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

    }
}
