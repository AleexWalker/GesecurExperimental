plugins {
    id("com.github.ben-manes.versions") version (Versions.gradleVersions)
}

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        jcenter()
    }

    dependencies {
        classpath(Plugins.androidGradle)
        classpath(Plugins.kotlinGradle)
        classpath(Plugins.googleServices)
        classpath(Plugins.navigationArgs)
        classpath(Plugins.gradleVersions)
        //classpath(Plugins.firebaseCrashlytics)
        classpath(Plugins.appCenterGradle)
        classpath(Plugins.playPublisher)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
        maven(url = "https://jitpack.io")
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}