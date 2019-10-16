package com.shanchain.shandata.ui.view.activity.settings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by WealChen
 * Date : 2019/9/27
 * Describe :查看单张图片 */
public class PhotoVSingleActivity extends BaseActivity {
    @Bind(R.id.pv_photo)
    PhotoView pvPhoto;
    @Bind(R.id.im_back)
    ImageView imBack;
    private String urlPath;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_photo_single;
    }

    @Override
    protected void initViewsAndEvents() {
        urlPath = getIntent().getStringExtra("url");
        Glide.with(mContext).load(urlPath)
                .into(pvPhoto);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
