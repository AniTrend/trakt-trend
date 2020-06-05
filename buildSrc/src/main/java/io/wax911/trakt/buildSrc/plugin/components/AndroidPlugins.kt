package io.wax911.trakt.buildSrc.plugin.components

import io.wax911.trakt.buildSrc.common.app
import io.wax911.trakt.buildSrc.common.data
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

private fun addAndroidPlugin(name: String, pluginContainer: PluginContainer) {
    if (name == app)
        pluginContainer.apply("com.android.application")
    else pluginContainer.apply("com.android.library")
}

private fun addAnnotationProcessor(name: String, pluginContainer: PluginContainer) {
    if (name == data || name == app)
        pluginContainer.apply("kotlin-kapt")
}

internal fun Project.configurePlugins() {
    addAndroidPlugin(project.name, plugins)
    plugins.apply("kotlin-android")
    plugins.apply("kotlin-android-extensions")
    addAnnotationProcessor(project.name, plugins)
}