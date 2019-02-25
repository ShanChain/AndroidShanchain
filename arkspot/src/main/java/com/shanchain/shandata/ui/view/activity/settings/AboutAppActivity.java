package com.shanchain.shandata.ui.view.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{

    @Bind(R.id.tb_about)
    ArthurToolBar tbAbout;
    @Bind(R.id.iv_app_logo)
    ImageView ivAppLogo;
    @Bind(R.id.tv_marjar_web_home)
    TextView tvMarjarWebHome;
    @Bind(R.id.tv_web_home_link)
    TextView tvWebHomeLink;
    @Bind(R.id.tv_user_rule)
    TextView tvUserRule;
    @Bind(R.id.privacy_policy)
    TextView privacyPolicy;
    @Bind(R.id.tv_copyright)
    TextView tvCopyright;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about_app;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        ArthurToolBar toolBar = findViewById(R.id.tb_about);
        toolBar.setTitleText("");
        toolBar.setOnLeftClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_web_home_link, R.id.tv_user_rule, R.id.privacy_policy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_web_home_link:
                Intent intent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
                JSONObject obj = new JSONObject();
                obj.clear();
                obj.put("url", HttpApi.APP_HOME);
                obj.put("title", "MarJar");
                String webParams = obj.toJSONString();
                intent.putExtra("webParams", webParams);
                startActivity(intent);
                break;
            case R.id.tv_user_rule:
                Intent ruleIntent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
                JSONObject ruleObj = new JSONObject();
                ruleObj.put("url", HttpApi.USER_RULE);
                ruleObj.put("title", "马甲用户协议");
                ruleIntent.putExtra("webParams", ruleObj.toString());
                startActivity(ruleIntent);
                break;
            case R.id.privacy_policy:
                Intent privacyIntent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
                JSONObject privacy = new JSONObject();
                privacy.put("url", HttpApi.PRIVACY_POLICY);
                privacy.put("title", "隐私政策");
                privacyIntent.putExtra("webParams", privacy.toString());
                startActivity(privacyIntent);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
