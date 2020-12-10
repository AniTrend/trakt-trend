package io.wax911.trakt.data.tmdb.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.extensions.generateIdFromPath
import io.wax911.trakt.data.tmdb.model.TmdbImageModel
import io.wax911.trakt.domain.entities.image.ShowImage
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType

internal class TmdbEntityConverter(
    override val fromType: (TmdbImageEntity) -> IShowImage = ::transform,
    override val toType: (IShowImage) -> TmdbImageEntity = { throw NotImplementedError() }
) : SupportConverter<TmdbImageEntity, IShowImage>() {
    private companion object : ISupportTransformer<TmdbImageEntity, IShowImage> {
        override fun transform(source: TmdbImageEntity) = ShowImage(
            path = source.path,
            type = ShowImageType.valueOf(source.type.name),
            language = source.language,
            rating = source.rating,
            isPrimary = source.isPrimary,
            id = source.id
        )
    }
}

internal class TmdbModelConverter(
    override val fromType: (TmdbImageModel) -> TmdbImageEntity = ::transform,
    override val toType: (TmdbImageEntity) -> TmdbImageModel = { throw NotImplementedError() }
) : SupportConverter<TmdbImageModel, TmdbImageEntity>() {
    private companion object : ISupportTransformer<TmdbImageModel, TmdbImageEntity> {
        override fun transform(source: TmdbImageModel) = TmdbImageEntity(
            id = source.image.file_path.generateIdFromPath(),
            showId = source.parentId,
            path = source.image.file_path,
            type = source.type,
            language = source.image.iso_639_1,
            rating = source.image.vote_average?.toFloat() ?: 0f,
            isPrimary = source.isPrimary
        )
    }
}