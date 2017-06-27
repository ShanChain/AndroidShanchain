package com.shanchain.shandata.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AiteContactsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ContactInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AiteContactsActivity extends BaseActivity {


    @Bind(R.id.et_aite_search)
    EditText mEtAiteSearch;
    @Bind(R.id.rv_aite_list)
    RecyclerView mRvAiteList;
    @Bind(R.id.activity_aite_contacts)
    LinearLayout mActivityAiteContacts;
    private List<ContactInfo> datas;

    private static final int RESULT_CODE = 100;

    private List<ContactInfo> show;

    private AiteContactsAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_aite_contacts;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mEtAiteSearch.addTextChangedListener(new TextWatcher() {
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



                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();
        for (int i = 0; i < 28; i ++) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName("李江" + i);
            datas.add(contactInfo);
        }
        show.addAll(datas);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvAiteList.setLayoutManager(linearLayoutManager);
        mRvAiteList.addItemDecoration(new RecyclerViewDivider(AiteContactsActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(AiteContactsActivity.this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        mAdapter = new AiteContactsAdapter(this, R.layout.item_aite_contacts,show);
        mRvAiteList.setAdapter(mAdapter);
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

    private void clickItem(int position) {
        ContactInfo contactInfo = show.get(position);
        Intent intent = new Intent();
        intent.putExtra("aiteReturn",contactInfo);
        setResult(RESULT_CODE,intent);
        finish();
    }


}
