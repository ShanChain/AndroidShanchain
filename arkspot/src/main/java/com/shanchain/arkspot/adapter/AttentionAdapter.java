package com.shanchain.arkspot.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.FloorsInfo;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.arkspot.widgets.other.AutoHeightListView;

import java.util.ArrayList;
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
        addItemType(StoryInfo.type1, R.layout.item_story_type1);
        addItemType(StoryInfo.type2, R.layout.item_story_type2);
        addItemType(StoryInfo.type3, R.layout.item_story_type3);
        addItemType(StoryInfo.type4, R.layout.item_story_type4);
    }

    @Override
    protected void convert(BaseViewHolder holder, StoryInfo item) {

        holder.addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more)
                .addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like);

        switch (holder.getItemViewType()) {
            case StoryInfo.type1:
                AutoHeightListView lvItemStory= holder.getView(R.id.lv_item_story);

                ArrayList<FloorsInfo> datas = new ArrayList<>();

                for (int i = 0; i < 3; i ++) {
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
                });
                break;
            case StoryInfo.type2:
                String str = "<font color='blue' >#送行墨子号#@冯提莫</font>";
                Spanned spanned = Html.fromHtml(str);
                String str2 = "\n一千米。\n是千米。\n一百千米。\n一万千米。\n十万千米。\n" +
                        "一百万千米。\n一千万千米。\n一亿千米。\n一亿一千米\n我的天";
                holder.setText(R.id.tv_item_story_content,spanned + str2);
                break;
            case StoryInfo.type3:
                NineGridImageView ngiv = holder.getView(R.id.ngiv_item_story);
                StoryItemNineAdapter itemNineAdapter = new StoryItemNineAdapter();
                ngiv.setAdapter(itemNineAdapter);
                List<Integer> imgs = new ArrayList();
                imgs.add(R.drawable.photo_city);
                imgs.add(R.drawable.photo_bear);
                imgs.add(R.drawable.photo_yue);
                ngiv.setImagesData(imgs);
                break;
            case StoryInfo.type4:

                break;
        }
    }
}
