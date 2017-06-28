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
import com.shanchain.shandata.mvp.view.activity.mine.PersonalDataActivity;
import com.shanchain.shandata.mvp.view.activity.mine.SettingActivity;
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

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_mine, null);
    }

    @Override
    public void initData() {
        int bgPath = PrefUtils.getInt(mActivity, "bgPath", R.drawable.mine_bg_spring_default);
        mIvMineBg.setImageResource(bgPath);

        String nickname = PrefUtils.getString(mActivity, "nickname", "");
        if (!TextUtils.isEmpty(nickname)){
            mTvMineNickname.setText(nickname);
        }else {
            mTvMineNickname.setText("默认签名");
        }


        String signature = PrefUtils.getString(mActivity, "signature", "");
        if (!TextUtils.isEmpty(signature)) {
            mTvMineSignature.setText(signature);
        }else {
            mTvMineSignature.setText("因为太个性所以没签名");
        }

    }

    @OnClick({R.id.iv_mine_avatar, R.id.tv_mine_modify, R.id.btn_mine_focus, R.id.btn_mine_fans, R.id.ll_mine_story, R.id.ll_mine_challenge, R.id.ll_mine_cards, R.id.tv_mine_shancurrency, R.id.tv_mine_shanvoucher, R.id.tv_mine_publicwelfare, R.id.tv_mine_reservation, R.id.tv_mine_setup, R.id.tv_mine_commonprobler})
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

                break;
            case R.id.btn_mine_fans:

                break;
            case R.id.ll_mine_story:

                break;
            case R.id.ll_mine_challenge:

                break;
            case R.id.ll_mine_cards:

                break;
            case R.id.tv_mine_shancurrency:

                break;
            case R.id.tv_mine_shanvoucher:

                break;
            case R.id.tv_mine_publicwelfare:

                break;
            case R.id.tv_mine_reservation:

                break;
            case R.id.tv_mine_setup:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_mine_commonprobler:

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
                if (bgPath != 0){
                    Glide.with(this).load(bgPath).into(mIvMineBg);
                }

            }
        }


    }

}
