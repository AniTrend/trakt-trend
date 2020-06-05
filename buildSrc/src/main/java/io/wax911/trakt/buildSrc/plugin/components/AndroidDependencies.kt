package io.wax911.trakt.buildSrc.plugin.components

import io.wax911.trakt.buildSrc.Libraries
import io.wax911.trakt.buildSrc.common.core
import io.wax911.trakt.buildSrc.common.data
import io.wax911.trakt.buildSrc.common.domain
import io.wax911.trakt.buildSrc.common.navigation
import io.wax911.trakt.buildSrc.common.isFeatureModule
import io.wax911.trakt.buildSrc.common.isAppModule
import io.wax911.trakt.buildSrc.common.featureModules
import io.wax911.trakt.buildSrc.plugin.strategy.DependencyStrategy
import org.gradle.api.Project

private fun Project.applyPlatformDependencies() {
    println("Applying common dependencies for feature module -> $path")

    dependencies.add("implementation", Libraries.AniTrend.Arch.ui)
    dependencies.add("implementation", Libraries.AniTrend.Arch.ext)
    dependencies.add("implementation", Libraries.AniTrend.Arch.core)
    dependencies.add("implementation", Libraries.AniTrend.Arch.data)
    dependencies.add("implementation", Libraries.AniTrend.Arch.theme)
    dependencies.add("implementation", Libraries.AniTrend.Arch.domain)
    dependencies.add("implementation", Libraries.AniTrend.Arch.recycler)

    dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)

    dependencies.add("implementation", Libraries.AndroidX.Paging.runtime)
    dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", project(":$core"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$navigation"))
}

private fun Project.applyFeatureModules() {
    featureModules.forEach { module ->
        println("Adding runtimeOnly dependency :$module -> ${project.path}")
        dependencies.add("runtimeOnly", project(":$module"))
    }
    dependencies.add("implementation", project(":$navigation"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$core"))
}

internal fun Project.configureDependencies() {
    val dependencyStrategy = DependencyStrategy(project.name)
    dependencies.add("implementation",
        fileTree("libs") {
            include("*.jar")
        }
    )
    dependencyStrategy.applyDependenciesOn(dependencies)

    if (isFeatureModule()) applyPlatformDependencies()
    if (isAppModule()) applyFeatureModules()
}