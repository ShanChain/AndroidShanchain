package com.shanchain.arkspot.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryTagInfo;

import java.util.List;


/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleGridAdapter extends BaseAdapter {

    private List<StoryTagInfo> mDatas;

    public StoryTitleGridAdapter(List<StoryTagInfo> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_head_tag,null);
        TextView tvHeadTag = (TextView) view.findViewById(R.id.tv_item_head_tag);

        StoryTagInfo storyTagInfo = mDatas.get(position);

        tvHeadTag.setText(storyTagInfo.getTag());

        if (position == mDatas.size() -1) {
            tvHeadTag.setBackgroundResource(R.mipmap.abs_therrbody_btn_more_default);
            tvHeadTag.setTextColor(parent.getResources().getColor(R.color.colorWhite));
        }else {
            tvHeadTag.setBackgroundResource(storyTagInfo.isSelected()
                    ?R.mipmap.abs_therrbody_btn_more_default
                    :R.mipmap.abs_therrbody_btn_label_default);

            tvHeadTag.setTextColor(parent.getResources()
                    .getColor(storyTagInfo.isSelected()?R.color.colorWhite:R.color.colorTextDefault));

        }


        return view;
    }


}
