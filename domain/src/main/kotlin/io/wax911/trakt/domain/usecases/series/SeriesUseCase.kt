package io.wax911.trakt.domain.usecases.series

import co.anitrend.arch.domain.state.UiState
import io.wax911.trakt.domain.common.ISharedUseCase
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.repositories.series.SeriesRepository

abstract class SeriesUseCase<R: UiState<*>>(
    protected val repository: SeriesRepository<R>
) : ISharedUseCase<MediaPayload, R> {


    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    operator fun invoke(param: MediaPayload) = repository.getPopularShows()
}