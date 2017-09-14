package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.SceneNumberInfo;

import java.util.List;

import utils.SpanUtils;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class SceneNumbersAdapter extends BaseQuickAdapter<SceneNumberInfo,BaseViewHolder> {
    public SceneNumbersAdapter(@LayoutRes int layoutResId, @Nullable List<SceneNumberInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneNumberInfo item) {
        String nick = item.getName() + "(No." + item.getNum() + ")";
        SpannableStringBuilder spannableStringBuilder = SpanUtils.buildSpannableString(nick, mContext.getResources().getColor(R.color.colorTextDefault), 0, item.getName().length());
        helper.setText(R.id.tv_item_all_nick,spannableStringBuilder);

        helper.addOnClickListener(R.id.tv_item_all_leave);

    }
}
