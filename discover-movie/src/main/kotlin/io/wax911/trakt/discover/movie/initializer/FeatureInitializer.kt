package io.wax911.trakt.discover.movie.initializer

import android.content.Context
import io.wax911.trakt.core.initializer.contract.AbstractFeatureInitializer
import io.wax911.trakt.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import io.wax911.trakt.discover.movie.di.dynamicModuleHelper

class FeatureInitializer : AbstractFeatureInitializer<Unit>() {
    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        dynamicModuleHelper.loadModules()
    }
}