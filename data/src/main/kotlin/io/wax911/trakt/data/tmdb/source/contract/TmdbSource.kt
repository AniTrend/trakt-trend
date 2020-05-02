package io.wax911.trakt.data.tmdb.source.contract

import androidx.lifecycle.LiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.coroutine.SupportCoroutineDataSource
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class TmdbSource(
    dispatchers: SupportDispatchers
) : SupportCoroutineDataSource<List<IShowImage>>(dispatchers) {

    protected var tmdbId: Int = 0
        private set

    protected abstract val flow: Flow<List<IShowImage>>
    protected abstract val observable: ISourceObservable<Nothing?, List<IShowImage>>

    protected abstract suspend fun findShowMatching(tmdb: Int): List<IShowImage>

    operator fun invoke(tmdb: Int): LiveData<List<IShowImage>> {
        tmdbId = tmdb
        retry = { findShowMatching(tmdb) }
        launch { findShowMatching(tmdbId) }
        return observable(null)
    }

    suspend fun get(tmdb: Int) {
        tmdbId = tmdb
        retry = { findShowMatching(tmdb) }
        findShowMatching(tmdbId)
    }

    fun getFlow(tmdb: Int): Flow<List<IShowImage>> {
        tmdbId = tmdb
        return flow
    }
}