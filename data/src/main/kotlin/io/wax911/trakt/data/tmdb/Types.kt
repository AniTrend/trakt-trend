package io.wax911.trakt.data.tmdb

import com.uwetrottmann.tmdb2.entities.Movie
import com.uwetrottmann.tmdb2.entities.TvShow
import io.wax911.trakt.data.arch.controller.core.DefaultController
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity

internal typealias TmdbShowController = DefaultController<TvShow, List<TmdbImageEntity>>
internal typealias TmdbMovieController = DefaultController<Movie, List<TmdbImageEntity>>