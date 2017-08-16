package com.shanchain.shandata.rn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.shanchain.shandata.rn.RNManager;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNfragment extends Fragment{
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactInstanceManager = RNManager.getInstance().getReactInstanceManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = inflater.getContext();
        mReactRootView = new ReactRootView(context);
        return mReactRootView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mReactInstanceManager == null)
            return false;
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReactRootView.startReactApplication(
                mReactInstanceManager,
                getArguments().getString("page"),
                getArguments()
        );
    }

    public void onDestroyView() {

        if (mReactRootView != null&&mReactInstanceManager!=null) {
            ViewGroup viewGroup = (ViewGroup) mReactRootView.getParent();
            if(viewGroup!=null) viewGroup.removeView(mReactRootView);
            mReactInstanceManager.detachRootView(mReactRootView);
            mReactRootView = null;
        }
        super.onDestroyView();
    }
}
