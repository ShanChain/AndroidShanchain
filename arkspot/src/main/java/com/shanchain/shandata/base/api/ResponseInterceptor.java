package com.shanchain.shandata.base.api;

import android.content.Context;
import android.os.Handler;

import com.aliyun.vod.common.utils.ToastUtil;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by 胡茂柜 on 2017/3/6.
 */

public class ResponseInterceptor implements Interceptor{
    private Context mContext;
    private Handler mHandler = new Handler();
    public ResponseInterceptor(Context context){
        this.mContext = context;
    }
    private static final Charset UTF8 = Charset.forName("UTF-8");
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Charset charset = UTF8;
        ResponseBody responseBody = response.body();
        MediaType contentType = responseBody.contentType();
        charset = contentType.charset(UTF8);
        Buffer buffer = responseBody.source().buffer();
        String json = buffer.clone().readString(charset);
        try {
            final JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            LogUtils.d("current ",code+"=="+jsonObject.getString("message"));
            if (code != ApiBase.ErrorCode.SUCCESS){//待定
                LogUtils.d("current error ",code+","+jsonObject.getString("message"));
                if (mHandler != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ToastUtil.showToast(MyApplication.getInstance() , jsonObject.getString("message") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d("code error ","===");
        }
        return response;
    }
}
