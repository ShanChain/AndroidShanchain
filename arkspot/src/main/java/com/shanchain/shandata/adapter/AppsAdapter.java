package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.model.AppBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AppsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<AppBean> mDdata = new ArrayList<AppBean>();

    public AppsAdapter(Context context, ArrayList<AppBean> data) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        if (data != null) {
            this.mDdata = data;
        }
    }

    @Override
    public int getCount() {
        return mDdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mDdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_app, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AppBean appBean = mDdata.get(position);
        if (appBean != null) {
            viewHolder.iv_icon.setBackgroundResource(appBean.getIcon() != 0 ? appBean.getIcon() : 0);
            viewHolder.tv_name.setText(appBean.getFuncName() != null ? appBean.getFuncName() : "");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appBean.getFuncName() == null) {
                        return;
                    }
                    if (appBean.getFuncName().equals("图片")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.IMAGE_MESSAGE));
                    } else if (appBean.getFuncName().equals("拍摄")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.TAKE_PHOTO_MESSAGE));
                    } else if (appBean.getFuncName().equals("位置")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.TAKE_LOCATION));
                    } else if (appBean.getFuncName().equals("文件")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.FILE_MESSAGE));
                    } else if (appBean.getFuncName().equals("视频")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.TACK_VIDEO));
                    } else if (appBean.getFuncName().equals("语音")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.TACK_VOICE));
                    } else if (appBean.getFuncName().equals("名片")) {
                        EventBus.getDefault().post(new ImageEvent(mContext, MyApplication.BUSINESS_CARD));
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
    }

    public class ImageEvent {
        private Context mContext;
        private int mFlag;

        public ImageEvent(int flag) {
            mFlag = flag;
        }

        public ImageEvent(Context context, int flag) {
            this.mContext = context;
            mFlag = flag;
        }

        public int getFlag() {
            return mFlag;
        }

        public Context getContext() {
            return mContext;
        }

    }
}