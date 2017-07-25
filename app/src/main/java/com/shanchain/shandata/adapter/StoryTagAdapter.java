package com.shanchain.shandata.adapter;

import android.view.View;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

public class StoryTagAdapter extends TagAdapter<String> {

    public StoryTagAdapter(String[] datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        if (position == 0){
            TextView textView = new TextView(parent.getContext());
            textView.setText(s + ":");
            textView.setTextSize(14);
            textView.setTextColor(parent.getResources().getColor(R.color.colorBtn));
            return textView;
        }else {
            View view = View.inflate(parent.getContext(), R.layout.item_tag,null);
            TextView tvTag = (TextView) view.findViewById(R.id.tv_item_tag);
            tvTag.setText(s);
            return view;
        }

    }
}
