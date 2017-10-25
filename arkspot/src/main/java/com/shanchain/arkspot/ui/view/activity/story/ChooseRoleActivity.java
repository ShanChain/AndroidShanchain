package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ChooseRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.SpaceBean;
import com.shanchain.arkspot.ui.model.SpaceCharacterBean;
import com.shanchain.arkspot.ui.model.SpaceCharacterModelInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
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
    @Bind(R.id.tv_choose_role_def)
    TextView mTvChooseRoleDef;
    private List<SpaceCharacterBean> datas = new ArrayList<>();
    private SpaceBean mSpaceBean;
    private ChooseRoleAdapter mRoleAdapter;
    private SpaceCharacterModelInfo mSpaceCharacterModelInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_role;
    }

    @Override
    protected void initViewsAndEvents() {
        mSpaceBean = (SpaceBean) getIntent().getSerializableExtra("spaceInfo");
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        int spaceId = mSpaceBean.getSpaceId();

        GlideUtils.load(mContext, mSpaceBean.getBackground(), mIvChooseRoleImg, 0);
        mTvChooseRoleTitle.setText(mSpaceBean.getName());
        mTvChooseRoleDes.setText(mSpaceBean.getSlogan());
        mTvChooseRoleDetail.setText(mSpaceBean.getIntro());


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
                mTvChooseRoleDef.setVisibility(View.GONE);
            }
        });
    }

    private void setRoleInfo(int position) {
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

                break;
            case R.id.iv_choose_role_select:
                if (mSpaceCharacterModelInfo != null&& TextUtils.equals(mSpaceCharacterModelInfo.getCode(),NetErrCode.COMMON_SUC_CODE)){
                    Intent intent = new Intent(this, SearchRoleActivity.class);
                    intent.putExtra("spaceInfo", mSpaceCharacterModelInfo);
                    startActivity(intent);
                }
                break;
            case R.id.btn_choose_role:

                break;
            default:
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
