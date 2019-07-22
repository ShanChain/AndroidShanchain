package com.shanchain.shandata.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;

/**
 * Created by hmg on 2019/7/20.
 */

public class BottomTab extends LinearLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout ll0;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private ImageView iv0;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private OnTabClickListener onTabClickListener;


    public BottomTab(Context context) {
        super(context);
    }

    public BottomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_bottom_tab, this);
        initViews(view);
        initListener();
    }

    private void initListener() {
        ll0.setOnClickListener(this);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
    }

    private void initViews(View view) {
        ll0= (LinearLayout) view.findViewById(R.id.ll_shouye);
        ll1 = (LinearLayout) view.findViewById(R.id.ll_cause);
        ll2 = (LinearLayout) view.findViewById(R.id.ll_bankuai);
        ll3 = (LinearLayout) view.findViewById(R.id.ll_news);
        iv0 = (ImageView) view.findViewById(R.id.iv_shouye);
        iv1 = (ImageView) view.findViewById(R.id.iv_cause);
        iv2 = (ImageView) view.findViewById(R.id.iv_bankuai);
        iv3 = (ImageView) view.findViewById(R.id.iv_news);
        tv0 = (TextView) view.findViewById(R.id.tv_shouye);
        tv1 = (TextView) view.findViewById(R.id.tv_cause);
        tv2 = (TextView) view.findViewById(R.id.tv_bankuai);
        tv3 = (TextView) view.findViewById(R.id.tv_news);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_shouye:
                changeState(0);
                if (onTabClickListener != null)
                    onTabClickListener.onItemClick(0);
                break;
            case R.id.ll_cause:
                changeState(1);
                if (onTabClickListener != null)
                    onTabClickListener.onItemClick(1);
                break;
            case R.id.ll_bankuai:
                changeState(2);
                if (onTabClickListener != null)
                    onTabClickListener.onItemClick(2);
                break;
            case R.id.ll_news:
                changeState(3);
                if (onTabClickListener != null)
                    onTabClickListener.onItemClick(3);
                break;
        }
    }

    public void changeState(int position) {
        tv0.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv1.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv2.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv3.setTextColor(getResources().getColor(R.color.white_btn_press));
        iv0.setImageResource(R.mipmap.maya_c);
        iv1.setImageResource(R.mipmap.yuansq_c);
        iv2.setImageResource(R.mipmap.sheqb_c);
        iv3.setImageResource(R.mipmap.mine_c);
        switch (position) {
            case 0:
                iv0.setImageResource(R.mipmap.majia_s);
                tv0.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 1:
                iv1.setImageResource(R.mipmap.yuansq_s);
                tv1.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 2:
                iv2.setImageResource(R.mipmap.sheqb_s);
                tv2.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 3:
                iv3.setImageResource(R.mipmap.mine_s);
                tv3.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;

        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    public interface OnTabClickListener {
        void onItemClick(int position);
    }
}
