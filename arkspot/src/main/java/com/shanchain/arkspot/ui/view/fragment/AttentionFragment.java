package com.shanchain.arkspot.ui.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.AttentionAdapter;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import utils.DensityUtils;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class AttentionFragment extends BaseFragment {
    @Bind(R.id.xrv_attention)
    RecyclerView mXrvAttention;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_attention, null);
    }

    @Override
    public void initData() {
        mXrvAttention.setLayoutManager(new LinearLayoutManager(mActivity));
        List<StoryInfo> datas = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            StoryInfo info = new StoryInfo();
            if (i % 4 == 0) {
                info.setItemType(StoryInfo.type1);
            } else if (i % 4 == 1) {
                info.setItemType(StoryInfo.type2);
            } else if (i % 4 == 2) {
                info.setItemType(StoryInfo.type3);
            } else if (i % 4 == 3) {
                info.setItemType(StoryInfo.type4);
            }
            datas.add(info);
        }

        AttentionAdapter adapter = new AttentionAdapter(datas);
        mXrvAttention.addItemDecoration(new RecyclerViewDivider(mActivity,LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity,5),getResources().getColor(R.color.colorDivider)));
        mXrvAttention.setAdapter(adapter);
    }

}
