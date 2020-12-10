package io.wax911.trakt.data.movie.koin

import io.wax911.trakt.data.arch.extensions.trakt
import io.wax911.trakt.data.arch.extensions.db
import io.wax911.trakt.data.arch.extensions.defaultController
import io.wax911.trakt.data.movie.FilmPageRepository
import io.wax911.trakt.data.movie.FilmUseCaseType
import io.wax911.trakt.data.movie.converter.MovieModelConverter
import io.wax911.trakt.data.movie.mapper.MovieMapper
import io.wax911.trakt.data.movie.repository.FilmPagedRepository
import io.wax911.trakt.data.movie.source.MoviePagedSourceImpl
import io.wax911.trakt.data.movie.source.contract.MoviePagedSource
import io.wax911.trakt.data.movie.usecase.FilmPagedListUseCase
import org.koin.dsl.module

private val dataSourceModule = module {
    factory<MoviePagedSource> {
        MoviePagedSourceImpl(
            remoteSource = trakt().movies(),
            localSource = db().showDao(),
            controller = defaultController(
                mapper = get<MovieMapper>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val converterModule = module {
    factory {
        MovieModelConverter()
    }
}

private val mapperModule = module {
    factory {
        MovieMapper(
            localSource = db().showDao(),
            converter = get()
        )
    }
}

private val repositoryModule = module {
    factory<FilmPageRepository> {
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
    dataSourceModule, converterModule, mapperModule, repositoryModule, useCaseModule
)