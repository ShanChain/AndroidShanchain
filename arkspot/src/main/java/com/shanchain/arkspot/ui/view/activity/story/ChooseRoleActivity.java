package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ChooseRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.manager.ActivityManager;
import com.shanchain.arkspot.ui.model.CharacterInfo;
import com.shanchain.arkspot.ui.model.RegisterHxBean;
import com.shanchain.arkspot.ui.model.RegisterHxInfo;
import com.shanchain.arkspot.ui.model.ResponseSwitchRoleInfo;
import com.shanchain.arkspot.ui.model.SpaceCharacterBean;
import com.shanchain.arkspot.ui.model.SpaceCharacterModelInfo;
import com.shanchain.arkspot.ui.model.SpaceInfo;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;


public class ChooseRoleActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_choose_role)
    ArthurToolBar mTbChooseRole;
    @Bind(R.id.iv_choose_role_img)
    ImageView mIvChooseRoleImg;
    @Bind(R.id.tv_choose_role_title)
    TextView mTvChooseRoleTitle;
    @Bind(R.id.tv_choose_role_des)
    TextView mTvChooseRoleDes;
    @Bind(R.id.iv_choose_role_collect)
    ImageView mIvChooseRoleCollect;
    @Bind(R.id.rv_choose_role)
    RecyclerView mRvChooseRole;
    @Bind(R.id.iv_choose_role_select)
    CircleImageView mIvChooseRoleSelect;
    @Bind(R.id.tv_choose_role_name)
    TextView mTvChooseRoleName;
    @Bind(R.id.btn_choose_role)
    Button mBtnChooseRole;
    @Bind(R.id.tv_choose_role_detail)
    TextView mTvChooseRoleDetail;
    private List<SpaceCharacterBean> datas = new ArrayList<>();
    private SpaceInfo mSpaceInfo;
    private ChooseRoleAdapter mRoleAdapter;
    private SpaceCharacterModelInfo mSpaceCharacterModelInfo;
    private int selected = -1;
    private boolean isCollected;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_role;
    }

    @Override
    protected void initViewsAndEvents() {
        mSpaceInfo = (SpaceInfo) getIntent().getSerializableExtra("spaceInfo");
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        int spaceId = mSpaceInfo.getSpaceId();
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String spaceCollection = SCCacheUtils.getCache(userId, Constants.CACHE_SPACE_COLLECTION);
        if (!TextUtils.isEmpty(spaceCollection)) {
            List<Integer> spaceIds = JSONObject.parseArray(spaceCollection, Integer.class);
            if (spaceIds.contains(spaceId)) {
                setIsCollection(true);
            } else {
                setIsCollection(false);
            }
        }

        GlideUtils.load(mContext, mSpaceInfo.getBackground(), mIvChooseRoleImg, 0);
        mTvChooseRoleTitle.setText(mSpaceInfo.getName());
        mTvChooseRoleDes.setText(mSpaceInfo.getSlogan());
        mTvChooseRoleDetail.setText(mSpaceInfo.getIntro());


        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_MODEL_QUERY_SPACEID)
                .addParams("spaceId", spaceId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取时空角色成功 = " + response);

                        mSpaceCharacterModelInfo = new Gson().fromJson(response, SpaceCharacterModelInfo.class);
                        if (!TextUtils.equals(mSpaceCharacterModelInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                            ToastUtils.showToast(mContext, "获取时空角色信息失败");
                        } else {
                            List<SpaceCharacterBean> characterBeanList = mSpaceCharacterModelInfo.getData();
                            datas.addAll(characterBeanList);
                            mRoleAdapter.notifyDataSetChanged();
                        }

                    }
                });

    }

    /**
     * 描述：ui上设置是否收藏了当前时空
     */
    private void setIsCollection(boolean isCollection) {
        isCollected = isCollection;
        mIvChooseRoleCollect.setImageResource(isCollected ? R.mipmap.abs_roleselection_btn_collect_selected : R.mipmap.abs_roleselection_btn_collect_default);

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvChooseRole.setLayoutManager(layoutManager);

        mRoleAdapter = new ChooseRoleAdapter(R.layout.item_choose_role, datas);
        mRvChooseRole.setAdapter(mRoleAdapter);
        mRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setRoleInfo(position);
            }
        });
    }

    private void setRoleInfo(int position) {
        selected = position;
        Glide.with(this).load(datas.get(position).getHeadImg()).into(mIvChooseRoleSelect);
        mTvChooseRoleName.setText(datas.get(position).getName());
    }

    private void initToolBar() {
        mTbChooseRole.setBtnEnabled(true, false);
        mTbChooseRole.setOnLeftClickListener(this);
    }

    @OnClick({R.id.iv_choose_role_collect, R.id.iv_choose_role_select, R.id.btn_choose_role})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose_role_collect:
                //收藏或取消收藏
                collectSpace();
                break;
            case R.id.iv_choose_role_select:
                if (mSpaceCharacterModelInfo != null && TextUtils.equals(mSpaceCharacterModelInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                    Intent intent = new Intent(this, SearchRoleActivity.class);
                    intent.putExtra("spaceInfo", mSpaceCharacterModelInfo);
                    intent.putExtra("spaceId", mSpaceInfo.getSpaceId());
                    startActivity(intent);
                }
                break;
            case R.id.btn_choose_role:
                //切换角色
                switchRole();
                break;
            default:
                break;
        }
    }

    //切换角色
    private void switchRole() {
        if (selected == -1) {
            ToastUtils.showToast(mContext, "选一个你喜欢的角色吧~");
            return;
        }
        int modelId = datas.get(selected).getModelId();

        showLoadingDialog();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_CHANGE)
                .addParams("spaceId", mSpaceInfo.getSpaceId() + "")
                .addParams("modelId",modelId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        error();
                        LogUtils.i("切换角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("切换角色成功" + response);

                        if (TextUtils.isEmpty(response)){
                            error();
                            return;
                        }

                        ResponseSwitchRoleInfo responseSwitchRoleInfo = JSONObject.parseObject(response, ResponseSwitchRoleInfo.class);
                        if (responseSwitchRoleInfo == null){
                            error();
                            return;
                        }

                        String code = responseSwitchRoleInfo.getCode();

                        if (!TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                            error();
                            return;
                        }

                        CharacterInfo data = responseSwitchRoleInfo.getData();

                        if (data == null) {
                            error();
                            return;
                        }

                        registerHxUserAndLogin(data);

                    }
                });
    }

    private void registerHxUserAndLogin(final CharacterInfo data) {
        SCHttpUtils.post()
                .url(HttpApi.HX_USER_REGIST)
                .addParams("characterId",data.getCharacterId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        error();
                        LogUtils.i("注册环信账号失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        LogUtils.i("注册环信账号成功 " + response );

                        String currentUser = EMClient.getInstance().getCurrentUser();
                        //String cacheHxUserName = SCCacheUtils.getCacheHxUserName();
                        //退出当前登录的账号
                        if (TextUtils.isEmpty(currentUser)){
                            login(response,data);
                            return;
                        }
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                login(response, data);
                            }

                            @Override
                            public void onError(int i, String s) {
                                error();
                                LogUtils.i("登出失败 = " + s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                    }
                });
    }

    private void login(String response, final CharacterInfo data) {
        try {
            RegisterHxInfo registerHxInfo = JSONObject.parseObject(response, RegisterHxInfo.class);
            String code = registerHxInfo.getCode();
            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                RegisterHxBean registerHxBean = registerHxInfo.getData();
                final String userName = registerHxBean.getHxUserName();
                final String pwd = registerHxBean.getHxPassword();
                EMClient.getInstance().login(userName, pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeLoadingDialog();
                                LogUtils.i("登录环信账号成功");
                                EMClient.getInstance().chatManager().loadAllConversations();
                                String spaceInfoJson = JSON.toJSONString(mSpaceInfo);
                                String characterInfoJson = JSON.toJSONString(data);
                                RoleManager.switchRoleCache(data.getCharacterId(),characterInfoJson,mSpaceInfo.getSpaceId(),spaceInfoJson,userName,pwd);
                                ToastUtils.showToast(mContext,"穿越角色成功");
                                Intent intent = new Intent(mContext, MainActivity.class);
                                ActivityManager.getInstance().finishAllActivity();
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onError(int i, String s) {
                        error();
                        LogUtils.i("登录环信账号失败");
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
        } catch (Exception e) {
            error();
            LogUtils.i("注册失败");
            e.printStackTrace();
        }
    }

    private void error(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(mContext,"穿越失败！");
                closeLoadingDialog();
            }
        });

    }

    /**
     * 收藏时空
     */
    private void collectSpace() {
        if (isCollected) {
            //取消收藏时空
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.SPACE_UNFAVORITE)
                    .addParams("spaceId", mSpaceInfo.getSpaceId() + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("取消收藏时空失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.i("取消收藏时空成功" + response);
                            ToastUtils.showToast(mContext, "已取消收藏");
                            setIsCollection(false);
                            setCollectionCache(false);
                        }
                    });
        } else {
            //收藏时空
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.SPACE_FAVORITE)
                    .addParams("spaceId", mSpaceInfo.getSpaceId() + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("收藏时空失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.i("收藏时空成功" + response);
                            ToastUtils.showToast(mContext, "已收藏");
                            setIsCollection(true);
                            setCollectionCache(true);
                        }
                    });
        }
    }

    /**
     * 描述：设置收藏时空缓存
     */
    private void setCollectionCache(boolean isCollection) {
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String cacheCollection = SCCacheUtils.getCache(userId, Constants.CACHE_SPACE_COLLECTION);
        int spaceId = mSpaceInfo.getSpaceId();
        if (isCollection) {
            //收藏成功
            if (TextUtils.isEmpty(cacheCollection)) {
                LogUtils.i("收藏成功，之前没有收藏过");
                List<Integer> collection = new ArrayList<>();
                collection.add(spaceId);
                String jArr = JSONObject.toJSONString(collection);
                SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_COLLECTION,jArr);
            } else {
                LogUtils.i("收藏成功，之前收藏过");
                List<Integer> spaceCollection = JSONObject.parseArray(cacheCollection, Integer.class);
                spaceCollection.add(spaceId);
                String jArr = JSONObject.toJSONString(spaceCollection);
                SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_COLLECTION,jArr);
            }

        } else {
            //取消收藏成功
            if (TextUtils.isEmpty(cacheCollection)){
                LogUtils.i("取消收藏成功，收藏缓存为空");
            }else {
                LogUtils.i("取消收藏成功，收藏缓存有数据");
                List<String> spaceCollection = JSONObject.parseArray(cacheCollection, String.class);
                if (spaceCollection.contains(mSpaceInfo.getSpaceId() + "")){
                    spaceCollection.remove(mSpaceInfo.getSpaceId() + "");

                    String jArr = JSONObject.toJSONString(spaceCollection);
                    SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_COLLECTION,jArr);
                }
            }
        }

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
