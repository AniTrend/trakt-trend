package io.wax911.trakt.data.show.usecase

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.contract.ISupportRepository
import io.wax911.trakt.data.show.repository.SeriesPagedRepository
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.domain.usecases.series.SeriesUseCase

internal class SeriesPagedListUseCase(
    repository: SeriesPagedRepository
) : SeriesUseCaseType(repository) {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: MediaPayload) =
        repository.getPopularShows()

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}

typealias SeriesUseCaseType = SeriesUseCase<UserInterfaceState<PagedList<ISharedMediaWithImage>>>