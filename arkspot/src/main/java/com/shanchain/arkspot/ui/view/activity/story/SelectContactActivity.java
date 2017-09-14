package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SelectContactAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.ContactInfo;
import com.shanchain.arkspot.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import utils.ToastUtils;


public class SelectContactActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int RESULT_CODE_CONTACTS = 100;
    @Bind(R.id.tb_select_contact)
    ArthurToolBar mTbSelectContact;
    @Bind(R.id.et_select_contact)
    EditText mEtSelectContact;
    @Bind(R.id.rv_select_contact)
    RecyclerView mRvSelectContact;
    @Bind(R.id.sb_select_contact)
    WaveSideBar mSbSelectContact;
    private List<ContactInfo> datas;
    private List<ContactInfo> show;
    private SelectContactAdapter mContactAdapter;
    private ArrayList<String> selected;
    /**
     * 描述：是否是选择艾特好友
     */
    private boolean mIsAt;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_select_contact;
    }

    @Override
    protected void initViewsAndEvents() {
        mIsAt = getIntent().getBooleanExtra("isAt", true);
        initToolBar();
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mEtSelectContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                show.clear();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getName().contains(input)) {
                        show.add(datas.get(i));
                    }
                }

                mContactAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSbSelectContact.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < show.size(); i++) {
                    if (show.get(i).getLetter().equalsIgnoreCase(index)) {
                        ((LinearLayoutManager) mRvSelectContact.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }


    private void initRecyclerView() {
        mRvSelectContact.setLayoutManager(new LinearLayoutManager(this));
        mContactAdapter = new SelectContactAdapter(R.layout.item_select_contact, show);
        mRvSelectContact.setAdapter(mContactAdapter);
        mContactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rb_item_contact) {
                    boolean selected = show.get(position).isSelected();
                    show.get(position).setSelected(!selected);
                    mContactAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initToolBar() {
        mTbSelectContact.setTitleText(mIsAt ? "选择联系人" : "邀请好友");
        mTbSelectContact.setOnLeftClickListener(this);
        mTbSelectContact.setOnRightClickListener(this);
    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();
        ContactInfo contactInfo1 = new ContactInfo();
        contactInfo1.setName("阿里");
        contactInfo1.setLetter("A");
        datas.add(contactInfo1);
        ContactInfo contactInfo2 = new ContactInfo();
        contactInfo2.setName("阿倍");
        contactInfo2.setLetter("A");
        datas.add(contactInfo2);
        ContactInfo contactInfo3 = new ContactInfo();
        contactInfo3.setName("阿咯");
        contactInfo3.setLetter("A");
        datas.add(contactInfo3);
        ContactInfo contactInfo4 = new ContactInfo();
        contactInfo4.setName("北京");
        contactInfo4.setLetter("B");
        datas.add(contactInfo4);
        ContactInfo contactInfo5 = new ContactInfo();
        contactInfo5.setName("北海");
        contactInfo5.setLetter("B");
        datas.add(contactInfo5);
        ContactInfo contactInfo6 = new ContactInfo();
        contactInfo6.setName("超市");
        contactInfo6.setLetter("C");
        datas.add(contactInfo6);
        ContactInfo contactInfo7 = new ContactInfo();
        contactInfo7.setName("潮汕");
        contactInfo7.setLetter("C");
        datas.add(contactInfo7);
        ContactInfo contactInfo8 = new ContactInfo();
        contactInfo8.setName("大帝");
        contactInfo8.setLetter("D");
        datas.add(contactInfo8);
        ContactInfo contactInfo9 = new ContactInfo();
        contactInfo9.setName("房子");
        contactInfo9.setLetter("F");
        datas.add(contactInfo9);
        ContactInfo contactInfo10 = new ContactInfo();
        contactInfo10.setName("哥哥");
        contactInfo10.setLetter("G");
        datas.add(contactInfo10);
        ContactInfo contactInfo11 = new ContactInfo();
        contactInfo11.setName("咖喱gaygay");
        contactInfo11.setLetter("G");
        datas.add(contactInfo11);
        ContactInfo contactInfo12 = new ContactInfo();
        contactInfo12.setName("歌姬");
        contactInfo12.setLetter("G");
        datas.add(contactInfo12);
        ContactInfo contactInfo13 = new ContactInfo();
        contactInfo13.setName("黄了");
        contactInfo13.setLetter("H");
        datas.add(contactInfo13);
        ContactInfo contactInfo14 = new ContactInfo();
        contactInfo14.setName("极限");
        contactInfo14.setLetter("J");
        datas.add(contactInfo14);
        ContactInfo contactInfo15 = new ContactInfo();
        contactInfo15.setName("激励");
        contactInfo15.setLetter("J");
        datas.add(contactInfo15);
        ContactInfo contactInfo16 = new ContactInfo();
        contactInfo16.setName("卡卡");
        contactInfo16.setLetter("K");
        datas.add(contactInfo16);
        ContactInfo contactInfo17 = new ContactInfo();
        contactInfo17.setName("牛B");
        contactInfo17.setLetter("N");
        datas.add(contactInfo17);
        ContactInfo contactInfo18 = new ContactInfo();
        contactInfo18.setName("闪现");
        contactInfo18.setLetter("S");
        datas.add(contactInfo18);
        ContactInfo contactInfo19 = new ContactInfo();
        contactInfo19.setName("是非");
        contactInfo19.setLetter("S");
        datas.add(contactInfo19);
        ContactInfo contactInfo20 = new ContactInfo();
        contactInfo20.setName("饕餮");
        contactInfo20.setLetter("T");
        datas.add(contactInfo20);
        ContactInfo contactInfo21 = new ContactInfo();
        contactInfo21.setName("小弟弟");
        contactInfo21.setLetter("X");
        datas.add(contactInfo21);
        ContactInfo contactInfo22 = new ContactInfo();
        contactInfo22.setName("洋葱");
        contactInfo22.setLetter("Y");
        datas.add(contactInfo22);
        ContactInfo contactInfo23 = new ContactInfo();
        contactInfo23.setName("颜色");
        contactInfo23.setLetter("Y");
        datas.add(contactInfo23);
        ContactInfo contactInfo24 = new ContactInfo();
        contactInfo24.setName("亚咩");
        contactInfo24.setLetter("Y");
        datas.add(contactInfo24);
        ContactInfo contactInfo25 = new ContactInfo();
        contactInfo25.setName("yes");
        contactInfo25.setLetter("Y");
        datas.add(contactInfo25);

        show.addAll(datas);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        selected = new ArrayList<>();
        for (int i = 0; i < show.size(); i++) {
            if (show.get(i).isSelected()) {
                selected.add(show.get(i).getName());
            }
        }

        if (mIsAt) {
            if (selected.size() == 0) {
                finish();
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("contacts", selected);
                setResult(RESULT_CODE_CONTACTS, intent);
                finish();
            }
        } else {
            if (selected.size() == 0) {
                //没有选择联系人
                ToastUtils.showToast(this, "还没选择联系人哦~");
                return;
            } else {
                Intent intent = new Intent(this, ChatRoomActivity.class);
                intent.putStringArrayListExtra("contacts", selected);
                startActivity(intent);
            }
        }
    }
}
