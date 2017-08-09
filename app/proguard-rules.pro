# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\develope\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes EnclosingMethod

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#百度地图混淆
#-libraryjars libs/BaiduLBS_Android.jar
#-keep class com.baidu.** { *; }
#-keep class vi.com.gdi.bgl.android.**{*;}

#友盟代码混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.shanchain.shandata.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# umeng消息推送
#-dontwarn com.taobao.**
#-dontwarn anet.channel.**
#-dontwarn anetwork.channel.**
#-dontwarn org.android.**
#-dontwarn org.apache.thrift.**
#-dontwarn com.xiaomi.**
#-dontwarn com.huawei.**
#
#-keepattributes *Annotation*
#
#-keep class com.taobao.** {*;}
#-keep class org.android.** {*;}
#-keep class anet.channel.** {*;}
#-keep class com.umeng.** {*;}
#-keep class com.xiaomi.** {*;}
#-keep class com.huawei.** {*;}
#-keep class org.apache.thrift.** {*;}
#
#-keep class com.alibaba.sdk.android.**{*;}
#-keep class com.ut.**{*;}
#-keep class com.ta.**{*;}
#
#-keep public class **.R$*{
#   public static final int *;
#}
#
##（可选）避免Log打印输出
#-assumenosideeffects class android.util.Log {
#   public static *** v(...);
#   public static *** d(...);
#   public static *** i(...);
#   public static *** w(...);
#}