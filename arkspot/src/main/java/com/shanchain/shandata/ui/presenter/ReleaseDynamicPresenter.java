package com.shanchain.shandata.ui.presenter;

import android.content.Context;

import com.shanchain.shandata.ui.model.RichTextModel;
import com.shanchain.shandata.widgets.rEdit.InsertModel;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface ReleaseDynamicPresenter {

    void releaseDynamic(String word,List<String> imgUrls,String tailId,List<InsertModel> richInsertList);

    void upLoadImgs(Context context, String word, List<String> imgPaths, String tailId,List<InsertModel> richInsertList);

    void ReleaseLongText(Context context , String title,List<RichTextModel> editData);
}
