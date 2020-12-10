package io.wax911.trakt.data.movie

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import com.uwetrottmann.trakt5.entities.Movie
import com.uwetrottmann.trakt5.entities.Show
import com.uwetrottmann.trakt5.services.Movies
import com.uwetrottmann.trakt5.services.Shows
import io.wax911.trakt.data.arch.controller.core.DefaultController
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.repositories.film.FilmRepository
import io.wax911.trakt.domain.usecases.film.FilmUseCase

internal typealias MovieRemoteSource = Movies

internal typealias FilmPageRepository = FilmRepository<DataState<PagedList<ISharedMediaWithImage>>>

internal typealias PopularMovieController = DefaultController<List<Movie>, List<ShowEntity>>

typealias FilmUseCaseType = FilmUseCase<DataState<PagedList<ISharedMediaWithImage>>>