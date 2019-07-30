package com.shanchain.shandata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IAddPhotoCallback;
import com.shanchain.shandata.interfaces.IDeletePhotoCallback;
import com.shanchain.shandata.ui.model.PhotoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/7/24
 * Describe :选择图片适配器
 */
public class PhotoArticleAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoBean> mList;
    private IAddPhotoCallback mCallback;
    private IDeletePhotoCallback mIDeletePhotoCallback;
    public PhotoArticleAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public void setList(List<PhotoBean> list) {
        if (list != null && list.size() > 0) {
            mList = list;
        }
    }

    public void setCallback(IAddPhotoCallback callback){
        this.mCallback = callback;
    }
    public void setIDeletePhotoCallback(IDeletePhotoCallback callback){
        this.mIDeletePhotoCallback = callback;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View convertView = view;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photo_article_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.llRootview.getLayoutParams();
        int with = (int)((DensityUtils.getScreenWidth(mContext)-DensityUtils.dip2px(mContext,50))/4);
        layoutParams.width = with;
        layoutParams.height = with;
        viewHolder.llRootview.setLayoutParams(layoutParams);
        PhotoBean photoBean = mList.get(position);
        if(photoBean!=null){
            if(TextUtils.isEmpty(photoBean.getUrl())){
                viewHolder.rlEntity.setVisibility(View.VISIBLE);
                viewHolder.rlPhoto.setVisibility(View.GONE);
            }else {
                viewHolder.rlEntity.setVisibility(View.GONE);
                viewHolder.rlPhoto.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(photoBean.getUrl()).into(viewHolder.ivPhoto);
            }
        }
        viewHolder.rlEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback!=null){
                    mCallback.addPhoto();
                }
            }
        });
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIDeletePhotoCallback!=null){
                    mIDeletePhotoCallback.deletePhoto(position);
                }
            }
        });
      return convertView;
    }


    class ViewHolder {
        @Bind(R.id.ll_rootview)
        RelativeLayout llRootview;
        @Bind(R.id.rl_entity)
        RelativeLayout rlEntity;
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;
        @Bind(R.id.rl_photo)
        RelativeLayout rlPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
