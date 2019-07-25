package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.data.common.base.Constants;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.presenter.SquarePresenter;
import com.shanchain.shandata.ui.presenter.impl.SquarePresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.PublishArticleActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.SquareView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/7/22
 * Describe :广场列表
 */
public class SquareFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        SquareView {
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.im_add_content)
    ImageView imAdd;

    private AlertDialog mAlertDialog;
    private SquarePresenter mSquarePresenter;
    private int pageIndex = 1;
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_square, null);
    }

    @Override
    public void initData() {
        mSquarePresenter = new SquarePresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSquarePresenter.getListData("",pageIndex, Constants.pageSize,Constants.pullRefress);
    }

    //点击添加内容弹窗
    @OnClick(R.id.im_add_content)
    void showAddContentView(){
        showContentViewDialog();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        mSquarePresenter.getListData("",pageIndex, Constants.pageSize,Constants.pullRefress);
    }

    private void showContentViewDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        if(mAlertDialog == null){
            mAlertDialog=builder.create();
        }
        View view = View.inflate(getActivity(),R.layout.add_artocle_show_view,null);
        LinearLayout llShrt = view.findViewById(R.id.ll_weiw);
        LinearLayout llLong = view.findViewById(R.id.ll_cwen);
        LinearLayout llLink = view.findViewById(R.id.ll_lj);
        TextView tvClose = view.findViewById(R.id.tv_closed);

        mAlertDialog.setContentView(view);
        mAlertDialog.setCanceledOnTouchOutside(true);
        Window window = mAlertDialog.getWindow();
        // 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置宽度
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        // 给 DecorView 设置背景颜色，很重要，不然导致 Dialog 内容显示不全，有一部分内容会充当 padding，上面例子有举出
        window.getDecorView().setBackgroundColor(Color.GREEN);

        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_menu_animation);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAlertDialog!=null && mAlertDialog.isShowing()){
                    mAlertDialog.dismiss();
                }
            }
        });
        //发表短文
        llShrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PublishArticleActivity.class)
                                .putExtra("type",1));
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog.show();

    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setListDataResponse(String response, int pullType) {
        refreshLayout.setRefreshing(false);
    }
}
