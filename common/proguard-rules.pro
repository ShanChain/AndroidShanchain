# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/flyye/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
#-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dump class_files.txt
 -printseeds seeds.txt
 -printusage unused.txt
  -printmapping mapping.txt


  -keep public class * extends android.app.Activity
  -keep public class * extends android.app.Application
  -keep public class * extends android.app.Service
  -keep public class * extends android.content.BroadcastReceiver
  -keep public class * extends android.content.ContentProvider
  -keep public class * extends android.app.backup.BackupAgentHelper
  -keep public class * extends android.preference.Preference
  -keep public class com.android.vending.licensing.ILicensingService

  #不提示V4包下错误警告
  -dontwarn android.support.v4.**
  #保持下面的V4兼容包的类不被混淆
  -keep class android.support.v4.**{*;}

  -keepclasseswithmembernames class * {
          native <methods>;
  }


-keep public class * extends android.view.View{
        *** get*();
        void set*(***);
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet,int);
}

   -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
}

 #不混淆Parcelable和它的实现子类，还有Creator成员变量
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #不混淆Serializable和它的实现子类、其成员变量
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

     #使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
        -keepclassmembers class * {
            public <init>(org.json.JSONObject);
        }

         # ==================环信混淆start=================
            -keep class com.hyphenate.** {*;}
            -dontwarn  com.hyphenate.**
            # ==================环信end======================

            # ==================bugly start==================
            -dontwarn com.tencent.bugly.**
            -keep public interface com.tencent.**
            -keep public class com.tencent.** {*;}
            -keep public class com.tencent.bugly.**{*;}
            # ==================bugly end====================

            # ===============百度定位 start====================
            -keep class vi.com.gdi.** { *; }
            -keep public class com.baidu.** {*;}
            -keep public class com.mobclick.** {*;}
            -dontwarn com.baidu.mapapi.utils.*
            -dontwarn com.baidu.platform.comapi.b.*
            -dontwarn com.baidu.platform.comapi.map.*
            # ===============百度定位 end======================
  # ==================picasso框架 start===============
    -keep class com.parse.*{ *; }
    -dontwarn com.parse.**
    -dontwarn com.squareup.picasso.**
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    # ==================picasso end====================

    # ==================EventBus start=================
    -keep class org.greenrobot.** {*;}
    -keep class de.greenrobot.** {*;}
    -keepclassmembers class ** {
        public void onEvent*(**);
        void onEvent*(**);
    }
    # ==================EventBus end===================

    # ==================okhttp start===================
    -dontwarn com.squareup.okhttp.**
    -keep class com.squareup.okhttp.** { *;}
    -dontwarn okio.**
    -keep class okio.**{*;}
    -keep interface okio.**{*;}
    # ==================okhttp end=====================

    #避免混淆属性动画兼容库
        -dontwarn com.nineoldandroids.*
        -keep class com.nineoldandroids.** { *;}

        #不混淆泛型
        -keepattributes Signature

        #避免混淆注解类
        -dontwarn android.annotation
        -keepattributes *Annotation*

        #避免混淆内部类
        -keepattributes InnerClasses

        #避免混淆实体类，修改成你对应的包名
        -keep class com.wyk.test.bean.** { *; }
        -keep class com.wyk.test.event.** { *; }
        -keep public class com.wyk.test.utils.eventbus.** { *;}

        #避免混淆Rxjava/RxAndroid
        -dontwarn sun.misc.**
        -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
         long producerIndex;
         long consumerIndex;
        }
        -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
         rx.internal.util.atomic.LinkedQueueNode producerNode;
        }
        -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
         rx.internal.util.atomic.LinkedQueueNode consumerNode;
        }

    #避免混淆js相关的接口
    -keepattributes *JavascriptInterface*
    -keep class com.wyk.test.js.** { *; }

#-keep public class com.shanchain.data.common.**{
#      *;
#}
-keep public class com.shanchain.data.common.rn.modules.**{
     *;
}
-keep public class com.shanchain.data.common.base.**{
     *;
}
-keep public class com.shanchain.data.common.bean.**{
     *;
}
-keep public class com.shanchain.data.common.eventbus.**{
     *;
}

  -keep class com.shanchain.data.common.cache.** {
         *;
         }
  -keep class com.shanchain.data.common.net.** {
          public *;
         }
  -keep class com.shanchain.data.common.ui.** {
         public   *;
         }
  -keep class com.shanchain.data.common.utils.** {
          public *;
           }



