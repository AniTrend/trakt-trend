package io.wax911.trakt.data.show.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import io.wax911.trakt.data.show.SeriesPageRepository
import io.wax911.trakt.data.show.SeriesUseCaseType

internal class SeriesPagedListUseCase(
    repository: SeriesPageRepository
) : SeriesUseCaseType(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}