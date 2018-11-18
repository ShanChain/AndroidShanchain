package com.shanchain.shandata.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanchain.data.common.utils.LogUtils;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private List<T> list;
    private int[] itemLayoutId;
    private int itemPosition;
//    private  OnItemClickListener listener;

    public CommonAdapter(Context context, List<T> list, int[] itemLayoutId) {
        this.list = list;
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(itemLayoutId[viewType], parent, false);
        BaseViewHolder holder = new BaseViewHolder(itemView, context);
        holder.setIsRecyclable(false);
        if (holder.itemView.getTag()==(Object) holder.getLayoutPosition()){
            BaseViewHolder newHolder = new BaseViewHolder(itemView, context);
            holder.itemView.setTag(holder.getLayoutPosition());
            return  newHolder;
        }else {

            return holder;
        }

    }


    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        LogUtils.d("复用的时候的position",holder.getLayoutPosition()+"");
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        LogUtils.d("绑定的时候的position",position+"");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
            setData(holder, list.get(position), getItemViewType(position),position);
        LogUtils.d("绑定的时候的position",position+"");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % itemLayoutId.length;
    }

    public void clear() {
        this.list.clear();
    }

    public void upData(List<T> data) {
        this.list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

// public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
// public abstract void setOnItemClickListener(OnItemClickListener listener);

    public abstract void setData(BaseViewHolder holder, T data, int viewType,int itemPosition);

//    public interface OnItemClickListener {
//
//        void OnItemClick(View view, int position);
//
//    }

}
