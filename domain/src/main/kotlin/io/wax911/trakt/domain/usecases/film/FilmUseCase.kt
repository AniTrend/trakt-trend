package io.wax911.trakt.domain.usecases.film

import co.anitrend.arch.domain.state.UiState
import io.wax911.trakt.domain.common.ISharedUseCase
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.repositories.film.FilmRepository

abstract class FilmUseCase<R: UiState<*>>(
    protected val repository: FilmRepository<R>
) : ISharedUseCase<MediaPayload, R> {


    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    operator fun invoke(param: MediaPayload) =
        repository.getPopularMovies()
}