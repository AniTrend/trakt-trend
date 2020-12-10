package io.wax911.trakt.data.movie.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import io.wax911.trakt.data.movie.FilmPageRepository
import io.wax911.trakt.data.movie.FilmUseCaseType

internal class FilmPagedListUseCase(
    repository: FilmPageRepository
) : FilmUseCaseType(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}