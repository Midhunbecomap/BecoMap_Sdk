# =============================================
# Basic Android rules
# =============================================
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

# =============================================
# SDK Public API Protection
# =============================================
-keep class com.becomap.sdk.UI.Becomap { *; }
-keep interface com.becomap.sdk.UI.Becomap$BecomapCallback { *; }

# =============================================
# Model Class Protection
# =============================================

# Keep all model classes and their fields (for reflection/Gson)
-keep class com.becomap.sdk.model.** {
    *;
}

# Keep constructors for model classes
-keepclassmembers class com.becomap.sdk.model.** {
    public <init>(...);
}

# Keep enum values in models
-keepclassmembers enum com.becomap.sdk.model.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Specific model protection
-keep class com.becomap.sdk.model.BuildingModel { *; }
-keep class com.becomap.sdk.model.FloorModel { *; }
-keep class com.becomap.sdk.model.LocationModel { *; }
-keep class com.becomap.sdk.model.Route { *; }
-keep class com.becomap.sdk.model.SearchResult { *; }
-keep class com.becomap.sdk.model.BCHappenings { *; }

# =============================================
# Serialization/Deserialization Support
# =============================================

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }

# Prevent obfuscation of types that are used as generic types
-keep class com.becomap.sdk.model.** { *; }

# Keep serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# =============================================
# WebView/JavaScript Interface Protection
# =============================================
-keepclassmembers class com.becomap.sdk.UI.Becomap$WebAppInterface {
    @android.webkit.JavascriptInterface <methods>;
    public void postMessage(java.lang.String);
}

# =============================================
# ViewModel and Configuration Protection
# =============================================
-keep class com.becomap.sdk.ViewModel.BecomapViewModel { *; }
-keep class com.becomap.sdk.Config.BecoWebInterface { *; }

# =============================================
# Retrofit/Network Protection
# =============================================
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepclassmembers,allowobfuscation class * {
    @retrofit2.http.* <methods>;
}

# =============================================
# Protocol Buffers Protection
# =============================================
-keep class com.google.protobuf.** { *; }
-keep class com.becomap.sdk.**Proto { *; }

# =============================================
# Debugging Information (Optional)
# =============================================
# -keepattributes SourceFile,LineNumberTable
# -renamesourcefileattribute SourceFile

# =============================================
# General Optimization Rules
# =============================================
-dontwarn java.lang.**
-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**
-dontwarn kotlin.**
-dontwarn com.google.protobuf.**