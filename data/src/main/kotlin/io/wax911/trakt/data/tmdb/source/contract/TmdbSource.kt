package io.wax911.trakt.data.tmdb.source.contract

import androidx.lifecycle.LiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.coroutine.SupportCoroutineDataSource
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class TmdbSource(
    dispatchers: SupportDispatchers
) : SupportCoroutineDataSource(dispatchers) {

    protected abstract val observable: ISourceObservable<ITmdbImage, List<IShowImage>>

    protected abstract suspend fun findMatching(tmdb: ITmdbImage)

    operator fun invoke(tmdb: ITmdbImage): LiveData<List<IShowImage>> {
        retry = { findMatching(tmdb) }
        launch { retry?.invoke() }
        return observable(tmdb)
    }

    abstract suspend fun get(tmdb: ITmdbImage): List<IShowImage>
}