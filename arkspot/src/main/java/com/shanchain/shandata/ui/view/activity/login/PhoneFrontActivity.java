package com.shanchain.shandata.ui.view.activity.login;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.PhoneFrontAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.PhoneFrontBean;
import com.shanchain.shandata.utils.entity.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :选择其他国家手机号前缀界面
 */
public class PhoneFrontActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_register)
    ArthurToolBar mTbRegister;
    @Bind(R.id.rv_phone_list)
    RecyclerView rvPhoneList;

    private String [] countrysAttr= new String[]{"中国大陆(CHN)","中国香港(HK)","中国澳门(MAC)","中国台湾(TWN)","新加坡(SGP)","英国(UK)",
                                                  "韩国(KR)","日本(JPN)","美国(USA)","加拿大(CAN)","马来西亚(MYS)","俄罗斯(RUS)","澳大利亚(AUS)",
                                                  "德国(GER)","法国(FRA)","泰国(THA)","菲律宾(PH)"};
    private String [] countryPhoneAttr = new String[]{"+86","+852","+853","+886","+65","+44","+82","+81","+1","+1","+60","+7","+61","+49","+33","+66","+63"};

    private List<PhoneFrontBean> mList = new ArrayList<>();
    private PhoneFrontAdapter mPhoneFrontAdapter;
    private int sourceType;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_phone_front;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPhoneList.setLayoutManager(linearLayoutManager);

        initListData();
    }
    //初始化数据
    private void initListData(){
        for (int i = 0; i <countrysAttr.length ; i++) {
            PhoneFrontBean p = new PhoneFrontBean();
            p.setContry(countrysAttr[i]);
            p.setPhoneFront(countryPhoneAttr[i]);
            p.setFront(countryPhoneAttr[i].substring(1,countryPhoneAttr[i].length()));
            mList.add(p);
        }

        mPhoneFrontAdapter = new PhoneFrontAdapter(R.layout.phone_front_item,mList);
        rvPhoneList.setAdapter(mPhoneFrontAdapter);

        mPhoneFrontAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhoneFrontBean p = (PhoneFrontBean) adapter.getItem(position);
                if(null != p){
                    p.setSourceType(sourceType);
                    mListener.getPhoneData(p);
                    EventBus.getDefault().post(p);
                    finish();
                }
            }
        });
    }

    private void initToolBar() {
        mTbRegister.setBtnEnabled(true, false);
        mTbRegister.setOnLeftClickListener(this);
        sourceType = getIntent().getIntExtra("type",0);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    public interface PhoneFrontNumCallback{
        void getPhoneData(PhoneFrontBean phoneFrontBean);
    }
    private static PhoneFrontNumCallback mListener;
    public static void setListener(PhoneFrontNumCallback listener){
        mListener = listener;
    }
}
