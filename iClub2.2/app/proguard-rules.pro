-dontwarn android.support.**
-dontwarn com.alibaba.fastjson.**


-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep class com.baidu.** { *; }
-keep class com.alibaba.fastjson.** { *; }
-keep class com.jeremy.Customer.view.** { *; }


-keepclassmembers class * {
public <methods>;
}

-keepattributes Signature

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontpreverify
-ignorewarnings
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn



-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}


-keep class com.hp.hpl.sparta.**{*;}
-keep class com.hp.hpl.sparta.xpath.**{*;}
-keep class demo.**{*;}
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}
-keep class com.google.gson.examples.android.model.** { *; }