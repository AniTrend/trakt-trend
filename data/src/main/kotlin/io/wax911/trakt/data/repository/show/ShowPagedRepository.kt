package io.wax911.trakt.data.repository.show

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.trakt.data.datasource.remote.show.contract.TraktShowPagedSource
import io.wax911.trakt.data.entitiy.show.ShowEntity
import io.wax911.trakt.domain.repositories.show.ITraktShowRepository

class ShowPagedRepository(
    private val source: TraktShowPagedSource
) : SupportRepository(source), ITraktShowRepository<UserInterfaceState<PagedList<ShowEntity>>> {

    override fun getPopularShows(): UserInterfaceState<PagedList<ShowEntity>> {
        return UserInterfaceState.create(
            model = source.popularShowObservable(null),
            source = source
        )
    }

    override fun getTrendingShows(): UserInterfaceState<PagedList<ShowEntity>> {
        return UserInterfaceState.create(
            model = source.trendingShowObservable(null),
            source = source
        )
    }
}