package io.wax911.trakt.buildSrc.common

import org.gradle.api.Project

internal const val app = "app"
internal const val core = "core"
internal const val data = "data"
internal const val domain = "domain"
internal const val navigation = "navigation"

internal const val discoverMovie = "discover-movie"
internal const val discoverShow = "discover-show"

internal val baseModules = listOf(app, core, data, domain, navigation)
internal val featureModules = listOf(discoverMovie, discoverShow)

fun Project.isAppModule() = name == app
fun Project.isDataModule() = name == data
fun Project.isDomainModule() = name == domain
fun Project.isCoreModule() = name == core
fun Project.isNavigationModule() = name == navigation
fun Project.isFeatureModule() = !baseModules.contains(name)
fun Project.hasCoroutineSupport() = name != navigation || name != domain