package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.ChooseRoleAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.RoleInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


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
    private List<RoleInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_role;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            RoleInfo roleInfo = new RoleInfo();

            if (i % 5== 0) {
                roleInfo.setImg(R.drawable.photo_city);
                roleInfo.setName("花千骨");
            }else if (i%5 == 1) {
                roleInfo.setImg(R.drawable.photo_bear);
                roleInfo.setName("普京");
            }else if (i%5 == 2) {
                roleInfo.setImg(R.drawable.photo_heart);
                roleInfo.setName("特朗普");
            }else if (i%5 == 3) {
                roleInfo.setImg(R.drawable.photo_public);
                roleInfo.setName("列夫·托尔斯泰");
            }else if (i%5 == 4) {
                roleInfo.setImg(R.drawable.photo_yue);
                roleInfo.setName("刘某");
            }
            datas.add(roleInfo);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvChooseRole.setLayoutManager(layoutManager);

        ChooseRoleAdapter roleAdapter = new ChooseRoleAdapter(R.layout.item_choose_role,datas);
        mRvChooseRole.setAdapter(roleAdapter);
        roleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setRoleInfo(position);
            }
        });
    }

    private void setRoleInfo(int position) {
        Glide.with(this).load(datas.get(position).getImg()).into(mIvChooseRoleSelect);
        mTvChooseRoleName.setText(datas.get(position).getName());
    }

    private void initToolBar() {
        mTbChooseRole.setBtnEnabled(true,false);
        mTbChooseRole.setOnLeftClickListener(this);
    }

    @OnClick({R.id.iv_choose_role_collect, R.id.iv_choose_role_select, R.id.btn_choose_role})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose_role_collect:

                break;
            case R.id.iv_choose_role_select:
                Intent intent = new Intent(this,SearchRoleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_choose_role:

                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
