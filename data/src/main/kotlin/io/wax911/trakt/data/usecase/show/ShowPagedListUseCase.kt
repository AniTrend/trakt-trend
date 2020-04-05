package io.wax911.trakt.data.usecase.show

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import io.wax911.trakt.data.entitiy.show.ShowEntity
import io.wax911.trakt.data.repository.show.ShowPagedRepository
import io.wax911.trakt.domain.usecases.show.TraktShowUseCase

class ShowPagedListUseCase(
    repository: ShowPagedRepository
) : TraktShowUseCase<UserInterfaceState<PagedList<ShowEntity>>>(repository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        (repository as ShowPagedRepository).onCleared()
    }
}