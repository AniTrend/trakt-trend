package io.wax911.trakt.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.core.provider.SupportFileProvider
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.systemServiceOf
import co.anitrend.arch.ui.util.StateLayoutConfig
import io.wax911.trakt.R
import io.wax911.trakt.core.koin.coreModules
import io.wax911.trakt.data.arch.di.dataModules
import io.wax911.trakt.movie.viewmodel.MovieViewModel
import io.wax911.trakt.show.viewmodel.ShowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val appModule = module {
    factory {
        SupportFileProvider()
    }
    single {
        StateLayoutConfig(
            loadingDrawable = null,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = null,
            retryAction = R.string.action_retry
        )
    }
    single {
        SupportConnectivity(
            androidApplication().systemServiceOf<ConnectivityManager>(
                Context.CONNECTIVITY_SERVICE
            )
        )
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
) + coreModules