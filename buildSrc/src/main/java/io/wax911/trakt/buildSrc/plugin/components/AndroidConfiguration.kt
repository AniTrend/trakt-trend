package io.wax911.trakt.buildSrc.plugin.components

import com.android.build.gradle.internal.dsl.DefaultConfig
import io.wax911.trakt.buildSrc.common.Versions
import io.wax911.trakt.buildSrc.common.hasCoroutineSupport
import io.wax911.trakt.buildSrc.common.isAppModule
import io.wax911.trakt.buildSrc.common.isFeatureModule
import io.wax911.trakt.buildSrc.plugin.extensions.baseExtension
import io.wax911.trakt.buildSrc.plugin.extensions.libraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.io.File

@Suppress("UnstableApiUsage")
private fun DefaultConfig.applyAdditionalConfiguration(project: Project) {
    if (project.isAppModule())
        applicationId = "io.wax911.trakt"
    else
        consumerProguardFiles.add(File("consumer-rules.pro"))

    if (project.isFeatureModule()) {
        project.libraryExtension().buildFeatures {
            viewBinding = true
        }

        println("Applying vector drawables configuration for module -> ${project.path}")
        vectorDrawables.useSupportLibrary = true
    }
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applyAdditionalConfiguration(project)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
        }
    }

    packagingOptions {
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/*kotlin_module")
    }

    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = false
        isIgnoreTestSources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java) {
        val compilerArgumentOptions = if (hasCoroutineSupport()) {
            listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xuse-experimental=kotlin.Experimental",
                "-Xopt-in=kotlin.Experimental"
            )
        } else {
            listOf(
                "-Xuse-experimental=kotlin.Experimental",
                "-Xopt-in=kotlin.Experimental"
            )
        }
        kotlinOptions {
            allWarningsAsErrors = false
            // Filter out modules that won't be using coroutines
            freeCompilerArgs = compilerArgumentOptions
        }
    }

    tasks.withType(KotlinJvmCompile::class.java) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}