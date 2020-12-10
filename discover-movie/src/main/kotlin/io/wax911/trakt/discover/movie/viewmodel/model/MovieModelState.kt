package io.wax911.trakt.discover.movie.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.domain.entities.NetworkState
import io.wax911.trakt.data.movie.FilmUseCaseType
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload

class MovieModelState(
    private val useCase: FilmUseCaseType
) : ISupportViewModelState<PagedList<ISharedMediaWithImage>> {

    private val useCaseResult = MutableLiveData<DataState<PagedList<ISharedMediaWithImage>>>()

    override val model =
        switchMap(useCaseResult) { it.model.asLiveData() }

    override val networkState =
        switchMap(useCaseResult) { it.networkState.asLiveData() }

    override val refreshState =
        switchMap(useCaseResult) { it.refreshState.asLiveData() }

    operator fun invoke(payload: MediaPayload) {
        val result = useCase(payload)
        useCaseResult.postValue(result)
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
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