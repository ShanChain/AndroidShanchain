package com.shanchain.arkspot.ui.view.activity.chat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ContactAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.BdContactInfo;
import com.shanchain.arkspot.ui.presenter.ContactPresenter;
import com.shanchain.arkspot.ui.presenter.impl.ContactPresenterImpl;
import com.shanchain.arkspot.ui.view.activity.chat.view.ContactView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;


public class ContactActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ContactView {

    @Bind(R.id.tb_contact)
    ArthurToolBar mTbContact;
    @Bind(R.id.et_contact)
    EditText mEtContact;
    @Bind(R.id.elv_contact)
    ExpandableListView mElvContact;
    private List<String> parentList = new ArrayList<>();
    private Map<String, List<BdContactInfo>> map = new HashMap<>();
    private ContactAdapter mContactAdapter;
    private ContactPresenter mPresenter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initViewsAndEvents() {

        mPresenter = new ContactPresenterImpl(this);

        initToolBar();
        initListView();
        initData();

    }

    private void initListView() {
        mContactAdapter = new ContactAdapter(parentList, map);

        mElvContact.setAdapter(mContactAdapter);

        mElvContact.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BdContactInfo bdContactInfo = map.get(parentList.get(groupPosition)).get(childPosition);
                boolean isGroup = bdContactInfo.isGroup();
                String name ="";
                if (isGroup){
                    name = bdContactInfo.getGroupInfo().getGroupId();
                }else {
                    name = bdContactInfo.getHxUerBean().getHxUserName();
                }

                if (TextUtils.isEmpty(name)){
                    ToastUtils.showToast(mContext,"当前用户异常");

                }else {
                    Intent intent = new Intent(mContext, ChatRoomActivity.class);
                    intent.putExtra("isGroup", isGroup);
                    intent.putExtra("toChatName", name);
                    startActivity(intent);
                }
                return true;
            }
        });


    }

    private void initData() {
        parentList.add("我的关注");
        parentList.add("互相关注");
        parentList.add("我的粉丝");
        parentList.add("对话场景");
        ArrayList<BdContactInfo> bdContactInfos = new ArrayList<>();
        map.put("我的关注", bdContactInfos);
        map.put("互相关注", bdContactInfos);
        map.put("我的粉丝", bdContactInfos);
        map.put("对话场景", bdContactInfos);
        mPresenter.initContact();

    }

    private void initToolBar() {
        mTbContact.setBtnEnabled(true, false);
        mTbContact.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void initContactSuccess(List<BdContactInfo> focus, List<BdContactInfo> fans, List<BdContactInfo> each) {
        if (focus == null) {
            return;
        }
        map.put("互相关注", each);
        map.put("我的粉丝", fans);
        map.put("我的关注", focus);
        mContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void initGroupSuccess(List<BdContactInfo> group) {
        if (group == null) {
            return;
        }

        map.put("对话场景", group);
        mContactAdapter.notifyDataSetChanged();
    }
}
