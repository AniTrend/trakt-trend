package io.wax911.trakt.data.show.usecase

import androidx.paging.PagedList
import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.arch.data.state.DataState
import io.wax911.trakt.data.show.repository.SeriesPagedRepository
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.usecases.series.SeriesUseCase

internal class SeriesPagedListUseCase(
    repository: SeriesPagedRepository
) : SeriesUseCaseType(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}

typealias SeriesUseCaseType = SeriesUseCase<DataState<PagedList<ISharedMediaWithImage>>>