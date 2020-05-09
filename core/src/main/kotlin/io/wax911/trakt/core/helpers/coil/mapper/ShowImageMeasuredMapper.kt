package io.wax911.trakt.core.helpers.coil.mapper

import coil.map.MeasuredMapper
import coil.size.PixelSize
import coil.size.Size
import io.wax911.trakt.data.tmdb.helper.TmdbImageUrlProvider
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class ShowImageMeasuredMapper(
    private val imageProvider: TmdbImageUrlProvider = TmdbImageUrlProvider()
) : MeasuredMapper<IShowImage, HttpUrl> {

    override fun handles(data: IShowImage): Boolean = true

    override fun map(data: IShowImage, size: Size): HttpUrl {
        val width = if (size is PixelSize) {
            size.width
        } else 0

        return when (data.type) {
            ShowImageType.BACKDROP -> {
                imageProvider.getBackdropUrl(
                    data.path, width
                ).toHttpUrl()
            }
            ShowImageType.POSTER -> {
                imageProvider.getPosterUrl(
                    data.path, width
                ).toHttpUrl()
            }
            ShowImageType.LOGO -> {
                imageProvider.getLogoUrl(
                    data.path, width
                ).toHttpUrl()
            }
        }
    }
}