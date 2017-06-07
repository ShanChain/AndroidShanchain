package com.shanchain.mvp.view.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HotAdapter;
import com.shanchain.base.BaseFragment;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.mvp.view.activity.DetailsActivity;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.ToastUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/19.
 * 热门页面
 */

public class HotFragment extends BaseFragment {
    /** 描述：列表*/
    @Bind(R.id.xrv_hot)
    XRecyclerView mXrvHot;
    private ArrayList<PublisherInfo> mDatas;
    private List<String> mImages;
    private boolean loadSuccess;

    @Override
    public View initView() {

        return View.inflate(mActivity, R.layout.fragment_hot, null);
    }

    @Override
    public void initData() {

        if (loadSuccess){
            LogUtils.d("加载成功" + loadSuccess);
        }else {
            LogUtils.d("还未加载" + loadSuccess);
            getDatas();
        }

        Gson gson = new Gson();
        String json = gson.toJson(mDatas);
        LogUtils.showLog(json);

        initRecycleView();
        bindData();
    }

    /**
     *  2017/5/23
     *  描述：绑定数据
     *
     */
    private void bindData() {
        Collections.shuffle(mDatas);

        HotAdapter adapter = new HotAdapter(mActivity,R.layout.item_hot,mDatas);
        mXrvHot.setAdapter(adapter);
        mXrvHot.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtils.d(TAG,Thread.currentThread().getName());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvHot.refreshComplete();
                    }
                },2000);

            }

            @Override
            public void onLoadMore() {
                LogUtils.d(TAG,Thread.currentThread().getName());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvHot.loadMoreComplete();
                    }
                },2000);
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ToastUtils.showToast(mActivity,"点击了列表的第" +position+"个条目");
                Intent intent = new Intent(mActivity, DetailsActivity.class);
                mActivity.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     *  2017/5/23
     *  描述：模拟数据
     *
     */
    private void getDatas() {


        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {

            mImages = new ArrayList<>();
            Random random = new Random();
            for (int j = 0; j <random.nextInt(9) ; j ++) {
                LogUtils.d("随机图片的数量 :" + j);
                mImages.add("" + j);
            }

            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("张建 " + i);
            publisherInfo.setTime(i + "分钟前");
            publisherInfo.setDes("今天很开心!");
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setImages(mImages);
            publisherInfo.setType(1);
            mDatas.add(publisherInfo);
        }

        for (int i = 0; i < 10; i ++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("石家海" + i);
            publisherInfo.setTime("2" + i + "分钟前");
            publisherInfo.setDes("哈哈哈哈哈!");
            if (i == 3) {
                publisherInfo.setStroyImgUrl("");
            }else {
                publisherInfo.setStroyImgUrl(i+".png");
            }
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setType(3);
            publisherInfo.setIconUrl(i+"icon.png");
            publisherInfo.setTitle("故事标题" + i);
            publisherInfo.setActiveDes("活动描述信息" + i);
            publisherInfo.setOtherDes("今天有" + i *2 + 3 + "人也在愉快的玩耍");
            mDatas.add(publisherInfo);
        }


        for (int i = 0; i < 6; i ++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setType(2);
            publisherInfo.setName("李江宇" + i);
            publisherInfo.setTime("3"+i + "分钟前");
            publisherInfo.setDes("#善数者正式发布#" + i+"我自定义了一个挑战任务,要不要和我一起来?");
            publisherInfo.setIconUrl(i+".png");
            publisherInfo.setTitle("寻找神秘点" );
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setChallegeTime("5月" + i + "日");
            publisherInfo.setAddr("深圳市");
            publisherInfo.setActiveDes("假期放松一下,率先找到神秘地点打卡者获胜。");
            publisherInfo.setOtherDes("一起来试试吧");
            mDatas.add(publisherInfo);
        }

        loadSuccess = true;
    }

    /**
     *  2017/5/23
     *  描述：初始化列表配置
     *  
     */
    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvHot.setLayoutManager(layoutManager);
        mXrvHot.setLoadingMoreEnabled(true);
        mXrvHot.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvHot.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
    }

}
