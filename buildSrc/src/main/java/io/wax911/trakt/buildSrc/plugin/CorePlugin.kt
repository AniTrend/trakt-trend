package io.wax911.trakt.buildSrc.plugin

import io.wax911.trakt.buildSrc.plugin.components.configureAndroid
import io.wax911.trakt.buildSrc.plugin.components.configureDependencies
import io.wax911.trakt.buildSrc.plugin.components.configureOptions
import io.wax911.trakt.buildSrc.plugin.components.configurePlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

open class CorePlugin : Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(target: Project) {
        target.configurePlugins()
        target.configureAndroid()
        target.configureOptions()
        target.configureDependencies()
    }
}