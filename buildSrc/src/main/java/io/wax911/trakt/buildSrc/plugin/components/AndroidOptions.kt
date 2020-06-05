package io.wax911.trakt.buildSrc.plugin.components

import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import io.wax911.trakt.buildSrc.plugin.extensions.androidExtensionsExtension
import io.wax911.trakt.buildSrc.plugin.extensions.libraryExtension
import io.wax911.trakt.buildSrc.common.isFeatureModule
import io.wax911.trakt.buildSrc.common.isDataModule
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.util.*

private fun Properties.applyToBuildConfigForBuild(buildType: BuildType) {
    forEach { propEntry ->
        val key = propEntry.key as String
        val value = propEntry.value as String
        println("Adding build config field property -> key: $key value: $value")
        buildType.buildConfigField("String", key, value)
    }
}

private fun NamedDomainObjectContainer<BuildType>.applyConfiguration(project: Project) {
    asMap.forEach { buildTypeEntry ->
        println("Configuring build type -> ${buildTypeEntry.key}")
        val buildType = buildTypeEntry.value

        val secretsFile = project.file(".config/secrets.properties")
        if (secretsFile.exists())
            secretsFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigForBuild(buildType)
                }
            }

        val configurationFile = project.file(".config/configuration.properties")
        if (configurationFile.exists())
            configurationFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigForBuild(buildType)
                }
            }
    }
}

private fun DefaultConfig.applyCompilerOptions(project: Project) {
    println("Adding java compiler options for room on module-> ${project.path}")
    javaCompileOptions {
        annotationProcessorOptions {
            arguments(
                mapOf(
                    "room.schemaLocation" to "${project.projectDir}/schemas",
                    "room.expandingProjections" to "true",
                    "room.incremental" to "true"
                )
            )
        }
    }
}

internal fun Project.configureOptions() {
    if (isDataModule()) {
        libraryExtension().run {
            defaultConfig {
                applyCompilerOptions(this@configureOptions)
            }
            buildTypes {
                applyConfiguration(this@configureOptions)
            }
        }
    }

    if (!isFeatureModule()) return

    println("Applying extension options for feature module -> ${project.path}")
    androidExtensionsExtension().isExperimental = true
}