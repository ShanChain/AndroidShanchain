package com.shanchain.arkspot.ui.presenter;

import android.content.Context;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface ReleaseDynamicPresenter {

    void releaseDynamic(String word,List<String> imgUrls,String title,String tailId, List<Integer> topics, int type);

    void upLoadImgs(Context context, String word, List<String> imgPaths, String title, String tailId, List<Integer> topics, int type);
}
