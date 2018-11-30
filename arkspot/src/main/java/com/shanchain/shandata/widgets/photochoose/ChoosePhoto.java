package com.shanchain.shandata.widgets.photochoose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.ImageUtils;
import com.shanchain.shandata.utils.SharePreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;


/**
 * Created by ${chenyn} on 2017/3/3.
 */

public class ChoosePhoto {
    public PhotoUtils photoUtils;
    private BottomMenuDialog mDialog;
    private Activity mContext;
    private boolean isFromPersonal;

    public void setInfo(MessageListActivity messageListActivity, boolean isFromPersonal) {
        this.mContext = messageListActivity;
        this.isFromPersonal = isFromPersonal;
    }

    public void showPhotoDialog(final Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mDialog = new BottomMenuDialog(context);
        mDialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                photoUtils.takePicture((Activity) context);
            }
        });
        mDialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                photoUtils.selectPicture((Activity) context);
            }
        });
        mDialog.show();
    }


    public void setPortraitChangeListener(final Context context, final ImageView iv_photo, final int count) {
        PhotoUtils.OnPhotoResultListener photoResultListener = new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(final Uri uri) {
                final Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                //图片设置给控件
                iv_photo.setImageBitmap(bitmap);
                if (count == 1) {
                    SharePreferenceManager.setRegisterAvatarPath(uri.getPath());
                } else {
                    SharePreferenceManager.setCachedAvatarPath(uri.getPath());
                }
                if (isFromPersonal) {

                    SCUploadImgHelper helper = new SCUploadImgHelper();
                    helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                        @Override
                        public void onUploadSuc(List<String> urls) {
                            RoleManager.switchRoleCacheHeadImg(urls.get(0));
                            //更改头像
                            String characterInfo = SCCacheUtils.getCacheCharacterInfo();
                            CharacterInfo character = JSONObject.parseObject(characterInfo,CharacterInfo.class);
                            ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
                            modifyUserInfo.setName(character.getName());
                            modifyUserInfo.setSignature(character.getSignature());
                            modifyUserInfo.setHeadImg(urls.get(0));
                            String modifyUser = JSONObject.toJSONString(modifyUserInfo);
                            org.greenrobot.eventbus.EventBus.getDefault().postSticky(modifyUserInfo);
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.MODIFY_CHARACTER)
                                    .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                                    .addParams("dataString", modifyUser)
                                    .build()
                                    .execute(new SCHttpStringCallBack() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("修改角色信息失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                                LogUtils.d("修改角色信息");
                                                String data = JSONObject.parseObject(response).getString("data");
                                                String signature = JSONObject.parseObject(data).getString("signature");
                                                String headImg = JSONObject.parseObject(data).getString("headImg");
                                                String name = JSONObject.parseObject(data).getString("name");
                                                String avatar = JSONObject.parseObject(data).getString("avatar");
                                                UserInfo jmUserInfo = JMessageClient.getMyInfo();
//                                                if (jmUserInfo!=null){
//                                                    jmUserInfo.setNickname(name);//设置昵称
//                                                    jmUserInfo.setSignature(signature);//设置签名
//                                                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, jmUserInfo, new BasicCallback() {
//                                                        @Override
//                                                        public void gotResult(int i, String s) {
//                                                            String s1 = s;
//                                                        }
//                                                    });
//                                                    JMessageClient.updateMyInfo(UserInfo.Field.signature, jmUserInfo, new BasicCallback() {
//                                                        @Override
//                                                        public void gotResult(int i, String s) {
//                                                            String s1 = s;
//                                                        }
//                                                    });
//                                                }

                                                CharacterInfo characterInfo = new CharacterInfo();
                                                characterInfo.setHeadImg(headImg);
                                                characterInfo.setSignature(signature);
                                                characterInfo.setName(name);
                                                String character = JSONObject.toJSONString(characterInfo);
                                                RoleManager.switchRoleCacheCharacterInfo(character);
                                                RoleManager.switchRoleCacheHeadImg(headImg);
//                                    RoleManager.switchRoleCacheHeadImg(avatar);
                                            }
                                        }
                                    });

                            File headFiel = null;
                            try {
                                Bitmap bitmap1 = ImageUtils.returnBitMap(urls.get(0));
                                headFiel = ImageUtils.saveUrlImgFile(bitmap, "head_img.jpg");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //调用极光更新头像
                           UserInfo userInfo = JMessageClient.getMyInfo();
                            JMessageClient.updateUserAvatar(headFiel, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    String s1 = s;
                                }
                            });
                        }

                        @Override
                        public void error() {
                            LogUtils.i("oss上传失败");
                        }
                    });
                    String filePath = uri.getPath();
                    List list = new ArrayList();
                    list.add(filePath);
                    helper.upLoadImg(mContext, list);

                }
            }

            @Override
            public void onPhotoCancel() {
            }
        };
        photoUtils = new PhotoUtils(photoResultListener);
    }

    //更新群组头像
    public void setGroupAvatarChangeListener(final Activity context, final long groupId) {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(final Uri uri) {
                LoadDialog.show(context);
                JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, GroupInfo groupInfo) {
                        if (i == 0) {
                            groupInfo.updateAvatar(new File(uri.getPath()), "", new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    LoadDialog.dismiss(context);
                                    if (i == 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("groupAvatarPath", uri.getPath());
                                        context.setResult(Activity.RESULT_OK, intent);
                                        ToastUtil.showToast(context, "更新成功");
                                        context.finish();
                                    } else {
                                        ToastUtil.showToast(context, "更新失败");
                                        context.finish();
                                    }
                                }
                            });
                        } else {
                            HandleResponseCode.onHandle(context, i, false);
                        }
                    }
                });
            }

            @Override
            public void onPhotoCancel() {
            }
        });
    }

}
