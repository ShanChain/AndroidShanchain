package com.shanchain.shandata.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdContactInfo;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoujian on 2017/9/8.
 */

public class ContactAdapter extends BaseExpandableListAdapter {

    List<String> parent;

    Map<String, List<BdContactInfo>> map;

    public ContactAdapter(List<String> parent, Map<String, List<BdContactInfo>> map) {
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
        BdContactInfo bdContactInfo = map.get(key).get(childPosition);
        return bdContactInfo;
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
            convertView = View.inflate(parent.getContext(), R.layout.item_contact_group, null);
        }
        TextView tvContactGroup = (TextView) convertView.findViewById(R.id.tv_item_contact_group);
        TextView tvContactCount = (TextView) convertView.findViewById(R.id.tv_item_contact_group_count);
        tvContactGroup.setText(this.parent.get(groupPosition));
        tvContactCount.setText(this.map.get(this.parent.get(groupPosition)).size() + "");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_contact_child, null);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_item_contact_child_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_contact_child_name);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_item_contact_child_des);
            holder.tvFocus = (TextView) convertView.findViewById(R.id.tv_item_contact_child_focus);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        BdContactInfo bdContactInfo = map.get(this.parent.get(groupPosition)).get(childPosition);
        boolean isGroup = bdContactInfo.isGroup();
        holder.tvName.setText(isGroup?bdContactInfo.getGroupInfo().getGroupName():bdContactInfo.getContactBean().getName());
        holder.tvDes.setText(isGroup?bdContactInfo.getGroupInfo().getGroupDesc():bdContactInfo.getContactBean().getIntro());
        GlideUtils.load(convertView.getContext(),isGroup?bdContactInfo.getGroupInfo().getIconUrl():bdContactInfo.getContactBean().getHeadImg(),holder.ivAvatar,0);

        if (this.parent.get(groupPosition).equals("我的关注")) {
            holder.tvFocus.setVisibility(View.GONE);
            Drawable drawable = parent.getResources().getDrawable(R.mipmap.abs_contactperson_btn_attention_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvFocus.setCompoundDrawables(null, drawable,null, null);
            holder.tvFocus.setCompoundDrawablePadding(DensityUtils.dip2px(parent.getContext(), 5));
            holder.tvFocus.setText("已关注");
            holder.tvFocus.setTextColor(parent.getContext().getResources().getColor(R.color.colorHint));
        } else if (this.parent.get(groupPosition).equals("互相关注")) {
            holder.tvFocus.setVisibility(View.GONE);
            Drawable drawable = parent.getResources().getDrawable(R.mipmap.abs_contactperson_btn_attention_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvFocus.setCompoundDrawables(null, drawable,null, null);
            holder.tvFocus.setCompoundDrawablePadding(DensityUtils.dip2px(parent.getContext(), 5));
            holder.tvFocus.setText("已关注");
            holder.tvFocus.setTextColor(parent.getContext().getResources().getColor(R.color.colorHint));
        } else if (this.parent.get(groupPosition).equals("我的粉丝")) {
            holder.tvFocus.setVisibility(View.GONE);
            Drawable drawable = parent.getResources().getDrawable(R.mipmap.abs_contactperson_btn_attention_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvFocus.setCompoundDrawables(null, drawable,null, null);
            holder.tvFocus.setCompoundDrawablePadding(DensityUtils.dip2px(parent.getContext(), 5));
            holder.tvFocus.setText("加关注");
            holder.tvFocus.setTextColor(parent.getContext().getResources().getColor(R.color.colorActive));
        } else if (this.parent.get(groupPosition).equals("群组")) {
            holder.tvFocus.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ChildViewHolder {
        TextView tvName;
        TextView tvDes;
        TextView tvFocus;
        ImageView ivAvatar;
    }

}
