package io.wax911.trakt.data.datasource.remote.movie.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.data.datasource.local.query.MovieDao
import io.wax911.trakt.data.datasource.remote.common.MediaPagedSource
import io.wax911.trakt.data.entitiy.movie.MovieEntity

abstract class TraktMoviePagedSource(
    dispatchers: SupportDispatchers
) : MediaPagedSource<MovieEntity>(dispatchers) {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val popularMovieObservable: ISourceObservable<Nothing?, PagedList<MovieEntity>>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val trendingMovieObservable: ISourceObservable<Nothing?, PagedList<MovieEntity>>
}