package com.shanchain.shandata.ui.view.fragment;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiMyTaskAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.TaskMode;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.ViewAnimUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jpush.im.android.eventbus.EventBus;
import okhttp3.Call;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class MyCreateCouponFragment extends BaseFragment{

//    private String roomId = "15198852";

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;

    private List<ChatEventMessage> taskList = new ArrayList();
    ;
    private MultiMyTaskAdapter adapter;
    private View.OnClickListener taskStatusListener;
    private ProgressDialog mDialog;
    private TaskPresenter taskPresenter;
    private String roomId;
    String characterId = SCCacheUtils.getCacheCharacterId();
    String userId = SCCacheUtils.getCacheUserId();
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;


    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_task_list, null);
    }

    @Override
    public void initData() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myTaskEvent(Object event) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /*
     * 下拉刷新
     * */

    /*
     * 上拉加载
     * */

    public void showProgress() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
