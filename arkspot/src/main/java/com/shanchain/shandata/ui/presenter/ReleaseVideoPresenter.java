package com.shanchain.shandata.ui.presenter;

import android.content.Context;

/**
 * Created by zhoujian on 2017/12/21.
 */

public interface ReleaseVideoPresenter {

    void releaseVideoDynamic(Context context,String videoTitle, String videoDes, String videoPath, String imgPath, String type);
}
