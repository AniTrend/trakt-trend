package io.wax911.trakt.discover.show.di

import io.wax911.trakt.core.koin.helper.DynamicFeatureModuleHelper
import io.wax911.trakt.discover.show.view.content.ShowListContent
import io.wax911.trakt.discover.show.viewmodel.ShowViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule = module {
    fragment {
        ShowListContent()
    }
}

private val viewModelModule = module {
    viewModel {
        ShowViewModel(
            useCase = get()
        )
    }
}

internal val dynamicModuleHelper = DynamicFeatureModuleHelper(
    listOf(viewModelModule, fragmentModule)
)