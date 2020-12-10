// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.wax911.trakt.buildSrc.Libraries

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

subprojects {
    configurations.configureEach {
        resolutionStrategy.eachDependency {
            val group: String = this.requested.group
            val module: String = this.requested.module.name

            if (group == "org.jetbrains.kotlin" && module == "kotlin-stdlib-jre7") {
                // kotlin-stdlib-jre7 no longer exists in 1.4, so we force the
                // kotlin-stdlib module instead
                this.useTarget(Libraries.JetBrains.Kotlin.stdlib)
            }
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
