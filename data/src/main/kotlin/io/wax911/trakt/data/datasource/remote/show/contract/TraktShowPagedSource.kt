package io.wax911.trakt.data.datasource.remote.show.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.data.datasource.local.query.ShowDao
import io.wax911.trakt.data.datasource.remote.common.MediaPagedSource
import io.wax911.trakt.data.entitiy.show.ShowEntity

abstract class TraktShowPagedSource(
    dispatchers: SupportDispatchers
) : MediaPagedSource<ShowEntity>(dispatchers) {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val popularShowObservable: ISourceObservable<Nothing?, PagedList<ShowEntity>>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val trendingShowObservable: ISourceObservable<Nothing?, PagedList<ShowEntity>>
}