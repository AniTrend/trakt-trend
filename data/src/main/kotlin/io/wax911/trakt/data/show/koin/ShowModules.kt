package io.wax911.trakt.data.show.koin

import io.wax911.trakt.data.arch.extensions.trakt
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.show.mapper.ShowMapper
import io.wax911.trakt.data.show.repository.SeriesPagedRepository
import io.wax911.trakt.data.show.source.ShowPagedSourceImpl
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.data.show.usecase.SeriesPagedListUseCase
import io.wax911.trakt.data.show.usecase.SeriesUseCaseType
import org.koin.dsl.module


private val dataSourceModule = module {
    factory<ShowPagedSource> {
        ShowPagedSourceImpl(
            remoteSource = trakt().shows(),
            localSource = db().showDao(),
            dispatchers = get(),
            mapper = get(),
            connectivity = get()
        )
    }
}

private val mapperModule = module {
    factory {
        ShowMapper(
            localSource = db().showDao()
        )
    }
}

private val repositoryModule = module {
    factory {
        SeriesPagedRepository(
            source = get()
        )
    }
}

private val useCaseModule = module {
    factory<SeriesUseCaseType> {
        SeriesPagedListUseCase(
            repository = get()
        )
    }

}

internal val showModules = listOf(
    dataSourceModule, mapperModule, repositoryModule, useCaseModule
)