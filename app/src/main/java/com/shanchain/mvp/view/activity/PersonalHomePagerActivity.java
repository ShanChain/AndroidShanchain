package com.shanchain.mvp.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.R;
import com.shanchain.adapter.HotAdapter;
import com.shanchain.adapter.PersonalStoryImagesAdapter;
import com.shanchain.base.BaseActivity;
import com.shanchain.mvp.model.PublisherInfo;
import com.shanchain.mvp.model.StoryInfo;
import com.shanchain.utils.GlideCircleTransform;
import com.shanchain.utils.LogUtils;
import com.shanchain.utils.SpanUtils;
import com.shanchain.utils.ToastUtils;
import com.shanchain.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

public class PersonalHomePagerActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, View.OnClickListener {


    @Bind(R.id.rv_personal_list)
    XRecyclerView mRvPersonalList;
    @Bind(R.id.activity_personal_home_pager)
    LinearLayout mActivityPersonalHomePager;
    private ArthurToolBar mPersonalToolBar;
    private List<PublisherInfo> mDatas;
    private ArrayList<String> mImages;
    private View mHeadView;

    private ImageView mIvHeaderPersonalAvatar;
    private TextView mTvHeaderPersonalName;
    private TextView mTvHeaderPersonalFocus;
    private TextView mTvHeaderPersonalFans;
    private Button mBtnHeaderPersonalFocus;
    private RelativeLayout mRlHeaderPersonalStory;
    private ImageView mIvHeaderPersonalStory;
    private RecyclerView mXrvImagesStory;
    private LinearLayout mLlHeaderPersonalShanyuan;
    private TextView mTvPersonalShanyuan;
    private LinearLayout mLlHeaderPersonalShanquan;
    private TextView mTvPersonalShanquan;
    private LinearLayout mLlHeaderPersonalStory;
    private TextView mTvPersonalStory;
    private LinearLayout mLlHeaderPersonalChallenge;
    private TextView mTvPersonalChallenge;
    private List<StoryInfo> storyDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_personal_home_pager;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initHeadView();
        initRecyclerView();
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this)
                .inflate(R.layout.headview_personal_homepager,(ViewGroup)findViewById(android.R.id.content),false);

        mIvHeaderPersonalAvatar = (ImageView) mHeadView.findViewById(R.id.iv_header_personal_avatar);
        mTvHeaderPersonalName = (TextView) mHeadView.findViewById(R.id.tv_header_personal_name);
        mTvHeaderPersonalFocus = (TextView) mHeadView.findViewById(R.id.tv_header_personal_focus);
        mTvHeaderPersonalFans = (TextView) mHeadView.findViewById(R.id.tv_header_personal_fans);
        mBtnHeaderPersonalFocus = (Button) mHeadView.findViewById(R.id.btn_header_personal_focus);
        mRlHeaderPersonalStory = (RelativeLayout) mHeadView.findViewById(R.id.rl_header_personal_story);
        mIvHeaderPersonalStory = (ImageView) mHeadView.findViewById(R.id.iv_header_personal_story);
        mXrvImagesStory = (RecyclerView) mHeadView.findViewById(R.id.xrv_images_story);
        mLlHeaderPersonalShanyuan = (LinearLayout) mHeadView.findViewById(R.id.ll_header_personal_shanyuan);
        mTvPersonalShanyuan = (TextView) mHeadView.findViewById(R.id.tv_personal_shanyuan);
        mLlHeaderPersonalShanquan = (LinearLayout) mHeadView.findViewById(R.id.ll_header_personal_shanquan);
        mTvPersonalShanquan = (TextView) mHeadView.findViewById(R.id.tv_personal_shanquan);
        mLlHeaderPersonalStory = (LinearLayout) mHeadView.findViewById(R.id.ll_header_personal_story);
        mTvPersonalStory = (TextView) mHeadView.findViewById(R.id.tv_personal_story);
        mLlHeaderPersonalChallenge = (LinearLayout) mHeadView.findViewById(R.id.ll_header_personal_challenge);
        mTvPersonalChallenge = (TextView) mHeadView.findViewById(R.id.tv_personal_challenge);

        initheadData();
        initHeadXrecyclerView();
        initHeadListener();

    }

    private void initHeadListener() {
        mBtnHeaderPersonalFocus.setOnClickListener(this);
        mRlHeaderPersonalStory.setOnClickListener(this);
        mLlHeaderPersonalShanyuan.setOnClickListener(this);
        mLlHeaderPersonalShanquan.setOnClickListener(this);
        mLlHeaderPersonalStory.setOnClickListener(this);
        mLlHeaderPersonalChallenge.setOnClickListener(this);
    }

    private void initHeadXrecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvImagesStory.setLayoutManager(linearLayoutManager);


       /* mXrvImagesStory.addItemDecoration(new RecyclerViewDivider(PersonalHomePagerActivity.this,
                LinearLayoutManager.VERTICAL, DensityUtils.dip2px(PersonalHomePagerActivity.this,10),
                getResources().getColor(R.color.colorAddFriendDivider)));*/


        PersonalStoryImagesAdapter personalStoryImagesAdapter = new PersonalStoryImagesAdapter(this, R.layout.item_story_images, storyDatas);
        mXrvImagesStory.setAdapter(personalStoryImagesAdapter);

        personalStoryImagesAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                readyGo(DetailsActivity.class);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initheadData() {
        storyDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.setVoteCounts(i * 20);
            storyInfo.setImageUrl("");
            storyDatas.add(storyInfo);
        }

        Glide.with(mContext).load(R.drawable.photo4).transform(new GlideCircleTransform(PersonalHomePagerActivity.this)).into(mIvHeaderPersonalAvatar);
        mTvHeaderPersonalName.setText("李江宇");

        mTvHeaderPersonalFocus.setText(SpanUtils.buildSpannableString("关注   20",getResources().getColor(R.color.colorBtn),0,2));
        mTvHeaderPersonalFans.setText(SpanUtils.buildSpannableString("粉丝   37",getResources().getColor(R.color.colorBtn),0,2));

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPersonalList.setLayoutManager(linearLayoutManager);
        mRvPersonalList.setPullRefreshEnabled(false);
        mRvPersonalList.setLoadingMoreEnabled(false);

        mRvPersonalList.addHeaderView(mHeadView);
        HotAdapter adapter = new HotAdapter(this, R.layout.item_hot, mDatas);
        mRvPersonalList.setAdapter(adapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {

            mImages = new ArrayList<>();
            Random random = new Random();
            for (int j = 0; j < random.nextInt(9); j++) {
                LogUtils.d("随机图片的数量 :" + j);
                mImages.add("" + j);
            }

            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("张建 " + i);
            publisherInfo.setTime(i + "分钟前");
            publisherInfo.setDes("今天很开心!");
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setImages(mImages);
            publisherInfo.setType(1);
            mDatas.add(publisherInfo);
        }

        for (int i = 0; i < 8; i++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setName("石家海" + i);
            publisherInfo.setTime("2" + i + "分钟前");
            publisherInfo.setDes("哈哈哈哈哈!");
            if (i == 3) {
                publisherInfo.setStroyImgUrl("");
            } else {
                publisherInfo.setStroyImgUrl(i + ".png");
            }
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setType(3);
            publisherInfo.setIconUrl(i + "icon.png");
            publisherInfo.setTitle("故事标题" + i);
            publisherInfo.setActiveDes("活动描述信息" + i);
            publisherInfo.setOtherDes("今天有" + i * 2 + 3 + "人也在愉快的玩耍");
            mDatas.add(publisherInfo);
        }


        for (int i = 0; i < 6; i++) {
            PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setType(2);
            publisherInfo.setName("李江宇" + i);
            publisherInfo.setTime("3" + i + "分钟前");
            publisherInfo.setDes("#善数者正式发布#" + i + "我自定义了一个挑战任务,要不要和我一起来?");
            publisherInfo.setIconUrl(i + ".png");
            publisherInfo.setTitle("寻找神秘点");
            publisherInfo.setLikes(new Random().nextInt(1000));

            publisherInfo.setComments(new Random().nextInt(400));
            publisherInfo.setChallegeTime("5月" + i + "日");
            publisherInfo.setAddr("深圳市");
            publisherInfo.setActiveDes("假期放松一下,率先找到神秘地点打卡者获胜。");
            publisherInfo.setOtherDes("一起来试试吧");
            mDatas.add(publisherInfo);
        }
    }

    private void initToolBar() {
        mPersonalToolBar = (ArthurToolBar) findViewById(R.id.toolbar_personal_homepager);
        mPersonalToolBar.setBtnEnabled(true, false);
        mPersonalToolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_header_personal_focus:

                ToastUtils.showToast(this, "关注");

                break;
            case R.id.rl_header_personal_story:
                readyGo(PersonalStoryActivity.class);
                break;
            case R.id.ll_header_personal_shanyuan:
                readyGo(ShanYuanActivity.class);
                break;
            case R.id.ll_header_personal_shanquan:
                ToastUtils.showToast(this, "善券");
                break;
            case R.id.ll_header_personal_story:
                readyGo(PersonalStoryActivity.class);
                break;
            case R.id.ll_header_personal_challenge:
                readyGo(PersonalChallengeActivity.class);
                break;

        }
    }
}
