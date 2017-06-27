package com.shanchain.shandata.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChooseContactsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ChooseContactsInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChooseContactsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.et_choise_search)
    EditText mEtChoiseSearch;
    @Bind(R.id.rv_choise_list)
    RecyclerView mRvChoiseList;
    @Bind(R.id.activity_choise_contacts)
    LinearLayout mActivityChoiseContacts;
    private ArthurToolBar mChoiseContactsToolBar;
    private List<ChooseContactsInfo> datas;
    private List<ChooseContactsInfo> datasChecked;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choise_contacts;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvChoiseList.setLayoutManager(linearLayoutManager);
        mRvChoiseList.addItemDecoration(new RecyclerViewDivider(ChooseContactsActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(ChooseContactsActivity.this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        ChooseContactsAdapter adapter = new ChooseContactsAdapter(this,R.layout.item_choose_contacts,datas);
        mRvChoiseList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 28; i ++) {
            ChooseContactsInfo contactInfo = new ChooseContactsInfo();
            contactInfo.setName("李江" + i);
            datas.add(contactInfo);
        }
    }

    private void initToolBar() {
        mChoiseContactsToolBar = (ArthurToolBar) findViewById(R.id.toolbar_choise_contacts);
        mChoiseContactsToolBar.setBtnEnabled(true);
        mChoiseContactsToolBar.setOnLeftClickListener(this);
        mChoiseContactsToolBar.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        datasChecked = new ArrayList<>();
        for (int i = 0; i < datas.size(); i ++) {
            if (datas.get(i).isChecked()){
                datasChecked.add(datas.get(i));
            }
        }


        LogUtils.d("被选中的联系人数量"+datasChecked.size());

    }
}
