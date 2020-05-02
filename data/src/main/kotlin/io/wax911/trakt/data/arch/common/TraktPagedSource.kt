package io.wax911.trakt.data.arch.common

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.SupportDispatchers
import kotlinx.coroutines.launch

/**
 * Helper for prototyping paging sources quicker
 *
 * @param supportDispatchers an object container various dispatchers
 */
internal abstract class TraktPagedSource<T>(
    supportDispatchers: SupportDispatchers
) : SupportPagingDataSource<T>(supportDispatchers) {

    protected lateinit var executionTarget: (PagingRequestHelper.Request.Callback) -> Unit

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        pagingRequestHelper.runIfNotRunning(
            PagingRequestHelper.RequestType.INITIAL
        ) {
            executionTarget(it)
        }
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within [PagedList.Config.prefetchDistance] of it.
     *
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    override fun onItemAtEndLoaded(itemAtEnd: T) {
        pagingRequestHelper.runIfNotRunning(
            PagingRequestHelper.RequestType.AFTER
        ) {
            supportPagingHelper.onPageNext()
            executionTarget(it)
        }
    }
}