package com.shanchain.shandata.ui.view.activity.story;

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
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChooseRoleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.SpaceCharacterBean;
import com.shanchain.shandata.ui.model.SpaceCharacterModelInfo;
import com.shanchain.shandata.ui.model.SpaceInfo;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private int selected = -1;
    private boolean isCollected;
    private SpaceCharacterModelInfo mModelInfo;
    private int page = 0;
    private int size = 10;
    private int mSpaceId;

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
        mSpaceId = mSpaceInfo.getSpaceId();
        spaceIsFav();
        GlideUtils.load(mContext, mSpaceInfo.getBackground(), mIvChooseRoleImg, 0);
        mTvChooseRoleTitle.setText(mSpaceInfo.getName());
        mTvChooseRoleDes.setText(mSpaceInfo.getSlogan());
        mTvChooseRoleDetail.setText(mSpaceInfo.getIntro());
        initSpaceModel(page,size);
    }

    private void spaceIsFav() {
        SCHttpUtils.postWithUserId()
                .addParams("spaceId",mSpaceId+"")
                .url(HttpApi.SPACE_IS_FAV)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空是否收藏失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取时空是否收藏成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                Boolean isFav = JSONObject.parseObject(response).getBoolean("data");
                                if (isFav){
                                    setIsCollection(true);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("数据解析错误");
                        }
                    }
                });
    }


    private void initSpaceModel(int page ,int size) {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_MODEL_QUERY_SPACEID)
                .addParams("spaceId", mSpaceId + "")
                .addParams("page",""+page)
                .addParams("size",""+size)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取时空角色失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取时空角色成功 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                mModelInfo = JSONObject.parseObject(response).getObject("data", SpaceCharacterModelInfo.class);
                                List<SpaceCharacterBean> characterBeanList = mModelInfo.getContent();
                                datas.addAll(characterBeanList);
                                mRoleAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i("获取时空角色列表失败");
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
        mRvChooseRole.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager){
                    int lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    LogUtils.i("最后可见条目位置 = " + lastVisibleItemPosition + "; 数据集长度 = "+ datas.size());
                    if (lastVisibleItemPosition == datas.size() - 1 ){
                        LogUtils.i("获取跟多角色 ====");
                        page++;
                        initSpaceModel(page,size);
                    }
                }
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
                if (mModelInfo != null) {
                    Intent intent = new Intent(this, SearchRoleActivity.class);
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
        RoleManager.switchRole(datas.get(selected).getModelId() + "",mSpaceInfo.getSpaceId() + "",JSON.toJSONString(mSpaceInfo));
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
                    .execute(new SCHttpStringCallBack() {
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
                    .execute(new SCHttpStringCallBack() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SCBaseEvent event){
        if(event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_MODEL_CREATE)){
            page = 0;
            mRoleAdapter.getData().clear();
            initData();
        }
    }
}
