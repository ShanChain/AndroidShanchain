package com.shanchain.data.common.rn.views;

import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by zhoujian on 2018/1/3.
 */

public class VideoPlayerView extends SimpleViewManager<JZVideoPlayerStandard> {

    public static final String REACT_CLASS = "VideoPlayerView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected JZVideoPlayerStandard createViewInstance(ThemedReactContext reactContext) {
        return new JZVideoPlayerStandard(reactContext);
    }

    @ReactProp(name = "backBtnVisible")
    public void setBackBtnVisible(JZVideoPlayerStandard playerStandard,boolean visible){
        playerStandard.backButton.setVisibility(visible? View.VISIBLE:View.GONE);
    }

    @ReactProp(name = "timeLayoutVisible")
    public void setTimeLayoutVisible(JZVideoPlayerStandard playerStandard,boolean visible){
        playerStandard.batteryTimeLayout.setVisibility(visible? View.VISIBLE:View.GONE);
    }

    @ReactProp(name = "coverImg")
    public void setCoverImg(JZVideoPlayerStandard playerStandard,String imgUrl){
        //GlideUtils.load();
    }


}
