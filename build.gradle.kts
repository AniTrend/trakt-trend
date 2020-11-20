// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
plugins {
    id("com.github.ben-manes.versions")
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(io.wax911.trakt.buildSrc.Libraries.Android.Tools.buildGradle)
        classpath(io.wax911.trakt.buildSrc.Libraries.Google.Services.googleServices)
        classpath(io.wax911.trakt.buildSrc.Libraries.Google.Firebase.Crashlytics.Gradle.plugin)

        classpath(io.wax911.trakt.buildSrc.Libraries.JetBrains.Kotlin.Gradle.plugin)
        classpath(io.wax911.trakt.buildSrc.Libraries.AndroidX.Navigation.safeArgsGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = java.net.URI("https://jitpack.io")
        }
    }
}

tasks.named(
    "dependencyUpdates",
    DependencyUpdatesTask::class.java
).configure {
    checkForGradleUpdate = false
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}
