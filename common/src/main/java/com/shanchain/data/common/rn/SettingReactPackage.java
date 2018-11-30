//package com.shanchain.data.common.rn;
//
//import com.facebook.react.ReactPackage;
//import com.facebook.react.bridge.NativeModule;
//import com.facebook.react.bridge.ReactApplicationContext;
//import com.facebook.react.modules.intent.IntentModule;
//import com.facebook.react.uimanager.ViewManager;
//import com.shanchain.data.common.rn.modules.SettingModule;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class SettingReactPackage implements ReactPackage {
//    @Override
//    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
//        List<NativeModule> modules = new ArrayList<>();
//        modules.add(new SettingModule(reactContext));
//        return modules;
//    }
//
//    @Override
//    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
//        return Collections.emptyList();
//    }
//}
