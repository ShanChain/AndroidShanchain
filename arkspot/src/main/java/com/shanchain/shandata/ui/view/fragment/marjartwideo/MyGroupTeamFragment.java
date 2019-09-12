package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.SCInputDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GroupTeamAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.GroupTeamBean;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.model.InsertdiggingsBean;
import com.shanchain.shandata.ui.model.SqureDataEntity;
import com.shanchain.shandata.ui.model.TDiggingJoinLogs;
import com.shanchain.shandata.ui.presenter.MyGroupTeamPresenter;
import com.shanchain.shandata.ui.presenter.impl.MyGroupTeamPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.square.SearchTeamActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MyGroupTeamView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :可加入矿区
 */
public class MyGroupTeamFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MyGroupTeamView {
    @Bind(R.id.recycler_view_coupon)
    RecyclerView recyclerViewCoupon;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

    private GroupTeamAdapter mGroupTeamAdapter;
    private List<GroupTeamBean> mList = new ArrayList<>();
    private MyGroupTeamPresenter mPresenter;
    private int pageIndex = 1;
    private boolean isLast = false;
    private StandardDialog standardDialog;
    private CustomDialog mShowPasswordDialog;
    private GroupTeamBean groupTeamBean;
    private boolean isPaySuccess = false;//是否成功支付
    private InsertdiggingsBean insertdiggingsBean;
    private String photoPath;
    public static MyGroupTeamFragment getInstance() {
        MyGroupTeamFragment fragment = new MyGroupTeamFragment();
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1002:
                    String filePath = (String) msg.obj;
                    if(TextUtils.isEmpty(filePath))return;
                    mPresenter.checkPasswordToServer(getActivity(),filePath,HttpApi.PAYFOR_MINING_MONEY);
                    //由于验证钱包功能接口暂时不可用，这里先直接加入矿区
//                    enterMiningEoom();
                    break;
            }

        }
    };

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_my_team, null);
    }

    @Override
    public void initData() {
        mPresenter = new MyGroupTeamPresenterImpl(this);
        refreshLayout.setOnRefreshListener(this);
        mGroupTeamAdapter = new GroupTeamAdapter(R.layout.group_team_item,mList);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.not_data_footer_view, null);
        mGroupTeamAdapter.addFooterView(view);
        mGroupTeamAdapter.setType(1);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCoupon.setLayoutManager(layoutManager);
        recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
        mGroupTeamAdapter.notifyDataSetChanged();

        initLoadMoreListener();
    }

    //获取数据
    private void getTeamData(){
        pageIndex = 1;
        mPresenter.queryGroupTeam("","","",pageIndex, Constants.pageSize,Constants.pullRefress,1,"");
    }

    @Override
    public void onResume() {
        super.onResume();
        getTeamData();
    }

    @Override
    public void onRefresh() {
        getTeamData();

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
        refreshLayout.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            String list = JSONObject.parseObject(data).getString("list");
            List<GroupTeamBean> listDara = JSONObject.parseArray(list,GroupTeamBean.class);
            if(listDara.size()<Constants.pageSize){
                isLast = true;
            }else {
                isLast = false;
            }
            if(pullType == Constants.pullRefress){
                mList.clear();
                mList.addAll(listDara);
                recyclerViewCoupon.setAdapter(mGroupTeamAdapter);
                mGroupTeamAdapter.notifyDataSetChanged();
            }else {
                mGroupTeamAdapter.addData(listDara);
            }
            if(mList!=null && mList.size()>0){
                llNotdata.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }else {
                llNotdata.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setCheckPasswResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.SUC_CODE)) {
            if (mShowPasswordDialog != null && mShowPasswordDialog.isShowing()) {
                /*mShowPasswordDialog.setPasswordBitmap(null);
                mShowPasswordDialog.dismiss();*/
            }
            //支付成功调用更新矿区接口
            mPresenter.updateMiningRoomRecord(insertdiggingsBean.getId(),"1");
        }
    }

    @Override
    public void setCheckPassFaile() {
        if (mShowPasswordDialog != null && mShowPasswordDialog.isShowing()) {
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
                    ToastUtils.showToast(getActivity(),getResources().getString(R.string.operation_failed));
                }
            });
        }
    }

    @Override
    public void setdeleteDigiRoomIdResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(getActivity(),getResources().getString(R.string.operation_failed));
                }
            });
        }
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
                    ToastUtils.showToast(getActivity(), R.string.success_join_mining);
                }
            });
            getTeamData();
        }else {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(getActivity(),getResources().getString(R.string.operation_failed));
                }
            });
        }
    }

    @Override
    public void setCheckUserHasWalletResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            //判断有钱包账号后插入矿区记录
            enterMiningEoom("0");
        }
    }

    //上拉加载监听
    private void initLoadMoreListener() {
        //上拉加载
        recyclerViewCoupon.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mGroupTeamAdapter.getItemCount()){
                    pageIndex ++;
                    mPresenter.queryGroupTeam("","","",pageIndex, Constants.pageSize,Constants.pillLoadmore,1,"");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
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
                                ToastUtils.showToast(getActivity(), R.string.mine_full);
                            }else {
                                isJoinMiningTip();
                            }
                        }
                    }
                }
            }
        });


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


    //进入聊天室
    private void gotoMessageRoom(GroupTeamBean groupTeamBean){
        if(TextUtils.isEmpty(groupTeamBean.getRoomId()))return;
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("roomId", "" + groupTeamBean.getRoomId());
        intent.putExtra("roomName", "" + groupTeamBean.getRoomName());
        startActivity(intent);
    }
    //弹窗提示支付进入矿区
    private void isJoinMiningTip(){
        standardDialog = new StandardDialog(getActivity());
        standardDialog.setStandardTitle(" ");
        standardDialog.setStandardMsg(getString(R.string.payfor_add_mining, HttpApi.PAYFOR_MINING_MONEY));
        standardDialog.setSureText(getString(R.string.commit_payfor));
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                standardDialog.dismiss();
                //支付前先判断是否有钱包
                mPresenter.checkUserHasWallet(getActivity());

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
    //支付密码弹窗
    private void showPasswordView(){
        //显示上传密码弹窗
        mShowPasswordDialog = new CustomDialog(getActivity(), true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        mShowPasswordDialog.setPasswordBitmap(null);
        mShowPasswordDialog.show();
        mShowPasswordDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.iv_dialog_add_picture:
                        selectImage(getActivity());
                        break;
                    case R.id.tv_dialog_sure:
                        if(mShowPasswordDialog.getPasswordBitmap() == null){
                            ToastUtils.showToast(getActivity(), R.string.upload_qr_code);
                        }else {
                            Message message = new Message();
                            message.what = 1002;
                            message.obj = photoPath;
                            handler.sendMessage(message);
                        }
                        break;
                }
            }
        });
        if(mShowPasswordDialog!=null){
            mShowPasswordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //密码上传框消失时删除加入矿区记录表
                    if(!isPaySuccess && insertdiggingsBean!=null){//未支付成功时密码框消失才删除
                        mPresenter.deleteMiningRoomRecord(insertdiggingsBean.getId());
                    }
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            if (requestCode == NetErrCode.WALLET_PHOTO) {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                photoPath = cursor.getString(columnIndex);
                cursor.close();
                LogUtils.showLog("----->MyGroupTeamFragment: select image path is "+photoPath);
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                if(mShowPasswordDialog!=null){
                    mShowPasswordDialog.setPasswordBitmap2(bitmap);
                }
            }
        }
    }

    //支付成功后加入矿区
    private void enterMiningEoom(String isPay){
        mPresenter.insertMiningRoomByOther(SCCacheUtils.getCacheUserId(),groupTeamBean.getId()+"",isPay);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDestroy();

    }
}
