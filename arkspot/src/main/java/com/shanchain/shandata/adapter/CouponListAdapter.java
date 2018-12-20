package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.ui.view.activity.coupon.CouponDetailsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


public class CouponListAdapter extends CommonAdapter<CouponInfo> implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnLayoutViewClickListener, View.OnClickListener {
    private List<ChatEventMessage> list;
    private int[] itemLayoutId;
    private Context context;
    private CouponInfo couponInfo;
    private OnItemClickListener onItemClickListener;
    private OnClickListener onClickListener;
    private BaseViewHolder holder;

    public CouponListAdapter(Context context, List list, int[] itemLayoutId) {
        super(context, list, itemLayoutId);
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public void setData(BaseViewHolder holder, CouponInfo item, int viewType, int position) {
        this.couponInfo = item;
        this.holder = holder;

        holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                Intent intent = new Intent(context,CouponDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }


    public BaseViewHolder getHolder() {
        return holder;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
//        onClickListener.OnClick();
    }


    @Override
    public void OnItemClick(View view) {

    }

    @Override
    public void OnLayoutViewClick(View view) {
        if (onClickListener != null) {
            onClickListener.OnClick(couponInfo, view, holder);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ChatEventMessage item, View view, BaseViewHolder holder, int position);
    }

    public interface OnClickListener {
        void OnClick(CouponInfo item, View view, BaseViewHolder holder);

    }
}
