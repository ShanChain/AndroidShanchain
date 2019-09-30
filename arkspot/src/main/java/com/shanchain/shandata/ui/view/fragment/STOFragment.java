package com.shanchain.shandata.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.base.MyApplication;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/12/4.
 */

public class STOFragment extends BaseFragment{
    @Bind(R.id.tb_coupon)
    ArthurToolBar toolBar;
    private View mView;
    private LinearLayout mLinearLayout;


    public static STOFragment newInstance() {
        STOFragment fragment = new STOFragment();
        return fragment;
    }


    @Override
    public View initView() {
        mView = View.inflate(getActivity(), R.layout.fragment_sto, null);
        return  mView;
    }

    //初始化标题栏
    private void initToolBar() {
        toolBar.setTitleText(getResources().getString(R.string.sto_pre_order));
        ImageView imageView = mView.findViewById(R.id.im_sto);
        if("zh".equals(MyApplication.getInstance().getSystemLanguge())){
            imageView.setBackgroundResource(R.mipmap.sto_icon);
        }else {
            imageView.setBackgroundResource(R.mipmap.sto_icon_en);
        }
    }

    @Override
    public void initData() {
        initToolBar();
    }



}
