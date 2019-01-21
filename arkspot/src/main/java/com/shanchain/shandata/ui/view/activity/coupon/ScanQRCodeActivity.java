package com.shanchain.shandata.ui.view.activity.coupon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScanQRCodeActivity extends AppCompatActivity {

    private boolean isLighting = false;
    private TextView tvLight;
    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanQRCodeActivity.this.setResult(RESULT_OK, resultIntent);
            ScanQRCodeActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanQRCodeActivity.this.setResult(RESULT_OK, resultIntent);
            ScanQRCodeActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        tvLight = findViewById(R.id.tv_light);

        //执行扫面Fragment的初始化操作
        CaptureFragment scannerFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(scannerFragment, R.layout.fragment_custom_scanner);
        scannerFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        scannerFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, scannerFragment).commit();
        tvLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLighting == false) {
                    CodeUtils.isLightEnable(true);
                    isLighting = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isLighting = false;
                }

            }
        });
    }

}
