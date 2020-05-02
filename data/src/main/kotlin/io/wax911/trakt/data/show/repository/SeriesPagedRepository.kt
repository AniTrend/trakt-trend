package io.wax911.trakt.data.show.repository

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.model.UserInterfaceState.Companion.create
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia
import io.wax911.trakt.domain.models.TimePeriod
import io.wax911.trakt.domain.repositories.series.SeriesRepository

internal class SeriesPagedRepository(
    private val source: ShowPagedSource
) : SupportRepository(source),
    SeriesRepository<UserInterfaceState<PagedList<ISharedMediaWithImage>>> {

    override fun getPopularShows() =
        source.create(
            model = source()
        )
}