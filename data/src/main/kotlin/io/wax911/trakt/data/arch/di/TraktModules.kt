package io.wax911.trakt.data.arch.di

import co.anitrend.arch.extension.util.date.SupportDateHelper
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.uwetrottmann.trakt5.TraktV2
import io.wax911.trakt.data.BuildConfig
import io.wax911.trakt.data.arch.database.TraktTrendRoomSource
import io.wax911.trakt.data.movie.koin.movieModules
import io.wax911.trakt.data.show.koin.showModules
import io.wax911.trakt.data.tmdb.koin.tmdbModules
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

private val dataModule = module {
    single {
        TraktTrendRoomSource.newInstance(
            context = androidContext()
        )
    }

    single {
        SupportDateHelper(
            context = androidContext()
        )
    }
}

private val networkModule = module {
    factory {
        ChuckerInterceptor(
            context = androidContext(),
            // The previously created Collector
            collector = ChuckerCollector(
                context = androidContext(),
                // Toggles visibility of the push notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            ),
            // The max body content length in bytes, after this responses will be truncated.
            maxContentLength = 250000L,
            // List of headers to replace with ** in the Chucker UI
            headersToRedact = setOf("Auth-Token")
        )
    }
    factory {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        httpLoggingInterceptor
    }
    single<TraktV2> {
        object : TraktV2(
            BuildConfig.clientId,
            BuildConfig.clientSecret,
            BuildConfig.redirectUri
        ) {
            override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
                super.setOkHttpClientDefaults(builder)
                builder.cache(
                    Cache(
                        File(
                            androidContext().cacheDir,
                            "trakt_cache"
                        ), 10 * 1024 * 1024
                    )
                ).connectTimeout(
                    20, TimeUnit.SECONDS
                ).readTimeout(
                    20, TimeUnit.SECONDS
                ).writeTimeout(
                    20, TimeUnit.SECONDS
                )

                if (BuildConfig.DEBUG) {
                    builder.addInterceptor(get<HttpLoggingInterceptor>())
                        .addInterceptor(get<ChuckerInterceptor>())
                }
            }
        }
    }
}

val dataModules = listOf(
    dataModule, networkModule
) + showModules + movieModules + tmdbModules