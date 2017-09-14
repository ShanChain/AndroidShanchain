package com.shanchain.arkspot.ui.view.activity.chat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ContactAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.ContactInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;


public class ContactActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_contact)
    ArthurToolBar mTbContact;
    @Bind(R.id.et_contact)
    EditText mEtContact;
    @Bind(R.id.elv_contact)
    ExpandableListView mElvContact;
    List<String> parent;
    Map<String, ArrayList<ContactInfo>> map;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initListView();
    }

    private void initListView() {
        ContactAdapter contactAdapter = new ContactAdapter(parent, map);

        mElvContact.setAdapter(contactAdapter);

        mElvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    private void initData() {
        map = new HashMap<>();
        parent = new ArrayList<>();
        parent.add("我的关注");
        parent.add("互相关注");
        parent.add("我的粉丝");
        parent.add("对话场景");

        ArrayList<ContactInfo> list1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName("我的关注" + i);
            list1.add(contactInfo);
        }

        map.put("我的关注", list1);

        ArrayList<ContactInfo> list2 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName("互相关注" + i);
            list2.add(contactInfo);
        }

        map.put("互相关注", list2);

        ArrayList<ContactInfo> list3 = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName("我的粉丝" + i);
            list3.add(contactInfo);
        }

        map.put("我的粉丝", list3);

        ArrayList<ContactInfo> list4 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setName("对话场景" + i);
            list4.add(contactInfo);
        }

        map.put("对话场景", list4);

    }

    private void initToolBar() {
        mTbContact.setBtnEnabled(true, false);
        mTbContact.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
