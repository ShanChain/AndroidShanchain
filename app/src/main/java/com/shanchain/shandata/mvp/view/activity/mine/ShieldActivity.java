package com.shanchain.shandata.mvp.view.activity.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ShieldPersonsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ShieldPersonInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShieldActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarShieldedPersons;
    @Bind(R.id.rv_shield_persons)
    XRecyclerView mRvShieldPersons;
    @Bind(R.id.activity_shield)
    LinearLayout mActivityShield;
    private List<ShieldPersonInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_shield;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvShieldPersons.setLayoutManager(layoutManager);
        ShieldPersonsAdapter personsAdapter = new ShieldPersonsAdapter(this, R.layout.item_shield_person, datas);
        mRvShieldPersons.setPullRefreshEnabled(false);
        mRvShieldPersons.setLoadingMoreEnabled(false);
        mRvShieldPersons.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));
        //View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view_shield,(ViewGroup)findViewById(android.R.id.content),false);

        View emptyView = View.inflate(this,R.layout.empty_view_shield,null);

        mRvShieldPersons.setEmptyView(emptyView);
        mRvShieldPersons.setAdapter(personsAdapter);

    }

    private void initData() {
       datas = new ArrayList<>();

        /*ShieldPersonInfo shieldPersonInfo1 = new ShieldPersonInfo();
        shieldPersonInfo1.setShielded(true);
        shieldPersonInfo1.setName("韩玉龙");
        datas.add(shieldPersonInfo1);

        ShieldPersonInfo shieldPersonInfo2 = new ShieldPersonInfo();
        shieldPersonInfo2.setShielded(true);
        shieldPersonInfo2.setName("陆明华");
        datas.add(shieldPersonInfo2);

        ShieldPersonInfo shieldPersonInfo3 = new ShieldPersonInfo();
        shieldPersonInfo3.setShielded(true);
        shieldPersonInfo3.setName("段恩新");
        datas.add(shieldPersonInfo3);

        ShieldPersonInfo shieldPersonInfo4 = new ShieldPersonInfo();
        shieldPersonInfo4.setShielded(true);
        shieldPersonInfo4.setName("李国栋");
        datas.add(shieldPersonInfo4);

        ShieldPersonInfo shieldPersonInfo5 = new ShieldPersonInfo();
        shieldPersonInfo5.setShielded(true);
        shieldPersonInfo5.setName("张晓阳");
        datas.add(shieldPersonInfo5);*/

    }

    private void initToolBar() {
        mToolbarShieldedPersons = (ArthurToolBar) findViewById(R.id.toolbar_shielded_persons);
        mToolbarShieldedPersons.setBtnEnabled(true, false);
        mToolbarShieldedPersons.setBtnVisibility(true, false);
        mToolbarShieldedPersons.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        for (int i = 0; i < datas.size(); i++) {
            ShieldPersonInfo shieldPersonInfo = datas.get(i);
            boolean shielded = shieldPersonInfo.isShielded();
            LogUtils.d("是否屏蔽:" + shielded);
        }
        finish();
    }
}
