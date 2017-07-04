package com.shanchain.shandata.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ReservationInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/30.
 */

public class MyReservationAdapter extends BaseCommonAdapter<ReservationInfo> {

    private List<ReservationInfo> mDatas;

    public MyReservationAdapter(Context context, int layoutId, List<ReservationInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindDatas(final ViewHolder holder, ReservationInfo reservationInfo, final int position) {
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

                CustomDialog customDialog = new CustomDialog(mContext,R.layout.dialog_code_qr,null);

                customDialog.show();

            }
        });

    }


    public Bitmap Create2DCode(String str) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(matrix.get(x, y)){
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
