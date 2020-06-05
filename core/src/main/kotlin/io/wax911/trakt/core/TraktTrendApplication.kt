package io.wax911.trakt.core

import android.app.Application
import androidx.work.Configuration
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import io.wax911.trakt.core.helpers.coil.fetch.ShowImageFetcher
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Core application
 */
abstract class TraktTrendApplication : Application(), Configuration.Provider {

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    protected abstract fun initializeDependencyInjection()

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    protected open fun plantLoggingTree() {
        val analytics by inject<ISupportAnalytics>()
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(analytics as Timber.Tree)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjection()
        plantLoggingTree()
        Coil.setImageLoader(object : ImageLoaderFactory {
            override fun newImageLoader(): ImageLoader {
                return ImageLoader.Builder(applicationContext)
                    .crossfade(true)
                    // Hardware bitmaps break with our transitions, disable them for now
                    .allowHardware(false)
                    // Since we don't use hardware bitmaps, we can pool bitmaps and use a higher
                    // ratio of memory
                    .bitmapPoolPercentage(0.5)
                    .okHttpClient {
                        get()
                    }
                    .componentRegistry {
                        add(get<ShowImageFetcher>())
                    }.build()
            }
        })
    }

    /**
     * @return The [Configuration] used to initialize WorkManager
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .build()
    }
}