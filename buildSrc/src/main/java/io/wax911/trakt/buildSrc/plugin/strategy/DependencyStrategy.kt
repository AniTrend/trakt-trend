package io.wax911.trakt.buildSrc.plugin.strategy

import io.wax911.trakt.buildSrc.Libraries
import io.wax911.trakt.buildSrc.common.*
import io.wax911.trakt.buildSrc.common.core
import io.wax911.trakt.buildSrc.common.data
import io.wax911.trakt.buildSrc.common.domain
import io.wax911.trakt.buildSrc.common.navigation
import io.wax911.trakt.buildSrc.plugin.extensions.baseExtension
import io.wax911.trakt.buildSrc.plugin.extensions.containsLibraryPlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(
    private val module: String
) {
    private fun DependencyHandler.applyDefaultDependencies() {
        add("implementation", Libraries.JetBrains.Kotlin.stdlib)
        add("implementation", Libraries.timber)

        add("testImplementation", Libraries.junit)
        add("testImplementation", Libraries.mockk)
    }

    private fun DependencyHandler.applyAndroidTestDependencies() {
        add("androidTestImplementation", Libraries.AndroidX.Test.core)
        add("androidTestImplementation", Libraries.AndroidX.Test.rules)
        add("androidTestImplementation", Libraries.AndroidX.Test.runner)
        add("androidTestImplementation", Libraries.AndroidX.Test.Extension.junitKtx)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataCoreKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.runTimeKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        add("implementation", Libraries.JetBrains.Coroutines.android)
        add("implementation", Libraries.JetBrains.Coroutines.core)
        add("testImplementation", Libraries.JetBrains.Coroutines.test)
    }

    private fun DependencyHandler.applyKoinDependencies() {
        add("implementation", Libraries.Koin.core)
        add("implementation", Libraries.Koin.extension)
        add("testImplementation", Libraries.Koin.test)
        if (module != data || module != core) {
            add("implementation", Libraries.Koin.AndroidX.scope)
            add("implementation", Libraries.Koin.AndroidX.fragment)
            add("implementation", Libraries.Koin.AndroidX.viewmodel)
        }
    }

    private fun DependencyHandler.applyOtherDependencies() {
        when (module) {
            app -> {
                add("implementation", Libraries.AniTrend.Arch.recycler)
                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.theme)
                add("implementation", Libraries.AniTrend.Arch.core)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)
                add("implementation", Libraries.AniTrend.Arch.ui)
            }
            core -> {
                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.theme)
                add("implementation", Libraries.AniTrend.Arch.core)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)
                add("implementation", Libraries.AniTrend.Arch.ui)
            }
            data -> {
                add("implementation", Libraries.AndroidX.Paging.common)
                add("implementation", Libraries.AndroidX.Paging.runtime)
                add("implementation", Libraries.AndroidX.Paging.runtimeKtx)
                add("implementation", Libraries.AndroidX.Room.runtime)
                add("implementation", Libraries.AndroidX.Room.ktx)
                add("kapt", Libraries.AndroidX.Room.compiler)

                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)

                add("implementation", Libraries.Square.Retrofit.retrofit)
                add("implementation", Libraries.Square.Retrofit.gsonConverter)
                add("implementation", Libraries.Square.OkHttp.logging)
            }
            domain -> {
                add("implementation", Libraries.AniTrend.Arch.domain)
            }
            navigation -> {
                add("implementation", Libraries.AndroidX.Activity.activity)
                add("implementation", Libraries.AndroidX.Fragment.fragment)
            }
        }
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        if (module != domain || module != navigation) {
            handler.applyLifeCycleDependencies()
            handler.applyAndroidTestDependencies()
            handler.applyCoroutinesDependencies()
            handler.applyKoinDependencies()
            handler.applyOtherDependencies()
        }
    }
}