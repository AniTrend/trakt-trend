package io.wax911.trakt.data.movie.usecase

import androidx.paging.PagedList
import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.arch.data.state.DataState
import io.wax911.trakt.data.movie.repository.FilmPagedRepository
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.usecases.film.FilmUseCase

internal class FilmPagedListUseCase(
    repository: FilmPagedRepository
) : FilmUseCaseType(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}

typealias FilmUseCaseType = FilmUseCase<DataState<PagedList<ISharedMediaWithImage>>>