package io.wax911.trakt.movie.viewmodel

import androidx.lifecycle.ViewModel
import io.wax911.trakt.data.movie.usecase.FilmUseCaseType
import io.wax911.trakt.movie.viewmodel.model.MovieModelState

class MovieViewModel(
    useCase: FilmUseCaseType
) : ViewModel() {

    val modelState by lazy {
        MovieModelState(useCase)
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