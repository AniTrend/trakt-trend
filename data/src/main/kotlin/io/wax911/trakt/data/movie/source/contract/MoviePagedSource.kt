package io.wax911.trakt.data.movie.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class MoviePagedSource(
    dispatcher: ISupportDispatcher
) : SupportPagingDataSource<ISharedMediaWithImage>(dispatcher) {

    abstract fun observable(): Flow<PagedList<ISharedMediaWithImage>>

    protected abstract suspend fun getPopularMovies(callback: RequestCallback)

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    override fun onItemAtEndLoaded(itemAtEnd: ISharedMediaWithImage) {
        supportPagingHelper.onPageNext()
        launch {
            requestHelper.runIfNotRunning(
                Request.Default(
                    "movie_paged_after",
                    Request.Type.AFTER
                )
            ) { getPopularMovies(it) }
        }
    }

    /**
     * Called when the item at the front of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     * No more data will be prepended to the PagedList before this item.
     *
     * @param itemAtFront The first item of PagedList
     */
    override fun onItemAtFrontLoaded(itemAtFront: ISharedMediaWithImage) {
        if (!supportPagingHelper.isFirstPage())
            supportPagingHelper.onPagePrevious()
        launch {
            requestHelper.runIfNotRunning(
                Request.Default(
                    "movie_paged_before",
                    Request.Type.BEFORE
                )
            ) {
                if (!supportPagingHelper.isFirstPage())
                    getPopularMovies(it)
            }
        }
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        launch {
            requestHelper.runIfNotRunning(
                Request.Default(
                    "movie_paged_initial",
                    Request.Type.INITIAL
                )
            ) { getPopularMovies(it) }
        }
    }
}