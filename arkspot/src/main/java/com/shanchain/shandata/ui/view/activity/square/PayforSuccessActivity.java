package com.shanchain.shandata.ui.view.activity.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.ShareBean;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.utils.ManagerUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;

/**
 * Created by WealChen
 * Date : 2019/8/8
 * Describe :支付成功回调界面
 */
public class PayforSuccessActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_register)
    ArthurToolBar mTbRegister;
    @Bind(R.id.iv_scode)
    ImageView ivScode;
    @Bind(R.id.tv_code_num)
    TextView tvCodeNum;
    @Bind(R.id.btn_reset_sure)
    Button btnResetSure;
    @Bind(R.id.tv_go_mining)
    TextView tvGoMining;

    private CustomDialog shareBottomDialog;
    private ShareParams redPaperParams;
    private ShareBean shareBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(PayforSuccessActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            closeLoadingDialog();
            shareBottomDialog.dismiss();
        }
    };
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_payfor_success;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        shareBean = (ShareBean) getIntent().getSerializableExtra("info");
        if(shareBean!=null){
            tvCodeNum.setText(shareBean.getInviteCode());
            Bitmap bitmap = CodeUtils.createImage(shareBean.getInviteCode(), 400, 400, null);
            ivScode.setImageBitmap(bitmap);
        }

    }

    private void initToolBar() {
        mTbRegister.setTitleTextColor(Color.BLACK);
        mTbRegister.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mTbRegister.setOnLeftClickListener(this);
        mTbRegister.setTitleText(getString(R.string.payfor_success_1));

        shareBottomDialog = new CustomDialog(PayforSuccessActivity.this,
                true, true, 1.0,
                R.layout.layout_bottom_share, new int[]{R.id.share_image,
                R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlWeibo, R.id.share_close});
//        shareBottomDialog.setShareBitmap(ImageUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.red_package)), true);
        shareBottomDialog.setCanceledOnTouchOutside(true);

        initListener();
    }

    //立即分享
    @OnClick(R.id.btn_reset_sure)
    void shareToOther(){
        shareBottomDialog.show();
    }
    //跳过直接进入矿区
    @OnClick(R.id.tv_go_mining)
    void gotoMining(){
        startActivity(new Intent(PayforSuccessActivity.this,MyGroupActivity.class).putExtra("type",2));
        finish();
    }

    //分享监听
    private void initListener(){
        shareBottomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                String shareUrl = HttpApi.BASE_URL_WALLET+"/join?"+"inviteUserId="+shareBean.getInviteUserId()+"&diggingsId="+shareBean.getDiggingsId()
                        +"&inviteCode="+shareBean.getInviteCode();
                redPaperParams = new ShareParams();
                redPaperParams.setTitle(getString(R.string.app_name));
                redPaperParams.setText(getString(R.string.invate_y_join));
                redPaperParams.setUrl(shareUrl);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                redPaperParams.setImageData(bitmap);
                switch (view.getId()) {
                    case R.id.mRlWechat://微信
                        if(!ManagerUtils.uninstallSoftware(PayforSuccessActivity.this,"com.tencent.mm")){
                            ToastUtils.showToast(PayforSuccessActivity.this, R.string.install_wechat);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到微信平台。
                        JShareInterface.share(Wechat.Name, redPaperParams, shareListener);
                        break;
                    case R.id.mRlWeixinCircle://朋友圈
                        if(!ManagerUtils.uninstallSoftware(PayforSuccessActivity.this,"com.tencent.mm")){
                            ToastUtils.showToast(PayforSuccessActivity.this, R.string.install_wechat);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到朋友圈平台。
                        JShareInterface.share(WechatMoments.Name, redPaperParams, shareListener);
                        break;
                    case R.id.mRlQQ://QQ
                        if(!ManagerUtils.uninstallSoftware(PayforSuccessActivity.this,"com.tencent.mobileqq")){
                            ToastUtils.showToast(PayforSuccessActivity.this, R.string.install_qq);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到QQ平台。
                        JShareInterface.share(QQ.Name, redPaperParams, shareListener);
                        break;
                    case R.id.share_close:
                        shareBottomDialog.dismiss();
                        break;

                }
            }
        });

        shareBottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                closeLoadingDialog();
            }
        });
    }

    private PlatActionListener shareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                handler.sendMessage(message);
            }

        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Logger.e(TAG, "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享失败:" + error.getMessage() + "---" + errorCode;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享取消";
                handler.sendMessage(message);
            }
        }
    };

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
