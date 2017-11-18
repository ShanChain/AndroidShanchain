package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNSquareFragment extends RNfragment {

    public RNSquareFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "SquarePage");
        setArguments(arguments);
    }
}
