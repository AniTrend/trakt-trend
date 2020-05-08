package io.wax911.trakt.discover.movie.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import androidx.paging.PagedList
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState
import io.wax911.trakt.data.movie.usecase.FilmUseCaseType
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.shared.discover.model.MediaItem

class MovieModelState(
    private val useCase: FilmUseCaseType
) : ISupportViewModelState<PagedList<MediaItem>> {

    private val useCaseResult = MutableLiveData<UserInterfaceState<PagedList<ISharedMediaWithImage>>>()

    override val model =
        Transformations.switchMap(useCaseResult) {
            it.model.map { pagedList ->
                PagedList.Builder(
                    pagedList.dataSource.map { item ->
                        MediaItem(item)
                    }, pagedList.config
                ).build()
            }
        }

    override val networkState: LiveData<NetworkState>? =
        Transformations.switchMap(useCaseResult) { it.networkState }

    override val refreshState: LiveData<NetworkState>? =
        Transformations.switchMap(useCaseResult) { it.refreshState }

    operator fun invoke(payload: MediaPayload) {
        val result = useCase(payload)
        useCaseResult.postValue(result)
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {
        useCase.onCleared()
    }
}