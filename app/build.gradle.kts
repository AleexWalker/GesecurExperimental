import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    kotlin("plugin.serialization") version Versions.kotlin
    id("androidx.navigation.safeargs.kotlin")
    //id("com.google.gms.google-services")
    //id("com.google.firebase.crashlytics")
    id("com.betomorrow.appcenter")
    //id("com.github.triplet.play")
}

android {
    compileSdkVersion(Sdk.compileSdkVersion)
    buildToolsVersion(Sdk.buildToolsVersion)

    defaultConfig {
        applicationId = "com.gesecur.app"

        minSdkVersion(Sdk.minSdkVersion)
        targetSdkVersion(Sdk.targetSdkVersion)

        versionCode = App.versionCode
        versionName = App.versionName

        base.archivesBaseName = "v$versionName ($versionCode)"
        //versionNameSuffix = "-${getCommitHash()}"
    }

    lintOptions {
        isAbortOnError = false
    }

    buildFeatures {
        viewBinding = true
    }

    signingConfigs {
        val keystoreFile = file("$rootDir/app/keystore.p12")
        val keystorePassword = "Dqpr3g7CeAH8AUFb"

        create("technical") {
            storeFile = keystoreFile
            storePassword = keystorePassword
            keyAlias = "technical"
            keyPassword = "KdX7NVS7P7qywAQW"
        }

        create("vigilant") {
            storeFile = keystoreFile
            storePassword = keystorePassword
            keyAlias = "vigilant"
            keyPassword = "Kex4JqAAk63CJpRa"
        }
    }

    flavorDimensions("app")

    productFlavors {
        create("technical") {
            dimension = "app"

            buildConfigField("String", "API_URL", "\"${Constants.Development.API_URL}\"")
            applicationId = "com.gesecur.app"
            applicationIdSuffix = ".technical"
        }

        create("vigilant") {
            dimension = "app"

            buildConfigField("String", "API_URL", "\"${Constants.Production.API_URL}\"")
            applicationId = "com.gesecur.app"
            applicationIdSuffix = ".vigilant"
        }
    }

    buildTypes {
        named("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true


            resValue("bool", "firebase_crashlytics_enabled", "false")
        }

        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            productFlavors.getByName("technical").signingConfig = signingConfigs.getByName("technical")
            productFlavors.getByName("vigilant").signingConfig = signingConfigs.getByName("vigilant")

            resValue("bool", "firebase_crashlytics_enabled", "true")
        }
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/java")
        java.srcDir("src/main/kotlin")
    }

    sourceSets.getByName("vigilant") {
        java.srcDir("src/vigilant/java")
        java.srcDir("src/vigilant/kotlin")
    }

    sourceSets.getByName("technical") {
        java.srcDir("src/technical/java")
        java.srcDir("src/technical/kotlin")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()

        //Opt-in option for Koin annotation of KoinComponent.
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    // Kotlin
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.kotlinReflect)
    implementation(platform(Libraries.coroutinesBom))
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.coroutinesPlayServices)

    // Android X
    implementation(Libraries.core)
    implementation(Libraries.preference)
    implementation(Libraries.appCompat)
    implementation(Libraries.recyclerView)
    //implementation(Libraries.paging)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleLiveData)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.navigationUi)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.activity)
    implementation(Libraries.fragment)
    implementation(Libraries.startup)
    implementation(Libraries.securityCrypto)
    implementation(Libraries.workManagerJava)
    implementation(Libraries.workManager)

    // Material
    implementation(Libraries.material)

    // OkHttp
    implementation(platform(Libraries.okHttpBom))
    implementation(Libraries.okHttp)
    implementation(Libraries.okHttpLoggingInterceptor)

    // Logging interceptor
    implementation(Libraries.loggingInterceptor) { exclude(group = "org.json", module = "json") }

    // Retrofit
    implementation(Libraries.retrofit)

    // Kotlinx Serialization
    implementation(Libraries.serialization)
    implementation(Libraries.serializationConverter)

    // Koin
    implementation(Libraries.koin)
    implementation(Libraries.koinScope)
    implementation(Libraries.koinViewModel)

    // Arrow
    implementation(Libraries.arrow)

    // Firebase
    implementation(platform(Libraries.firebaseBom))
    implementation(Libraries.firebaseAnalytics)
    implementation(Libraries.firebaseCrashlytics)
    implementation(Libraries.firebaseMessaging)
    implementation(Libraries.firebaseConfig)

    // Glide
    //kapt("android.arch.lifecycle:compiler:1.1.1")
    implementation(Libraries.glide)
    implementation(Libraries.glideOkHttp)
    kapt(Libraries.glideCompiler)

    // Timber
    implementation(Libraries.timber)

    // View binding delegate
    implementation(Libraries.viewBindingDelegate)
    implementation(Libraries.googlPlayServicesLocation)
    implementation(Libraries.googleMapsService)

    implementation ("com.microsoft.appcenter:appcenter-crashes:4.1.0")
    implementation ("androidx.exifinterface:exifinterface:1.3.3")

    // Desugar JDK Libs
    coreLibraryDesugaring(Libraries.desugarJDKLibs)
}

//appcenter {
//    ownerName = "Dekalabs"
//    distributionGroups = listOf("")
//    notifyTesters = true
//
//    apps {
//        apiToken = gradleLocalProperties(rootDir).getProperty("appCenterTokenDevelopment") ?: Credentials.appCenterTokenDevelopment
//        create("development") {
//            dimension = "server"
//            appName = ""
//        }
//
//        create("production") {
//            apiToken = gradleLocalProperties(rootDir).getProperty("appCenterTokenProduction") ?: Credentials.appCenterTokenProduction
//            dimension = "server"
//            appName = ""
//        }
//    }
//}

//play {
//    serviceAccountCredentials.set(file("$rootDir/credentials/gp_api_key.json"))
//    track.set("internal")
//    releaseStatus.set(com.github.triplet.gradle.androidpublisher.ReleaseStatus.COMPLETED)
//    defaultToAppBundles.set(true)
//}

//fun getCommitHash(): String {
//    val byteOut = com.sun.xml.internal.messaging.saaj.util.ByteOutputStream()
//    project.exec {
//        commandLine("git", "rev-parse", "--short", "HEAD")
//        standardOutput = byteOut
//    }
//    return byteOut.toString().trim()
//}