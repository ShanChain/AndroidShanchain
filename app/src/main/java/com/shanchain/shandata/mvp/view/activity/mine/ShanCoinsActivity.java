package com.shanchain.shandata.mvp.view.activity.mine;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CoinsAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ShanCoinsInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShanCoinsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarShanCoins;
    @Bind(R.id.xrv_shan_coins)
    XRecyclerView mXrvShanCoins;
    private View mHeadView;
    private List<ShanCoinsInfo> mdatas;
    private boolean mShanType;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_shan_coins;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        mShanType = intent.getBooleanExtra("isShanCoins", false);

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvShanCoins.setLayoutManager(linearLayoutManager);
        mXrvShanCoins.addHeaderView(mHeadView);
        mXrvShanCoins.setLoadingMoreEnabled(false);
        mXrvShanCoins.setPullRefreshEnabled(false);

        mXrvShanCoins.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        CoinsAdapter coinsAdapter = new CoinsAdapter(this,R.layout.item_coins,mdatas);
        mXrvShanCoins.setAdapter(coinsAdapter);



    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_shan_coins,(ViewGroup)findViewById(android.R.id.content),false);

        TextView tvHeadCoinsAvailable = (TextView) mHeadView.findViewById(R.id.tv_head_coins_available);
        TextView tvHeadCoinsIn = (TextView) mHeadView.findViewById(R.id.tv_head_coins_in);
        TextView tvHeadCoinsOut = (TextView) mHeadView.findViewById(R.id.tv_head_coins_out);
        TextView tvHeadCoinsDes1 = (TextView) mHeadView.findViewById(R.id.tv_head_coins_des1);
        TextView tvHeadCoinsDes2 = (TextView) mHeadView.findViewById(R.id.tv_head_coins_des2);
        Resources res = getResources();
        Drawable img_off = res.getDrawable(mShanType?R.mipmap.coins:R.mipmap.vouchers);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
        tvHeadCoinsAvailable.setCompoundDrawables(img_off, null, null, null); //设置左图标
        tvHeadCoinsDes1.setText(mShanType?"善圆  总支出":"善券  总支出");
        tvHeadCoinsDes2.setText(mShanType?"善圆":"善券");
    }

    private void initData() {
        mdatas = new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            ShanCoinsInfo shanCoinsInfo = new ShanCoinsInfo();
            shanCoinsInfo.setType("兑换商品");
            shanCoinsInfo.setDes("啤酒");
            shanCoinsInfo.setTime("6月24日 17:22");
            shanCoinsInfo.setCounts(3);
            mdatas.add(shanCoinsInfo);
        }
    }

    private void initToolBar() {
        mToolbarShanCoins = (ArthurToolBar) findViewById(R.id.toolbar_shan_coins);
        mToolbarShanCoins.setTitleText(mShanType?"善圆收支":"善券收支");
        mToolbarShanCoins.setBtnEnabled(true, false);
        mToolbarShanCoins.setBtnVisibility(true, false);
        mToolbarShanCoins.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
