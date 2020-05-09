package io.wax911.trakt.data.tmdb.repository

import co.anitrend.arch.data.model.UserInterfaceState.Companion.create
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage

class TmdbRepository(
    private val source: TmdbSource
) : SupportRepository(source) {

    fun images(tmdbId: ITmdbImage) =
        source.create(
            model = source(tmdbId)
        )
}