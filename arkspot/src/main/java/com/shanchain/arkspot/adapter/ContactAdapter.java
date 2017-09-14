package com.shanchain.arkspot.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.ContactInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoujian on 2017/9/8.
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    List<String> parent;

    Map<String, ArrayList<ContactInfo>> map;

    public ContactAdapter(List<String> parent, Map<String, ArrayList<ContactInfo>> map) {
        this.parent = parent;
        this.map = map;
    }

    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key = parent.get(groupPosition);
        int size = map.get(key).size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = parent.get(groupPosition);
        ContactInfo contactInfo = map.get(key).get(childPosition);
        return contactInfo;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_contact_group,null);
        }
        TextView tvContactGroup = (TextView) convertView.findViewById(R.id.tv_item_contact_group);
        TextView tvContactCount = (TextView) convertView.findViewById(R.id.tv_item_contact_group_count);
        tvContactGroup.setText(this.parent.get(groupPosition));
        tvContactCount.setText(this.map.get(this.parent.get(groupPosition)).size()+"");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(parent.getContext(),R.layout.item_contact_child,null);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_item_contact_child_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_contact_child_name);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_item_contact_child_des);
            holder.tvFocus = (TextView) convertView.findViewById(R.id.tv_item_contact_child_focus);
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        ContactInfo contactInfo = map.get(this.parent.get(groupPosition)).get(childPosition);
        holder.tvName.setText(contactInfo.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ChildViewHolder{
        TextView tvName;
        TextView tvDes;
        TextView tvFocus;
        ImageView ivAvatar;
    }

}
