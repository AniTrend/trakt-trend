package io.wax911.trakt.data.show.repository

import androidx.paging.PagedList
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.repositories.series.SeriesRepository

internal class SeriesPagedRepository(
    private val source: ShowPagedSource
) : SupportRepository(source),
    SeriesRepository<DataState<PagedList<ISharedMediaWithImage>>> {

    override fun getPopularShows() =
        source.create(
            model = source()
        )
}