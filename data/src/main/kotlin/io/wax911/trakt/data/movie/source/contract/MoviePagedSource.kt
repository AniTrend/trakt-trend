package io.wax911.trakt.data.movie.source.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import io.wax911.trakt.data.arch.common.TraktPagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia
import io.wax911.trakt.domain.models.TimePeriod

internal abstract class MoviePagedSource(
    dispatchers: SupportDispatchers
) : TraktPagedSource<ISharedMediaWithImage>(dispatchers) {
    protected abstract val popularObservable: ISourceObservable<Nothing?, PagedList<ISharedMediaWithImage>>

    protected abstract suspend fun getPopularMovies(callback: PagingRequestHelper.Request.Callback)

    operator fun invoke() : LiveData<PagedList<ISharedMediaWithImage>> {
        executionTarget = { getPopularMovies(it) }
        return popularObservable(null)
    }
}