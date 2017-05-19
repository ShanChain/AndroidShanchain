package com.shanchain.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/18.
 */

public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private Context mContext;
    private List<T> dataList;

    public BaseRecyclerViewAdapter(Context context, List<T> dataList) {
        mContext = context;
        this.dataList = dataList;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
