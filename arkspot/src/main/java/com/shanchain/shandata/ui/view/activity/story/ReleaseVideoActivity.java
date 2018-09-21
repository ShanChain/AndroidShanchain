package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.presenter.ReleaseVideoPresenter;
import com.shanchain.shandata.ui.presenter.impl.ReleaseVideoPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.ReleaseVideoView;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class ReleaseVideoActivity extends BaseActivity implements ReleaseVideoView,ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int REQUEST_VIDEO_CODE = 100;
    @Bind(R.id.tb_release_video)
    ArthurToolBar mTbReleaseVideo;
    @Bind(R.id.et_release_video_title)
    EditText mEtReleaseVideoTitle;
    @Bind(R.id.et_release_video_des)
    EditText mEtReleaseVideoDes;
    @Bind(R.id.iv_video_first)
    ImageView mIvVideoFirst;
    @Bind(R.id.rl_video_show)
    RelativeLayout mRlVideoShow;
    @Bind(R.id.iv_video_delete)
    ImageView mIvVideoDelete;
    private String mVideoPath;
    private String mImgPath;
    private boolean isRelease = false;
    private String mVideoTitle;
    private String mVideoDes;
    private ReleaseVideoPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_release_video;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        mPresenter = new ReleaseVideoPresenterImpl(this);
    }

    private void initToolBar() {
        File cacheDir = getCacheDir();
        File filesDir = getFilesDir();
        File externalCacheDir = getExternalCacheDir();
        File storageDirectory = Environment.getExternalStorageDirectory();
        File dataDirectory = Environment.getDataDirectory();
        LogUtils.i("cacheDir = " + cacheDir.getAbsolutePath() + "\n filesDir = " + filesDir.getAbsolutePath()
                + " \n externalCacheDir = " + externalCacheDir.getAbsolutePath() + "\n storageDir = " + storageDirectory.getAbsolutePath() + "\n dataDir = " + dataDirectory.getAbsolutePath());
        long storageDirectoryFreeSpace = storageDirectory.getFreeSpace();
        long cacheSpace = cacheDir.getFreeSpace();
        long filesDirFreeSpace = filesDir.getFreeSpace();
        long dataDirectoryFreeSpace = dataDirectory.getFreeSpace();
        long externalCacheDirFreeSpace = externalCacheDir.getFreeSpace();
        LogUtils.i("storageDirectoryFreeSpace = " + storageDirectoryFreeSpace + "\n cacheSpace = " + cacheSpace + "\n filesDirFreeSpace = " +
                filesDirFreeSpace + "\n dataDirectoryFreeSpace = " + dataDirectoryFreeSpace + "\n externalCacheDirFreeSpace = " + externalCacheDirFreeSpace);

        mTbReleaseVideo.setOnLeftClickListener(this);
        mTbReleaseVideo.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        if (isRelease) {
            releaseVideoDynamic();
        } else {
            Intent intent = new Intent(this, RecordVideoActivity.class);
            startActivityForResult(intent, REQUEST_VIDEO_CODE);
        }

    }

    /**
     * 描述：发布视频动态
     */
    private void releaseVideoDynamic() {
        mVideoTitle = mEtReleaseVideoTitle.getText().toString().trim();
        mVideoDes = mEtReleaseVideoDes.getText().toString().trim();
        if (TextUtils.isEmpty(mVideoTitle)) {
            ToastUtils.showToast(mContext,"视频标题不能为空");
            return;
        }

        if (TextUtils.isEmpty(mVideoPath)){
            ToastUtils.showToast(mContext,"请拍摄将要上传的短视频");
            return;
        }

        uploadVideo();
    }

    private void uploadVideo() {
        mPresenter.releaseVideoDynamic(mContext,mVideoTitle,mVideoDes,mVideoPath,mImgPath, Constants.VIDEO_UPLOAD_DYNAMIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mVideoPath = data.getStringExtra("videoPath");
            mImgPath = data.getStringExtra("imgPath");
            LogUtils.i("视频路径 = " + mVideoPath);
            LogUtils.i("第一帧图片地址 = " + mImgPath);
            mRlVideoShow.setVisibility(View.VISIBLE);
            GlideUtils.load(mContext, mImgPath, mIvVideoFirst, 0);
            isRelease = true;
        }
    }

    @OnClick({R.id.iv_video_first, R.id.iv_video_delete, R.id.iv_video_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_first:
                playPreviewVideo();
                break;
            case R.id.iv_video_start:
                playPreviewVideo();
                break;
            case R.id.iv_video_delete:
                mRlVideoShow.setVisibility(View.GONE);
                mVideoPath = "";
                mImgPath = "";
                isRelease = false;
                break;
        }
    }

    /**
     * 描述：直接全屏播放视频
     */
    private void playPreviewVideo() {
        if (!TextUtils.isEmpty(mVideoPath)) {
            KeyboardUtils.hideSoftInput(this);
            JZVideoPlayerStandard.startFullscreen(this, JZVideoPlayerStandard.class, mVideoPath, "视频预览");
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void error(String msg) {
        LogUtils.i(msg);
    }

    @Override
    public void suc(String data) {
        ToastUtils.showToast(mContext,"发布成功");
        finish();
//        readyGo(VideoDynamicDetailActivity.class);
    }
}
