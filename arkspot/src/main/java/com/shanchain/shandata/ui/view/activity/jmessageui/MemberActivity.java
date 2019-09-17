package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.SCBottomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupMenberAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.interfaces.IChatGroupMenberCallback;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.shandata.ui.presenter.GroupMenberPresenter;
import com.shanchain.shandata.ui.presenter.impl.GroupMenberPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.GroupMenberView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MemberActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener , GroupMenberView {
    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv_select_num)
    TextView tvSelectNum;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    @Bind(R.id.select_all)
    TextView selectAll;
    @Bind(R.id.ll_mycollection_bottom_dialog)
    LinearLayout llMycollectionBottomDialog;


    private String roomID;
    private boolean isSelect = false, editStatus;
    private BaseQuickAdapter adapter;
    private int page = 0, size = 10, count = 1, selectCount;
    private List chatRoomlist = new ArrayList();
    private List delMemberList = new ArrayList();
    private String photoUrlBase = "http://shanchain-picture.oss-cn-beijing.aliyuncs.com/";
    private boolean mIsOwner;
    private CustomDialog mDeleteMemberDialog;
    private GroupMenberAdapter mGroupMenberAdapter;
    private List<Members> mGroupList = new ArrayList<>();
    private int pageIndex = 0;
    private boolean isLast = false;
    private GroupMenberPresenter mMenberPresenter;
    private SCBottomDialog scBottomDialog;
    private StandardDialog standardDialog;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_member;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        count = intent.getIntExtra("count", 0);
        refreshLayout.setOnRefreshListener(this);
        mMenberPresenter = new GroupMenberPresenterImpl(this);
        mGroupMenberAdapter = new GroupMenberAdapter(R.layout.item_members_chat_room,mGroupList);
        View view = LayoutInflater.from(this).inflate(R.layout.not_data_footer_view, null);
        mGroupMenberAdapter.addFooterView(view);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);
        rvMessageList.setAdapter(mGroupMenberAdapter);
        mGroupMenberAdapter.notifyDataSetChanged();
        initToolBar();
        setListener();
