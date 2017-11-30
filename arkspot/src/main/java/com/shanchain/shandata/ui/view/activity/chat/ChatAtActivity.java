package com.shanchain.shandata.ui.view.activity.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.SelectContactAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.BdAtContactInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChatAtActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_chat_at)
    ArthurToolBar mTbChatAt;
    @Bind(R.id.et_chat_at)
    EditText mEtChatAt;
    @Bind(R.id.rv_chat_at)
    RecyclerView mRvChatAt;
    @Bind(R.id.sb_chat_at)
    WaveSideBar mSbChatAt;
    private List<BdAtContactInfo> datas = new ArrayList<>();
    private List<BdAtContactInfo> show = new ArrayList<>();
    private SelectContactAdapter mContactAdapter;
    private ArrayList<String> selected = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat_at;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
        initData();
    }



    private void initRecyclerView() {
        mRvChatAt.setLayoutManager(new LinearLayoutManager(this));
        mContactAdapter = new SelectContactAdapter(R.layout.item_select_contact, show);
        mRvChatAt.setAdapter(mContactAdapter);
        mContactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rb_item_contact) {
                    boolean selected = show.get(position).isSelected();
                    show.get(position).setSelected(!selected);
                    mContactAdapter.notifyDataSetChanged();
                }
            }
        });
        mContactAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean selected = show.get(position).isSelected();
                show.get(position).setSelected(!selected);
                mContactAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {



    }

    private void initToolBar() {
        mTbChatAt.setOnLeftClickListener(this);
        mTbChatAt.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
