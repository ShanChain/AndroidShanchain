package com.shanchain.data.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.List;

/**
 * 阿里云多图片上传
 * 使用三部曲：
 *  1，创建ossHelper对象
 *  2，设置上传回调监听器
 *  3，调用上传方法
 */

public class OssHelper {
//    public static final String OSS_ENDPOINT = "oss-cn-hongkong.aliyuncs.com";
//    public static final String OSS_BUCKET = "shanchain-seller";
//    public static final String OSS_HOST = "http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/";
    public static final String OSS_ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    public static final String OSS_BUCKET = "shanchain-picture";
    public static final String OSS_HOST = "http://shanchain-picture.oss-cn-beijing.aliyuncs.com/";
    private OSS oss;

    public OssHelper(Context context, String accessKeyId, String accessKeySecret, String securityToken){
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        OSSLog.enableLog();
        oss = new OSSClient(context, OSS_ENDPOINT, credentialProvider);
    }


    /**
     * 阿里云OSS上传（默认是异步多文件上传）
     * @param filePaths 本地文件路径集合
     * @param urls 图片url集合
     */
    public void ossUpload(final List<String> filePaths, final List<String> urls) {

        if (oss == null) {
            throw new NullPointerException("OSS Object can not be null");
        }

        if (filePaths.size() <= 0) {
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            mListener.upLoadSuccess(true);
            return;
        }
        final String filePath = filePaths.get(0);
        String url = urls.get(0);
        if (TextUtils.isEmpty(filePath)) {
            filePaths.remove(0);
            urls.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(filePaths,urls);
            return;
        }

        File file = new File(filePath);
        if (null == file || !file.exists()) {
            filePaths.remove(0);
            urls.remove(0);
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(filePaths,urls);
            return;
        }

        // 文件标识符objectKey
        final String objectKey = url+".jpg";
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(OSS_BUCKET, objectKey, filePath);

        // 设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 进度逻辑
            }
        });
        // 异步上传
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) { // 上传成功
                        urls.remove(0);
                        filePaths.remove(0);
                        ossUpload(filePaths,urls);// 递归同步效果
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) { // 上传失败
                        mListener.upLoadSuccess(false);
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                        }
                    }
                });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }


    private OnUploadListener mListener;
    public void setOnUploadListener(OnUploadListener listener){
        this.mListener = listener;
    }

    public interface OnUploadListener{
        void upLoadSuccess(boolean isSuccess);
    }

    public String getImgUrl(String imgName){
        return OSS_HOST + imgName + ".jpg";
    }

}
