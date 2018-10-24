//package com.shanchain.data.common.rn;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.widget.Toast;
//
//import com.facebook.react.ReactActivity;
//import com.facebook.react.ReactActivityDelegate;
//import com.facebook.react.ReactInstanceManager;
//import com.facebook.react.ReactNativeHost;
//import com.facebook.react.ReactRootView;
//import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
//import com.facebook.react.modules.core.PermissionAwareActivity;
//import com.facebook.react.modules.core.PermissionListener;
//import com.shanchain.data.common.BaseApplication;
//import com.shanchain.data.common.base.ActivityStackManager;
//import com.shanchain.data.common.rn.modules.NavigatorModule;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.Iterator;
//
//import javax.annotation.Nullable;
//
///**
// * Created by flyye on 2017/9/20.
// */
//
//public class SCReactActivity extends ReactActivity{
//
//    private static final String TAG = SCReactActivity.class.getSimpleName();
//    private ReactRootView rootView;
//    private ReactInstanceManager reactInstanceManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActivityStackManager.getInstance().addActivity(this);
//        Intent intent = getIntent();
//        String screenName = intent.getStringExtra(NavigatorModule.REACT_SCREEN);
//        if (TextUtils.isEmpty(screenName)) {
//            finish();
//            return;
//        }
//
//        reactInstanceManager = ((BaseApplication) getApplication()).getReactInstanceManager();
//        Bundle bundle = getIntent().getBundleExtra(NavigatorModule.REACT_INITIAL_PROPS);
//        if (bundle == null) {
//            bundle = new Bundle();
//        } else {
//            castParcelableToBundle(bundle);
//        }
//        bundle.putString("screen", screenName);
//        rootView = new ReactRootView(this);
//        rootView.startReactApplication(reactInstanceManager, screenName, bundle);
//        setContentView(rootView);
//    }
//
//    private void castParcelableToBundle(Bundle bundle) {
//        Iterator<String> iterator = bundle.keySet().iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            Object value = bundle.get(key);
//            if (value instanceof Bundle) {
//                castParcelableToBundle((Bundle) value);
//            } else if (value instanceof Parcelable[]) {
//                int len = ((Parcelable[]) value).length;
//                Parcelable[] parcelables = (Parcelable[]) value;
//                Bundle[] bundles = new Bundle[len];
//                for (int i = 0; i < len; i++) {
//                    bundles[i] = (Bundle) parcelables[i];
//                    castParcelableToBundle(bundles[i]);
//                }
//                bundle.putParcelableArray(key, bundles);
//            }
//        }
//    }
//
//    private boolean isFinished = false;
//    @Override
//    public void finish() {
//        if(!isFinished){
//            ActivityStackManager.getInstance().finishActivity(this);
//            isFinished = true;
//        }
//        super.finish();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe
//    public void onEvent(String event) {
//        Toast.makeText(this, event, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }
//
//}


package com.shanchain.data.common.rn;

        import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.umeng.message.PushAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.annotation.Nullable;

/**
 * Base Activity for React Native applications.
 */
public    class SCReactActivity extends Activity
        implements DefaultHardwareBackBtnHandler, PermissionAwareActivity {

    private SCReactActivityDelegate mDelegate;
    private ResultListener mResultLisener;



    protected SCReactActivityDelegate createReactActivityDelegate(String componentName, final Bundle bundle) {
        return new SCReactActivityDelegate(this, componentName){
            @Nullable
            @Override
            protected Bundle getLaunchOptions() {
                return bundle;
            }
        };
    }

    public interface ResultListener {
      void onResult(int requestCode, int resultCode, Intent data);
    }

    public void setResultListener(ResultListener resultListener) {
        mResultLisener = resultListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        Intent intent = getIntent();
        String screenName = intent.getStringExtra(NavigatorModule.REACT_SCREEN);
        if (TextUtils.isEmpty(screenName)) {
            finish();
            return;
        }
        Bundle bundle = getIntent().getBundleExtra(NavigatorModule.REACT_INITIAL_PROPS);
        mDelegate = createReactActivityDelegate(screenName,bundle);
        mDelegate.onCreate(savedInstanceState);
        initPushAgent();
    }

    private void initPushAgent(){
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mDelegate.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mDelegate.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
    }

    private boolean isFinished = false;
    @Override
    public void finish() {
        if(!isFinished){
            ActivityStackManager.getInstance().finishActivity(this);
            isFinished = true;
        }
        super.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mDelegate.onActivityResult(requestCode, resultCode, data);
        if(mResultLisener != null){
            mResultLisener.onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mDelegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (!mDelegate.onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void requestPermissions(
            String[] permissions,
            int requestCode,
            PermissionListener listener) {
        mDelegate.requestPermissions(permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected final ReactNativeHost getReactNativeHost() {
        return mDelegate.getReactNativeHost();
    }

    protected final ReactInstanceManager getReactInstanceManager() {
        return mDelegate.getReactInstanceManager();
    }

    protected final void loadApp(String appKey) {
        mDelegate.loadApp(appKey);
    }


    @Subscribe
    public void onEvent(String event) {
        Toast.makeText(this, event, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

}

