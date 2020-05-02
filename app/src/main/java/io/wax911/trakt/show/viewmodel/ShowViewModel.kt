package io.wax911.trakt.show.viewmodel

import androidx.lifecycle.ViewModel
import io.wax911.trakt.data.show.usecase.SeriesUseCaseType
import io.wax911.trakt.show.viewmodel.model.ShowModelState

class ShowViewModel(
    useCase: SeriesUseCaseType
) : ViewModel() {

    val modelState by lazy {
        ShowModelState(useCase)
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        modelState.onCleared()
        super.onCleared()
    }
}