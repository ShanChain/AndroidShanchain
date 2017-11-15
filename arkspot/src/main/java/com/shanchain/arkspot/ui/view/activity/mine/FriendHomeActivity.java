package com.shanchain.arkspot.ui.view.activity.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.CurrentAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.FriendDetailInfo;
import com.shanchain.arkspot.ui.model.ResponseFocusData;
import com.shanchain.arkspot.ui.model.ResponseFocusInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


public class FriendHomeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {

    @Bind(R.id.tb_friend_home)
    ArthurToolBar mTbFriendHome;
    @Bind(R.id.rv_friend_home)
    RecyclerView mRvFriendHome;
    private View mHeadView;
    private List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;
    private ImageView mIvBg;
    private ImageView mIvHead;
    private TextView mTvName;
    private Button mBtnFocus;
    private TextView mTvDes;
    private TextView mTvConversation;
    private int mCharacterId;
    private FriendDetailInfo mFriendDetailInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friend_home;
    }

    @Override
    protected void initViewsAndEvents() {
        mCharacterId = getIntent().getIntExtra("characterId", 0);
        String rnExtra = getIntent().getStringExtra(NavigatorModule.REACT_EXTRA);
        if(!getIntent().hasExtra("characterId")){
            JSONObject jsonObject = JSONObject.parseObject(rnExtra);
            JSONObject rnGData = jsonObject.getJSONObject("gData");
            JSONObject rnData = jsonObject.getJSONObject("data");
            mCharacterId = Integer.parseInt(rnData.getString("characterId"));
        }

        initToolBar();
        initData();
        initRecyclerView();

    }

    private void initData() {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_QUERY)
                .addParams("characterId",mCharacterId + "")
                .build()
                .execute(new SCHttpCallBack<FriendDetailInfo>(FriendDetailInfo.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(FriendDetailInfo response, int id) {
                        if (response != null){
                            mFriendDetailInfo = response;
                            fillData();
                        }else {
                            ToastUtils.showToast(mContext,"获取角色信息失败");
                        }
                    }
                });
    }

    private void fillData() {
        GlideUtils.load(mContext,mFriendDetailInfo.getHeadImg(),mIvHead,0);
        mTvName.setText(mFriendDetailInfo.getName() + "(No." + mFriendDetailInfo.getModelNo() + ")");
        mTvDes.setText(mFriendDetailInfo.getSignature());
    }

    private void initRecyclerView() {
        initHeadView();
        mRvFriendHome.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setHeaderView(mHeadView);
        mRvFriendHome.setAdapter(mAdapter);


    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_friend_home, (ViewGroup) findViewById(android.R.id.content), false);
        mIvBg = (ImageView) mHeadView.findViewById(R.id.iv_friend_home_bg);
        mIvHead = (ImageView) mHeadView.findViewById(R.id.iv_friend_home_head);
        mTvName = (TextView) mHeadView.findViewById(R.id.tv_friend_home_name);
        mTvDes = (TextView) mHeadView.findViewById(R.id.tv_friend_home_des);
        mBtnFocus = (Button) mHeadView.findViewById(R.id.btn_friend_home_focus);
        mTvConversation = (TextView) mHeadView.findViewById(R.id.tv_friend_home_conversation);


        mTvConversation.setOnClickListener(this);
        mBtnFocus.setOnClickListener(this);
    }

    private void initToolBar() {
        mTbFriendHome.setBtnEnabled(true,false);
        mTbFriendHome.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_friend_home_focus:
                //关注
                String btnTxt = mBtnFocus.getText().toString().trim();
                if (TextUtils.equals(btnTxt,"已关注")){

                    return;
                }
                focus();
                break;
            case R.id.tv_friend_home_conversation:
                //发起对话

                break;
        }
    }

    private void focus() {
         String cacheCharacterId = SCCacheUtils.getCacheCharacterId();
        SCHttpUtils.post()
                .url(HttpApi.FOCUS_FOCUS)
                .addParams("funsId",cacheCharacterId)
                .addParams("characterId",mCharacterId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("关注角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("关注角色成功 = " + response);
                        if (TextUtils.isEmpty(response)){
                            return;
                        }
                        ResponseFocusInfo responseFocusInfo = JSONObject.parseObject(response, ResponseFocusInfo.class);
                        if (responseFocusInfo == null){
                            return;
                        }

                        String code = responseFocusInfo.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            return;
                        }

                        ResponseFocusData focusData = responseFocusInfo.getData();
                        if (focusData == null){
                            return;
                        }

                        //关注成功
                        mBtnFocus.setText("已关注");
                        ToastUtils.showToast(mContext,"关注成功");

                    }
                });
    }
}
