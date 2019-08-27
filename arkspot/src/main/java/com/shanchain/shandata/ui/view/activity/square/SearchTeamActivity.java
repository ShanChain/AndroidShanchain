package com.shanchain.shandata.ui.view.activity.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupTeamAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.InsertdiggingsBean;
import com.shanchain.shandata.ui.model.TDiggingJoinLogs;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.presenter.impl.MyGroupTeamPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WealChen
 * Date : 2019/8/13
 * Describe :搜索矿区
 */
public class SearchTeamActivity extends BaseActivity implements MyGroupTeamView {
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;

    private GroupTeamAdapter mGroupTeamAdapter;
    private List<GroupTeamBean> mList = new ArrayList<>();
    private MyGroupTeamPresenter mPresenter;
    private GroupTeamBean groupTeamBean;
    private CustomDialog mShowPasswordDialog;
    private StandardDialog standardDialog;
    private boolean isPaySuccess = false;//是否成功支付
    private InsertdiggingsBean insertdiggingsBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1002:
                    String filePath = (String) msg.obj;
                    mPresenter.checkPasswordToServer(SearchTeamActivity.this,filePath,Constants.PAYFOR_MINING_MONEY);
                    //由于验证钱包功能接口暂时不可用，这里先直接加入矿区
//                    enterMiningEoom();
                    break;
            }

        }
    };
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_team;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new MyGroupTeamPresenterImpl(this);
        mGroupTeamAdapter = new GroupTeamAdapter(R.layout.group_team_item, mList);
        mGroupTeamAdapter.setType(2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
        mGroupTeamAdapter.notifyDataSetChanged();
        initListener();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    mPresenter.queryGroupTeam("", "",s.toString(),1, 30,Constants.pullRefress,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //初始化监听
    private void initListener(){
        mGroupTeamAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                groupTeamBean = (GroupTeamBean) adapter.getItem(position);
                if(groupTeamBean!=null){
                    if(groupTeamBean.getCreateUser().equals(SCCacheUtils.getCacheUserId())){//自己创建的直接进入聊天室
                        gotoMessageRoom(groupTeamBean);
                    }else {
                        //不是自己创建的
                        //自己参与的
                        if(checkUserInMining(groupTeamBean)){
                            gotoMessageRoom(groupTeamBean);
                        }else {
                            if(groupTeamBean.getUserCount() >=4){//如果人数已满，则直接进入聊天室，但不能发言
                                ToastUtils.showToast(SearchTeamActivity.this, R.string.have_not_perission);
                            }else {
                                isJoinMiningTip();
                            }
                        }
                    }
                }
            }
        });

    }

    @OnClick(R.id.tv_cancel)
    void finished(){
        finish();
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
    public void setQuearyMygoupTeamResponse(String response, int pullType) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            List<GroupTeamBean> listDara = JSONObject.parseArray(list,GroupTeamBean.class);
            mList.clear();
            mList.addAll(listDara);
            recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
            mGroupTeamAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCheckPasswResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.SUC_CODE)) {
            /*if (mShowPasswordDialog != null) {
                mShowPasswordDialog.dismiss();
            }*/
            //支付成功调用更新矿区接口
            mPresenter.updateMiningRoomRecord(insertdiggingsBean.getId(),"1");
        }
    }

    @Override
    public void setCheckPassFaile() {
        if (mShowPasswordDialog != null) {
            mShowPasswordDialog.dismiss();
            mShowPasswordDialog.setPasswordBitmap(null);
        }
    }

    @Override
    public void setAddMinigRoomResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            insertdiggingsBean = SCJsonUtils.parseObj(data, InsertdiggingsBean.class);
            //弹出支付图片框
            showPasswordView();

        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(SearchTeamActivity.this,getResources().getString(R.string.operation_failed));
                }
            });

        }
    }

    //支付密码弹窗
    private void showPasswordView(){
        //显示上传密码弹窗
        mShowPasswordDialog = new CustomDialog(SearchTeamActivity.this, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        mShowPasswordDialog.setPasswordBitmap(null);
        mShowPasswordDialog.show();
        mShowPasswordDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.iv_dialog_add_picture:
                        selectImage(SearchTeamActivity.this);
                        break;
                    case R.id.tv_dialog_sure:
                        ToastUtils.showToast(SearchTeamActivity.this, R.string.upload_qr_code);
                        break;
                }
            }
        });
        mShowPasswordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                LogUtils.d("----->>>Dismiss"+isPaySuccess+"---"+insertdiggingsBean.getId());
                //密码上传框消失时删除加入矿区记录表
                if(!isPaySuccess && insertdiggingsBean!=null){//未支付成功时密码框消失才删除
                    mPresenter.deleteMiningRoomRecord(insertdiggingsBean.getId());
                }
            }
        });
    }

    @Override
    public void setdeleteDigiRoomIdResponse(String response) {

    }

    @Override
    public void setUpdateMiningRoomResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            isPaySuccess = true;
            if (mShowPasswordDialog != null && mShowPasswordDialog.isShowing()) {
                mShowPasswordDialog.dismiss();
                mShowPasswordDialog.setPasswordBitmap(null);
            }
            //加入成功进入聊天室
            gotoMessageRoom(groupTeamBean);
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(SearchTeamActivity.this, R.string.success_join_mining);
                }
            });
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(SearchTeamActivity.this,getResources().getString(R.string.operation_failed));
                }
            });
        }
    }

    //进入聊天室
    private void gotoMessageRoom(GroupTeamBean groupTeamBean){
        if(TextUtils.isEmpty(groupTeamBean.getRoomId()))return;
        Intent intent = new Intent(this, MessageListActivity.class);
        intent.putExtra("roomId", "" + groupTeamBean.getRoomId());
        intent.putExtra("roomName", "" + groupTeamBean.getRoomName());
        startActivity(intent);
    }

    //判断是否是自己参与的矿区
    private boolean checkUserInMining(GroupTeamBean groupTeamBean){
        boolean isJoin = false;
        List<TDiggingJoinLogs> logsList = groupTeamBean.gettDiggingJoinLogs();
        if(logsList.size() == 0){
            isJoin = false;
        }
        for (int i = 0; i <logsList.size() ; i++) {
            if(SCCacheUtils.getCacheUserId().equals(logsList.get(i).getUserId())){
                isJoin =  true;
                break;
            }
        }
        return isJoin;
    }

    //弹窗提示支付进入矿区
    private void isJoinMiningTip(){
        standardDialog = new StandardDialog(SearchTeamActivity.this);
        standardDialog.setStandardTitle(" ");
        standardDialog.setStandardMsg(getString(R.string.payfor_add_mining,Constants.PAYFOR_MINING_MONEY));
        standardDialog.setSureText(getString(R.string.commit_payfor));
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                //支付前插入矿区记录
                enterMiningEoom("0");
            }
        }, new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getData() != null) {
            if (requestCode == NetErrCode.WALLET_PHOTO) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                final String photoPath = cursor.getString(columnIndex);
                cursor.close();
                LogUtils.d("----->SearchActivity: select image path is "+photoPath);
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                /*if (mShowPasswordDialog != null) {
                    mShowPasswordDialog.dismiss();
                    mShowPasswordDialog.setPasswordBitmap(null);
                }*/
                mShowPasswordDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(SearchTeamActivity.this, true, 1.0,
                        R.layout.dialog_bottom_wallet_password,
                        new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
                mShowPasswordDialog.setPasswordBitmap(bitmap);
                mShowPasswordDialog.show();
                mShowPasswordDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.iv_dialog_add_picture:
                                selectImage(SearchTeamActivity.this);
                                break;
                            case R.id.tv_dialog_sure:
                                //去支付
                                Message message = new Message();
                                message.what = 1002;
                                message.obj = photoPath;
                                handler.sendMessage(message);
                                break;
                        }
                    }
                });
            }
        }
    }

    //支付成功后加入矿区
    private void enterMiningEoom(String isPay){
        mPresenter.insertMiningRoomByOther(SCCacheUtils.getCacheUserId(),groupTeamBean.getId()+"",isPay);
    }
}
