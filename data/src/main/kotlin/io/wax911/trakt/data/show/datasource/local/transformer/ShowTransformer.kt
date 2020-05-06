package io.wax911.trakt.data.show.datasource.local.transformer

import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import com.uwetrottmann.trakt5.entities.Airs
import com.uwetrottmann.trakt5.entities.Show
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.models.MediaType

internal object ShowTransformer : ISupportMapperHelper<Show, ShowEntity> {

    private fun Airs?.toEpochTimeStamp(): Long? {
        return if (this != null) {
            val timeStamp = "$day$time$timezone"
            0
        } else 0
    }

    /**
     * Transforms the the [source] to the target type
     */
    override fun transform(source: Show): ShowEntity {
        return ShowEntity(
            airs = source.airs.toEpochTimeStamp(),
            network = source.network,
            country = source.country,
            status = source.status?.name,
            title = source.title,
            overview = source.overview,
            rating = source.rating,
            votes = source.votes,
            updated = source.updated_at?.toEpochSecond(),
            translations = source.available_translations.orEmpty(),
            year = source.year,
            certification = source.certification,
            tagLine = null,
            released = source.first_aired?.toEpochSecond(),
            runtime = source.runtime,
            trailer = source.trailer,
            homepage = source.homepage,
            language = source.language,
            genres = source.genres.orEmpty(),
            traktId = source.ids?.trakt,
            tmdbId = source.ids?.tmdb,
            showType = MediaType.SERIES,
            id = source.ids?.trakt ?: 0
        )
    }
}