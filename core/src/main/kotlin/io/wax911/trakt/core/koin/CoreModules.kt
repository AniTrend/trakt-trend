package io.wax911.trakt.core.koin

import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.core.analytics.AnalyticsLogger
import io.wax911.trakt.core.helpers.coil.ShowTypeRegistry
import io.wax911.trakt.core.presenter.CorePresenter
import io.wax911.trakt.core.settings.Settings
import io.wax911.trakt.data.arch.di.dataModules
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

private val coreModule = module {
    factory<ISupportAnalytics> {
        AnalyticsLogger(
            context = androidContext()
        )
    }
    factory {
        ShowTypeRegistry(
            imageProvider = get()
        )
    }
    factory {
        Settings(
            androidContext()
        )
    } binds(Settings.BINDINGS)
    single {
        SupportDispatchers()
    }
}

private val presenterModule = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

val coreModules = listOf(
    coreModule, presenterModule
) + dataModules