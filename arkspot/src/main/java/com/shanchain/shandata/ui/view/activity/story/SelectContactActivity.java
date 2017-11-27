package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SelectContactAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdAtContactInfo;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ContactInfo;
import com.shanchain.shandata.ui.model.CreateGroupBean;
import com.shanchain.shandata.ui.model.RequestContactInfo;
import com.shanchain.shandata.ui.model.ResponseContactArr;
import com.shanchain.shandata.ui.model.ResponseContactBean;
import com.shanchain.shandata.ui.model.ResponseContactData;
import com.shanchain.shandata.ui.model.ResponseFocusContactArr;
import com.shanchain.shandata.ui.model.ResponseFocusContactBean;
import com.shanchain.shandata.ui.model.ResponseFocusContactInfo;
import com.shanchain.shandata.ui.model.ResponseHxUerBean;
import com.shanchain.shandata.ui.model.ResponseHxUserListInfo;
import com.shanchain.shandata.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.shandata.utils.EditTextUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

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
    private List<BdAtContactInfo> datas = new ArrayList<>();
    private List<BdAtContactInfo> show = new ArrayList<>();
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
        EditTextUtils.banEnterInput(mEtSelectContact);
        mEtSelectContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                show.clear();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getContactInfo().getName().contains(input)) {
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
                    if (show.get(i).getContactInfo().getLetter().equalsIgnoreCase(index)) {
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
                    mContactAdapter.notifyItemChanged(position);
                }
            }
        });
        mContactAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean selected = show.get(position).isSelected();
                show.get(position).setSelected(!selected);
                mContactAdapter.notifyItemChanged(position);
            }
        });
    }

    private void initToolBar() {
        mTbSelectContact.setTitleText(mIsAt ? "选择联系人" : "邀请好友");
        mTbSelectContact.setOnLeftClickListener(this);
        mTbSelectContact.setOnRightClickListener(this);
    }

    private void initData() {
        if (mIsAt) { //从发布动态跳入该页面

            SCHttpUtils.postWithSpaceId()
                    .url(HttpApi.SPACE_CONTACT_LIST)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (TextUtils.isEmpty(response)) {
                                return;
                            }
                            RequestContactInfo requestContactInfo = JSONObject.parseObject(response, RequestContactInfo.class);
                            if (requestContactInfo == null) {
                                return;
                            }
                            String code = requestContactInfo.getCode();
                            if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                return;
                            }

                            ResponseContactData data = requestContactInfo.getData();

                            if (data == null) {
                                return;
                            }
                            List<ResponseContactArr> contactArrs = data.getArray();
                            if (contactArrs == null) {
                                return;
                            }
                            for (int i = 0; i < contactArrs.size(); i++) {
                                String letter = contactArrs.get(i).getLetter();
                                List<ResponseContactBean> contactBeenList = contactArrs.get(i).getList();
                                for (int j = 0; j < contactBeenList.size(); j++) {
                                    String headImg = contactBeenList.get(j).getHeadImg();
                                    int modelId = contactBeenList.get(j).getModelId();
                                    String name = contactBeenList.get(j).getName();
                                    ContactInfo info = new ContactInfo();
                                    info.setName(name);
                                    info.setImg(headImg);
                                    info.setLetter(letter);
                                    info.setModuleId(modelId);
                                    BdAtContactInfo bdAtContactInfo = new BdAtContactInfo();
                                    bdAtContactInfo.setAt(true);
                                    bdAtContactInfo.setContactInfo(info);
                                    datas.add(bdAtContactInfo);

                                }
                            }
                            show.addAll(datas);
                            mContactAdapter.notifyDataSetChanged();

                        }
                    });
        } else { //从新场景跳入该页面

            SCHttpUtils.postWithUidAndSpaceId()
                    .url(HttpApi.FOCUS_CONTACTS)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("获取互相关注列表失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.i("获取互相关注列表成功 = " + response);
                            ResponseFocusContactInfo responseContactInfo = JSONObject.parseObject(response, ResponseFocusContactInfo.class);
                            String code = responseContactInfo.getCode();
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {

                                List<ResponseFocusContactArr> array = responseContactInfo.getData().getArray();

                                if (array != null && array.size() > 0) {
                                    ArrayList<BdAtContactInfo> bdAtContactInfos = new ArrayList<>();
                                    ArrayList<Integer> characterIds = new ArrayList<>();
                                    for (int i = 0; i < array.size(); i++) {
                                        ResponseFocusContactArr responseFocusContactArr = array.get(i);
                                        String letter = responseFocusContactArr.getLetter();
                                        List<ResponseFocusContactBean> list = responseFocusContactArr.getList();
                                        for (int j = 0; j < list.size(); j++) {
                                            ResponseFocusContactBean contactBean = list.get(j);
                                            int characterId = contactBean.getCharacterId();
                                            String headImg = contactBean.getHeadImg();
                                            String intro = contactBean.getIntro();
                                            String name = contactBean.getName();
                                            int modelNo = contactBean.getModelNo();
                                            characterIds.add(characterId);
                                            ContactInfo info = new ContactInfo();
                                            info.setLetter(letter);
                                            info.setName(name);
                                            info.setModuleId(modelNo);
                                            info.setImg(headImg);
                                            info.setIntro(intro);
                                            info.setCharacterId(characterId);
                                            BdAtContactInfo bdAtContactInfo = new BdAtContactInfo();
                                            bdAtContactInfo.setAt(false);
                                            bdAtContactInfo.setContactInfo(info);
                                            bdAtContactInfos.add(bdAtContactInfo);
                                        }
                                    }
                                    getHxUserInfo(bdAtContactInfos, characterIds);
                                }
                            }
                        }
                    });
        }
    }

    private void getHxUserInfo(final ArrayList<BdAtContactInfo> bdAtContactInfos, ArrayList<Integer> characterIds) {

        String jArr = JSON.toJSONString(characterIds);
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_LIST)
                .addParams("characterIds", jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取联系人失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取联系人成功");
                        ResponseHxUserListInfo hxUserListInfo = JSONObject.parseObject(response, ResponseHxUserListInfo.class);
                        String code = hxUserListInfo.getCode();
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            List<ResponseHxUerBean> hxUerBeanList = hxUserListInfo.getData();

                            for (int i = 0; i < bdAtContactInfos.size(); i++) {
                                BdAtContactInfo bdAtContactInfo = bdAtContactInfos.get(i);
                                for (ResponseHxUerBean bean : hxUerBeanList) {
                                    if (bdAtContactInfo.getContactInfo().getCharacterId() == bean.getCharacterId()) {
                                        bdAtContactInfo.setHxUerBean(bean);
                                    }

                                }
                            }

                            datas.addAll(bdAtContactInfos);
                            show.addAll(datas);
                            mContactAdapter.notifyDataSetChanged();

                        }
                    }
                });


    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        if (mIsAt) {
            ArrayList<Integer> moduleIds = new ArrayList<>();
            for (int i = 0; i < show.size(); i++) {
                BdAtContactInfo bdAtContactInfo = show.get(i);
                if (bdAtContactInfo.isSelected()) {
                    selected.add(show.get(i).getContactInfo().getName());
                    int moduleId = bdAtContactInfo.getContactInfo().getModuleId();
                    moduleIds.add(moduleId);
                }
            }

            if (selected.size() == 0) {
                finish();
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("contacts", selected);
                intent.putIntegerArrayListExtra("moduleIds",  moduleIds);
                setResult(RESULT_CODE_CONTACTS, intent);
                finish();
            }
        } else {

            for (int i = 0; i < show.size(); i++) {
                if (show.get(i).isSelected()) {
                    selected.add(show.get(i).getHxUerBean().getHxUserName());
                }
            }

            if (selected.size() == 0) {
                //没有选择联系人
                ToastUtils.showToast(this, "还没选择联系人哦~");
                return;
            }
            String cacheHxUserName = SCCacheUtils.getCacheHxUserName();
            String cacheCharacterInfo = SCCacheUtils.getCacheCharacterInfo();
            CharacterInfo characterInfo = JSONObject.parseObject(cacheCharacterInfo, CharacterInfo.class);
            final String name = characterInfo.getName();
            CreateGroupBean groupBean = new CreateGroupBean();
            groupBean.setMembers(selected);
            groupBean.setOwner(cacheHxUserName);
            groupBean.setGroupname(name + "创建的群");
            groupBean.setDesc(name + "创建的群");
            groupBean.setIcon_url("");
            groupBean.setApproval(true);
            groupBean.setPub(true);
            groupBean.setMaxusers(100);

            String dataString = JSON.toJSONString(groupBean);

            SCHttpUtils.postWithSpaceId()
                    .url(HttpApi.HX_GROUP_CREATE)
                    .addParams("dataString", dataString)
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("创建群失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.i("创建群成功 = " + response);
                            try {
                                String code = JSONObject.parseObject(response).getString("code");
                                if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                    String data = JSONObject.parseObject(response).getString("data");
                                    String groupId = JSONObject.parseObject(data).getString("groupid");
                                    if (!TextUtils.isEmpty(groupId)) {
                                        LogUtils.i("创建群成功");
                                        Intent intent = new Intent(mContext, ChatRoomActivity.class);
                                        intent.putExtra("isGroup",true);
                                        intent.putExtra("toChatName",groupId);
                                        intent.putExtra("name",name + "创建的群");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        ToastUtils.showToast(mContext, "创建群失败");
                                    }
                                } else {
                                    ToastUtils.showToast(mContext, "创建群失败");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.showToast(mContext, "创建群失败");
                            }
                        }
                    });
        }
    }
}
