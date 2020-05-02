package io.wax911.trakt.data.tmdb.repository

import co.anitrend.arch.data.model.UserInterfaceState.Companion.create
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource

internal class TmdbRepository(
    private val source: TmdbSource
) : SupportRepository(source) {

    suspend fun loadImages(tmdbId: Int) = source.get(tmdbId)

    fun getImages(tmdbId: Int) =
        source.create(
            model = source(tmdbId)
        )

    fun getImagesFlow(tmdbId: Int) =
        source.getFlow(tmdbId)
}