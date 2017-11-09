package com.shanchain.arkspot.ui.presenter;

import android.content.Context;

import com.shanchain.arkspot.ui.model.RichTextModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface ReleaseDynamicPresenter {

    void releaseDynamic(String word,List<String> imgUrls,String tailId, List<Integer> atList , List<Integer> topics);

    void upLoadImgs(Context context, String word, List<String> imgPaths, String tailId,List<Integer> atList , List<Integer> topics);

    void ReleaseLongText(Context context , String title,List<RichTextModel> editData);
}
