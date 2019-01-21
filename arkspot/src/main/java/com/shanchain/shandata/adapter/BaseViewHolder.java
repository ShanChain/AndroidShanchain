package com.shanchain.shandata.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.shandata.R;
import com.squareup.picasso.Picasso;

import cn.jiguang.imui.model.ChatEventMessage;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    private Context context;
    public TextView textView;
    public ImageView imageView;
    private OnItemClickListener listener;
    private OnLayoutViewClickListener layoutViewlistener;

    public BaseViewHolder(View itemView, Context context) {
        super(itemView);
        this.itemView = itemView;
        this.context = context;
//        itemView.setOnClickListener(this);
    }

//    public <T extends View> T getItemView(int viewType,) {
//
//    }

    public <T extends View> T getViewId(@IdRes int viewId) {
        return (T) itemView.findViewById(viewId);
    }

    public View getConvertView() {
        return itemView;
    }

    public void setTextView(@IdRes int viewId, CharSequence text) {
        textView = getViewId(viewId);
        textView.setText(text);

    }

    public void setImageView(@IdRes int viewId, String url) {
        imageView = getViewId(viewId);
        Glide.with(context).load(url).into(imageView);
//        Picasso.with(context).load(url).into(imageView);
    }

    //    public void setImageView( int[] viewIds,int[] itemLayoutId,int viewType,List<String> urlList) {
//        ImageView[] imgs = new ImageView[viewIds.length];
//        for (int i = 0; i < viewIds.length; i++) {
//           imageView= getViewId(viewIds[i]);
//            imgs[i] = imageView;
//        }
//        for (int i = 0; i < itemLayoutId.length; i++) {
//            if (viewType==i){
//                if (imageView != null) {
//                    for (int i1 = 0; i1 < urlList.size(); i1++) {
//                            Glide.with(context).load(urlList.get(i1)).into(imgs[i1]);
//                    }
//                    return;
//                }
//
//            }
//        }
//    }
    public void setImageURL(@IdRes int viewId, String url) {
        imageView = getViewId(viewId);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.aurora_headicon_default);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

//    public void setPicassoImage(@IdRes int viewId, String url) {
//        imageView = getViewId(viewId);
//        Picasso.with(context).load(url)
//                .transform(new CropCircleTransformation())
//                .into(imageView);
//    }

    public void setViewOnClick(@IdRes int viewId, int[] layoutId, int viewType, int position, final OnItemClickListener listener) {
        View listenView = null;
        if (listener != null) {
            for (int i = 0; i < layoutId.length; i++) {
                if (viewType == i) {
//            View layoutView = getLayoutId(layoutId);
                    listenView = getViewId(viewId);
                    if (listenView != null) {
                        listenView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.OnItemClick(view);
                            }
                        });
                    }
                }
            }

        }
    }

    public void setLayoutViewOnClick(int[] layoutId, int viewType, ChatEventMessage chatEventMessage, final OnLayoutViewClickListener layoutViewListener) {
        if (layoutViewListener != null) {
            for (int i = 0; i < layoutId.length; i++) {
                if (viewType == i) {

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutViewListener.OnLayoutViewClick(view);
                        }
                    });
                }
            }
        }
    }

    interface OnItemClickListener {

        void OnItemClick(View view);

    }

    interface OnLayoutViewClickListener {

        void OnLayoutViewClick(View view);

    }
}



