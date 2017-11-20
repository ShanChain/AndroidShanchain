package com.shanchain.shandata.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryListDataBean;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.Date;
import java.util.List;


public class AttentionAdapter extends BaseMultiItemQuickAdapter<StoryInfo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AttentionAdapter(List<StoryInfo> data) {
        super(data);
        addItemType(StoryInfo.type1, R.layout.item_story_type3);
        addItemType(StoryInfo.type2, R.layout.item_story_type2);
        addItemType(StoryInfo.type3, R.layout.item_story_type4);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoryInfo item) {
        StoryListDataBean storyListDataBean = item.getStoryListDataBean();

        holder.setText(R.id.tv_item_story_name,storyListDataBean.getInfo().getName());
        ImageView ivAvatar = holder.getView(R.id.iv_item_story_avatar);
        GlideUtils.load(mContext,storyListDataBean.getInfo().getHeadImg(),ivAvatar,0);
        String time = DateUtils.formatFriendly(new Date(storyListDataBean.getUpdateTime()));
        int commentCount = storyListDataBean.getCommentCount();
        holder.setText(R.id.tv_item_story_comment,commentCount + "");
        holder.setText(R.id.tv_item_story_time,time);
        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                holder.setVisible(R.id.tv_item_story_forwarding,true);
                String intro = storyListDataBean.getIntro();
                NineGridImageView nineGridImageView = holder.getView(R.id.ngiv_item_story);
                nineGridImageView.setVisibility(View.GONE);
                /*AutoHeightListView lvItemStory = holder.getView(R.id.lv_item_story);
                ArrayList<FloorsInfo> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    FloorsInfo floorsInfo = new FloorsInfo();
                    datas.add(floorsInfo);
                }

                StoryItemFloorsAdapter floorsAdapter = new StoryItemFloorsAdapter(datas);
                lvItemStory.setAdapter(floorsAdapter);
                lvItemStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mContext, DynamicDetailsActivity.class);
                        mContext.startActivity(intent);
                    }
                });*/

                holder.setText(R.id.tv_item_story_content,storyListDataBean.getIntro());

                /*NineGridImageView ngiv = holder.getView(R.id.ngiv_item_story);
                StoryItemNineAdapter itemNineAdapter = new StoryItemNineAdapter();
                ngiv.setAdapter(itemNineAdapter);
                List<Integer> imgs = new ArrayList<>();
                imgs.add(R.drawable.photo_bear);
                imgs.add(R.drawable.photo_city);
                ngiv.setImagesData(imgs);*/
                break;
            case StoryInfo.type2:
                holder.setText(R.id.tv_item_story_content,storyListDataBean.getIntro());
                holder.setVisible(R.id.tv_item_story_forwarding,false);
                break;
            case StoryInfo.type3:
                holder.setVisible(R.id.tv_item_story_forwarding,false);
                break;
        }
    }
}
