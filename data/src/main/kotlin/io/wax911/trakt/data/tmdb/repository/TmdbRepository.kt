package io.wax911.trakt.data.tmdb.repository

import androidx.lifecycle.liveData
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage

class TmdbRepository(
    private val source: TmdbSource
) : SupportRepository(source) {

    fun images(tmdbId: ITmdbImage) =
        source.create(
            model = liveData {
                emit(source.invoke(tmdbId))
            }
        )
}