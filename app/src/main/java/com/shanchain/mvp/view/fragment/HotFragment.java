package com.shanchain.mvp.view.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HotAdapter;
import com.shanchain.base.BaseFragment;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.ToastUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public View initView() {

        return View.inflate(mActivity, R.layout.fragment_hot, null);
    }

    @Override
    public void initData() {
        mImages = new ArrayList<>();
        mImages.add("1");
        mImages.add("2");
        mImages.add("3");
        mImages.add("4");
        mImages.add("5");
        mImages.add("6");
        mImages.add("7");
        getDatas();
        initRecycleView();
        bindData();
    }

    /**
     *  2017/5/23
     *  描述：绑定数据
     *
     */
    private void bindData() {

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
        for (int i = 0; i < 30; i ++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("张建 " + i);
            publisherInfo.setTime("1" + i);
            publisherInfo.setLikes(i);
            publisherInfo.setComments(i*5-3);
            publisherInfo.setImages(mImages);
            mDatas.add(publisherInfo);
        }
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
