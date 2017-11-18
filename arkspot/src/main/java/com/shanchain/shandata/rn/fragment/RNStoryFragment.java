package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNStoryFragment extends RNfragment{

    public RNStoryFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "StoryPage");
        setArguments(arguments);
    }
}
