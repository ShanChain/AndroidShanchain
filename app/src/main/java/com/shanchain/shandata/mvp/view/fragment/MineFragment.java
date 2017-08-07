package com.shanchain.shandata.mvp.view.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.mvp.view.activity.PersonalHomePagerActivity;
import com.shanchain.shandata.mvp.view.activity.mine.AboutActivity;
import com.shanchain.shandata.mvp.view.activity.mine.BackgroundActivity;
import com.shanchain.shandata.mvp.view.activity.mine.CommonProblemActivity;
import com.shanchain.shandata.mvp.view.activity.mine.FansActivity;
import com.shanchain.shandata.mvp.view.activity.mine.FocusActivity;
import com.shanchain.shandata.mvp.view.activity.mine.LifeLabelActivity;
import com.shanchain.shandata.mvp.view.activity.mine.MyChallengeActivity;
import com.shanchain.shandata.mvp.view.activity.mine.MyPublicWelfareActivity;
import com.shanchain.shandata.mvp.view.activity.mine.MyReservationActivity;
import com.shanchain.shandata.mvp.view.activity.mine.MyStoryActivity;
import com.shanchain.shandata.mvp.view.activity.mine.PersonalDataActivity;
import com.shanchain.shandata.mvp.view.activity.mine.SettingActivity;
import com.shanchain.shandata.mvp.view.activity.mine.ShanCoinsActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.utils.PrefUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhoujian on 2017/5/19.
 * 我的页面
 */

public class MineFragment extends BaseFragment {


