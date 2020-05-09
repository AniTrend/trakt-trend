package io.wax911.trakt.core.helpers.coil.model

import io.wax911.trakt.data.tmdb.extensions.findHighestRatedBackdrop
import io.wax911.trakt.data.tmdb.extensions.findHighestRatedLogo
import io.wax911.trakt.data.tmdb.extensions.findHighestRatedPoster
import io.wax911.trakt.domain.entities.image.contract.IShowImage

data class ImageConfig(
    val showImages: Collection<IShowImage>
) {
    fun bestPosterImage() =
        showImages.findHighestRatedPoster() ?: showImages.firstOrNull()
    fun bestBackDropImage() =
        showImages.findHighestRatedBackdrop() ?: showImages.firstOrNull()
    fun bestLogoImage() =
        showImages.findHighestRatedLogo() ?: showImages.firstOrNull()
}