//        initData();
        getGroupMenberList();
        mMenberPresenter.checkIsGroupCreater(roomID);

        initLoadMoreListener();
    }

    //获取群组成员
    private void getGroupMenberList(){
        mMenberPresenter.getGroupMenberList(roomID,count+"",pageIndex+"", Constants.pageSize+"",Constants.pullRefress);
    }


    private void initToolBar() {
        tbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        tbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tbMain.getTitleView().setLayoutParams(layoutParams);
        tbMain.setTitleText(getString(R.string.group_menber));
        if (mIsOwner == true) {
            tbMain.setRightText(getString(R.string.Manager_1));
            tbMain.setRightTextColor(getResources().getColor(R.color.colorViolet));
        }
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.delete_group_menber));
        items.add(getString(R.string.str_cancel));
        scBottomDialog = new SCBottomDialog(MemberActivity.this);
        scBottomDialog.setItems(items);
        scBottomDialog.setCallback(new SCBottomDialog.BottomCallBack() {
            @Override
            public void btnClick(String btnValue) {
                if (btnValue.equals(getString(R.string.delete_group_menber))) {
                    editStatus = true;
                    changeMenberState(true);
                    llMycollectionBottomDialog.setVisibility(View.VISIBLE);
                    tbMain.setRightText(getString(R.string.str_cancel));
                    scBottomDialog.dismiss();
                } else if (btnValue.equals(getString(R.string.str_cancel))) {
                    editStatus = false;
                    llMycollectionBottomDialog.setVisibility(View.GONE);
                    tbMain.setRightText(getString(R.string.Manager_1));
                    scBottomDialog.dismiss();
                }
            }
        });
        tbMain.setOnLeftClickListener(new ArthurToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        tbMain.setOnRightClickListener(new ArthurToolBar.OnRightClickListener() {
            @Override
            public void onRightClick(final View v) {
                //当前不是编辑模式，显示弹窗编辑
                if (!editStatus) {
                    scBottomDialog.show();
                } else {
                    llMycollectionBottomDialog.setVisibility(View.GONE);
                    tbMain.setRightText(getString(R.string.Manager_1));
                    editStatus = false;
                    changeMenberState(false);
                }
            }
        });
    }

    //选择状态
    private void setListener(){
        mGroupMenberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Members item = (Members) adapter.getItem(position);
                for (Members m : mGroupList){
                    if(m.getUsername().equals(item.getUsername())){
                        if(m.isSelect()){
                            m.setSelect(false);
                        }else {
                            m.setSelect(true);
                        }
                    }
                }
                mGroupMenberAdapter.notifyDataSetChanged();
                List<Members> list = checkNumsFromGroupMenber();
                tvSelectNum.setText("" + list.size());
            }
        });

        mGroupMenberAdapter.setMenberCallback(new IChatGroupMenberCallback() {
            @Override
            public void chatUser(Members members) {
                if(members == null)return;
                JMessageClient.getUserInfo(members.getUsername(), new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, final UserInfo userInfo) {
                        String avatar = userInfo.getAvatarFile() != null ? userInfo.getAvatarFile().getAbsolutePath() : "";
                        DefaultUser defaultUser = new DefaultUser(userInfo.getUserID(), userInfo.getNickname(), avatar);
                        defaultUser.setSignature(userInfo.getSignature());
                        defaultUser.setHxUserId(userInfo.getUserName());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("userInfo", defaultUser);
                        readyGo(SingerChatInfoActivity.class, bundle);
                    }
                });
            }
        });
    }

    //删除成员
    @OnClick(R.id.btn_delete)
    void deleteGroupMenbers(){
        List<Members> list = checkNumsFromGroupMenber();
        if(list.size() == 0){
            ToastUtils.showToast(this, R.string.delete_group_team_tip);
            return;
        }
        List<String> userNames = new ArrayList();
        String myUserName = JMessageClient.getMyInfo().getUserName();
        for (int i = 0; i < list.size(); i++) {
            userNames.add(list.get(i).getUsername());
            if (userNames.contains(myUserName)) {
                ToastUtils.showToast(MemberActivity.this, R.string.delete_not_y);
                return;
            }
        }
        String jArray = JSONArray.toJSONString(userNames);
        isDeleteGroupMenberTip(jArray);

    }

    //弹窗提示删除用户
    private void isDeleteGroupMenberTip(final String jArray){
        standardDialog = new StandardDialog(this);
        standardDialog.setStandardTitle(getString(R.string.delete_group_tip));
        standardDialog.setStandardMsg(getString(R.string.is_delete_group_menber));
        standardDialog.setSureText(getString(R.string.str_sure));
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                //弹窗提示输入密码
                mMenberPresenter.deleteGroupMenber(roomID,jArray);
            }
        }, new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
            }
        });
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                standardDialog.show();
                TextView msgTextView = standardDialog.findViewById(R.id.dialog_msg);
                msgTextView.setTextSize(18);
            }
        });
    }


    @Override
    public void onRefresh() {
        pageIndex = 0;
        getGroupMenberList();
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setGroupMenberResponse(String response,int pullType) {
        refreshLayout.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String content = JSONObject.parseObject(data).getString("content");
            List<Members> membersList = JSONArray.parseArray(content, Members.class);
            if(membersList!=null && membersList.size()>0){
                for (int i = 0; i <membersList.size() ; i++) {
                    if("11111".equals(membersList.get(i).getUsername())){
                        membersList.remove(i);
                    }
                }
            }
            if(membersList.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                mGroupList.clear();
                mGroupList.addAll(membersList);
                rvMessageList.setAdapter(mGroupMenberAdapter);
                mGroupMenberAdapter.notifyDataSetChanged();
            }else {
                mGroupMenberAdapter.addData(membersList);
            }

        }
    }

    @Override
    public void setCheckGroupCreateeResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
            String data = SCJsonUtils.parseData(response);
            /*mIsOwner = Boolean.parseBoolean(data);
            if (mIsOwner == true) {
                tbMain.setRightText(getString(R.string.Manager_1));
                tbMain.setRightTextColor(getResources().getColor(R.color.colorViolet));

            }*/
        }
    }

    @Override
    public void setDeleteGroupMenberResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
            ToastUtils.showToast(MemberActivity.this, getString(R.string.delete_success));
            mGroupList.removeAll(delMemberList);
            mGroupMenberAdapter.notifyDataSetChanged();
        }else {
            ToastUtils.showToast(MemberActivity.this, getString(R.string.operation_failed));
        }
    }

    //改变列表状态
    private void changeMenberState(boolean isEdite){
        if(mGroupList.size() == 0)return;
        for (int i = 0; i < mGroupList.size(); i++) {
            mGroupList.get(i).setEdite(isEdite);
        }
        mGroupMenberAdapter.notifyDataSetChanged();
    }
    //判断选择几个
    private List<Members> checkNumsFromGroupMenber(){
        if(mGroupList.size()==0)return new ArrayList<>();
        List<Members> members = new ArrayList<>();
        for (int i = 0; i <mGroupList.size() ; i++) {
            if(mGroupList.get(i).isSelect()){
                members.add(mGroupList.get(i));
            }
        }
        return members;
    }

    //上拉加载监听
    private void initLoadMoreListener() {
        //上拉加载
        rvMessageList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isLast) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mGroupMenberAdapter.getItemCount()) {
                    pageIndex++;
                    mMenberPresenter.getGroupMenberList(roomID,count+"",pageIndex+"", Constants.pageSize+"",Constants.pillLoadmore);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

    }
}
