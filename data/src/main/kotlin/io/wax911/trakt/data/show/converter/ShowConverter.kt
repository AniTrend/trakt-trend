package io.wax911.trakt.data.show.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import com.uwetrottmann.trakt5.entities.Airs
import com.uwetrottmann.trakt5.entities.Show
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.entities.show.ShowItem
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia
import io.wax911.trakt.domain.models.MediaType

internal class ShowEntityConverter(
    override val fromType: (ShowEntity) -> ISharedMedia = ::transform,
    override val toType: (ISharedMedia) -> ShowEntity = { throw NotImplementedError() }
) : SupportConverter<ShowEntity, ISharedMedia>() {
    private companion object : ISupportTransformer<ShowEntity, ISharedMedia> {
        override fun transform(source: ShowEntity) = ShowItem(
            title = source.title.orEmpty(),
            rating = source.rating ?: 0.0,
            votes = source.votes ?: 0,
            updated = source.updated ?: 0,
            year = source.year ?: 0,
            id = source.id
        )

    }
}

internal class ShowModelConverter(
    override val fromType: (Show) -> ShowEntity = ::transform,
    override val toType: (ShowEntity) -> Show = { throw NotImplementedError() }
) : SupportConverter<Show, ShowEntity>() {
    private companion object : ISupportTransformer<Show, ShowEntity> {

        private fun Airs?.toEpochTimeStamp(): Long {
            return if (this != null) {
                val timeStamp = "$day$time$timezone"
                0
            } else 0
        }

        override fun transform(source: Show) = ShowEntity(
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