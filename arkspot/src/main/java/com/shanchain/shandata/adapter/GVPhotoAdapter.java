package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/7/29
 * Describe :
 */
public class GVPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> photoList;

    public GVPhotoAdapter(Context context) {
        this.mContext = context;
        photoList = new ArrayList<>();
    }
    public void setPhotoList(List<String> list){
        if(list!=null && list.size()>0){
            this.photoList = list;
        }
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView = view;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gvphoto_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.llRootview.getLayoutParams();
        int with = (int)((DensityUtils.getScreenWidth(mContext)-DensityUtils.dip2px(mContext,110))/3);
        layoutParams.width = with;
        layoutParams.height = with;
        viewHolder.llRootview.setLayoutParams(layoutParams);
        LogUtils.d("---photo path-- ",photoList.get(position));
        String s = photoList.get(position).replaceAll("\\\\","");
        Glide.with(mContext).load(s.substring(1,s.length()-1))
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default)).into(viewHolder.ivItem);

        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.iv_item)
        ImageView ivItem;
        @Bind(R.id.ll_rootview)
        RelativeLayout llRootview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
