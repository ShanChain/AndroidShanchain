package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ContactAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdContactInfo;
import com.shanchain.shandata.ui.model.GroupInfo;
import com.shanchain.shandata.ui.model.ResponseHxUerBean;
import com.shanchain.shandata.ui.presenter.ContactPresenter;
import com.shanchain.shandata.ui.presenter.impl.ContactPresenterImpl;
import com.shanchain.shandata.ui.view.activity.chat.view.ContactView;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
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
                String hxUserName ="";
                String name = "";
                if (isGroup){
                    GroupInfo groupInfo = bdContactInfo.getGroupInfo();
                    if (groupInfo == null){
                        ToastUtils.showToast(mContext,"当前场景异常");
                        return true;
                    }
                    hxUserName = groupInfo.getGroupId();
                    name = bdContactInfo.getGroupInfo().getGroupName();
                }else {
                    ResponseHxUerBean hxUerBean = bdContactInfo.getHxUerBean();
                    if (hxUerBean == null){
                        ToastUtils.showToast(mContext,"当前用户异常");
                        return true;
                    }
                    hxUserName = hxUerBean.getHxUserName();
                    name = bdContactInfo.getContactBean().getName();
                }

                if (TextUtils.isEmpty(hxUserName)){
                    ToastUtils.showToast(mContext,"当前用户异常");
                }else {
                    Intent intent = new Intent(mContext, ChatRoomActivity.class);
                    intent.putExtra("isGroup", isGroup);
                    intent.putExtra("toChatName", hxUserName);
                    intent.putExtra("name",name);
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
        parentList.add("群组");
        ArrayList<BdContactInfo> bdContactInfos = new ArrayList<>();
        map.put("我的关注", bdContactInfos);
        map.put("互相关注", bdContactInfos);
        map.put("我的粉丝", bdContactInfos);
        map.put("群组", bdContactInfos);
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

        for (int i = 0; i < focus.size(); i ++) {
            LogUtils.i("我关注的人 = " + focus.get(i).getHxUerBean() + " ; "+focus.get(i).getContactBean());
        }

        for (int i = 0; i < fans.size(); i ++) {
            LogUtils.i("我的粉丝 = " + fans.get(i).getHxUerBean() + " ; "+fans.get(i).getContactBean());
        }

        for (int i = 0; i < each.size(); i ++) {
            LogUtils.i("互相关注 = " + each.get(i).getHxUerBean() + " ; "+each.get(i).getContactBean());
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
        map.put("群组", group);
        mContactAdapter.notifyDataSetChanged();
    }
}
