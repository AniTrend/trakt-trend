package io.wax911.trakt.data.movie.koin

import io.wax911.trakt.data.arch.extensions.trakt
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.movie.mapper.MovieMapper
import io.wax911.trakt.data.movie.repository.FilmPagedRepository
import io.wax911.trakt.data.movie.source.MoviePagedSourceImpl
import io.wax911.trakt.data.movie.source.contract.MoviePagedSource
import io.wax911.trakt.data.movie.usecase.FilmPagedListUseCase
import io.wax911.trakt.data.movie.usecase.FilmUseCaseType
import org.koin.dsl.module

private val dataSourceModule = module {
    factory<MoviePagedSource> {
        MoviePagedSourceImpl(
            remoteSource = trakt().movies(),
            localSource = db().showDao(),
            dispatchers = get(),
            mapper = get(),
            connectivity = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MovieMapper(
            localSource = db().showDao()
        )
    }
}

private val repositoryModule = module {
    factory {
        FilmPagedRepository(
            source = get()
        )
    }
}

private val useCaseModule = module {
    factory<FilmUseCaseType> {
        FilmPagedListUseCase(
            repository = get()
        )
    }
}

internal val movieModules = listOf(
    dataSourceModule, mapperModule, repositoryModule, useCaseModule
)