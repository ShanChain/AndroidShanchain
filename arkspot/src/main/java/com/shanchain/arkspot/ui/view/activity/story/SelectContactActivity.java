package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.SelectContactAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.ContactInfo;
import com.shanchain.arkspot.ui.model.RequestContactInfo;
import com.shanchain.arkspot.ui.model.ResponseContactArr;
import com.shanchain.arkspot.ui.model.ResponseContactBean;
import com.shanchain.arkspot.ui.model.ResponseContactData;
import com.shanchain.arkspot.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


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
    private List<ContactInfo> datas = new ArrayList<>();
    private List<ContactInfo> show = new ArrayList<>();
    private SelectContactAdapter mContactAdapter;
    private ArrayList<String> selected = new ArrayList<>();
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
        initRecyclerView();
        initData();
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
        SCHttpUtils.postWithSpaceId()
                .url(HttpApi.SPACE_CONTACT_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            return;
                        }
                        RequestContactInfo requestContactInfo = JSONObject.parseObject(response, RequestContactInfo.class);
                        if (requestContactInfo == null){
                            return;
                        }
                        String code = requestContactInfo.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            return;
                        }

                        ResponseContactData data = requestContactInfo.getData();

                        if (data == null){
                            return;
                        }
                        List<ResponseContactArr> contactArrs = data.getArray();
                        if (contactArrs == null){
                            return;
                        }
                        for (int i = 0; i < contactArrs.size(); i ++) {
                            String letter = contactArrs.get(i).getLetter();
                            List<ResponseContactBean> contactBeenList = contactArrs.get(i).getList();
                            for (int j = 0; j < contactBeenList.size(); j ++) {
                                String headImg = contactBeenList.get(j).getHeadImg();
                                int modelId = contactBeenList.get(j).getModelId();
                                String name = contactBeenList.get(j).getName();
                                ContactInfo info = new ContactInfo();
                                info.setName(name);
                                info.setImg(headImg);
                                info.setLetter(letter);
                                info.setModuleId(modelId);
                                datas.add(info);

                            }

                        }

                        show.addAll(datas);
                        mContactAdapter.notifyDataSetChanged();

                    }
                });

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
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
