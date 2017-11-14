# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/iliyan/programs/android_sdk/tools/proguard/proguard-android.txt
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

#Firebase Authentication and Realtime DB
-keepattributes Signature

#Firebase Auth
-keepattributes *Annotation*


#Firebase DB---------------------------------------------------------


# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.babushka.slav_squad.persistence.database.model.** {
  *;
}
#Firebase DB---------------------------------------------------------

#Glide -------------------------------------------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#Glide -------------------------------------------------------------------------

#uCrop -------------------------------------
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#uCrop -------------------------------------

#BlurKit
-keep class com.wonderkiln.blurkit.** { *; }

-dontwarn android.support.v8.renderscript.*
-keepclassmembers class android.support.v8.renderscript.RenderScript {
  native *** rsn*(...);
  native *** n*(...);
}