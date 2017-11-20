package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SceneImgInfo;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class SceneDetailsAdapter extends BaseQuickAdapter<SceneImgInfo,BaseViewHolder> {

    public SceneDetailsAdapter(@LayoutRes int layoutResId, @Nullable List<SceneImgInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneImgInfo item) {
        ImageView iv = helper.getView(R.id.iv_item_scene_numbers);
        GlideUtils.load(mContext,item.getImg(),iv,0);
    }
}
