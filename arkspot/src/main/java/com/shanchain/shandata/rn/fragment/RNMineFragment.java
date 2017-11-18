package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNMineFragment extends RNfragment {

    public RNMineFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "MinePage");
        setArguments(arguments);
    }
}
