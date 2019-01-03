package com.shanchain.shandata.ui.view.activity.coupon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CouponDetailsActivity extends BaseActivity {

    @Bind(R.id.tb_coupon_detail)
    ArthurToolBar tbCouponDetail;
    @Bind(R.id.tv_coupon_name)
    TextView tvCouponName;
    @Bind(R.id.linear_show_QRcode)
    LinearLayout linearShowQRcode;
    @Bind(R.id.tv_coupon_currency)
    TextView tvCouponCurrency;
    @Bind(R.id.tv_coupon_currency_num)
    TextView tvCouponCurrencyNum;
    @Bind(R.id.tv_coupon_seller_name)
    TextView tvCouponSellerName;
    @Bind(R.id.tv_coupon_code)
    TextView tvCouponCode;
    @Bind(R.id.tv_use_rule)
    TextView tvUseRule;
    @Bind(R.id.tv_coupon_details_expiration)
    TextView tvCouponDetailsExpiration;
    @Bind(R.id.linear_show_details)
    LinearLayout linearShowDetails;
    @Bind(R.id.linear_coupon_detail)
    LinearLayout linearCouponDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void initViewsAndEvents() {

    }
}
