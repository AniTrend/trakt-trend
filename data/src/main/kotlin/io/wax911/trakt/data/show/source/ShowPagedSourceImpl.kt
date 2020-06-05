package io.wax911.trakt.data.show.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.util.SupportDataKeyStore
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Shows
import io.wax911.trakt.data.arch.controller.policy.OnlineControllerPolicy
import io.wax911.trakt.data.arch.extensions.controller
import io.wax911.trakt.data.show.datasource.local.ShowDao
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.models.MediaType
import io.wax911.trakt.data.show.mapper.ShowMapper
import io.wax911.trakt.data.show.source.contract.ShowPagedSource
import io.wax911.trakt.data.tmdb.enums.TmdbImageType
import io.wax911.trakt.domain.entities.image.TmdbImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import io.wax911.trakt.domain.entities.shared.ShowWithImage
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage

internal class ShowPagedSourceImpl(
    private val localSource: ShowDao,
    private val remoteSource: Shows,
    private val mapper: ShowMapper,
    private val connectivity: SupportConnectivity,
    dispatchers: SupportDispatchers
) : ShowPagedSource(dispatchers) {

    override suspend fun getPopularShows(callback: PagingRequestHelper.Request.Callback) {
        val call = remoteSource.popular(
            supportPagingHelper.page,
            supportPagingHelper.pageSize,
            Extended.FULL
        )

        val controller =
            mapper.controller(
                OnlineControllerPolicy.create(
                    connectivity
                ),
                dispatchers
            )

        controller(call, callback)
    }

    override val popularObservable =
        object : ISourceObservable<Nothing?, PagedList<ISharedMediaWithImage>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter parameters, implementation is up to the developer
             */
            override operator fun invoke(parameter: Nothing?): LiveData<PagedList<ISharedMediaWithImage>> {
                val dataSourceFactory = localSource.getPopular(MediaType.SERIES)

                val result: DataSource.Factory<Int, ISharedMediaWithImage> = dataSourceFactory.map {
                    val show = ShowEntity.transform(it)

                    ShowWithImage(
                        media = show,
                        image = TmdbImage(
                            type = MediaType.SERIES,
                            imageType = ShowImageType.POSTER,
                            id = it.tmdbId ?: 0
                        )
                    )
                }

                return result.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@ShowPagedSourceImpl
                )
            }
        }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        localSource.deleteAll(MediaType.SERIES)
    }
}
