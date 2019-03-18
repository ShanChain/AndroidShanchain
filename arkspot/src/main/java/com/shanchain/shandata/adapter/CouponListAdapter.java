package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.view.activity.coupon.CouponDetailsActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCreateDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.model.ChatEventMessage;


public class CouponListAdapter extends CommonAdapter<CouponSubInfo> implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnLayoutViewClickListener {
    private List<CouponSubInfo> list;
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
    public void setData(BaseViewHolder holder, final CouponSubInfo item, int viewType, int position) {
        this.couponInfo = item;
        this.holder = holder;
        holder.setTextView(R.id.even_message_bounty, item.getPrice());//价格
        holder.setTextView(R.id.tv_coupon_code, item.getTokenSymbol());//代号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date(Long.valueOf(item.getDeadline())));
        holder.setTextView(R.id.even_message_last_time, "有效期至： " + time);//失效时间
//        holder.setTextView(R.id.even_message_last_time, item.getDeadline());//失效时间
        holder.setImageURL(R.id.iv_item_story_avatar, item.getPhotoUrl());
        //卡劵状态
        switch (item.getTokenStatus()) {
            case CouponSubInfo.CREATE_INVALID:
                holder.setTextView(R.id.tv_item_story_name, item.getName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "已失效");
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        if (SCCacheUtils.getCacheUserId().equals(String.valueOf(item.getUserId()))) {
                            Intent intent = new Intent(context, MyCreateDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, CouponDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        }
                    }
                });
                //itemView设置监听
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SCCacheUtils.getCacheUserId().equals(String.valueOf(item.getUserId()))) {
                            Intent intent = new Intent(context, MyCreateDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, CouponDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case CouponSubInfo.CREATE_WAIT:
                holder.setTextView(R.id.tv_item_story_name, item.getName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "查看");
                if (item.getGetStatus() != 0) {
                    switch (item.getGetStatus()) {
                        case CouponInfo.COUPONS_CREATE:
                            holder.setTextView(R.id.tv_coupon_check, "查看");
                            break;
                        case CouponInfo.COUPONS_RECEIVER:
                            holder.setTextView(R.id.tv_coupon_check, "已领取");
                            break;
                        case CouponInfo.COUPONS_UN_RECEIVER:
                            if (item.getSubuserId() != Integer.valueOf(SCCacheUtils.getCacheUserId())) {
                                holder.setTextView(R.id.tv_coupon_check, "领取");
                            }
                            break;
                    }
                }
                holder.setTextView(R.id.even_message_location, "剩余 " + item.getRemainAmount() + " 张");
                if (item.getRemainAmount().equals("0")) {
                    holder.setTextView(R.id.even_message_location, "全部被领取");
                }
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        if (SCCacheUtils.getCacheUserId().equals(String.valueOf(item.getUserId()))) {
                            Intent intent = new Intent(context, MyCreateDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, CouponDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        }
                    }
                });
                //itemView设置监听
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SCCacheUtils.getCacheUserId().equals(String.valueOf(item.getUserId()))) {
                            Intent intent = new Intent(context, MyCreateDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, CouponDetailsActivity.class);
                            intent.putExtra("couponsId", item.getCouponsId());
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case CouponSubInfo.RECEIVER://已领取待使用
                holder.setTextView(R.id.tv_item_story_name, item.getTokenName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "待使用");
                holder.getViewId(R.id.even_message_location).setVisibility(View.GONE);
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                break;
            case CouponSubInfo.RECEIVER_UN_USE:
                holder.setTextView(R.id.tv_item_story_name, item.getTokenName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "");//转让的状态
                holder.getViewId(R.id.even_message_location).setVisibility(View.GONE);
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                break;
            case CouponSubInfo.RECEIVER_USE:
                holder.setTextView(R.id.tv_item_story_name, item.getTokenName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "已使用");
                holder.getViewId(R.id.even_message_location).setVisibility(View.GONE);
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                break;
            case CouponSubInfo.RECEIVER_INVALID:
                holder.setTextView(R.id.tv_item_story_name, item.getTokenName() + "");//卡劵名称
                holder.setTextView(R.id.tv_coupon_check, "已失效");
                holder.getViewId(R.id.even_message_location).setVisibility(View.GONE);
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                break;
            default:
                holder.setTextView(R.id.tv_coupon_check, "查看");
                holder.setViewOnClick(R.id.tv_coupon_check, new int[]{R.layout.item_coupon_one, R.layout.item_coupon_two}, viewType, position, new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CouponDetailsActivity.class);
                        intent.putExtra("subCoupId", item.getSubCoupId());
                        context.startActivity(intent);
                    }
                });
                break;
        }

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
