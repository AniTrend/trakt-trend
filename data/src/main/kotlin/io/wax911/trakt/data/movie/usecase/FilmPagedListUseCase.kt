package io.wax911.trakt.data.movie.usecase

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.contract.ISupportRepository
import io.wax911.trakt.data.movie.repository.FilmPagedRepository
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.domain.usecases.film.FilmUseCase

internal class FilmPagedListUseCase(
    repository: FilmPagedRepository
) : FilmUseCaseType(repository) {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: MediaPayload) =
        repository.getPopularMovies()

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}

typealias FilmUseCaseType = FilmUseCase<UserInterfaceState<PagedList<ISharedMediaWithImage>>>