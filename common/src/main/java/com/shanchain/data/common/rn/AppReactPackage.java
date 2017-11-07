package com.shanchain.data.common.rn;

import com.facebook.react.LazyReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.shanchain.data.common.rn.modules.AppManagerModule;
import com.shanchain.data.common.rn.modules.BirthdayPickerModule;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.data.common.rn.modules.PhotoPickerModule;
import com.shanchain.data.common.rn.modules.RNCommonCacheHelper;
import com.shanchain.data.common.rn.modules.RNNetworkModule;
import com.shanchain.data.common.rn.modules.SCBottomDialogModule;
import com.shanchain.data.common.rn.modules.SCDialogModule;
import com.shanchain.data.common.rn.modules.SCInputDialogModule;
import com.shanchain.data.common.rn.modules.SCToastModule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Provider;

/**
 * Created by zhoujian on 2017/8/18.
 */

public class AppReactPackage extends LazyReactPackage {


    private static List<Class<? extends NativeModule>> mNativeModules = new ArrayList<>();
    private static List<Class> mViewManagers = new ArrayList<>();


    public static void registerNativeModule(Class<? extends NativeModule> nativeModule) {
        if (mNativeModules.contains(nativeModule))
            return;
        mNativeModules.add(nativeModule);
    }

    @Override
    public List<ModuleSpec> getNativeModules(final ReactApplicationContext reactContext) {
        List<ModuleSpec> nativeModules = new ArrayList<>();
        nativeModules.add(new ModuleSpec(NavigatorModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new NavigatorModule(reactContext);
            }
        }));

        nativeModules.add(new ModuleSpec(SCBottomDialogModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new SCBottomDialogModule(reactContext);
            }
        }));

        nativeModules.add(new ModuleSpec(SCDialogModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new SCDialogModule(reactContext);
            }
        }));
        nativeModules.add(new ModuleSpec(RNCommonCacheHelper.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new RNCommonCacheHelper(reactContext);
            }
        }));
        nativeModules.add(new ModuleSpec(RNNetworkModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new RNNetworkModule(reactContext);
            }
        }));
        nativeModules.add(new ModuleSpec(SCInputDialogModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new SCInputDialogModule(reactContext);
            }
        }));
        nativeModules.add(new ModuleSpec(SCToastModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new SCToastModule(reactContext);
            }
        }));

        nativeModules.add(new ModuleSpec(BirthdayPickerModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new BirthdayPickerModule(reactContext);
            }
        }));

        nativeModules.add(new ModuleSpec(PhotoPickerModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new PhotoPickerModule(reactContext);
            }
        }));
        nativeModules.add(new ModuleSpec(AppManagerModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new AppManagerModule(reactContext);
            }
        }));


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
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }


    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        // This has to be done via reflection or we break open source.
        return LazyReactPackage.getReactModuleInfoProviderViaReflection(this);
    }
}
