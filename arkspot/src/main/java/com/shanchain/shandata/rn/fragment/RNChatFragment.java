package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNChatFragment extends RNfragment {

    public RNChatFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "ChatPage");
        setArguments(arguments);
    }
}
