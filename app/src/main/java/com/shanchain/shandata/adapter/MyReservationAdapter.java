package com.shanchain.shandata.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ReservationInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.QrCodeUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MyReservationAdapter extends BaseCommonAdapter<ReservationInfo> {

    private List<ReservationInfo> mDatas;

    public MyReservationAdapter(Context context, int layoutId, List<ReservationInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindData(final ViewHolder holder, final ReservationInfo reservationInfo, final int position) {
        holder.setText(R.id.tv_item_reservation_goods,reservationInfo.getGoods());
        holder.setText(R.id.tv_item_reservation_order,"单号:"+reservationInfo.getOrderNum());
        Glide.with(mContext)
                .load(R.drawable.photo_bear)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_reservation_goods));



        holder.setOnClickListener(R.id.tv_item_reservation_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position-1);
                notifyDataSetChanged();
            }
        });

        holder.setOnClickListener(R.id.iv_item_reservation_code, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_code_qr,null);

                ImageView ivDialogQr = (ImageView) view.findViewById(R.id.iv_dialog_qr);
                TextView tvExchangeCode = (TextView) view.findViewById(R.id.tv_dialog_exchange_code);
                tvExchangeCode.setText("兑换码:  " + reservationInfo.getExchangeCode());
                Bitmap qrImage = QrCodeUtils.createQRImage(reservationInfo.getExchangeCode(),
                        DensityUtils.dip2px(mContext, 180),
                        DensityUtils.dip2px(mContext, 180));
                if (qrImage != null) {
                    ivDialogQr.setImageBitmap(qrImage);
                }
                builder.setView(view);
                builder.show();

            }
        });

    }


}
