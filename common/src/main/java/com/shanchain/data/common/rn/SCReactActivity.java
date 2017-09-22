package com.shanchain.data.common.rn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.rn.modules.NavigatorModule;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;

/**
 * Created by flyye on 2017/9/20.
 */

public class SCReactActivity extends ReactActivity{

    private static final String TAG = SCReactActivity.class.getSimpleName();
    private ReactRootView rootView;
    private ReactInstanceManager reactInstanceManager;

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

        reactInstanceManager = RNManager.getInstance().getReactInstanceManager();
        Bundle bundle = getIntent().getBundleExtra(NavigatorModule.REACT_INIT_PROPS);
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            castParcelableToBundle(bundle);
        }
        bundle.putString("screen", screenName);
        rootView = new ReactRootView(this);
        rootView.startReactApplication(reactInstanceManager, "App", bundle);
        setContentView(rootView);
    }

    private void castParcelableToBundle(Bundle bundle) {
        Iterator<String> iterator = bundle.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = bundle.get(key);
            if (value instanceof Bundle) {
                castParcelableToBundle((Bundle) value);
            } else if (value instanceof Parcelable[]) {
                int len = ((Parcelable[]) value).length;
                Parcelable[] parcelables = (Parcelable[]) value;
                Bundle[] bundles = new Bundle[len];
                for (int i = 0; i < len; i++) {
                    bundles[i] = (Bundle) parcelables[i];
                    castParcelableToBundle(bundles[i]);
                }
                bundle.putParcelableArray(key, bundles);
            }
        }
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
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }



}
