package com.shanchain.shandata.ui.view.activity.chat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryItemNineAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeetPersonActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_meet_person)
    ArthurToolBar mTbMeetPerson;
    @Bind(R.id.iv_meet_person_img)
    CircleImageView mIvMeetPersonImg;
    @Bind(R.id.tv_meet_person_name)
    TextView mTvMeetPersonName;
    @Bind(R.id.btn_meet_person_chat)
    Button mBtnMeetPersonChat;
    @Bind(R.id.btn_meet_person_focus)
    Button mBtnMeetPersonFocus;
    @Bind(R.id.tv_meet_person_opration)
    TextView mTvMeetPersonOpration;
    @Bind(R.id.tv_meet_person_from)
    TextView mTvMeetPersonFrom;
    @Bind(R.id.ngiv_meet_person)
    NineGridImageView mNgivMeetPerson;
    @Bind(R.id.tv_meet_person_notify)
    TextView mTvMeetPersonNotify;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_meet_person;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {


        StoryItemNineAdapter itemNineAdapter = new StoryItemNineAdapter();
        mNgivMeetPerson.setAdapter(itemNineAdapter);
        List<String> imgs = new ArrayList();
        imgs.add("http://a3.topitme.com/0/1c/12/1128107705fd5121c0l.jpg");
        mNgivMeetPerson.setImagesData(imgs);
    }

    private void initToolBar() {
        mTbMeetPerson.setBtnEnabled(true,false);
        mTbMeetPerson.setOnLeftClickListener(this);
    }

    @OnClick({R.id.iv_meet_person_img, R.id.btn_meet_person_chat, R.id.btn_meet_person_focus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_meet_person_img:
                //头像,进入个人中心

                break;
            case R.id.btn_meet_person_chat:
                //聊天
                Intent chatIntent = new Intent(this,ChatRoomActivity.class);
                startActivity(chatIntent);
                break;
            case R.id.btn_meet_person_focus:
                //关注，添加关注

                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
