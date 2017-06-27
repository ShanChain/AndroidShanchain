package com.shanchain.shandata.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.SpanUtils;

import java.util.List;


/**
 * Created by zhoujian on 2017/5/17.
 */

public class AreaAdapter<T> extends RecyclerView.Adapter<AreaAdapter.MyViewHolder> {

    private List<T> dataList;
    private Context mContext;

    public AreaAdapter(Activity activity, List<T> dataList) {
        this.mContext = activity;
        this.dataList = dataList;
    }

    public interface OnItemClickListener{
        void OnItemClickListener(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  View view = View.inflate(mContext, R.layout.item_area,parent);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_area, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final AreaAdapter.MyViewHolder holder, final int position) {
        holder.setData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.OnItemClickListener(holder.itemView,holder.getLayoutPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvArea;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
        }

        public void setData(int position) {
            String t = (String) dataList.get(position);
            String[] result = t.split("\\+");
            if (result.length>1){

                String area = result[0].trim();

                String code = result[1].trim();
                tvArea.setText(SpanUtils.buildSpannableString(area + " +" + code, mContext.getResources().getColor(R.color.colorBtn),0,area.length()));
            }else {
                LogUtils.d(t + "========="+position);
            }
        }
    }
}
