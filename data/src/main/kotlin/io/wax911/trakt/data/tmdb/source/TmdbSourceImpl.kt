package io.wax911.trakt.data.tmdb.source

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class TmdbSourceImpl(
    private val localSource: TmdbDao,
    private val remoteTvSource: TvService,
    private val remoteMovieSource: MoviesService,
    private val connectivity: SupportConnectivity,
    private val movieMapper: TmdbMovieMapper,
    private val showMapper: TmdbShowMapper,
    dispatchers: SupportDispatchers
) : TmdbSource(dispatchers) {

    override fun imagesFromCache(tmdb: ITmdbImage) =
        localSource.getByIdFlow(tmdb.id)
            .map {
                it.mapTo(mutableListOf()) { entity ->
                    TmdbImageEntity.transform(entity)
                }
            }


    override suspend fun findMatching(tmdb: ITmdbImage): List<IShowImage> {
        val cachedImages = localSource.getById(tmdb.id)
        if (cachedImages.isNotEmpty())
            return cachedImages.map {
                TmdbImageEntity.transform(it)
            }
        return when (tmdb.type) {
            MediaType.MOVIE -> findMovieMatching(tmdb.id)
            else -> findShowMatching(tmdb.id)
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

        val imageEntities = controller(call)

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

        val imageEntities = controller(call)

        val result = mutableListOf<IShowImage>()
        imageEntities?.mapTo(result) { entity ->
            TmdbImageEntity.transform(entity)
        }
        return result
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        withContext(context) {
            localSource.deleteAll()
        }
    }
}