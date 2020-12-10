package io.wax911.trakt.data.show.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import io.wax911.trakt.data.show.SeriesPageRepository
import io.wax911.trakt.data.show.source.contract.ShowPagedSource

internal class SeriesPagedRepository(
    private val source: ShowPagedSource
) : SupportRepository(source), SeriesPageRepository {

    override fun getPopularShows() =
        source create source.observable()
}