package io.wax911.trakt.koin

import co.anitrend.arch.core.provider.SupportFileProvider
import io.wax911.trakt.core.koin.coreModules
import io.wax911.trakt.data.koin.dataModules
import io.wax911.trakt.movie.viewmodel.MovieViewModel
import io.wax911.trakt.show.viewmodel.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val appModule = module {
    factory {
        SupportFileProvider()
    }
}


private val viewModelModule = module {
    viewModel {
        ShowViewModel(
            useCase = get()
        )
    }
    viewModel {
        MovieViewModel(
            useCase = get()
        )
    }
}

val appModules = listOf(
    appModule, viewModelModule
) + coreModules + dataModules