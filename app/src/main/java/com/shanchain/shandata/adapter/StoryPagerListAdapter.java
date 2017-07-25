package com.shanchain.shandata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.mvp.model.StoryPagerListInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/7/18.
 */

public class StoryPagerListAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List<StoryPagerListInfo> mListInfos;

    public StoryPagerListAdapter(BaseActivity activity, List<StoryPagerListInfo> listInfos) {
        mActivity = activity;
        mListInfos = listInfos;
    }

    @Override
    public int getCount() {
        return mListInfos.size();
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
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_story_pager, (ViewGroup)mActivity.findViewById(android.R.id.content),false);
            holder.mTvItemStoryPagerType = (TextView) convertView.findViewById(R.id.tv_item_story_pager_type);
            holder.mTvItemStoryPagerDes = (TextView) convertView.findViewById(R.id.tv_item_story_pager_des);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        StoryPagerListInfo storyListInfo = mListInfos.get(position);

        holder.mTvItemStoryPagerType.setText(storyListInfo.getTime() + "  " + storyListInfo.getType());
        holder.mTvItemStoryPagerDes.setText(storyListInfo.getDes());

        return convertView;
    }

    static class ViewHolder{
        TextView mTvItemStoryPagerType;
        TextView mTvItemStoryPagerDes;
    }

}
