package io.wax911.trakt.data.entitiy.movie.contract

import androidx.room.PrimaryKey
import io.wax911.trakt.domain.entities.contract.TraktIds

data class MovieEntityIds(
    @PrimaryKey
    override val trakt: Int?,
    override val slug: String?,
    override val imdb: String?,
    override val tmdb: Int?
): TraktIds