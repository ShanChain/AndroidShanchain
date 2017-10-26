package com.shanchain.arkspot.ui.view.activity.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.CurrentAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.FriendDetailInfo;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.ToastUtils;

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
    private TextView mTvDrama;
    private TextView mTvConversation;
    private TextView mTvLongtext;
    private int mCharacterId;
    private FriendDetailInfo mFriendDetailInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_friend_home;
    }

    @Override
    protected void initViewsAndEvents() {
        mCharacterId = getIntent().getIntExtra("characterId", 0);
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
        mTvDrama = (TextView) mHeadView.findViewById(R.id.tv_friend_home_drama);
        mTvConversation = (TextView) mHeadView.findViewById(R.id.tv_friend_home_conversation);
        mTvLongtext = (TextView) mHeadView.findViewById(R.id.tv_friend_home_long);


        mTvConversation.setOnClickListener(this);
        mTvDrama.setOnClickListener(this);
        mTvLongtext.setOnClickListener(this);
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

                break;
            case R.id.tv_friend_home_drama:
                //大戏

                break;
            case R.id.tv_friend_home_conversation:
                //发起对话

                break;
            case R.id.tv_friend_home_long:
                //长文

                break;


        }
    }
}
