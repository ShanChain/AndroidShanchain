package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.cache.SharedPreferencesUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.interfaces.IUpdateUserHeadCallback;
import com.shanchain.shandata.rn.activity.SCWebViewActivity;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.InvationBean;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.model.ShareBean;
import com.shanchain.shandata.ui.presenter.MinePresenter;
import com.shanchain.shandata.ui.presenter.impl.MinePresenterImpl;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CreateCouponActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MyMessageActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.activity.mine.ReturnInvationActivity;
import com.shanchain.shandata.ui.view.activity.settings.ClientListActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.square.PayforSuccessActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MineView;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :我的
 */
public class MineFragment extends BaseFragment implements MineView {
    @Bind(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_userdescipt)
    TextView tvUserdescipt;
    @Bind(R.id.im_edit)
    ImageView imEdit;
    @Bind(R.id.ll_may_wallet)
    LinearLayout llMayWallet;
    @Bind(R.id.ll_counp)
    LinearLayout llCounp;
    @Bind(R.id.ll_comunity)
    LinearLayout llComunity;
    @Bind(R.id.ll_message)
    LinearLayout llMessage;
    @Bind(R.id.ll_setting)
    LinearLayout llSetting;
    @Bind(R.id.ll_client)
    LinearLayout llClient;

