package io.wax911.trakt.data.tmdb.source.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single

abstract class TmdbSource(
    dispatcher: ISupportDispatcher
) : SupportCoreDataSource(dispatcher) {

    protected abstract fun imagesFromCache(tmdb: ITmdbImage): Flow<List<IShowImage>>

    protected abstract suspend fun findMatching(tmdb: ITmdbImage, callback: RequestCallback)

    suspend operator fun invoke(tmdb: ITmdbImage): List<IShowImage> {
        requestHelper.runIfNotRunning(
            Request.Default(
                "tmdb_${tmdb.id}_initial",
                Request.Type.INITIAL
            )
        ) { findMatching(tmdb, it) }

        return imagesFromCache(tmdb).first()
    }
}