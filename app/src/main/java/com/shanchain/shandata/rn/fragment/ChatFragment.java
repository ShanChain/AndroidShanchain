package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by flyye on 2017/8/16.
 */

public class ChatFragment extends RNfragment {

    public ChatFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "ChatPage");
        setArguments(arguments);
    }
}
