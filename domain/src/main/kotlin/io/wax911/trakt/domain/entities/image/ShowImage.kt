package io.wax911.trakt.domain.entities.image

import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType

data class ShowImage(
    override val path: String,
    override val type: ShowImageType,
    override val language: String? = null,
    override val rating: Float,
    override val isPrimary: Boolean,
    override val id: Int
) : IShowImage