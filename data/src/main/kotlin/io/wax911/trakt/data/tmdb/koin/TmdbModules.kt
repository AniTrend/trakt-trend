package io.wax911.trakt.data.tmdb.koin

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.uwetrottmann.tmdb2.Tmdb
import io.wax911.trakt.data.BuildConfig
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.arch.extensions.tmdb
import io.wax911.trakt.data.arch.extensions.defaultController
import io.wax911.trakt.data.tmdb.converter.TmdbEntityConverter
import io.wax911.trakt.data.tmdb.converter.TmdbModelConverter
import io.wax911.trakt.data.tmdb.mapper.TmdbMovieMapper
import io.wax911.trakt.data.tmdb.mapper.TmdbShowMapper
import io.wax911.trakt.data.tmdb.source.TmdbSourceImpl
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

private val networkModule = module {
    single<Tmdb> {
        object: Tmdb(BuildConfig.tmdbSecret) {
            override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
                super.setOkHttpClientDefaults(builder)
                builder.cache(
                    Cache(
                        File(
                            androidContext().cacheDir,
                            "tmdb_cache"
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

private val dataSourceModule = module {
    factory<TmdbSource> {
        TmdbSourceImpl(
            localSource = db().tmdbDao(),
            remoteTvSource = tmdb().tvService(),
            remoteMovieSource = tmdb().moviesService(),
            showController = defaultController(
                mapper = get<TmdbShowMapper>()
            ),
            movieController = defaultController(
                mapper = get<TmdbMovieMapper>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val converterModule = module {
    factory {
        TmdbEntityConverter()
    }
    factory {
        TmdbModelConverter()
    }
}

private val mapperModule = module {
    factory {
        TmdbMovieMapper(
            localSource = db().tmdbDao(),
            converter = get()
        )
    }
    factory {
        TmdbShowMapper(
            localSource = db().tmdbDao(),
            converter = get()
        )
    }
}

internal val tmdbModules = listOf(
    networkModule, converterModule, mapperModule, dataSourceModule
)