package com.shanchain.shandata.ui.view.activity.mine;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.rn.activity.SCWebViewXYActivity;
import com.shanchain.shandata.ui.model.InvationBean;
import com.shanchain.shandata.ui.presenter.ReturnInvationPresenter;
import com.shanchain.shandata.ui.presenter.impl.ReturnInvationPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintNewActivity;
import com.shanchain.shandata.ui.view.activity.mine.view.ReturnInvationView;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.InvationFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamGetFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/8/22
 * Describe :邀请返佣
 */
public class ReturnInvationActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnLeftClickListener, ReturnInvationView {
    @Bind(R.id.tb_coupon)
    ArthurToolBar toolBar;
    @Bind(R.id.tv_invation_code)
    TextView tvInvationCode;
    @Bind(R.id.tv_fz_lilnk)
    TextView tvFzLilnk;
    @Bind(R.id.tv_invation_link)
    TextView tvInvationLink;
    @Bind(R.id.tv_fz_code)
    TextView tvFzCode;
    @Bind(R.id.tv_nums)
    TextView tvNums;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_proportion)
    TextView tvProportion;
    @Bind(R.id.tab_task)
    TabLayout tabTask;
    @Bind(R.id.vp_invation)
    ViewPager vpInvation;
    @Bind(R.id.tv_dengji)
    TextView tvDengji;
    private String imgURl;//图片的URL地址
    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片

    private InvationBean invationBean;
    private ReturnInvationPresenter mPresenter;
    private List<Fragment> fragmentList = new ArrayList();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_invation_return;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        setFragment();
    }

    //初始化标题栏
    private void initToolBar() {
        toolBar.setTitleText(getResources().getString(R.string.yaoq_fanyong));
        toolBar.setRightText(getString(R.string.rebate_rules));
        toolBar.setOnLeftClickListener(this);
        toolBar.setOnRightClickListener(this);

        mPresenter = new ReturnInvationPresenterImpl(this);

        mPresenter.getInvationDataFromUser(SCCacheUtils.getCacheUserId());
    }

    private void setFragment(){
        String[] titles = {getString(R.string.invation_record), getString(R.string.rebate_record)};
        fragmentList.add(InvationFragment.getInstance(1));
        fragmentList.add(InvationFragment.getInstance(2));
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        vpInvation.setOffscreenPageLimit(2);
        vpInvation.setAdapter(adapter);
        tabTask.setupWithViewPager(vpInvation);
    }
    //复制邀请码
    @OnClick(R.id.tv_fz_code)
    void fzInvateCode(){
        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(invationBean.getUserId());
        ToastUtils.showToast(this, R.string.invation_copy);
    }

    //复制邀链接
    @OnClick(R.id.tv_fz_lilnk)
    void fzInvateLink(){
        //显示图片二维码
        if(invationBean!=null && !TextUtils.isEmpty(invationBean.getInviteCodeImg())){
            startActivity(new Intent(ReturnInvationActivity.this,ViewImageActivity.class).putExtra("url",invationBean.getInviteCodeImg()));
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //返佣规则
        Intent intent = new Intent(ReturnInvationActivity.this, SCWebViewXYActivity.class);
        JSONObject obj = new JSONObject();
        obj.put("url", HttpApi.BASE_URL_WALLET+"/promotionaward");
        String webParams = obj.toJSONString();
        intent.putExtra("webParams", webParams);
        startActivity(intent);
    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setInvationDataResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            invationBean = SCJsonUtils.parseObj(data,InvationBean.class);
            if(invationBean!=null){
                tvNums.setText(invationBean.getAcceptUserCount()+"");
                tvMoney.setText(invationBean.getFrozenCoin());
                tvProportion.setText(invationBean.getBrokerageNotFrozenCoin());
                tvInvationCode.setText("邀请码:"+invationBean.getUserId());
                tvDengji.setText(invationBean.getAccountLevel());
            }else{
                noDataTip();
            }
        }
    }

    @Override
    public void setQuearyInvatRecordResponse(String response, int pullType) {

    }


    //无数据弹窗提示
    private void noDataTip(){
        final StandardDialog standardDialog = new StandardDialog(ReturnInvationActivity.this);
        standardDialog.setStandardTitle("提示");
        standardDialog.setStandardMsg("您还没有成为仿生挖矿矿工，请先去创建或者加入矿区");
        standardDialog.setSureText(getString(R.string.commit_payfor));
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                finish();
            }
        }, new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                finish();
            }
        });
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                standardDialog.show();
                TextView msgTextView = standardDialog.findViewById(R.id.dialog_msg);
                msgTextView.setTextSize(18);
            }
        });
    }
}
