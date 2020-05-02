package io.wax911.trakt.data.movie.repository

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.model.UserInterfaceState.Companion.create
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.trakt.data.movie.source.contract.MoviePagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.repositories.film.FilmRepository

internal class FilmPagedRepository(
    private val source: MoviePagedSource
) : SupportRepository(source), FilmRepository<UserInterfaceState<PagedList<ISharedMediaWithImage>>> {

    override fun getPopularMovies() =
        source.create(
            model = source()
        )
}