    private static final int REQUEST_CODE_PERSONAL_INFO = 10;
    @Bind(R.id.iv_mine_bg)
    ImageView mIvMineBg;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.iv_mine_avatar)
    ImageView mIvMineAvatar;
    @Bind(R.id.tv_mine_modify)
    TextView mTvMineModify;
    @Bind(R.id.tv_mine_nickname)
    TextView mTvMineNickname;
    @Bind(R.id.btn_mine_focus)
    TextView mBtnMineFocus;
    @Bind(R.id.btn_mine_fans)
    TextView mBtnMineFans;
    @Bind(R.id.ll_mine_story)
    LinearLayout mLlMineStory;
    @Bind(R.id.ll_mine_challenge)
    LinearLayout mLlMineChallenge;
    @Bind(R.id.ll_mine_cards)
    LinearLayout mLlMineCards;
    @Bind(R.id.tv_mine_shancurrency)
    TextView mTvMineShancurrency;
    @Bind(R.id.tv_mine_shanvoucher)
    TextView mTvMineShanvoucher;
    @Bind(R.id.tv_mine_publicwelfare)
    TextView mTvMinePublicwelfare;
    @Bind(R.id.tv_mine_reservation)
    TextView mTvMineReservation;
    @Bind(R.id.tv_mine_setup)
    TextView mTvMineSetup;
    @Bind(R.id.tv_mine_commonprobler)
    TextView mTvMineCommonprobler;
    @Bind(R.id.ll_fragment_mine)
    LinearLayout mLlFragmentMine;
    @Bind(R.id.tv_mine_signature)
    TextView mTvMineSignature;
    @Bind(R.id.tv_mine_life_label)
    TextView mTvMineLifeLabel;
    @Bind(R.id.tv_mine_about)
    TextView mTvMineAbout;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_mine, null);
    }

    @Override
    public void initData() {
        int bgPath = PrefUtils.getInt(mActivity, "bgPath", R.drawable.mine_bg_spring_default);
        mIvMineBg.setImageResource(bgPath);

        String nickname = PrefUtils.getString(mActivity, "nickname", "");
        if (!TextUtils.isEmpty(nickname)) {
            mTvMineNickname.setText(nickname);
        } else {
            mTvMineNickname.setText("默认签名");
        }


        String signature = PrefUtils.getString(mActivity, "signature", "");
        if (!TextUtils.isEmpty(signature)) {
            mTvMineSignature.setText(signature);
        } else {
            mTvMineSignature.setText("因为太个性所以没签名");
        }
        String headImageUrlLarge = "http://q.qlogo.cn/qqapp/1106258060/D74BE50E6729D32C37164B5CA9C47BF7/100";
        Glide.with(this).load(headImageUrlLarge)
                .transform(new GlideCircleTransform(mActivity))
                .into(mIvMineAvatar);

    }

    @OnClick({R.id.iv_mine_avatar, R.id.tv_mine_modify, R.id.btn_mine_focus, R.id.btn_mine_fans,
            R.id.ll_mine_story, R.id.ll_mine_challenge, R.id.ll_mine_cards, R.id.tv_mine_shancurrency,
            R.id.tv_mine_shanvoucher, R.id.tv_mine_publicwelfare, R.id.tv_mine_reservation,
            R.id.tv_mine_setup, R.id.tv_mine_commonprobler,R.id.tv_mine_life_label,R.id.tv_mine_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mine_avatar:
                Intent intent = new Intent(mActivity, PersonalHomePagerActivity.class);
                intent.putExtra("isFriend", false);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_mine_modify:
                modifyInfo();
                break;
            case R.id.btn_mine_focus:
                startActivity(new Intent(mActivity, FocusActivity.class));
                break;
            case R.id.btn_mine_fans:
                startActivity(new Intent(mActivity, FansActivity.class));
                break;
            case R.id.ll_mine_story:
                intent = new Intent(mActivity, MyStoryActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_mine_challenge:
                startActivity(new Intent(mActivity, MyChallengeActivity.class));
                break;
            case R.id.ll_mine_cards:
                Intent intentCards = new Intent(mActivity, BackgroundActivity.class);
                intentCards.putExtra("isBg", false);
                startActivity(intentCards);
                break;
            case R.id.tv_mine_shancurrency:
                Intent intentShancoins = new Intent(mActivity, ShanCoinsActivity.class);
                intentShancoins.putExtra("isShanCoins", true);
                startActivity(intentShancoins);
                break;
            case R.id.tv_mine_shanvoucher:
                Intent intentShanvoucher = new Intent(mActivity, ShanCoinsActivity.class);
                intentShanvoucher.putExtra("isShanCoins", false);
                startActivity(intentShanvoucher);
                break;
            case R.id.tv_mine_publicwelfare:
                startActivity(new Intent(mActivity, MyPublicWelfareActivity.class));

                break;
            case R.id.tv_mine_reservation:
                startActivity(new Intent(mActivity, MyReservationActivity.class));
                break;
            case R.id.tv_mine_setup:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_mine_commonprobler:
                startActivity(new Intent(mActivity, CommonProblemActivity.class));
                break;
            case R.id.tv_mine_life_label:
                startActivity(new Intent(mActivity, LifeLabelActivity.class));
                break;
            case R.id.tv_mine_about:
                startActivity(new Intent(mActivity, AboutActivity.class));
                break;
        }
    }

    private void modifyInfo() {
        Intent intent = new Intent(mActivity, PersonalDataActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PERSONAL_INFO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PERSONAL_INFO) {
            if (data != null) {
                String avatarPath = data.getStringExtra("avatarPath");
                if (avatarPath != null) {
                    Glide.with(mActivity)
                            .load(avatarPath)
                            .transform(new GlideCircleTransform(mActivity))
                            .into(mIvMineAvatar);
                }
                String nickname = data.getStringExtra("nickname");
                if (nickname != null) {
                    mTvMineNickname.setText(nickname);
                }

                String signature = data.getStringExtra("signature");
                if (signature != null) {
                    mTvMineSignature.setText(signature);
                }

                int bgPath = data.getIntExtra("bgPath", 0);
                if (bgPath != 0) {
                    Glide.with(this).load(bgPath).into(mIvMineBg);
                }

            }
        }


    }

}
