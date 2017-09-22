package com.shanchain.data.common.rn;

import com.facebook.react.LazyReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.modules.vibration.VibrationModule;
import com.facebook.react.modules.websocket.WebSocketModule;
import com.facebook.react.shell.MainReactPackage;
import com.shanchain.data.common.rn.modules.NavigatorModule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Provider;

/**
 * Created by zhoujian on 2017/8/18.
 */

public class AppReactPackage extends MainReactPackage {


    private static List<Class<? extends NativeModule>> mNativeModules = new ArrayList<>();
    private static List<Class> mViewManagers = new ArrayList<>();


    public static void registerNativeModule(Class<? extends NativeModule> nativeModule) {
        if (mNativeModules.contains(nativeModule))
            return;
        mNativeModules.add(nativeModule);
    }

    @Override
    public List<ModuleSpec> getNativeModules(final ReactApplicationContext reactContext) {
        List<ModuleSpec> nativeModules = super.getNativeModules(reactContext);

        nativeModules.addAll(Arrays.asList(
                new ModuleSpec(NavigatorModule.class, new Provider<NativeModule>() {
                    @Override
                    public NativeModule get() {
                        return new NavigatorModule(reactContext);
                    }
                })));

        for (int i = 0; i < nativeModules.size(); i++) {
            final Object module;
            try {
                Constructor constructor = mNativeModules.get(i).getConstructor(ReactApplicationContext.class);
                module = constructor.newInstance(reactContext);
            }catch (Exception e){
                continue;
            }
                new ModuleSpec(mNativeModules.get(i), new Provider<NativeModule>() {
                    @Override
                    public NativeModule get() {
                        return (NativeModule) module;
                    }
                });
        }
        return nativeModules;
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return null;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
}
