package io.wax911.trakt.domain.entities.image

import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import io.wax911.trakt.domain.models.MediaType

data class TmdbImage(
    override val type: MediaType,
    override val imageType: ShowImageType,
    override val id: Int
) : ITmdbImage