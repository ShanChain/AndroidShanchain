package com.shanchain.shandata.mvp.view.activity.found;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CityGridAdapter;
import com.shanchain.shandata.adapter.SwitchCityAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.global.Constans;
import com.shanchain.shandata.mvp.model.RecommendedCityInfo;
import com.shanchain.shandata.mvp.model.SwitchCityInfo;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.Bind;


public class SwitchCityActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.et_switch_city_search)
    EditText mEtSwitchCitySearch;
    @Bind(R.id.xrv_switch_city)
    XRecyclerView mXrvSwitchCity;
    @Bind(R.id.side_bar)
    WaveSideBar mSideBar;
    private ArthurToolBar mToolbarSwitchCity;

    private View mHeadView;
    private ImageView mIvHeadCityRefresh;
    private TextView mTvHeadCityAddress;
    private TextView mTvHeadCityHistory1;
    private TextView mTvHeadCityHistory2;
    private TextView mTvHeadCityHistory3;
    private GridView mGvHeadCity;
    private ArrayList<SwitchCityInfo> mDatas;
    private SwitchCityAdapter mSwitchCityAdapter;
    private ArrayList<RecommendedCityInfo> mGridDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_switch_city;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
        initListener();
    }


    private void initListener() {
        mSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                LogUtils.d("选中的条目:" + index);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getPinying().equalsIgnoreCase(index)) {
                        ((LinearLayoutManager)mXrvSwitchCity.getLayoutManager()).scrollToPositionWithOffset(i + 2,0);
                        return;
                    }
                }

            }
        });
    }

    private void initRecyclerView() {
        initHeadView();
       LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        mXrvSwitchCity.setLayoutManager(LayoutManager);
        mXrvSwitchCity.setPullRefreshEnabled(false);
        mXrvSwitchCity.setLoadingMoreEnabled(false);
        mXrvSwitchCity.addHeaderView(mHeadView);
        mSwitchCityAdapter = new SwitchCityAdapter(this, R.layout.item_city_switch, mDatas);
        mXrvSwitchCity.setAdapter(mSwitchCityAdapter);
        mSwitchCityAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position>1){
                    ToastUtils.showToast(SwitchCityActivity.this,mDatas.get(position-2).getAddress());

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_switch_city, (ViewGroup) findViewById(android.R.id.content), false);

        mIvHeadCityRefresh = (ImageView) mHeadView.findViewById(R.id.iv_head_city_refresh);
        mTvHeadCityAddress = (TextView) mHeadView.findViewById(R.id.tv_head_city_address);
        mTvHeadCityHistory1 = (TextView) mHeadView.findViewById(R.id.tv_head_city_history1);
        mTvHeadCityHistory2 = (TextView) mHeadView.findViewById(R.id.tv_head_city_history2);
        mTvHeadCityHistory3 = (TextView) mHeadView.findViewById(R.id.tv_head_city_history3);
        mGvHeadCity = (GridView) mHeadView.findViewById(R.id.gv_head_city);
        final CityGridAdapter cityGridAdapter = new CityGridAdapter(mGridDatas);
        mGvHeadCity.setAdapter(cityGridAdapter);

        mGvHeadCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < mGridDatas.size(); i++) {
                    if (i == position) {
                        mGridDatas.get(i).setSeletcted(true);
                    } else {
                        mGridDatas.get(i).setSeletcted(false);
                    }
                }
                cityGridAdapter.notifyDataSetChanged();
            }
        });

    }


    private void initData() {
        mGridDatas = new ArrayList<>();

        for (int i = 0; i < Constans.CITY_RECOMMENDED.length; i++) {
            RecommendedCityInfo city = new RecommendedCityInfo();
            city.setCityName(Constans.CITY_RECOMMENDED[i]);
            city.setSeletcted(false);
            mGridDatas.add(city);
        }



        mDatas = new ArrayList<>();
        SwitchCityInfo cityInfo1 = new SwitchCityInfo();
        cityInfo1.setPinying("A");
        cityInfo1.setAddress("阿贝");
        mDatas.add(cityInfo1);

        SwitchCityInfo cityInfo2 = new SwitchCityInfo();
        cityInfo2.setPinying("A");
        cityInfo2.setAddress("阿里");
        mDatas.add(cityInfo2);

        SwitchCityInfo cityInfo3 = new SwitchCityInfo();
        cityInfo3.setPinying("A");
        cityInfo3.setAddress("阿紫");
        mDatas.add(cityInfo3);

        SwitchCityInfo cityInfo4 = new SwitchCityInfo();
        cityInfo4.setPinying("A");
        cityInfo4.setAddress("阿亮");
        mDatas.add(cityInfo4);

        SwitchCityInfo cityInfo5 = new SwitchCityInfo();
        cityInfo5.setPinying("B");
        cityInfo5.setAddress("北京");
        mDatas.add(cityInfo5);

        SwitchCityInfo cityInfo6 = new SwitchCityInfo();
        cityInfo6.setPinying("B");
        cityInfo6.setAddress("北海");
        mDatas.add(cityInfo6);

        SwitchCityInfo cityInfo7 = new SwitchCityInfo();
        cityInfo7.setPinying("C");
        cityInfo7.setAddress("潮汕");
        mDatas.add(cityInfo7);

        SwitchCityInfo cityInfo8 = new SwitchCityInfo();
        cityInfo8.setPinying("C");
        cityInfo8.setAddress("曹阳");
        mDatas.add(cityInfo8);

        SwitchCityInfo cityInfo9 = new SwitchCityInfo();
        cityInfo9.setPinying("F");
        cityInfo9.setAddress("福州");
        mDatas.add(cityInfo9);

        SwitchCityInfo cityInfo10 = new SwitchCityInfo();
        cityInfo10.setPinying("W");
        cityInfo10.setAddress("王城");
        mDatas.add(cityInfo10);

        SwitchCityInfo cityInfo11 = new SwitchCityInfo();
        cityInfo11.setPinying("W");
        cityInfo11.setAddress("魏城");
        mDatas.add(cityInfo10);

        SwitchCityInfo cityInfo12 = new SwitchCityInfo();
        cityInfo12.setPinying("W");
        cityInfo12.setAddress("潍坊");
        mDatas.add(cityInfo12);

        SwitchCityInfo cityInfo13 = new SwitchCityInfo();
        cityInfo13.setPinying("X");
        cityInfo13.setAddress("襄阳");
        mDatas.add(cityInfo13);

        SwitchCityInfo cityInfo14 = new SwitchCityInfo();
        cityInfo14.setPinying("X");
        cityInfo14.setAddress("西安");
        mDatas.add(cityInfo14);

        SwitchCityInfo cityInfo15 = new SwitchCityInfo();
        cityInfo15.setPinying("X");
        cityInfo15.setAddress("咸宁");
        mDatas.add(cityInfo15);

        SwitchCityInfo cityInfo16 = new SwitchCityInfo();
        cityInfo16.setPinying("Y");
        cityInfo16.setAddress("宜昌");
        mDatas.add(cityInfo16);

        SwitchCityInfo cityInfo17 = new SwitchCityInfo();
        cityInfo17.setPinying("Y");
        cityInfo17.setAddress("榆林");
        mDatas.add(cityInfo17);

        SwitchCityInfo cityInfo18 = new SwitchCityInfo();
        cityInfo18.setPinying("Y");
        cityInfo18.setAddress("宜宾");
        mDatas.add(cityInfo18);

        SwitchCityInfo cityInfo19 = new SwitchCityInfo();
        cityInfo19.setPinying("Z");
        cityInfo19.setAddress("漳州");
        mDatas.add(cityInfo19);

        SwitchCityInfo cityInfo20 = new SwitchCityInfo();
        cityInfo20.setPinying("Z");
        cityInfo20.setAddress("漳州");
        mDatas.add(cityInfo20);

        SwitchCityInfo cityInfo21 = new SwitchCityInfo();
        cityInfo21.setPinying("Z");
        cityInfo21.setAddress("智障");
        mDatas.add(cityInfo21);

    }

    private void initToolBar() {
        mToolbarSwitchCity = (ArthurToolBar) findViewById(R.id.toolbar_switch_city);
        mToolbarSwitchCity.setBtnEnabled(true, false);
        mToolbarSwitchCity.setBtnVisibility(true, false);
        mToolbarSwitchCity.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
    
}
