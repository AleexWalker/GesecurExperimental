object App {
    const val versionCode = 2
    const val versionName = "2.8"
}

object Sdk {
    const val minSdkVersion = 23
    const val targetSdkVersion = 30
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
}

object Versions {
    // Plugins
    const val kotlin = "1.5.0"
    const val androidGradle = "4.1.3"
    const val googleServices = "4.3.5"
    const val gradleVersions = "0.38.0"
    const val firebaseCrashlytics = "2.5.1"
    const val appCenterGradle = "1.2.1"
    const val playPublisher = "3.3.0"

    // Android X
    const val core = "1.5.0-rc01"
    const val preference = "1.1.1"
    const val appCompat = "1.3.0-rc01"
    const val recyclerView = "1.2.0-rc01"
    //const val paging = "3.0.0-beta03"
    const val constraintLayout = "2.0.4"
    const val lifecycle = "2.3.1"
    const val navigation = "2.3.4"
    const val activity = "1.2.2"
    const val fragment = "1.3.3"
    const val startup = "1.0.0"
    const val securityCrypto = "1.1.0-alpha02"
    const val workManager = "2.5.0"

    // Google
    const val material = "1.3.0"
    const val firebase = "26.7.0"
    const val maps = "3.1.0-beta"
    const val googleMaps = "17.0.0"

    // Libraries
    const val coroutines = "1.4.3"
    const val okHttp = "4.9.1"
    const val loggingInterceptor = "3.1.0"
    const val retrofit = "2.9.0"
    const val serialization = "1.2.0"
    const val serializationConverter = "0.8.0"
    const val koin = "2.2.0-alpha-1"
    const val arrow = "0.11.0"
    const val glide = "4.12.0"
    const val timber = "4.7.1"
    const val viewBindingDelegate = "1.4.5"
    const val desugarJDKLibs = "1.0.10"
    const val googleLibs = "18.0.0"
}

object Plugins {
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val navigationArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}"
    const val appCenterGradle = "gradle.plugin.com.betomorrow.gradle:appcenter-plugin:${Versions.appCenterGradle}"
    const val playPublisher = "com.github.triplet.gradle:play-publisher:${Versions.playPublisher}"
}

object Libraries {
    // Kotlin
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val coroutinesBom = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.coroutines}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
    const val coroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"

    // Android X
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val preference = "androidx.preference:preference-ktx:${Versions.preference}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    //const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val startup = "androidx.startup:startup-runtime:${Versions.startup}"
    const val securityCrypto = "androidx.security:security-crypto-ktx:${Versions.securityCrypto}"
    const val workManagerJava = "androidx.work:work-runtime:${Versions.workManager}"
    const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"

    // Material
    const val material = "com.google.android.material:material:${Versions.material}"

    // OkHttp
    const val okHttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"
    const val okHttp = "com.squareup.okhttp3:okhttp"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor"

    // Logging Interceptor
    const val loggingInterceptor = "com.github.ihsanbal:LoggingInterceptor:${Versions.loggingInterceptor}"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"

    // Kotlinx Serialization
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
    const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serializationConverter}"

    // Koin
    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

    // Arrow
    const val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"

    // Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
    const val firebaseConfig = "com.google.firebase:firebase-config-ktx"

    // Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val glideOkHttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"

    const val googlPlayServicesLocation = "com.google.android.gms:play-services-location:${Versions.googleLibs}"
    const val googleMaps = "com.google.android.libraries.maps:maps:${Versions.maps}"
    const val googleMapsService = "com.google.android.gms:play-services-maps:${Versions.googleMaps}"

    // Timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // View binding delegate
    const val viewBindingDelegate = "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingDelegate}"

    // Desugar JDK Libs
    const val desugarJDKLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJDKLibs}"

}