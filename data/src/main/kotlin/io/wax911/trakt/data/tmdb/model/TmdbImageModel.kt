package io.wax911.trakt.data.tmdb.model

import com.uwetrottmann.tmdb2.entities.Image

internal data class TmdbImageModel(
    val parentId: Long,
    val type: ImageType,
    val image: Image,
    val isPrimary: Boolean
) {
    enum class ImageType(key: String) {
        BACKDROP("backdrop"),
        POSTER("poster"),
        LOGO("logo")
    }
}