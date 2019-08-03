package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.shandata.ui.presenter.MinePresenter;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.MineView;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;
import okhttp3.Call;

/**
 * Created by WealChen
 * Date : 2019/8/2
 * Describe :
 */
public class MinePresenterImpl implements MinePresenter {
    private MineView mMineView;
    public MinePresenterImpl(MineView mineView){
        this.mMineView = mineView;
    }

    @Override
    public void modifyUserInfo(String modifyUserInfo) {
        mMineView.showProgressStart();

        SCHttpUtils.postWithUserId()
                .url(HttpApi.MODIFY_CHARACTER)
                .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                .addParams("dataString", modifyUserInfo)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("修改角色信息失败");
                        mMineView.showProgressEnd();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mMineView.showProgressEnd();
                        mMineView.updateUserInfoResponse(response);
                    }
                });
    }
}
