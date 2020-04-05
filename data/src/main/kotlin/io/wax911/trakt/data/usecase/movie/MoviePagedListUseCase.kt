package io.wax911.trakt.data.usecase.movie

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import io.wax911.trakt.data.entitiy.movie.MovieEntity
import io.wax911.trakt.data.repository.movie.MoviePagedRepository
import io.wax911.trakt.domain.usecases.movie.TraktMovieUseCase

class MoviePagedListUseCase(
    repository: MoviePagedRepository
) : TraktMovieUseCase<UserInterfaceState<PagedList<MovieEntity>>>(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        (repository as MoviePagedRepository).onCleared()
    }
}