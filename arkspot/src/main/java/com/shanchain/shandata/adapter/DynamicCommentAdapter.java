package com.shanchain.shandata.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.CommentBean;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.widgets.scrollView.SlidingButtonView;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/4.
 */

public class DynamicCommentAdapter extends BaseQuickAdapter<BdCommentBean, BaseViewHolder> implements SlidingButtonView.IonSlidingButtonListener {

    private Drawable likeDefault;
    private Drawable likeSelected;

    boolean isScroll = false;
    private  SlidingButtonView slidingButtonView;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private BaseViewHolder baseViewHolder;
    private BdCommentBean bdCommentBean;
    private SlidingButtonView mMenu = null;

    public void setScrollEnable(boolean isScroll) {
        this.isScroll = isScroll;

    }

    public DynamicCommentAdapter(@LayoutRes int layoutResId, @Nullable List<BdCommentBean> data,Context mContext) {
        super(layoutResId, data);
        this.mIDeleteBtnClickListener = (IonSlidingViewClickListener) mContext;
    }

    @Override
    protected void convert(final BaseViewHolder helper, BdCommentBean item) {
        this.baseViewHolder = helper;
        this.bdCommentBean = item;
        helper.setText(R.id.tv_item_dynamic_comment, item.getCommentBean().getContent());
        helper.setText(R.id.tv_item_dynamic_comment_time, DateUtils.formatFriendly(new Date(item.getCommentBean().getCreateTime())));
        helper.addOnClickListener(R.id.tv_item_comment_like)
        .addOnClickListener(R.id.iv_item_dynamic_comment_avatar);
        helper.setText(R.id.tv_item_dynamic_comment_name,item.getContactBean().getName());
        TextView tvLike = helper.getView(R.id.tv_item_comment_like);
        tvLike.setText("" + item.getCommentBean().getSupportCount());
        ImageView ivHead = helper.getView(R.id.iv_item_dynamic_comment_avatar);

        //获取滑动删除按钮
        TextView commentDelete = helper.getView(R.id.tv_item_dynamic_comment_delete);
        //设置删除监听
        commentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = helper.getLayoutPosition()-1;
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);

            }
        });

        //获取SlidingButtonView
        slidingButtonView = (SlidingButtonView) helper.getConvertView();
        slidingButtonView.setSlidingButtonListener(DynamicCommentAdapter.this);
        //删除自己
        int characterId =  bdCommentBean.getCommentBean().getCharacterId();
        if (characterId!=Integer.valueOf(SCCacheUtils.getCacheCharacterId())){
            this.slidingButtonView.setCanTouch(false);
        }

        //获取外部LinearLayout
        ViewGroup  layoutContent = helper.getView(R.id.iv_item_dynamic_comment_show);
        ViewGroup  relativeContent = helper.getView(R.id.rl_left);


        //设置内容布局的宽为屏幕宽度

        layoutContent.getLayoutParams().width = DensityUtils.getScreenWidth(mContext) + relativeContent.getLayoutParams().width;

//        LogUtils.d("项：" + position + "是否开:" + allopen);
/*        if (!isScroll) {
            LogUtils.d("关闭滑动功能");
            slidingButtonView.openMenu();
            slidingButtonView.setCanTouch(false);
        } else {
            slidingButtonView.closeMenu();
            slidingButtonView.setCanTouch(true);
        }*/




        GlideUtils.load(mContext,item.getContactBean().getHeadImg(),ivHead,0);
        if (likeDefault == null) {
            likeDefault = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_default);
        }

        if (likeSelected == null){
            likeSelected = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_selected);
        }

        likeDefault.setBounds(0,0,likeDefault.getMinimumWidth(),likeDefault.getMinimumHeight());
        likeSelected.setBounds(0,0,likeSelected.getMinimumWidth(),likeSelected.getMinimumHeight());

        tvLike.setCompoundDrawables(null,null,item.getCommentBean().isMySupport()?likeSelected:likeDefault,null);

    }

    //关闭滑动功能


    @Override
    public Boolean setMenuCanTouch(SlidingButtonView slidingButtonView, Boolean isTouch) {

        return null;
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;

    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
        int characterId =  bdCommentBean.getCommentBean().getCharacterId();
        if (characterId!=Integer.valueOf(SCCacheUtils.getCacheCharacterId())){
            this.slidingButtonView.setCanTouch(false);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (mMenu==null){
            return;
        }
        mMenu.closeMenu();
        mMenu = null;

    }

    public SlidingButtonView getSlidingButtonView() {
        return slidingButtonView;
    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }

    public void addData(List mDatas,int position) {
        mDatas.add(position, "添加项");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
//        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

}
