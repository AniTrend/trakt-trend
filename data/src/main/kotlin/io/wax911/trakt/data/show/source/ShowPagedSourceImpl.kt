package io.wax911.trakt.data.show.source

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import com.uwetrottmann.trakt5.enums.Extended
import io.wax911.trakt.data.show.PopularShowController
import io.wax911.trakt.data.show.ShowRemoteSource
import io.wax911.trakt.data.show.converter.ShowEntityConverter
import io.wax911.trakt.data.show.datasource.local.ShowLocalSource
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.domain.entities.image.TmdbImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import io.wax911.trakt.domain.entities.shared.ShowWithImage
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class ShowPagedSourceImpl(
    private val localSource: ShowLocalSource,
    private val remoteSource: ShowRemoteSource,
    private val controller: PopularShowController,
    private val converter: ShowEntityConverter,
    dispatcher: ISupportDispatcher
) : ShowPagedSource(dispatcher) {

    override fun observable(): Flow<PagedList<ISharedMediaWithImage>> {
        val dataSourceFactory = localSource
                .getPopular(MediaType.SERIES)
                .map<ISharedMediaWithImage> {
                    ShowWithImage(
                        media = converter.convertFrom(it),
                        image = TmdbImage(
                            type = MediaType.SERIES,
                            imageType = ShowImageType.POSTER,
                            id = it.tmdbId ?: 0
                        )
                    )
                }

        return FlowPagedListBuilder(
            dataSourceFactory,
            PAGING_CONFIGURATION,
            null,
            this@ShowPagedSourceImpl
        ).buildFlow()
    }

    override suspend fun getPopularShows(callback: RequestCallback) {
        val request = remoteSource.popular(
            supportPagingHelper.page,
            supportPagingHelper.pageSize,
            Extended.FULL
        )

        controller(request, callback)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        withContext(context) {
            localSource.deleteAll(MediaType.SERIES)
        }
    }
}
