package io.wax911.trakt.data.movie.repository

import androidx.paging.PagedList
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import io.wax911.trakt.data.movie.FilmPageRepository
import io.wax911.trakt.data.movie.source.contract.MoviePagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.repositories.film.FilmRepository

internal class FilmPagedRepository(
    private val source: MoviePagedSource
) : SupportRepository(source), FilmPageRepository {

    override fun getPopularMovies() =
        source create source.observable()

}