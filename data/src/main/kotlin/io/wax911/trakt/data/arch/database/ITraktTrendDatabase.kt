package io.wax911.trakt.data.arch.database

import io.wax911.trakt.data.show.datasource.local.ShowDao
import io.wax911.trakt.data.tmdb.datasource.local.TmdbDao

internal interface ITraktTrendDatabase  {
    fun showDao(): ShowDao
    fun tmdbDao(): TmdbDao
}