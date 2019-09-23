package com.shanchain.shandata.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.MyApplication;

/**
 * Created by zhoujian on 2017/12/4.
 */

public class GuideFragmentFour extends Fragment {
    private View mView;
    private LinearLayout mLinearLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_guide_four,null);
        return  mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLinearLayout = mView.findViewById(R.id.ll_rootview);
        String sta = MyApplication.systemLanguge;
        if(sta.equals("zh")){
            mLinearLayout.setBackgroundResource(R.mipmap.guide_four_new);
        }else {
            mLinearLayout.setBackgroundResource(R.mipmap.guide_four_en);
        }
    }
}
