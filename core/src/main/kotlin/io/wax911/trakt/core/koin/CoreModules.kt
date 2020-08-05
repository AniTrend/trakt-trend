package io.wax911.trakt.core.koin

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import coil.map.MeasuredMapper
import coil.util.CoilUtils
import io.wax911.trakt.core.helpers.coil.fetch.ShowImageFetcher
import io.wax911.trakt.core.helpers.coil.mapper.ShowImageMeasuredMapper
import io.wax911.trakt.core.presenter.CorePresenter
import io.wax911.trakt.core.settings.Settings
import io.wax911.trakt.data.arch.di.dataModules
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

private val coreModule = module {
    factory {
        Settings(
            androidContext()
        )
    } binds(Settings.BINDINGS)
    single {
        SupportDispatchers()
    }
}

private val coilModules = module {
    factory<MeasuredMapper<IShowImage, HttpUrl>> {
        ShowImageMeasuredMapper()
    }
    factory {
        ShowImageFetcher(
            client = OkHttpClient.Builder()
                .cache(
                    CoilUtils.createDefaultCache(
                        androidApplication()
                    )
                ).build(),
            tmdbSource = get(),
            mapper = get()
        )
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
    coreModule, coilModules, presenterModule
) + dataModules