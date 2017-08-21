package com.shanchain.shandata.rn.module;

import com.facebook.react.LazyReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Provider;

/**
 * Created by zhoujian on 2017/8/18.
 */

public class AppReactPackage extends LazyReactPackage {

    public AppReactPackage() {
    }

    @Override
    public List<ModuleSpec> getNativeModules(final ReactApplicationContext reactContext) {
        return Arrays.asList(
            new ModuleSpec(SCToastModule.class, new Provider<NativeModule>() {
                @Override
                public NativeModule get() {
                    return new SCToastModule(reactContext);
                }
            })

        );
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
