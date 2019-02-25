package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.List;

import cn.jiguang.imui.view.RoundImageView;
import okhttp3.Call;

public class FootPrintActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private ArthurToolBar arthurToolBar;
    private LinearLayout linearFootPrint;
    private RecyclerView reviewFoodPrint;
    private List<HotChatRoom> hotChatRoomList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_food_print;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();
        initData();


    }

    private void initData() {

    }

    private void initView() {
        linearFootPrint = findViewById(R.id.linear_foot_print);
        reviewFoodPrint = findViewById(R.id.review_food_print);
        reviewFoodPrint.setVisibility(View.VISIBLE);
        linearFootPrint.setVisibility(View.GONE);
        SCHttpUtils.get()
                .url(HttpApi.HOT_CHAT_ROOM)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = com.alibaba.fastjson.JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = com.alibaba.fastjson.JSONObject.parseObject(response).getString("data");
//                            String roomId = com.alibaba.fastjson.JSONObject.parseObject(data).getString("roomId");
////                            String Id = com.alibaba.fastjson.JSONObject.parseObject(data).getString("id");
//                            String roomName = com.alibaba.fastjson.JSONObject.parseObject(data).getString("roomName");
//                            String sortNo = com.alibaba.fastjson.JSONObject.parseObject(data).getString("sortNo");
//                            String background = com.alibaba.fastjson.JSONObject.parseObject(data).getString("background");
//                            String thumbnails = com.alibaba.fastjson.JSONObject.parseObject(data).getString("thumbnails");
                            hotChatRoomList = JSONArray.parseArray(data, HotChatRoom.class);
                            BaseQuickAdapter adapter = new BaseQuickAdapter<HotChatRoom,BaseViewHolder>(R.layout.item_hot_chat_room, hotChatRoomList) {
                                @Override
                                protected void convert(BaseViewHolder helper, final HotChatRoom item) {
                                    RoundImageView roundImageView = helper.getView(R.id.item_round_view);
                                    RoundImageView avatar = helper.getView(R.id.iv_item_msg_home_avatar);
                                    TextView roomName = helper.getView(R.id.tv_item_room_name);
                                    TextView roomNum = helper.getView(R.id.tv_item_member_num);
                                    TextView visitTime = helper.getView(R.id.tv_item_visit_time);
                                    Button btnJoin = helper.getView(R.id.bt_item_join);
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.empty_foot_print);
                                    Glide.with(FootPrintActivity.this).load(item.getBackground()).apply(options).into(roundImageView);
                                    Glide.with(FootPrintActivity.this).load(item.getThumbnails()).apply(options).into(avatar);
                                    roomName.setText(item.getRoomName());
                                    roomNum.setText(item.getUserNum());
                                    btnJoin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(FootPrintActivity.this, MessageListActivity.class);
                                            intent.putExtra("roomId", item.getRoomId());
                                            intent.putExtra("roomName", item.getRoomName());
                                            intent.putExtra("hotChatRoom",item);
                                            intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            };
                            LinearLayoutManager layoutManager = new LinearLayoutManager(FootPrintActivity.this,LinearLayoutManager.VERTICAL,false);
                            reviewFoodPrint.setLayoutManager(layoutManager);
                            reviewFoodPrint.setAdapter(adapter);
                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(FootPrintActivity.this, MessageListActivity.class);
                                    intent.putExtra("roomId", hotChatRoomList.get(position).getRoomId());
                                    intent.putExtra("roomName", hotChatRoomList.get(position).getRoomName());
                                    intent.putExtra("hotChatRoom",hotChatRoomList.get(position));
                                    intent.putExtra("isHotChatRoom", true);
//                                intent.putExtra("isInCharRoom", isIn);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });
    }

    private void initToolBar() {
        arthurToolBar = findViewById(R.id.toolbar_nav);
        arthurToolBar.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        arthurToolBar.getTitleView().setLayoutParams(layoutParams);
        arthurToolBar.setTitleText("热门元社区");
        arthurToolBar.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        arthurToolBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        arthurToolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        arthurToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();

    }
}
