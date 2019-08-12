package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SqureAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.interfaces.IAttentionCallback;
import com.shanchain.shandata.interfaces.ICheckBigPhotoCallback;
import com.shanchain.shandata.interfaces.IPraiseCallback;
import com.shanchain.shandata.ui.model.CouponSubInfo;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.ui.presenter.SquarePresenter;
import com.shanchain.shandata.ui.presenter.impl.SquarePresenterImpl;
import com.shanchain.shandata.ui.view.activity.article.ArticleDetailActivity;
import com.shanchain.shandata.ui.view.activity.article.PhotoPagerActivity;
import com.shanchain.shandata.ui.view.activity.article.PublishArticleActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.SquareView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private int pageIndex = 0;
    private SqureAdapter mSqureAdapter;
    private List<SqureDataEntity> mList = new ArrayList<>();
    private View footerView;
    private boolean isLast = false;
    private SqureDataEntity mSqureDataEntity = null;
    private String userId = SCCacheUtils.getCache("0", "curUser");
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_square, null);
    }

    @Override
    public void initData() {
        mSquarePresenter = new SquarePresenterImpl(this);
        mSqureAdapter = new SqureAdapter(R.layout.square_list_item_1,mList);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mSqureAdapter);
        mSqureAdapter.notifyDataSetChanged();

        initLoadMoreListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        mSquarePresenter.getListData("",userId,pageIndex, Constants.pageSize,Constants.pullRefress);
    }

    //点击添加内容弹窗
    @OnClick(R.id.im_add_content)
    void showAddContentView(){
        startActivity(new Intent(getActivity(), PublishArticleActivity.class)
                .putExtra("type",1));
//        showContentViewDialog();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        mSquarePresenter.getListData("",userId,pageIndex, Constants.pageSize,Constants.pullRefress);
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
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String listdata = JSONObject.parseObject(data).getString("content");
            if (listdata==null) {
                return;
            }
            List<SqureDataEntity> list = JSONObject.parseArray(listdata,SqureDataEntity.class);
            if(list.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                mList.clear();
                mList.addAll(list);
                recyclerViewCoupon.setAdapter(mSqureAdapter);
                mSqureAdapter.notifyDataSetChanged();
            }else {
                mSqureAdapter.addData(list);
            }

        }
    }

    @Override
    public void setAttentionResponse(String response,int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            updateUserAttention(type);
            if(type==0){
                ToastUtil.showToast(getActivity(),getString(R.string.Concerned));
            }else {
                ToastUtil.showToast(getActivity(), R.string.unfollowed);
            }

        }else {
            ToastUtil.showToast(getActivity(), R.string.operation_failed);
        }
    }

    @Override
    public void setPraiseResponse(String response, int type) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            updateUserPraise(type);
            if(type == 0){
                ToastUtil.showToast(getActivity(), R.string.liked);
            }else {
                ToastUtil.showToast(getActivity(), R.string.cancel_like);
            }
        }else {
            ToastUtil.showToast(getActivity(), R.string.operation_failed);
        }
    }

    //相关监听
    private void initLoadMoreListener() {
        //上拉加载
        recyclerViewCoupon.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mSqureAdapter.getItemCount()){
                    pageIndex ++;
                    mSquarePresenter.getListData("",userId,pageIndex, Constants.pageSize,Constants.pillLoadmore);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        mSqureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SqureDataEntity s = (SqureDataEntity) adapter.getItem(position);
                if(null != s){
                    startActivity(new Intent(getActivity(), ArticleDetailActivity.class).putExtra("info",s));
                }
            }
        });
        //关注
        mSqureAdapter.setIAttentionCallback(new IAttentionCallback() {
            @Override
            public void attentionUser(SqureDataEntity item) {
                mSqureDataEntity = item;
                if(Integer.parseInt(userId) == item.getUserId()){
                    ToastUtil.showToast(getActivity(), R.string.myself_attention);
                    return;
                }
                //未关注
                if("0".equals(mSqureDataEntity.getIsAttention())){
                    mSquarePresenter.attentionUser(Integer.parseInt(userId),item.getUserId());
                }else {//已关注
                    mSquarePresenter.deleteAttentionUser(Integer.parseInt(userId),item.getUserId());
                }
            }
        });
        //点赞
        mSqureAdapter.setIPraiseCallback(new IPraiseCallback() {
            @Override
            public void praiseToArticle(SqureDataEntity item) {
                mSqureDataEntity = item;
                if("0".equals(mSqureDataEntity.getIsPraise())){//未点赞
                    mSquarePresenter.addPraiseToArticle(Integer.parseInt(userId),item.getId());
                }else {
                    mSquarePresenter.deletePraiseToArticle(Integer.parseInt(userId),item.getId());
                }
            }
        });
        //查看大图
        mSqureAdapter.setPhotoCallback(new ICheckBigPhotoCallback() {
            @Override
            public void checkBigPhoto(SqureDataEntity item) {
                if(!TextUtils.isEmpty(item.getListImg())){
                    String attrPhotos[] = item.getListImg().split(",");
                    if(attrPhotos.length>0){
                        List<String> list = Arrays.asList(attrPhotos);
                        ArrayList<String> arrayList = new ArrayList<>(list);
                        startActivity(new Intent(getActivity(), PhotoPagerActivity.class)
                                .putExtra("list", arrayList));
                    }
                }
            }
        });
    }

    //关注成功后更新界面
    private void updateUserAttention(int type){
        if(mSqureDataEntity == null || mList.size()==0)return;
        for (SqureDataEntity s:mList) {
            if(s.getUserId() == mSqureDataEntity.getUserId()){
                if(type==0){
                    s.setIsAttention("1");
                }else {
                    s.setIsAttention("0");
                }
                continue;
            }
        }
        mSqureAdapter.notifyDataSetChanged();
    }
    //点赞成功后更新数据
    private void updateUserPraise(int type){
        if(mSqureDataEntity == null || mList.size()==0)return;
        for (SqureDataEntity s:mList) {
            if(s.getId() == mSqureDataEntity.getId()){
                if(type==0){
                    s.setIsPraise("1");
                    s.setPraiseCount(s.getPraiseCount()+1);
                }else {
                    s.setIsPraise("0");
                    s.setPraiseCount(s.getPraiseCount()-1);
                }
                break;
            }
        }
        mSqureAdapter.notifyDataSetChanged();
    }
}
