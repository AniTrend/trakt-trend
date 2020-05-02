package io.wax911.trakt.data.tmdb.extensions

import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.enums.TmdbImageType
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType


internal fun Collection<TmdbImageEntity>.findHighestRatedPoster(): TmdbImageEntity? {
    return filter { it.type == TmdbImageType.POSTER }
        .maxBy { it.rating + (if (it.isPrimary) 10f else 0f) }
}

internal fun Collection<TmdbImageEntity>.findHighestRatedBackdrop(): TmdbImageEntity? {
    return filter { it.type == TmdbImageType.BACKDROP }
        .maxBy { it.rating + (if (it.isPrimary) 10f else 0f) }
}

fun Collection<IShowImage>.findHighestRatedPoster(): IShowImage? {
    return filter { it.type == ShowImageType.POSTER }
        .maxBy { it.rating + (if (it.isPrimary) 10f else 0f) }
}

fun Collection<IShowImage>.findHighestRatedBackdrop(): IShowImage? {
    return filter { it.type == ShowImageType.BACKDROP }
        .maxBy { it.rating + (if (it.isPrimary) 10f else 0f) }
}