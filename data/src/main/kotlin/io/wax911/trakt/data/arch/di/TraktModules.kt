package io.wax911.trakt.data.arch.di

import co.anitrend.arch.extension.util.date.SupportDateHelper
import com.uwetrottmann.trakt5.TraktV2
import io.wax911.trakt.data.BuildConfig
import io.wax911.trakt.data.arch.database.TraktTrendRoomSource
import io.wax911.trakt.data.movie.koin.movieModules
import io.wax911.trakt.data.show.koin.showModules
import io.wax911.trakt.data.tmdb.koin.tmdbModules
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

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
    single {
        TraktV2(
            BuildConfig.clientId,
            BuildConfig.clientSecret,
            BuildConfig.redirectUri
        )
    }
}

val dataModules = listOf(
    dataModule, networkModule
) + showModules + movieModules + tmdbModules