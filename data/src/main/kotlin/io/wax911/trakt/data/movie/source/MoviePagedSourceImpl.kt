package io.wax911.trakt.data.movie.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.util.SupportDataKeyStore
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Movies
import io.wax911.trakt.data.arch.controller.policy.OnlineControllerPolicy
import io.wax911.trakt.data.arch.extensions.controller
import io.wax911.trakt.data.movie.mapper.MovieMapper
import io.wax911.trakt.data.movie.source.contract.MoviePagedSource
import io.wax911.trakt.data.show.datasource.local.ShowDao
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.data.show.enums.ShowType
import io.wax911.trakt.data.tmdb.repository.TmdbRepository
import io.wax911.trakt.domain.entities.shared.ShowWithImages
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class MoviePagedSourceImpl(
    private val localSource: ShowDao,
    private val remoteSource: Movies,
    private val tmdbRepository: TmdbRepository,
    private val connectivity: SupportConnectivity,
    dispatchers: SupportDispatchers
)  : MoviePagedSource(dispatchers) {

    override fun getPopularMovies(callback: PagingRequestHelper.Request.Callback) {
        val call =
            remoteSource.popular(
                supportPagingHelper.page,
                supportPagingHelper.pageSize,
                Extended.FULL
            )

        val mapper = MovieMapper(localSource) { tmdbId ->
            if (tmdbId != null)
                tmdbRepository.loadImages(tmdbId)
        }


        val controller =
            mapper.controller(
                OnlineControllerPolicy.create(
                    connectivity
                ),
                dispatchers
            )

        launch {
            controller(call, callback)
        }

    }

    override val popularObservable =
        object : ISourceObservable<Nothing?, PagedList<ISharedMediaWithImage>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<PagedList<ISharedMediaWithImage>> {
                val dataSourceFactory = localSource.getPopular(ShowType.MOVIE)

                val result: DataSource.Factory<Int, ISharedMediaWithImage> = dataSourceFactory.map {
                    val images = runBlocking(dispatchers.io) {
                        tmdbRepository.getImagesFlow(it.tmdbId ?: 0).single()
                    }
                    val show = ShowEntity.transform(it)

                    ShowWithImages(
                        media = show,
                        images = images
                    )
                }

                return result.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@MoviePagedSourceImpl
                )
            }
        }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        localSource.deleteAll(ShowType.MOVIE)
    }
}