package com.shanchain.arkspot.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.ui.model.StoryModelInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/24.
 */

public class StoryItemFloorsAdapter extends BaseAdapter {

    private List<StoryModelInfo> datas;

    public StoryItemFloorsAdapter(List<StoryModelInfo> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_floors,null);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_item_floors_content);
        StoryModelBean bean = datas.get(position).getBean();
        if(bean != null){
            String intro = bean.getIntro();
            String characterName = bean.getCharacterName();
            int genNum = bean.getGenNum();
            String content = "";
            if (intro.contains("content")){
                content = JSONObject.parseObject(intro).getString("content");
            }else {
                content = intro;
            }
            String text = genNum +"楼  " + characterName + ": " + content;
            tvContent.setText(text);
        }
        return view;
    }
}
