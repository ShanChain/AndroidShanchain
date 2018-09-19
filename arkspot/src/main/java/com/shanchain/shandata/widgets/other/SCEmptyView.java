package com.shanchain.shandata.widgets.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;


/**
 * Created by zhoujian on 2017/9/29.
 */

public class SCEmptyView extends LinearLayout {
    private String word;
    private int imgRes;
    public SCEmptyView(Context context,String word,int imgRes) {
        super(context,null);
        this.imgRes = imgRes;
        this.word = word;
        init(context);
    }

    public SCEmptyView(Context context,int wordRes,int imgRes) {
        super(context,null);
        this.imgRes = imgRes;
        this.word = getResources().getString(wordRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.empty_search_role,this);
        ImageView ivEmpty = (ImageView) view.findViewById(R.id.iv_empty_img);
        TextView tvEmpty = (TextView) view.findViewById(R.id.tv_empty_word);
        ivEmpty.setImageResource(imgRes);
        tvEmpty.setText(word);
    }


}
