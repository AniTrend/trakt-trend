package io.wax911.trakt.data.show.koin

import io.wax911.trakt.data.arch.extensions.trakt
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.arch.extensions.defaultController
import io.wax911.trakt.data.show.SeriesPageRepository
import io.wax911.trakt.data.show.SeriesUseCaseType
import io.wax911.trakt.data.show.converter.ShowEntityConverter
import io.wax911.trakt.data.show.converter.ShowModelConverter
import io.wax911.trakt.data.show.mapper.ShowMapper
import io.wax911.trakt.data.show.repository.SeriesPagedRepository
import io.wax911.trakt.data.show.source.ShowPagedSourceImpl
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.data.show.usecase.SeriesPagedListUseCase
import io.wax911.trakt.domain.repositories.series.SeriesRepository
import org.koin.dsl.module


private val dataSourceModule = module {
    factory<ShowPagedSource> {
        ShowPagedSourceImpl(
            remoteSource = trakt().shows(),
            localSource = db().showDao(),
            controller = defaultController(
                mapper = get<ShowMapper>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val converterModule = module {
    factory {
        ShowModelConverter()
    }
    factory {
        ShowEntityConverter()
    }
}

private val mapperModule = module {
    factory {
        ShowMapper(
            localSource = db().showDao(),
            converter = get()
        )
    }
}

private val repositoryModule = module {
    factory<SeriesPageRepository> {
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
    dataSourceModule, converterModule, mapperModule, repositoryModule, useCaseModule
)