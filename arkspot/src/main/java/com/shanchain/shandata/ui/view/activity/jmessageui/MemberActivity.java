package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.SCBottomDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.shandata.widgets.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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

public class MemberActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {


    @Bind(R.id.tb_main)
    ArthurToolBar tbMain;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;
    @Bind(R.id.srl_message_list)
    BGARefreshLayout srlMessageList;
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_member;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        count = intent.getIntExtra("count", 0);
        srlMessageList.setDelegate(this);
        srlMessageList.beginLoadingMore();
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(MemberActivity.this, true);//微博效果
        srlMessageList.setRefreshViewHolder(refreshViewHolder);
        // 设置正在加载更多时不显示加载更多控件
        srlMessageList.setIsShowLoadingMoreView(true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("加载更多");
        initToolBar();
        initData();
    }

    private void initData() {
        //获取群成员
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_MEMBER)
                .addParams("roomId", roomID)
                .addParams("count", count + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取聊天成员失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");
                            List<Members> membersList = JSONArray.parseArray(content, Members.class);
                            chatRoomlist.addAll(membersList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemberActivity.this, LinearLayoutManager.VERTICAL, false);
                            rvMessageList.setLayoutManager(linearLayoutManager);
                            adapter = new BaseQuickAdapter<Members, BaseViewHolder>(R.layout.item_members_chat_room, chatRoomlist) {
                                @Override
                                protected void convert(final BaseViewHolder helper, final Members item) {
                                    helper.setIsRecyclable(false);
//                                    BaseViewHolder viewHolder;
                                    final TextView focus = helper.getView(R.id.tv_item_contact_child_focus);
                                    //显示勾选按钮
                                    if (editStatus == true) {
                                        helper.getView(R.id.check_box).setVisibility(View.VISIBLE);
                                        focus.setOnClickListener(null);
                                    } else {
                                        helper.getView(R.id.check_box).setVisibility(View.GONE);
                                    }
                                    JMessageClient.getUserInfo(item.getUsername(), new GetUserInfoCallback() {
                                        @Override
                                        public void gotResult(int i, String s, final UserInfo userInfo) {
                                            String name = userInfo.getNickname() != null ? userInfo.getNickname() : userInfo.getUserName();
                                            helper.setText(R.id.tv_item_contact_child_name, TextUtils.isEmpty(name) ? "" + userInfo.getDisplayName() : name);
                                            final CircleImageView circleImageView = helper.getView(R.id.iv_item_contact_child_avatar);
//                                            String avatar = photoUrlBase + userInfo.getAvatarFile().getAbsolutePath();
                                            String avatar = photoUrlBase + userInfo.getAvatar();
                                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                                @Override
                                                public void gotResult(int i, String s, Bitmap bitmap) {
                                                    Bitmap bitmap1 = bitmap;
                                                    if (bitmap != null) {
                                                        circleImageView.setImageBitmap(bitmap);
                                                    } else {
                                                        circleImageView.setBackground(getResources().getDrawable(R.mipmap.aurora_headicon_default));
                                                    }

                                                }
                                            });
                                            focus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
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
                                    helper.setText(R.id.tv_item_contact_child_focus, "对话");
                                }
                            };
                            rvMessageList.setAdapter(adapter);
                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                    ToastUtils.showToast(MemberActivity.this, "点击Item:" + position);
                                    final ImageView checkBox = view.findViewById(R.id.check_box);
                                    final Members item = (Members) adapter.getItem(position);
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkBox.setImageResource(R.mipmap.ic_checked);
                                            if (item.isSelect() == false) {
                                                checkBox.setImageResource(R.mipmap.ic_checked);
                                                selectCount++;
                                                item.setSelect(true);
                                                delMemberList.add(item);
                                            } else {
                                                checkBox.setImageResource(R.mipmap.ic_uncheck);
                                                selectCount--;
                                                item.setSelect(false);
                                                if (delMemberList.size() > 0) {
                                                    delMemberList.remove(item);
                                                }
                                            }
//                                            ToastUtils.showToast(MemberActivity.this, "选择item数:" + selectCount);
                                            tvSelectNum.setText("" + selectCount);
                                            if (selectCount > 0) {
                                                btnDelete.setTextColor(getResources().getColor(R.color.colorViolet));
                                                //删除群成员操作
                                                btnDelete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
//                                                        ToastUtils.showToast(MemberActivity.this, "删除群成员" + selectCount + "个");
                                                        //调用删除接口
                                                        deleteMember(delMemberList);
                                                        tvSelectNum.setText("" + 0);
                                                    }
                                                });
                                            } else {
                                                btnDelete.setTextColor(getResources().getColor(R.color.colorHint));
                                                btnDelete.setOnClickListener(null);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }

                });
        //判断是否为群主
        SCHttpUtils.get()
                .url(HttpApi.ROOM_OWNER)
                .addParams("roomId", roomID)
                .build()
                .execute(new SCHttpStringCallBack(MemberActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                            String data = SCJsonUtils.parseData(response);
                            mIsOwner = Boolean.parseBoolean(data);
                            if (mIsOwner == true) {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tbMain.setRightText("管理");
                                        tbMain.setRightTextColor(getResources().getColor(R.color.colorViolet));
                                    }
                                });

                            }
                        }
                    }
                });

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
        tbMain.setTitleText("群成员");
        if (mIsOwner == true) {
            tbMain.setRightText("管理");
            tbMain.setRightTextColor(getResources().getColor(R.color.colorViolet));
        }
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        List<String> items = new ArrayList<>();
        items.add("删除聊天室成员");
        items.add("取消");
        final SCBottomDialog scBottomDialog = new SCBottomDialog(MemberActivity.this);
        scBottomDialog.setItems(items);
        scBottomDialog.setCallback(new SCBottomDialog.BottomCallBack() {
            @Override
            public void btnClick(String btnValue) {
                if (adapter == null) {
                    return;
                }
                if (btnValue.equals("删除聊天室成员")) {
                    editStatus = true;
                    adapter.notifyDataSetChanged();
                    llMycollectionBottomDialog.setVisibility(View.VISIBLE);
                    tbMain.setRightText("取消");
                    scBottomDialog.dismiss();
                } else if (btnValue.equals("取消")) {
                    editStatus = false;
                    adapter.notifyDataSetChanged();
                    llMycollectionBottomDialog.setVisibility(View.GONE);
                    tbMain.setRightText("管理");
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
                if (editStatus == false) {
                    scBottomDialog.show();
                } else {
                    editStatus = false;
                    adapter.notifyDataSetChanged();
                    llMycollectionBottomDialog.setVisibility(View.GONE);
                    tbMain.setRightText("管理");
                }
            }
        });
    }

    private void deleteMember(final List<Members> delMemberList) {
        List<String> userNames = new ArrayList();
        String myUserName = JMessageClient.getMyInfo().getUserName();
        for (int i = 0; i < delMemberList.size(); i++) {
            userNames.add(delMemberList.get(i).getUsername());
            if (userNames.contains("11111")) {
                ToastUtils.showToast(MemberActivity.this, "不能删除系统成员");
                return;
            } else if (userNames.contains(myUserName)) {
                ToastUtils.showToast(MemberActivity.this, "不能删除自己");
                return;
            }
        }
        String jArray = JSONArray.toJSONString(userNames);
        SCHttpUtils.post()
                .url(HttpApi.DELETE_ROOM_MEMBERS)
                .addParams("roomId", roomID + "")
                .addParams("jArray", jArray + "")
                .build()
                .execute(new SCHttpStringCallBack(MemberActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        String msg = SCJsonUtils.parseMsg(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            ToastUtils.showToast(MemberActivity.this, "删除成功");
                            if (chatRoomlist != null && delMemberList != null) {
                                chatRoomlist.removeAll(delMemberList);
                                adapter.notifyDataSetChanged();
                                selectCount = 0;

                                delMemberList.clear();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (chatRoomlist != null) {
            chatRoomlist.clear();
            initData();
        }
        refreshLayout.endRefreshing();
    }

    //加载
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(final BGARefreshLayout refreshLayout) {
        page++;
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_MEMBER)
                .addParams("roomId", roomID)
                .addParams("count", count + "")
                .addParams("page", page + "")
                .addParams("size", size + "")
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("获取聊天成员失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");

                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");
                            String totalPages = JSONObject.parseObject(data).getString("totalPages");
                            String totalElements = JSONObject.parseObject(data).getString("totalElements");
                            String number = JSONObject.parseObject(data).getString("number");
                            if (Integer.valueOf(number) <= Integer.valueOf(totalPages)) {
                                List<Members> loadMore = JSONArray.parseArray(content, Members.class);
                                adapter.addData(loadMore);
                                adapter.notifyLoadMoreToLoading();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        refreshLayout.endLoadingMore();
                    }
                });

        return false;
    }
}
