package io.wax911.trakt.data.tmdb.koin

import com.uwetrottmann.tmdb2.Tmdb
import io.wax911.trakt.data.BuildConfig
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.arch.extensions.tmdb
import io.wax911.trakt.data.tmdb.repository.TmdbRepository
import io.wax911.trakt.data.tmdb.source.TmdbSourceImpl
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.data.tmdb.usecase.TmdbUseCaseManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

private val networkModule = module {
    single<Tmdb> {
        object: Tmdb(BuildConfig.tmdbSecret) {
            override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
                super.setOkHttpClientDefaults(builder)
                builder.apply {
                    cache(
                        Cache(
                            File(
                                androidContext().cacheDir,
                                "tmdb_cache"
                            ), 10 * 1024 * 1024
                        )
                    )
                    connectTimeout(20, TimeUnit.SECONDS)
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(20, TimeUnit.SECONDS)
                }
            }
        }
    }
}

private val dataSourceModule = module {
    factory<TmdbSource> {
        TmdbSourceImpl(
            localSource = db().tmdbDao(),
            remoteSource = tmdb().tvService(),
            connectivity = get(),
            dispatchers = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        TmdbRepository(
            source = get()
        )
    }
}

private val useCaseModule = module {
    single {
        TmdbUseCaseManager(
            source = tmdb().configurationService()
        )
    }
}

internal val tmdbModules = listOf(
    networkModule, dataSourceModule, repositoryModule, useCaseModule
)