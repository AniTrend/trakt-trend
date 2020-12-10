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
abstract class TraktTrendApplication : Application() {


    override fun onCreate() {
        super.onCreate()
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
}