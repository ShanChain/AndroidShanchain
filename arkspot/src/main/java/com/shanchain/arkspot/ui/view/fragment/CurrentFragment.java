package com.shanchain.arkspot.ui.view.fragment;

import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.widgets.other.TextViewExpandableAnimation;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class CurrentFragment extends BaseFragment {
    @Bind(R.id.tv_test)
    TextViewExpandableAnimation mTvTest;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_current, null);
    }

    @Override
    public void initData() {
        mTvTest.setText("是高达回答我你都为奥迪你都晒得和我待温度你咯爱我还得两年多了玩的路径不对了哇卡见不到我来你大冷的逼得我还表白第八代微博低矮五斗柜一把我不丢爱我不 大回屋打我卡的把握比的收看的瓦还能到安徽大开杀戒俺的我吧难道我活动奥委会的扩大未必哦多好玩你到我个活动啊微博的了哇哦ID啊我i");
    }


}
