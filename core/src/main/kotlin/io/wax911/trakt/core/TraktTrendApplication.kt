package io.wax911.trakt.core

import android.app.Application
import androidx.work.Configuration
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import io.wax911.trakt.core.analytics.AnalyticsLogger
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Core application
 */
abstract class TraktTrendApplication : Application(), Configuration.Provider {

    private val analytics by inject<ISupportAnalytics>()

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    protected abstract fun initializeDependencyInjection()

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    private fun plantLoggingTree() {
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(analytics as AnalyticsLogger)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjection()
        plantLoggingTree()
    }

    /**
     * @return The [Configuration] used to initialize WorkManager
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .build()
    }
}