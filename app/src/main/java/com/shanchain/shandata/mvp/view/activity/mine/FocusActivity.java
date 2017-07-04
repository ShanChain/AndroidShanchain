package com.shanchain.shandata.mvp.view.activity.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FocusAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.FocusInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FocusActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarFocus;
    @Bind(R.id.et_focus_search)
    EditText mEtFocusSearch;
    @Bind(R.id.tv_focus_cancel)
    TextView mTvFocusCancel;
    @Bind(R.id.xrv_focus)
    XRecyclerView mXrvFocus;
    private List<FocusInfo> datas;

    private List<FocusInfo> show;
    private FocusAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_focus;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mEtFocusSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().trim();
                show.clear();

                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getName().contains(search)) {
                        show.add(datas.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        mXrvFocus.setLayoutManager(new LinearLayoutManager(this));
        mXrvFocus.setPullRefreshEnabled(false);
        mXrvFocus.setLoadingMoreEnabled(false);
        mXrvFocus.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        mAdapter = new FocusAdapter(this, R.layout.item_focus, show);
        mXrvFocus.setAdapter(mAdapter);

    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            FocusInfo focusInfo = new FocusInfo();
            focusInfo.setFocused(true);
            focusInfo.setName("善数" + i);
            datas.add(focusInfo);
        }
        show.addAll(datas);
    }

    private void initToolBar() {
        mToolbarFocus = (ArthurToolBar) findViewById(R.id.toolbar_focus);
        mToolbarFocus.setBtnEnabled(true, false);
        mToolbarFocus.setBtnVisibility(true, false);
        mToolbarFocus.setOnLeftClickListener(this);
    }


    @OnClick(R.id.tv_focus_cancel)
    public void onClick() {
        finish();
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
