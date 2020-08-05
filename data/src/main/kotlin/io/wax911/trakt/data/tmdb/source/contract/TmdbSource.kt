package io.wax911.trakt.data.tmdb.source.contract

import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

abstract class TmdbSource(
    dispatchers: SupportDispatchers
) : SupportCoreDataSource(dispatchers) {

    protected abstract fun imagesFromCache(tmdb: ITmdbImage): Flow<List<IShowImage>>

    protected abstract suspend fun findMatching(tmdb: ITmdbImage): List<IShowImage>

    suspend operator fun invoke(tmdb: ITmdbImage): List<IShowImage> = findMatching(tmdb)
}