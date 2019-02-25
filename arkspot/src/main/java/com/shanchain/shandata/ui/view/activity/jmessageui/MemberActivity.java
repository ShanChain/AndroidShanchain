package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.Members;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

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

    private String roomID;
    private BaseQuickAdapter adapter;
    private int page = 0, size = 10, count = 1;
    private List chatRoomlist = new ArrayList();
    private String photoUrlBase = "http://shanchain-picture.oss-cn-beijing.aliyuncs.com/";

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
                                            TextView focus = helper.getView(R.id.tv_item_contact_child_focus);
                                            focus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String avatar = userInfo.getAvatarFile() != null ? userInfo.getAvatarFile().getAbsolutePath() : "";
                                                    DefaultUser defaultUser = new DefaultUser(userInfo.getUserID(), userInfo.getNickname(),avatar);
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
        tbMain.setBackgroundColor(getResources().getColor(R.color.white));
        tbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        tbMain.setOnLeftClickListener(new ArthurToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
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
        initData();
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
