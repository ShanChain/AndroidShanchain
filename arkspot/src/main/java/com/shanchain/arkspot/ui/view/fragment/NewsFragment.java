package com.shanchain.arkspot.ui.view.fragment;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.MessageHomeAdapter;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.MessageHomeInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.shanchain.data.common.utils.ToastUtils;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class NewsFragment extends BaseFragment {
    @Bind(R.id.rv_fragment_news)
    RecyclerView mRvFragmentNews;
    private List<MessageHomeInfo> sourceDatas;
    private List<MessageHomeInfo> topDatas;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_news, null);
    }

    @Override
    public void initData() {
        initDatas();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvFragmentNews.setLayoutManager(layoutManager);

        mRvFragmentNews.addItemDecoration(new RecyclerViewDivider(mActivity));
        MessageHomeAdapter messageHomeAdapter = new MessageHomeAdapter(R.layout.item_msg_home, sourceDatas);
        mRvFragmentNews.setAdapter(messageHomeAdapter);

        messageHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        messageHomeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                showDialog(position);

                return true;
            }
        });

    }

    /**
     *  描述：长按点击事件
     */
    private void showDialog(int position) {
        final AlertDialog builder = new AlertDialog.Builder(mActivity).create();
        View view = View.inflate(mActivity,R.layout.dialog_msg_home,null);
        TextView tvDelete = (TextView) view.findViewById(R.id.tv_dialog_msg_delete);
        TextView tvDetails = (TextView) view.findViewById(R.id.tv_dialog_msg_details);
        TextView tvTop = (TextView) view.findViewById(R.id.tv_dialog_msg_top);

        tvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //置顶消息
                ToastUtils.showToast(mActivity,"置顶消息");
                builder.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除消息
                ToastUtils.showToast(mActivity,"删除消息");
                builder.dismiss();
            }
        });

        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //消息详情
                ToastUtils.showToast(mActivity,"消息详情");
                builder.dismiss();
            }
        });

        builder.setView(view);
        builder.show();
    }

    private void initDatas() {
        sourceDatas = new ArrayList<>();
        topDatas = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            MessageHomeInfo messageHomeInfo = new MessageHomeInfo();
            messageHomeInfo.setUnRead(i*5 - 40);
            sourceDatas.add(messageHomeInfo);
        }
    }



}
