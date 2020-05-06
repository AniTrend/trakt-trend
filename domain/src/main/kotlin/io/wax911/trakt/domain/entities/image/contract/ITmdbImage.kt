package io.wax911.trakt.domain.entities.image.contract

import io.wax911.trakt.domain.entities.contract.IEntity
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import io.wax911.trakt.domain.models.MediaType

interface ITmdbImage : IEntity {
    val type: MediaType
    val imageType: ShowImageType
}