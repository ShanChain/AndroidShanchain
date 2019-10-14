package com.shanchain.shandata.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.interfaces.IAttentionCallback;
import com.shanchain.shandata.interfaces.IChatByUserHeartCallback;
import com.shanchain.shandata.interfaces.ICheckBigPhotoCallback;
import com.shanchain.shandata.interfaces.IPraiseCallback;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.ui.view.activity.article.ArticleDetailActivity;
import com.shanchain.shandata.ui.view.activity.article.PhotoPagerActivity;
import com.shanchain.shandata.utils.DisplayImageOptionsUtils;
import com.shanchain.shandata.utils.EmojiUtils;
import com.shanchain.shandata.utils.TimeUtils;
import com.shanchain.shandata.widgets.CustomGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.view.CircleImageView;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :
 */
public class SqureAdapter extends BaseMultiItemQuickAdapter<SqureDataEntity,BaseViewHolder> {
    private IAttentionCallback mIAttentionCallback;
    private IPraiseCallback mIPraiseCallback;
    private ICheckBigPhotoCallback mPhotoCallback;
    private IChatByUserHeartCallback mChatCallback;
    private Context mContext;
    public void setIAttentionCallback(IAttentionCallback callback){
        this.mIAttentionCallback = callback;
    }
    public void setIPraiseCallback(IPraiseCallback callback){
        this.mIPraiseCallback = callback;
    }
    public void setPhotoCallback(ICheckBigPhotoCallback callback){
        this.mPhotoCallback = callback;
    }
    public void setChatCallback(IChatByUserHeartCallback chatCallback){
        this.mChatCallback = chatCallback;
    }
    public SqureAdapter(Context context, @Nullable List<SqureDataEntity> data) {
        super(data);
        this.mContext = context;
        addItemType(0,R.layout.square_list_item_0);
        addItemType(1,R.layout.square_list_item_1);
        addItemType(2,R.layout.square_list_item_2);

    }


    @Override
    protected void convert(BaseViewHolder helper, final SqureDataEntity item) {
        if(item.getItemType() == 0){

        }
        CircleImageView circleImageView = helper.getView(R.id.iv_user_head);
        Glide.with(mContext).load(item.getHeadIcon())
                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .error(R.drawable.aurora_headicon_default)).into(circleImageView);
        helper.setText(R.id.tv_nickname,item.getNickName());
        helper.setText(R.id.tv_time, TimeUtils.friendlyTime1(mContext,new Date(item.getUpdateTime())));
        helper.setText(R.id.tv_create_time,TimeUtils.formatTimeMonth((item.getCreateTime()/1000)+""));
        helper.setText(R.id.et_content, EmojiUtils.utf8ToString(item.getContent()));
        helper.setText(R.id.tv_conin,item.getPraiseCount()+"");
        helper.setText(R.id.tv_message,item.getReviceCount()+"");
        helper.setText(R.id.tv_share,item.getCollectCount()+"");
        RelativeLayout relativeLayout = helper.getView(R.id.rl_bg);
        ImageView ivBg = helper.getView(R.id.im_bg);
        if(item.getItemType()==0){

        }else if(item.getItemType()==1){
            String []attr = item.getListImg().replaceAll("\\\\","").split(",");
            Glide.with(mContext).load(HttpApi.BASE_URL+attr[0])
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.place_image_commen).dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(R.drawable.squrea_bg_shape)).into(ivBg);
            ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
            int with = (int)((DensityUtils.getScreenWidth(mContext)-DensityUtils.dip2px(mContext,50)*2));
            layoutParams.width = with;
            layoutParams.height = with*2/3;
            relativeLayout.setLayoutParams(layoutParams);
        }else {
            CustomGridView gridView = helper.getView(R.id.gv_photo);
            final String []attrPhotos = item.getListImg().replaceAll("\\\\","").split(",");
            GVPhotoAdapter gvPhotoAdapter = new GVPhotoAdapter(mContext);
            gvPhotoAdapter.setPhotoList(Arrays.asList(attrPhotos));
            gridView.setAdapter(gvPhotoAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<String> list = Arrays.asList(attrPhotos);
                    ArrayList<String> arrayList = new ArrayList<>(list);
                    mContext.startActivity(new Intent(mContext, PhotoPagerActivity.class)
                            .putExtra("list", arrayList)
                            .putExtra("position",position));
                }
            });
        }

        if("0".equals(item.getIsPraise())){
            //未点赞
            helper.getView(R.id.iv_paise).setBackgroundResource(R.mipmap.dianzan);
        }else {
            helper.getView(R.id.iv_paise).setBackgroundResource(R.mipmap.dianzan_done);
        }
        TextView textView = helper.getView(R.id.tv_attention);
        //点击头像聊天
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChatCallback!=null){
                    mChatCallback.chatByUserIcon(item);
                }
            }
        });
        if("0".equals(item.getIsAttention())){
            //未关注
            textView.setBackgroundResource(R.drawable.squra_attention_n_shape);
            textView.setTextColor(mContext.getResources().getColor(R.color.login_marjar_color));
            textView.setText(mContext.getResources().getString(R.string.attention));
        }else {
            textView.setBackgroundResource(R.drawable.squra_attent_shape);
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setText(mContext.getResources().getString(R.string.Concerned));
        }
        if(Integer.parseInt(SCCacheUtils.getCacheUserId()) == item.getUserId()){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
        //关注
        helper.getView(R.id.tv_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIAttentionCallback!=null){
                    mIAttentionCallback.attentionUser(item);
                }
            }
        });
        //点赞
        helper.getView(R.id.ll_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIPraiseCallback!=null){
                    mIPraiseCallback.praiseToArticle(item);
                }
            }
        });
        //查看大图
        helper.getView(R.id.im_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotoCallback!=null){
                    mPhotoCallback.checkBigPhoto(item);
                }
            }
        });
    }


}


