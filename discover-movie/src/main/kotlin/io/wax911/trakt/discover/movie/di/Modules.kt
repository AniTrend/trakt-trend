package io.wax911.trakt.discover.movie.di

import io.wax911.trakt.core.koin.helper.DynamicFeatureModuleHelper
import io.wax911.trakt.discover.movie.view.content.MovieListContent
import io.wax911.trakt.discover.movie.viewmodel.MovieViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule = module {
    fragment {
        MovieListContent()
    }
}

private val viewModelModule = module {
    viewModel {
        MovieViewModel(
            useCase = get()
        )
    }
}

internal val dynamicModuleHelper = DynamicFeatureModuleHelper(
    listOf(viewModelModule, fragmentModule)
)