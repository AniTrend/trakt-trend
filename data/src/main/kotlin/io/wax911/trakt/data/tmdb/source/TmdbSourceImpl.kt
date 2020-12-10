package io.wax911.trakt.data.tmdb.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import com.uwetrottmann.tmdb2.entities.AppendToResponse
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem
import com.uwetrottmann.tmdb2.services.MoviesService
import com.uwetrottmann.tmdb2.services.TvService
import io.wax911.trakt.data.tmdb.TmdbMovieController
import io.wax911.trakt.data.tmdb.TmdbShowController
import io.wax911.trakt.data.tmdb.converter.TmdbEntityConverter
import io.wax911.trakt.data.tmdb.datasource.local.TmdbLocalSource
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.models.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class TmdbSourceImpl(
    private val localSource: TmdbLocalSource,
    private val remoteTvSource: TvService,
    private val remoteMovieSource: MoviesService,
    private val movieController: TmdbMovieController,
    private val showController: TmdbShowController,
    private val converter: TmdbEntityConverter,
    dispatcher: ISupportDispatcher
) : TmdbSource(dispatcher) {

    override fun imagesFromCache(tmdb: ITmdbImage) =
        localSource.getByIdFlow(tmdb.id)
            .map { converter.convertFrom(it) }

    override suspend fun findMatching(
        tmdb: ITmdbImage,
        callback: RequestCallback
    ) {
        val cachedImages = localSource.getById(tmdb.id)
        if (cachedImages.isEmpty())
            when (tmdb.type) {
                MediaType.MOVIE -> findMovieMatching(tmdb.id, callback)
                else -> findShowMatching(tmdb.id, callback)
            }
    }

    private suspend fun findShowMatching(tmdbId: Int, callback: RequestCallback) {
        val request = remoteTvSource.tv(
            tmdbId,
            "",
            AppendToResponse(
                AppendToResponseItem.IMAGES
            )
        )

        showController(request, callback)
    }

    private suspend fun findMovieMatching(tmdbId: Int, callback: RequestCallback) {
        val request = remoteMovieSource.summary(
            tmdbId,
            "",
            AppendToResponse(
                AppendToResponseItem.IMAGES
            )
        )

        movieController(request, callback)
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