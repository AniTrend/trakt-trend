package io.wax911.trakt.data.tmdb.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import com.uwetrottmann.tmdb2.entities.AppendToResponse
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem
import com.uwetrottmann.tmdb2.services.MoviesService
import com.uwetrottmann.tmdb2.services.TvService
import io.wax911.trakt.data.arch.controller.policy.OnlineControllerPolicy
import io.wax911.trakt.data.arch.extensions.controller
import io.wax911.trakt.data.tmdb.datasource.local.TmdbDao
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.mapper.TmdbMovieMapper
import io.wax911.trakt.data.tmdb.mapper.TmdbShowMapper
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.models.MediaType
import kotlinx.coroutines.flow.map
import timber.log.Timber

internal class TmdbSourceImpl(
    private val localSource: TmdbDao,
    private val remoteTvSource: TvService,
    private val remoteMovieSource: MoviesService,
    private val connectivity: SupportConnectivity,
    private val movieMapper: TmdbMovieMapper,
    private val showMapper: TmdbShowMapper,
    dispatchers: SupportDispatchers
) : TmdbSource(dispatchers) {

    override val observable =
        object : ISourceObservable<ITmdbImage, List<IShowImage>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: ITmdbImage): LiveData<List<IShowImage>> {
                return localSource.getByIdFlow(parameter.id)
                    .map {
                        it.mapTo(mutableListOf()) { entity ->
                            TmdbImageEntity.transform(entity)
                        }
                    }.asLiveData(coroutineContext)
            }
        }

    override suspend fun findMatching(tmdb: ITmdbImage) {
        when (tmdb.type) {
            MediaType.MOVIE -> findMovieMatching(tmdb.id)
            MediaType.SERIES -> findShowMatching(tmdb.id)
        }
    }

    override suspend fun get(tmdb: ITmdbImage): List<IShowImage> {
        val items = localSource.getById(tmdb.id)
        return if (items.isNotEmpty()) {
            items.map {
                TmdbImageEntity.transform(it)
            }
        }
        else {
            val images = runCatching{
                findShowMatching(tmdb.id)
            }.getOrElse {
                Timber.tag(moduleTag).e(it)
                emptyList()
            }
            images
        }
    }

    private suspend fun findShowMatching(tmdbId: Int): List<IShowImage> {
        val call = remoteTvSource.tv(
            tmdbId,
            "",
            AppendToResponse(
                AppendToResponseItem.IMAGES
            )
        )

        val controller =
            showMapper.controller(
                OnlineControllerPolicy.create(
                    connectivity
                ),
                dispatchers
            )

        val imageEntities = controller(call, networkState)

        val result = mutableListOf<IShowImage>()
        imageEntities?.mapTo(result) { entity ->
            TmdbImageEntity.transform(entity)
        }
        return result
    }

    private suspend fun findMovieMatching(tmdbId: Int): List<IShowImage> {
        val call = remoteMovieSource.summary(
            tmdbId,
            "",
            AppendToResponse(
                AppendToResponseItem.IMAGES
            )
        )

        val controller =
            movieMapper.controller(
                OnlineControllerPolicy.create(
                    connectivity
                ),
                dispatchers
            )

        val imageEntities = controller(call, networkState)

        val result = mutableListOf<IShowImage>()
        imageEntities?.mapTo(result) { entity ->
            TmdbImageEntity.transform(entity)
        }
        return result
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        localSource.deleteAll()
    }
}