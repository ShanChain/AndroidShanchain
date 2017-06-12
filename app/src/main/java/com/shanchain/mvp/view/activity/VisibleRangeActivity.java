package com.shanchain.mvp.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shanchain.R;
import com.shanchain.base.BaseActivity;
import com.shanchain.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class VisibleRangeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private static final int CHOOSE_CONTACTS_REQUESTCODE = 10;
    private static final int PUBLIC_RESULTCODE = 100;
    private static final int FRIENDS_RESULTCODE = 200;
    private static final int OWNERSELF_RESULTCODE = 300;
    @Bind(R.id.rb_visible_public)
    RadioButton mRbVisiblePublic;
    @Bind(R.id.ll_visible_public)
    LinearLayout mLlVisiblePublic;
    @Bind(R.id.rb_visible_friends)
    RadioButton mRbVisibleFriends;
    @Bind(R.id.ll_visible_friends)
    LinearLayout mLlVisibleFriends;
    @Bind(R.id.rb_visible_ownself)
    RadioButton mRbVisibleOwnself;
    @Bind(R.id.ll_visible_ownself)
    LinearLayout mLlVisibleOwnself;
    @Bind(R.id.tv_visiable_select_friends)
    TextView mTvVisiableSelectFriends;
    @Bind(R.id.activity_visible_range)
    LinearLayout mActivityVisibleRange;
    private ArthurToolBar mVisibleRangeToolBar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_visible_range;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mVisibleRangeToolBar = (ArthurToolBar) findViewById(R.id.toolbar_visible_range);
        mVisibleRangeToolBar.setBtnEnabled(true,false);
        mVisibleRangeToolBar.setOnLeftClickListener(this);
    }


    @OnClick({R.id.ll_visible_public, R.id.ll_visible_friends, R.id.ll_visible_ownself, R.id.tv_visiable_select_friends})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_visible_public:
                setButtonSelected(0);

                intent.putExtra("visible","公开");
                setResult(PUBLIC_RESULTCODE,intent);
                finish();
                break;
            case R.id.ll_visible_friends:
                setButtonSelected(1);

                intent.putExtra("visible","好友圈");
                setResult(FRIENDS_RESULTCODE,intent);
                finish();

                break;
            case R.id.ll_visible_ownself:
                setButtonSelected(2);

                intent.putExtra("visible","仅对自己可见");
                setResult(OWNERSELF_RESULTCODE,intent);
                finish();

                break;
            case R.id.tv_visiable_select_friends:
                setButtonSelected(3);
                readyGoForResult(ChooseContactsActivity.class,CHOOSE_CONTACTS_REQUESTCODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择可见联系人的结果=======>未实现


    }

    private boolean[] isSelected = {true,false,false};

    private void setButtonSelected(int position) {
        for (int i = 0; i < isSelected.length; i ++) {
            if (position == i){
                isSelected[i] = true;
            }else {
                isSelected[i] = false;
            }
        }
        mRbVisiblePublic.setChecked(isSelected[0]);
        mRbVisibleFriends.setChecked(isSelected[1]);
        mRbVisibleOwnself.setChecked(isSelected[2]);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
