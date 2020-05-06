package io.wax911.trakt.data.movie.datasource.local.transformer

import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import com.uwetrottmann.trakt5.entities.Movie
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.models.MediaType

internal object MovieTransformer : ISupportMapperHelper<Movie, ShowEntity> {

    /**
     * Transforms the the [source] to the target type
     */
    override fun transform(source: Movie): ShowEntity {
        return ShowEntity(
            airs = null,
            network = null,
            country = null,
            status = null,
            title = source.title,
            overview = source.overview,
            rating = source.rating,
            votes = source.votes,
            updated = source.updated_at?.toEpochSecond(),
            translations = source.available_translations.orEmpty(),
            year = source.year,
            certification = source.certification,
            tagLine = source.tagline,
            released = source.released?.toEpochDay(),
            runtime = source.runtime,
            trailer = source.trailer,
            homepage = source.homepage,
            language = source.language,
            genres = source.genres.orEmpty(),
            traktId = source.ids?.trakt,
            tmdbId = source.ids?.tmdb,
            showType = MediaType.MOVIE,
            id = source.ids?.trakt ?: 0
        )
    }
}