    private MinePresenter mMinePresenter;
    private String photoPath = "";
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_mine_new, null);
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String obj = (String) msg.obj;
                    mMinePresenter.modifyUserInfo(obj);
                    break;
                case 2:
                    /*Intent intent=new Intent();
                    intent.setAction("com.my.sentborad");
                    intent.putExtra("url",currentPath);
                    getActivity().sendBroadcast(intent);*/
                    break;
            }
        }
    };


    @Override
    public void initData() {
        mMinePresenter = new MinePresenterImpl(this);
        initUserData();

    }

    //设置用户基本信息
    private void initUserData(){
        CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
        if(characterInfo!=null){
            String nikeName = characterInfo.getName() != null ? characterInfo.getName() : "";
            String signature = characterInfo.getSignature() != null ? characterInfo.getSignature() : "";
            final String headImg = characterInfo.getHeadImg() != null ? characterInfo.getHeadImg() : "";
            if (!TextUtils.isEmpty(nikeName)) {
                tvUsername.setText(nikeName);
            }else {
                tvUsername.setText(characterInfo.getUserId()+"");
            }
            if (!TextUtils.isEmpty(signature)) {
                tvUserdescipt.setText(signature);
            }
            if (!TextUtils.isEmpty(headImg)) {
                Glide.with(this).load(headImg)
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ivUserHead);
            }else {
                UserInfo userInfo = JMessageClient.getMyInfo();
                Glide.with(this).load(userInfo.getAvatarFile().getAbsolutePath())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                .error(R.drawable.aurora_headicon_default)).into(ivUserHead);
            }
        }
    }

    //修改名称页面修改之后更新我的页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(ModifyUserInfo modifyUserInfo){
        if(modifyUserInfo!=null){
            if(!TextUtils.isEmpty(modifyUserInfo.getName())){
                tvUsername.setText(modifyUserInfo.getName());
            }
            if(!TextUtils.isEmpty(modifyUserInfo.getSignature())){
                tvUserdescipt.setText(modifyUserInfo.getSignature());
            }
            if(!TextUtils.isEmpty(modifyUserInfo.getHeadImg())){
                Glide.with(this)
                        .load(modifyUserInfo.getHeadImg())
                        .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                        .error(R.drawable.aurora_headicon_default))
                        .into(ivUserHead);
            }
        }
    }


    //编辑用户签名
    @OnClick(R.id.im_edit)
    void editUserInfo(){
        Intent intent = new Intent(getActivity(), ModifyUserInfoActivity.class);
        String nikeName = tvUsername.getText().toString();
        String userSign = tvUserdescipt.getText().toString();
        intent.putExtra("nikeName", nikeName);
        intent.putExtra("userSign", userSign);
        startActivity(intent);
    }

    //我的钱包
    @OnClick(R.id.ll_may_wallet)
    void myWallet(){
        Intent intent = new Intent(getActivity(), SCWebViewActivity.class);
        JSONObject obj = new JSONObject();
        obj.put("url", HttpApi.SEAT_WALLET);
        obj.put("title", getResources().getString(R.string.nav_my_wallet));
        String webParams = obj.toJSONString();
        intent.putExtra("webParams", webParams);
        startActivity(intent);
    }
    //我的马甲券
    @OnClick(R.id.ll_counp)
    void myCounp(){
        Intent intent = new Intent(getActivity(), MyCouponListActivity.class);
        intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
        startActivity(intent);
    }

    //我的社区帮
    @OnClick(R.id.ll_comunity)
    void myComunity(){
        Intent intent = new Intent(getActivity(), TaskListActivity.class);
        intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
        startActivity(intent);
    }

    //我的消息
    @OnClick(R.id.ll_message)
    void myMessage(){
        startActivity(new Intent(getActivity(),MyMessageActivity.class));
    }

    //设置
    @OnClick(R.id.ll_setting)
    void setting(){
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    //邀请返佣
    @OnClick(R.id.ll_fanyong)
    void invation(){
        mMinePresenter.getInvationDataFromUser(SCCacheUtils.getCacheUserId());
        /*ShareBean shareBean = new ShareBean();
        shareBean.setInviteCode("123456");
        shareBean.setDiggingsId("11111");
        shareBean.setInviteUserId("123654");
        shareBean.setRoomImage("http://shanchain-picture.oss-cn-beijing.aliyuncs.com/dd1d89bf1cf54ddea767d9bc9fcb3358.jpg");
        startActivity(new Intent(getActivity(), PayforSuccessActivity.class).putExtra("info",shareBean));*/

    }


    //点击头像替换
    @OnClick(R.id.iv_user_head)
    void updateUserHeadImage(){
        int requestCode = PhotoUtils.INTENT_SELECT;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        }
    }

    //联系客服
    @OnClick(R.id.ll_client)
    void contactClient(){
        startActivity(new Intent(getActivity(), ClientListActivity.class));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PhotoUtils.INTENT_SELECT:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
                LogUtils.d("-----iamge url---",photoPath);

                uploadImageToOss(photoPath);
                break;
        }
    }
    private String currentPath = "";
    //上传图片到阿里云oss
    private void uploadImageToOss(String imagePath){
        if(TextUtils.isEmpty(imagePath))return;
        SCUploadImgHelper helper = new SCUploadImgHelper();
        List list = new ArrayList();
        list.add(imagePath);
        helper.upLoadImg(getActivity(), list);
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                //把图片地址上传到服务器
                LogUtils.d("----upload address-oss",urls.get(0));
                currentPath = urls.get(0);
                //更改头像
                /*String characterInfo = SCCacheUtils.getCacheCharacterInfo();
                CharacterInfo character = JSONObject.parseObject(characterInfo, CharacterInfo.class);
                ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
                modifyUserInfo.setName(character.getName());
                modifyUserInfo.setSignature(character.getSignature());
                modifyUserInfo.setHeadImg(urls.get(0));*/
                Map<String  ,String> dataMap = new HashMap<>();
                dataMap.put("headImg",urls.get(0));
                String modifyUser = JSONObject.toJSONString(dataMap);
                Message message = new Message();
                message.what = 1;
                message.obj = modifyUser;
                handler.sendMessage(message);
            }

            @Override
            public void error() {
                LogUtils.i("oss上传失败");
            }
        });


    }

    @Override
    public void showProgressStart() {
        showLoadingDialog();
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    //头像上传之后
    @Override
    public void updateUserInfoResponse(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            String signature = JSONObject.parseObject(data).getString("signature");
            String headImg = JSONObject.parseObject(data).getString("headImg");
            String name = JSONObject.parseObject(data).getString("name");
            String avatar = JSONObject.parseObject(data).getString("avatar");
            CharacterInfo characterInfo = new CharacterInfo();
            characterInfo.setHeadImg(headImg);
            characterInfo.setName(name);
            characterInfo.setSignature(signature);
            String character = JSONObject.toJSONString(characterInfo);
            RoleManager.switchRoleCacheCharacterInfo(character);
            RoleManager.switchRoleCacheHeadImg(avatar);

            //更新头像
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            //个人中心换头像
            ivUserHead.setImageBitmap(bitmap);

            //更新到极光
            JMessageClient.updateUserAvatar(new File(photoPath), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if(i == 0){
                        //通知侧滑栏更新头像
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }
            });

        }else {
            ToastUtils.showToast(getActivity(),getString(R.string.operation_failed));
        }
    }

    @Override
    public void setInvationDataResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            InvationBean invationBean = SCJsonUtils.parseObj(data, InvationBean.class);
            if(invationBean!=null){
                startActivity(new Intent(getActivity(), ReturnInvationActivity.class));
            }else{
                ToastUtils.showToast(getActivity(), R.string.not_mine_tip);
            }
        }else{
            ToastUtils.showToast(getActivity(), R.string.not_mine_tip);
        }
    }
}
