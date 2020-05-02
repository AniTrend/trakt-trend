package io.wax911.trakt.data.show.source.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.data.arch.common.TraktPagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage

internal abstract class ShowPagedSource(
    dispatchers: SupportDispatchers
) : TraktPagedSource<ISharedMediaWithImage>(dispatchers) {
    protected abstract val popularObservable:
            ISourceObservable<Nothing?, PagedList<ISharedMediaWithImage>>

    protected abstract fun getPopularShows(callback: PagingRequestHelper.Request.Callback)

    operator fun invoke() : LiveData<PagedList<ISharedMediaWithImage>> {
        executionTarget = { getPopularShows(it) }
        return popularObservable(null)
    }
}