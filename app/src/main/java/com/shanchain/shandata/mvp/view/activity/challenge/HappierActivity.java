package com.shanchain.shandata.mvp.view.activity.challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.HappierAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.HappierRankInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.iwf.photopicker.PhotoPicker;

public class HappierActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {

    @Bind(R.id.xrv_happier)
    XRecyclerView mXrvHappier;
    private ArthurToolBar mHappierToolBar;
    private List<HappierRankInfo> datas;
    private View mHeadView;

    private TextView mTvHeadHappierCounts;
    private TextView mTvHeadHappierRulesDetails;
    private TextView mTvHeadHappierWord;
    private ImageView mIvHeadHappierRefresh;
    private TextView mTvHeadHappierWordDes;
    private ImageView mIvHeadHappierPhoto;
    private TextView mTvHeadHappierRank;

    private List<String> images;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_happier;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initHeadView();
        initRecyclerView();
    }

    private void initHeadView() {

        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_happier,(ViewGroup)findViewById(android.R.id.content),false);

        mTvHeadHappierCounts = (TextView) mHeadView.findViewById(R.id.tv_head_happier_counts);
        mTvHeadHappierRulesDetails = (TextView) mHeadView.findViewById(R.id.tv_head_happier_rules_details);
        mTvHeadHappierWord = (TextView) mHeadView.findViewById(R.id.tv_head_happier_word);
        mIvHeadHappierRefresh = (ImageView) mHeadView.findViewById(R.id.iv_head_happier_refresh);
        mTvHeadHappierWordDes = (TextView) mHeadView.findViewById(R.id.tv_head_happier_word_des);
        mIvHeadHappierPhoto = (ImageView) mHeadView.findViewById(R.id.iv_head_happier_photo);
        mTvHeadHappierRank = (TextView) mHeadView.findViewById(R.id.tv_head_happier_rank);
        mTvHeadHappierCounts.setText("你参加过5次");
        mTvHeadHappierWord.setText("望穿秋水");
        mTvHeadHappierWordDes.setText("不是所有的渴望都会回应，但我依然在等。");
        mTvHeadHappierRulesDetails.setOnClickListener(this);
        mIvHeadHappierRefresh.setOnClickListener(this);
        mIvHeadHappierPhoto.setOnClickListener(this);
        mTvHeadHappierRank.setOnClickListener(this);

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvHappier.setPullRefreshEnabled(false);
        mXrvHappier.setLoadingMoreEnabled(false);
        mXrvHappier.setLayoutManager(linearLayoutManager);
        mXrvHappier.addItemDecoration(new RecyclerViewDivider(HappierActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(HappierActivity.this,1),
                getResources().getColor(R.color.colorListDivider)));
        mXrvHappier.addHeaderView(mHeadView);
        HappierAdapter happierAdapter = new HappierAdapter(this,R.layout.item_happier_rank,datas);
        mXrvHappier.setAdapter(happierAdapter);
    }

    private void initData() {
        datas = new ArrayList<>();

        HappierRankInfo happierRankInfo = new HappierRankInfo();
        happierRankInfo.setNickName("天王盖地虎");
        happierRankInfo.setPriseCount(1234);
        datas.add(happierRankInfo);

        HappierRankInfo happierRankInfo2 = new HappierRankInfo();
        happierRankInfo2.setNickName("小鸡炖蘑菇");
        happierRankInfo2.setPriseCount(998);
        datas.add(happierRankInfo2);

        HappierRankInfo happierRankInfo3 = new HappierRankInfo();
        happierRankInfo3.setNickName("宝塔镇河妖");
        happierRankInfo3.setPriseCount(666);
        datas.add(happierRankInfo3);

        HappierRankInfo happierRankInfo4 = new HappierRankInfo();
        happierRankInfo4.setNickName("蘑菇放辣椒");
        happierRankInfo4.setPriseCount(435);
        datas.add(happierRankInfo4);

        for (int i = 0; i < 6; i ++) {
            HappierRankInfo happierRankInfo1 = new HappierRankInfo();
            happierRankInfo1.setNickName("提莫一米五");
            happierRankInfo1.setPriseCount(400 - i * 30);
            datas.add(happierRankInfo1);
        }

    }

    private void initToolBar() {
        mHappierToolBar = (ArthurToolBar) findViewById(R.id.toolbar_happier);
        mHappierToolBar.setBtnEnabled(true,false);
        mHappierToolBar.setBtnVisibility(true,false);
        mHappierToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_happier_rules_details:
                CustomDialog customDialog = new CustomDialog(this,false,0.8,R.layout.dialog_happier,new int[]{});
                customDialog.show();
                break;
            case R.id.iv_head_happier_refresh:
                //请求数据，刷新关键词和描述信息

                break;

            case R.id.iv_head_happier_photo:
                selectImages();
                break;

            case R.id.tv_head_happier_rank:
                readyGo(RankActivity.class);
                break;
        }
    }


    private void selectImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0权限申请
            LogUtils.d("版本6.0");
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("未申请权限,正在申请");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                LogUtils.d("已经申请权限");
                pickImages();
            }
        } else {
            LogUtils.d("版本低于6.0");
            pickImages();
        }
    }

    private void pickImages() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String path = photos.get(0);
                Intent intent = new Intent(this,HappierCompleteActivity.class);
                intent.putExtra("imgPath",path);
                startActivity(intent);
            }
        }
    }
}
