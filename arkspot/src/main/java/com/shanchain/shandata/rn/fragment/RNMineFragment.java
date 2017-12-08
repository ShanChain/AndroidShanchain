package com.shanchain.shandata.rn.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.SCReactActivity;

import static com.shanchain.data.common.rn.modules.NavigatorModule.REACT_PROPS;


/**
 * Created by flyye on 2017/8/16.
 */

public class RNMineFragment extends RNfragment {

    public RNMineFragment() {
        String gDataString = SCCacheUtils.getCacheGData();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gData", JSONObject.parse(gDataString));
        Bundle bundle = new Bundle();
        bundle.putString(REACT_PROPS, jsonObject.toString());
        bundle.putString("page", "MinePage");
        setArguments(bundle);
    }

}
