package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.QuestionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/9/23
 * Describe :
 */
public class QuestionListAdapter extends BaseAdapter {
    private Context mContext;
    private List<QuestionBean> mList;

    public QuestionListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public void setList(List<QuestionBean> list) {
        if (list != null && list.size() > 0) {
            mList = list;
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.activity_question_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvUserName.setText(mList.get(position).getTitle());

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_user_name)
        TextView tvUserName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
