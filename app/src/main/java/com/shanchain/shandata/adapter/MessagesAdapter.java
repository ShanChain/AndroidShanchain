package com.shanchain.shandata.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.mvp.model.DynamicMessageInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessagesAdapter extends BaseAdapter {

    private ArrayList<DynamicMessageInfo> mDatas;

    public MessagesAdapter(ArrayList<DynamicMessageInfo> datas) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),R.layout.item_dynamic,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DynamicMessageInfo dynamicMessageInfo = mDatas.get(position);
        viewHolder.mIvItemDynamicTip.setVisibility(dynamicMessageInfo.isRead()?View.GONE:View.VISIBLE);
        viewHolder.mTvItemDynamicTitle.setText(dynamicMessageInfo.getTitle());
        viewHolder.mTvItemDynamicTime.setText(dynamicMessageInfo.getTime());
        viewHolder.mTvItemDynamicDes.setText(dynamicMessageInfo.getDes());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_item_dynamic_img)
        ImageView mIvItemDynamicImg;
        @Bind(R.id.iv_item_dynamic_tip)
        ImageView mIvItemDynamicTip;
        @Bind(R.id.tv_item_dynamic_title)
        TextView mTvItemDynamicTitle;
        @Bind(R.id.tv_item_dynamic_time)
        TextView mTvItemDynamicTime;
        @Bind(R.id.tv_item_dynamic_des)
        TextView mTvItemDynamicDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
