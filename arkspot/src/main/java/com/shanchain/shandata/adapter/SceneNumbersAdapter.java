package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SceneMemberInfo;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.SpanUtils;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class SceneNumbersAdapter extends BaseQuickAdapter<SceneMemberInfo, BaseViewHolder> {

    private boolean isAdmin;

    private SceneNumbersAdapter(@LayoutRes int layoutResId, @Nullable List<SceneMemberInfo> data) {
        super(layoutResId, data);
    }

    public SceneNumbersAdapter(@LayoutRes int layoutResId, @Nullable List<SceneMemberInfo> data, boolean isAdmin) {
        super(layoutResId, data);
        this.isAdmin = isAdmin;
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneMemberInfo item) {
        String nick = item.getName() + "(No." + item.getNum() + ")";
        SpannableStringBuilder spannableStringBuilder = SpanUtils.buildSpannableString(nick, mContext.getResources().getColor(R.color.colorTextDefault), 0, item.getName().length());
        helper.setText(R.id.tv_item_all_nick, spannableStringBuilder);

        helper.setText(R.id.tv_item_all_permission, item.getPermission());
        helper.setText(R.id.tv_item_all_des, item.getDes());


        GlideUtils.load(mContext,item.getAvatar(),(ImageView) helper.getView(R.id.iv_item_scene_all_avatar),0);

        helper.addOnClickListener(R.id.tv_item_all_leave);

        if (isAdmin) {
            helper.setVisible(R.id.tv_item_all_leave, true);
        } else {
            helper.setVisible(R.id.tv_item_all_leave, false);
        }

    }
}
