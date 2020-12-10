package io.wax911.trakt.data.arch.database

import io.wax911.trakt.data.show.datasource.local.ShowLocalSource
import io.wax911.trakt.data.tmdb.datasource.local.TmdbLocalSource

internal interface ITraktTrendDatabase  {
    fun showDao(): ShowLocalSource
    fun tmdbDao(): TmdbLocalSource
}