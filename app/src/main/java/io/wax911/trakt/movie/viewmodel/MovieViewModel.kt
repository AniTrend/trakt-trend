package io.wax911.trakt.movie.viewmodel

import androidx.paging.PagedList
import co.anitrend.arch.core.viewmodel.SupportPagingViewModel
import io.wax911.trakt.data.entitiy.movie.MovieEntity
import io.wax911.trakt.data.usecase.movie.MoviePagedListUseCase
import io.wax911.trakt.domain.usecases.movie.TraktMovieUseCase

class MovieViewModel(
    override val useCase: MoviePagedListUseCase
) : SupportPagingViewModel<TraktMovieUseCase.Payload, PagedList<MovieEntity>>()