package io.wax911.trakt.initializer

import android.content.Context
import androidx.startup.Initializer
import io.wax911.trakt.core.initializer.KoinInitializer
import io.wax911.trakt.core.initializer.contract.AbstractInitializer
import io.wax911.trakt.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import io.wax911.trakt.koin.appModules

class AppInitializer : AbstractInitializer<Unit>() {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        appModules.loadModules()
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another
     * [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(KoinInitializer::class.java)
}