package com.shanchain.shandata.ui.view.activity.story;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AddRoleAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.StoryTagInfo;
import com.shanchain.shandata.ui.model.TagContentBean;
import com.shanchain.shandata.ui.model.TagDataBean;
import com.shanchain.shandata.ui.model.TagInfo;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

public class MoreTagActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_more_tag)
    ArthurToolBar mTbMoreTag;
    @Bind(R.id.et_more_tag_search)
    EditText mEtMoreTagSearch;
    @Bind(R.id.rv_more_tag)
    RecyclerView mRvMoreTag;
    private List<StoryTagInfo> datas = new ArrayList<>();
    private AddRoleAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_more_tag;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        SCHttpUtils.post()
                .url(HttpApi.TAG_QUERY)
                .addParams("type","space")
                .addParams("page","0")
                .addParams("size","100")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取标签失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取标签成功 = " + response);

                        TagInfo tagInfo = JSONObject.parseObject(response, TagInfo.class);
                        String code = tagInfo.getCode();
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                            TagDataBean data = tagInfo.getData();
                            List<TagContentBean> content = data.getContent();
                            for (TagContentBean bean : content){
                                StoryTagInfo info = new StoryTagInfo();
                                info.setTag(bean.getTagName());
                                info.setTagBean(bean);
                                datas.add(info);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRvMoreTag.setLayoutManager(gridLayoutManager);
        mAdapter = new AddRoleAdapter(R.layout.item_add_role, datas);
        mRvMoreTag.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean selected = datas.get(position).isSelected();
                datas.get(position).setSelected(!selected);
                mAdapter.notifyItemChanged(position);
            }
        });
    }

    private void initToolBar() {
        mTbMoreTag.setBtnEnabled(true,false);
        mTbMoreTag.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
