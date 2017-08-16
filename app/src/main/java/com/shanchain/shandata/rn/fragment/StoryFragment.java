package com.shanchain.shandata.rn.fragment;

import android.os.Bundle;

/**
 * Created by flyye on 2017/8/16.
 */

public class StoryFragment  extends RNfragment{

    public StoryFragment(){
        Bundle arguments = new Bundle();
        arguments.putString("page", "StoryPage");
        setArguments(arguments);
    }
}
