package com.shanchain.shandata.rn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.shanchain.shandata.base.MyApplication;

import static com.shanchain.data.common.rn.modules.NavigatorModule.REACT_PROPS;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNfragment extends Fragment{
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getActivity().getApplication()).getReactNativeHost();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mReactRootView = new ReactRootView(context);
        mReactInstanceManager = ((MyApplication) getActivity().getApplication())
                .getReactNativeHost()
                .getReactInstanceManager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = inflater.getContext();
        mReactRootView = new ReactRootView(context);
        return mReactRootView;
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (mReactInstanceManager == null)
//            return false;
//        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            mReactInstanceManager.showDevOptionsDialog();
//            return true;
//        }
//        return false;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReactRootView.startReactApplication(
                mReactInstanceManager,
                getArguments().getString("page"),
                getArguments()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostResume(getActivity(), (DefaultHardwareBackBtnHandler) getActivity());
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        if (getReactNativeHost().getUseDeveloperSupport() && getReactNativeHost().hasInstance()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
                handled = true;
            }
        }
        return handled;
    }

    public void onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostPause(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
        }
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(getActivity());
        }
    }

}
