##### General
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes SourceFile
-keepattributes LineNumberTable

#### Keep companions
#-keepclassmembers class com.gesecur.app.** {
#    public static ** Companion;
#}


##### Keep enums
#-keepclassmembers enum com.gesecur.app.** { *; }
#-keep class com.gesecur.app.** {
#    public enum **;
#}

-keepnames class com.gesecur.app.domain.models.**

##### Retrofit
-keepattributes Signature
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn retrofit2.-KotlinExtensions
-dontwarn retrofit2.BuiltInConverters**


##### OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


#### Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}



##### Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.gesecur.app.**$$serializer { *; }
-keepclassmembers class com.gesecur.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.gesecur.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,allowoptimization class com.google.android.libraries.maps.** { *; }